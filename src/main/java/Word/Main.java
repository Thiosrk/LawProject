package Word;

import com.sun.jna.Native;
import lombok.NonNull;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.eval.Evaluation;
//import org.deeplearning4j.iterator.*;
//import org.deeplearning4j.iterator.CnnSentenceDataSetIterator;
import org.deeplearning4j.iterator.LabeledSentenceProvider;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.GlobalPoolingLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.PoolingType;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by 69401 on 2018/4/2.
 */
public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    private static final String filePath = "C:\\Users\\69401\\Desktop\\毕业设计资料\\分词\\";

    public static final String XmlPath = "C:\\Users\\69401\\Desktop\\毕业设计资料\\分词\\文书测试数据\\民事一审\\";

    public static final String WORD2VEC_MODEL_PATH = filePath+"vector_model_v1.txt";

    public static final String DataPath = "C:\\Users\\69401\\Desktop\\毕业设计资料\\分词\\文书测试数据\\data.txt";

    public static final String TestDataPath = "C:\\Users\\69401\\Desktop\\毕业设计资料\\分词\\文书测试数据\\test_data.txt";

    public static final String ModelPath = "C:\\Users\\69401\\Desktop\\毕业设计资料\\分词\\文书测试数据\\Model.zip";

    private static String unicodeToUtf8 (String s) throws UnsupportedEncodingException {
        return new String( s.getBytes("GBK") , "GBK");
    }

    private static void writeStringlistTotxt(List<String> content,String filePath){
        FileWriter fwriter = null;
//        int nums  = lawlist.size();
        log.info("start write to txt!");
        try {
            fwriter = new FileWriter(new File(filePath));
            BufferedWriter bw = new BufferedWriter(fwriter);

            for (String str :content){
                bw.write(str+"\r\n");
            }


//            fwriter.write(nums+"");

            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("finish write!");

    }

    private static void writeTotxt(String content,String filePath){
        FileWriter fwriter = null;
//        int nums  = lawlist.size();
        log.info("start write to txt!");
        try {
            fwriter = new FileWriter(new File(filePath));
            BufferedWriter bw = new BufferedWriter(fwriter);

                bw.write(content+"\r\n");

//            fwriter.write(nums+"");

            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("finish write!");

    }

    public static void main(String[] args) throws IOException {



//        String sInput = "原告薛仰林诉称：2015年8月5日上午12时，原告从公司西侧大门买水进公司时候，被告由南向北驾驶机动车，其前轮压到原告右脚。" +
//                "被告承诺如不好带其去医院就诊。第二天早上，原告在无法联系被告的情况下，独自去义桥镇社区卫生服务中心诊治，诊断结果为第2、3、4骨骨折。" +
//                "后被告去原告家里探望，但拒不支付医药费等费用。故起诉，要求被告支付医药费1369．01元，误工费5890元，营养费350元，护理费924元，合计8571元。" +
//                " 被告李开通辩称：其没有撞到原告，不承担赔偿责任。 原告为支持其主张的事实，在举证期限内向本院提供了下列证据材料：" +
//                "1．门诊病例本1份，欲证明原告因伤治疗的情况；2．门诊收费票据13张，欲证明原告因外伤花费的医疗费用的情况；3．萧山中医骨伤科医院诊断证明书5份，欲证明原告因外伤需误工38天的事实；" +
//                "4．检查诊断报告单2份，欲证明原告的受伤情况；5．银行卡账户交易明细2份，欲证明原告的收入情况；6．光盘1份，欲证明原告受伤系被告造成的事实。" +
//                "经质证，被告认为证据1-5与本案无关，证据6不具有真实性。经审查，本院认为上述证据真实、合法，与本案事实具有关联性，对其证明效力予以认定。" +
//                " 被告未向本院提供证据。 根据法庭调查和上述有效证据，本院查明的事实如下：2015年8月5日，原告在杭州中泰实业有限公司西侧大门被公司内由南向北驾驶机动车的被告前轮压到右脚。" +
//                "2015年8月6日，原告前往医院就诊，其诊断结果为右足第2、3、4蹠骨骨折。 经审核，原告合理的物质损失如下：" +
//                "医疗费1369．01元，误工费为4650元（155元／天×30天），营养费350元（50元／天×7天），共计6369．01元。";
//

//
//        String result = getNLPIRresult(sInput,stopWordSet);
//        Collection<String> sentences = new ArrayList<String>();
//        sentences.add(result);
//
//

//        一拖（洛阳）神通工程机械有限公司与一孙亚丽、一李强一案.xml	中华人民共和国婚姻法 第二十四条  ,中华人民共和国合同法 第一百零七条  ,中华人民共和国担保法 第十八条 第三十一条  ,中华人民共和国民事诉讼法 第一百四十四条 第六十四条第一款  ,	原告 一拖 洛阳 神通 工程 机械 有限公司 诉 称 二 被告 系 夫妻 关系 2011年 6月 9日 被告 李强 中国一拖集团 财务 有限 责任 公司 签订 产品 融资 租赁 合同 合同 约定 中国一拖集团 财务 有限 责任 公司 被告 李强 提供 选定 原告 一拖 洛阳 神通 工程 机械 有限公司 生产 矿用车 台 被告 李强 采用 融资 租赁 方式 承租 租赁 本金 448000 元 被告 李强 月 支付 租金 保证 融资 租赁 合同 履行 中国 拖 财务 集团 有限 责任 公司 原告 一拖 洛阳 神通 工程 机械 有限公司 签订 保证 合同 原告 一拖 洛阳 神通 工程 机械 有限公司 被告 李强 提供 连带 责任 保证 被告 李强 孙亚丽 原告 一拖 洛阳 神通 工程 机械 有限公司 签订 自然人 担保书 自愿 原告 一拖 洛阳 神通 工程 机械 有限公司 提供 反 担保 2012年 12月 9日 合同 期满 被告 李强 次 逾期 租金 累计 137580．45 元 原告 一拖 洛阳 神通 工程 机械 有限公司 保证 合同 中国 拖 财务 集团 有限 责任 公司 履行 担保 责任 原告 催 二 被告 未 付 拖欠 租金 相关 法律 请求 1 判 令 二 被告 支付 欠 租金 137580．45 元 逾期 利息 21153．09 元 暂 计算 2013年 11月 20日 2 本案 诉讼费 律师费 差旅费 债权 费用 被告 承担 被告 李强 孙亚丽 逾期 未 答辩 审理 查明 被告 李强 孙亚丽 系 夫妻 关系 2011年 6月 9日 被告 李强 中国一拖集团 财务 有限 责任 公司 签订 产品 融资 租赁 合同 合同 约定 中国一拖集团 财务 有限 责任 公司 被告 李强 提供 选定 原告 一拖 洛阳 神通 工程 机械 有限公司 生产 矿用车 台 被告 李强 采用 融资 租赁 方式 承租 租赁 本金 448000 元 被告 李强 月 支付 租金 保证 融资 租赁 合同 履行 中国 拖 财务 集团 有限 责任 公司 原告 一拖 洛阳 神通 工程 机械 有限公司 签订 保证 合同 原告 一拖 洛阳 神通 工程 机械 有限公司 被告 李强 提供 连带 责任 保证 被告 李强 孙亚丽 原告 一拖 洛阳 神通 工程 机械 有限公司 签订 自然人 担保书 自愿 原告 一拖 洛阳 神通 工程 机械 有限公司 提供 反 担保 2012年 12月 9日 合同 期满 被告 李强 次 逾期 租金 累计 137580．45 元 原告 一拖 洛阳 神通 工程 机械 有限公司 保证 合同 中国 拖 财务 集团 有限 责任 公司 履行 担保 责任 二 被告 李强 孙亚丽 追 果 诉讼 院

//        处理数据
//        Set stopWordSet = getStopWords();
//        List<String> filenames = getAllfilename(XmlPath);
//        System.out.println("files size is : "+filenames.size());
//        List<String> datalist = new ArrayList<>();
//        for (String str :filenames){
//            System.out.println(str);
//            String data = dealDatas(str,stopWordSet);
//            if (data!=null){
//                datalist.add(data);
//            }
//        }
//
//
//        writeStringlistTotxt(datalist,DataPath);

//        制作词向量模型
//        List<String> data = readFromtxt(DataPath);
//        word2vec(makeSentences(data));

        //卷积训练
//        CnnTrainModel();

        //word2vec测试
//        System.out.println("Loading word vectors and creating DataSetIterators");
//
//        Word2Vec wordVectors = WordVectorSerializer.readWord2VecModel(new File(WORD2VEC_MODEL_PATH));
//        System.out.println(wordVectors.similarity("婚姻","感情"));
//        WeightLookupTable weightLookupTable = wordVectors.lookupTable();
//        VocabCache vocabCache = weightLookupTable.getVocabCache();
//        System.out.println(vocabCache.hasToken("婚姻"));
//        Iterator<INDArray> vectors = weightLookupTable.vectors();
//        INDArray wordVectorMatrix = wordVectors.getWordVectorMatrix("婚姻");
//        double[] wordVector = wordVectors.getWordVector("婚姻");
//        for (double a : wordVector){
//            System.out.print(a+" ");
//        }
//        if (wordVectors.hasWord("婚姻 ")){
//            log.info("有");
//        }else
//            log.info("没有");

        //词量
        List<String> data = readFromtxt(DataPath);


    }
    //用例测试
    private void test() throws IOException {
        int batchSize = 10;
        int truncateReviewsToLength = 256;//词长大于1000抛弃
        Random random = new Random(100);//随机抽样
        Word2Vec wordVectors = WordVectorSerializer.readWord2VecModel(new File(WORD2VEC_MODEL_PATH));
        DataSetIterator trainIter = getDataSetIterator(true, wordVectors,batchSize, truncateReviewsToLength, random);
        ComputationGraph net = ModelSerializer.restoreComputationGraph(ModelPath);
        String contentsFirstPas = "原告 丁某 诉 称 原 被告 夫妻 感情 不合 2011年 9月 协议 离婚 孩子 完整 家庭 复婚 复婚 被告 外遇 家庭 发生 争执 现 夫妻 分居 生活 夫妻 感情 破裂 特 诉至 法院 被告 离婚 被告 王某 辩称 原 被告 婚后 未 矛盾 夫妻 感情 原告 陈述 提交 证据 证明 被告 外遇 证明 夫妻 感情 破裂 孩子 完整 家庭 被告 诚实 态度 请求 原告 回 心 转 意 同意 离婚 审理 查明 原告 被告 2006年 夏天 自由 恋爱 ×××× 年 ×× 月 ×× 日 原 兖州市 民政局 办理 结婚 登记 手续 2011年 9月 16日 夫妻 矛盾 原 兖州市 民政局 协议 离婚 2013年 1月 27日 原 兖州市 民政局 办理 复婚 手续 2011年 3月 29日 婚 生 男孩 取名 王星元 原 被告 生活 期间 生活 琐事 发生 吵闹 2014年 3月 中旬 深夜 孩子 上学 意见 原告 家中 搬 2014年 4月 14日 原告 诉讼 院 称 被告 孩子 面向 原告 说 脏话 影响 孩子 健康 成长 小事 原告 原因 被告 外遇 伤害 夫妻 感情 原告 被告 生活 被告 离婚 被告 应诉 承认 第三者 原告 暧昧关系 证明 被告 暧昧关系 原告 向本庭 提交 光盘 1 张 内存 女子 龚 照片 21 张 原告 女子 合影 8 张 宗 照片 拍摄 2010年 4月 3日 系 原 被告 第一 次 婚姻 关系 存 续 期间 原告 提供 书写 书面 材料 4 张 4 张 书面 材料 内容 抄 被告 手机 中 短 信 内容 被告 女子 王 暧昧关系 被告 照片 真实性 异议 照片 拍摄 原 被告 第一 次 婚姻 关系 存 续 期间 本案 复婚 感情 照片 被告 外遇 被告 4 张 书面 材料 真实性 内容 异议 被告 手机 中 抄 内容 被告 外遇 被告 原告 外遇 原告 认可 被告 未 提供 证据 证明 事实 原 被告 陈述 提交 证据 认定 材料 收集 记录 卷";

        String b = "张南子 诉 称 冀 号 车辆 处 投保 机动车 损失 险 险 签订 合同 合同 投保 车辆 险 金额 予以 载明 付永刚 驾驶 投保 车辆 行驶 龙海 大道 山海关 机场 立交桥 路段 时 情况 不当 发生 单方 事故 投保 车辆 受损 事故 秦 市 公安 警察 现场 勘查 认定 付永刚 负 事故 责任 出具 道路 交通 事故 认定书 事故 发生 交警 部门 依法 委托 秦皇岛市 价格 认证 中心 事故 车辆 价格 鉴 证 确认 车辆 损失 数额 车辆 损失 价格 鉴 证 结论 相关 材料 索赔 损失 数额 自行 核算 数额 不符 为由 拒绝 赔偿 情况 原 告特 提起 诉讼 请求 人民法院 依法 判 令 付 各项 理 赔款 合计 金额 元 包括 车 损 元 鉴定 费 元 施 救 费 元 拆解 费 元 提交 证据 车辆 行驶 证 份 复印件 证明 车辆 张南子 商业 险 保险单 份 证明 原 之间 合同 关系 投保 车辆 损失 险 金额 元 投保 计 免 赔 事故 认定书 份 证明 事故 发生 时间 地点 事故 责任 事故 责任 事故 发生 时 驾驶 驾驶证 复印件 证明 事故 发生 时 驾驶 驾驶 资格 财产 损失 价格 鉴定 结论 书 份 确认 损失 元 价格 鉴定 费 单据 张 证明 花费 鉴定 费 元 事故 现场 施 救 费 单据 证明 事故 发生 施 救 车辆 支付 施 救 费 元 拆解 费 单据 张 证明 花费 拆解 费 元 中国 平安 财产 股份 有限公司 北京 分公司 辩称 涉案 车辆 事发 时 公司 投 车辆 损失 险 公司 异议 认可 诉讼 请求 签订 保险单 车辆 初始 登记 日期 年 辆 车 事 辆 车 鉴定 评估 元 签订 条款 情况 公司 赔偿 车辆 价值 同意 诉讼 请求 提供 评估 报告 认可 提交 证据 投保单 机动 车辆 条款 审理 院 认定 付永刚 驾驶证 号 驾驶证 起始 日期 期限 年 驾驶 冀 号 小型 轿车 龙海 大道 西 东 行驶 山海关 机场 立交桥 路段 遇 情况 措施 不当 发生 单方 事故 车辆 损坏 交通 事故 秦皇岛市 公安 交通 警察 支队 六 大队 作出 道路 交通 事故 认定书 认定 付永刚 负 事故 责任 冀 号 小型 轿车 系 张 南 子 秦皇岛市 公安 交通 警察 支队 六 大队 委托 秦皇岛市 物价局 价格 认证 中心 冀 号 小型 轿车 修复 费用 鉴定 鉴定 机构 作出 鉴 证 结论 书 鉴 证 结论 冀 号 小型 轿车 损失 总价值 元 花费 价格 鉴 证 费 元 拆解 费 元 施 救 费 元 中国 平安 财产 股份 有限公司 北京 分 公司 所属 北京市 第一 营业部 投保 机动 车辆 车辆 损失 险 赔偿 限额 元 计 免 赔 率 本次 事故 发生 期间 中国 平安 财产 股份 有限公司 机动 车辆 条款 第一 险 中 车辆 损失 险 第十九 条 车辆 发生 损失 金额 高于 出险 价值 出险 价值 计算 赔偿 施 救 费用 车辆 损失 赔偿 金额 计算 超过 金额 数额 第二十一 条 车辆 发生 事故 遭受 损失 残余 协商 折 协商 价值 赔款 中 扣除 条款 中 折 协商 价值 赔款 中 扣除 黑体 字 第四 释义 中 损失 定义 指 车辆 整体 损毁 车辆 修复 费用 施 救 费用 之和 超过 出险 价值 推 全 损 签字 确认 投保单 载明 确认 收到 平安 机动 车辆 条款 贵 公司 详细 介绍 条款 内容 特别 黑体 字 条款 内容 手 写 打印 版 特别 约定 内容 作 理解 同意 投保 中国 平安 财产 股份 有限公司 北京市 第一 营业部 院 起诉 变更 中国 平安 财产 股份 有限公司 北京 分公司 庭审 时 申请 鉴定 庭审 提出 书面 评估 申请书 申请 冀 号 小型 轿车 事故 发生 时 价值 事故 发生 残 值 评估 院 委托 秦皇岛 星 日 阳 资产 评估 有限 责任 公司 作出 秦 星 评 字 ［ ］ 号 资产 评估 报告 书 评估 结论 截止 评估 基准 日 车辆 评估 价值 元 截止 评估 基准 日 车辆 评估 价值 元 事实 提交 证据 秦星 评 字 ［ ］ 号 资产 评估 报告 书 院 庭审 笔录 卷 证 足以 认定 ";
        INDArray featuresFirstNegative = ((CnnSentenceDataSetIterator)trainIter).loadSingleSentence(b);
        INDArray predictionsFirstNegative = net.outputSingle(featuresFirstNegative);
        List<String> labels = trainIter.getLabels();
        System.out.println("\n\nPredictions for first negative review:");
        System.out.println("labels size is : "+labels.size());
        double k = 0;
        int flag = 0;
        for( int i=0; i<labels.size(); i++ ){
            if (predictionsFirstNegative.getDouble(i) > k){
                k = predictionsFirstNegative.getDouble(i);
                flag = i;
            }
        }
        System.out.println("nearest law is : "+labels.get(flag));
        System.out.println("The similar is : "+k);
    }

    //制作词向量数据
    private static Collection<String> makeSentences(List<String> data){
        Collection<String> sentences = new ArrayList<>();
        for (String str :data){
            String[] tmp = str.split("\t");
            System.out.println(tmp[1]);
            String[] a = tmp[1].split(" ");
            ArrayList<String> a1 = new ArrayList<>();
            a1.addAll(Arrays.asList(a));
            for (int i=0;i<a1.size();++i){
                String b = a1.get(i);
//                b = b.replaceAll("[a-zA-Z]","" );
                if (isContainNumber(b)){
                    a1.remove(i);
                    i--;
                }
            }
            StringBuilder resutlt = new StringBuilder();
            for (String b : a1){
                resutlt.append(b);
                resutlt.append(" ");
            }
            sentences.add(resutlt.toString());
        }
        return sentences;
    }
    //是否包含数字
    private static boolean isContainNumber(String company) {

        Pattern p = Pattern.compile("[0-9]|[a-zA-Z]");
        Matcher m = p.matcher(company);
        if (m.find())
            return true;
        else
            return (company.contains("原告")||company.contains("被告"));

    }
    //读取文件
    private static List<String> readFromtxt(String filename){
        File file = new File(filename);
        BufferedReader reader = null;
        List<String> lawlist = new ArrayList<String>();
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while((tempString = reader.readLine()) != null) {
                System.out.println("line " + line + ": " + tempString);
                line++;
                lawlist.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader!=null){
                try{
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return lawlist;
    }

    //处理数据源
    private static String dealDatas(String filename,Set stopWordSet){
        XmlParserUtil xmlParserUtil = new XmlParserUtil(XmlPath+filename);

        Map<String,String> content = xmlParserUtil.getJBQK();
        Set<String> names = content.keySet();
        String ajjbqk ="";
        System.out.println("----------------案件基本情况---------------");
        for (String str : names){
            System.out.println("key is : "+str);
            ajjbqk = content.get(str);
            System.out.println("value is : "+content.get(str));
            System.out.println("value length is : "+content.get(str).length());
        }
        ajjbqk = replaceBlank(ajjbqk);
        ajjbqk = getNLPIRresult(ajjbqk,stopWordSet);
        System.out.println("ajjbqk is : "+ajjbqk);
        System.out.println("----------------案件基本情况---------------");

        Map<String,String> result = xmlParserUtil.getFLFT();
        Set<String> keyset = result.keySet();
        StringBuilder label = new StringBuilder();
        System.out.println("----------------Law---------------");
        for (String str : keyset){
            label.append(str);
            label.append(" ");
            label.append(result.get(str));
            System.out.println("key is : "+str);
            System.out.println("value is : "+result.get(str));
            label.append(",");
        }
        System.out.println("label is : "+label);
        System.out.println("----------------Law---------------");

        if ("".equals(ajjbqk)||" ".equals(ajjbqk)){
            return null;
        }
        //filename+"\t"+
        return label+"\t"+ajjbqk;

    }

    //获取目录下所有文件名
    private static List<String> getAllfilename(String path){
        File file = new File(path);
        File[] files = file.listFiles();
//        String [] names = file.list();
        List<String> names = new ArrayList<>();
        if (files!=null){
            for(File a : files){
                if (!a.isDirectory()){
                    names.add(a.getName());
                }
            }
        }
        return names;
    }

    //去掉空格回车换行
    private static String replaceBlank(String str){
        String tmp = "";
        if (str!=null){
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            tmp = m.replaceAll("");
        }
        return tmp;
    }

    private static void CnnTrainModel() throws IOException {
//        String WORD_VECTORS_PATH = "src/main/resources/test_vectors.txt";

        //基础配置

        int batchSize = 10;
        int vectorSize = 100;//词典向量的维度，这边是100
        int nEpochs = 10;//迭代次数
        int truncateReviewsToLength = 256;//词长大于1000抛弃
        int cnnLayerFeatureMaps = 100;// 卷积神经网络特征图标 / channels / CNN每层layer的深度
        PoolingType globalPoolingType = PoolingType.MAX;
        Random random = new Random(100);//随机抽样

        //设置网络配置->我们有多个卷积层，每个带宽3,4,5的滤波器



        ComputationGraphConfiguration config = new NeuralNetConfiguration.Builder()
                .weightInit(WeightInit.RELU)
                .activation(Activation.LEAKYRELU)
                .updater(Updater.ADAM).convolutionMode(ConvolutionMode.Same) //This is important so we can 'stack' the results later
                .regularization(true).l2(0.0001)
                .learningRate(0.01)
                .graphBuilder()
                .addInputs("input")
                .addLayer("cnn3",new ConvolutionLayer.Builder()
                    .kernelSize(3,vectorSize)
                    .stride(1,vectorSize)
                    .nIn(1)
                    .nOut(cnnLayerFeatureMaps)
                    .build(),"input")
                .addLayer("cnn4",new ConvolutionLayer.Builder()
                    .kernelSize(4,vectorSize)
                    .stride(1,vectorSize)
                    .nIn(1)
                    .nOut(cnnLayerFeatureMaps)
                    .build(),"input")
                .addLayer("cnn5",new ConvolutionLayer.Builder()
                    .kernelSize(5,vectorSize)
                    .stride(1,vectorSize)
                    .nIn(1)
                    .nOut(cnnLayerFeatureMaps)
                    .build(),"input")
                .addVertex("merge",new MergeVertex(),"cnn3","cnn4","cnn5")
                .addLayer("globalPool",new GlobalPoolingLayer.Builder()
                    .poolingType(globalPoolingType)
                    .build(),"merge")
                .addLayer("out",new OutputLayer.Builder()
                    .lossFunction(LossFunctions.LossFunction.MCXENT)
                    .activation(Activation.SOFTMAX)
                    .nIn(3*cnnLayerFeatureMaps)
                    .nOut(1138) //2 classes: positive or negative
                    .build(),"globalPool")
                .setOutputs("out")
                .build();

        UIServer uiServer = UIServer.getInstance();
        StatsStorage statsStorage = new InMemoryStatsStorage();

        uiServer.attach(statsStorage);

        ComputationGraph net = new ComputationGraph(config);
        net.init();
        net.setListeners(new ScoreIterationListener(1));

        net.setListeners(new StatsListener(statsStorage));
        //加载向量字典并获取训练集合测试集的DataSetIterators

        System.out.println("Loading word vectors and creating DataSetIterators");

//        WordVectors wordVectors = WordVectorSerializer.fromPair(WordVectorSerializer.loadTxt(new File(WORD2VEC_MODEL_PATH)));

        Word2Vec wordVectors = WordVectorSerializer.readWord2VecModel(new File(WORD2VEC_MODEL_PATH));

        DataSetIterator trainIter = getDataSetIterator(true, wordVectors,batchSize, truncateReviewsToLength, random);

        DataSetIterator testIter = getDataSetIterator(false, wordVectors,batchSize, truncateReviewsToLength, random);

        log.info("-------------Starting training--------------");
        for (int i=0;i<nEpochs;++i){
            net.fit(trainIter);
            trainIter.reset();

            // 进行网络演化(进化)获得网络判定参数
//            Evaluation evaluation = net.evaluate(testIter);
//
//            testIter.reset();
//            log.info(evaluation.stats());
        }
        ModelSerializer.writeModel(net,ModelPath,true);

        String contentsFirstPas = "原告 丁某 诉 称 原 被告 夫妻 感情 不合 2011年 9月 协议 离婚 孩子 完整 家庭 复婚 复婚 被告 外遇 家庭 发生 争执 现 夫妻 分居 生活 夫妻 感情 破裂 特 诉至 法院 被告 离婚 被告 王某 辩称 原 被告 婚后 未 矛盾 夫妻 感情 原告 陈述 提交 证据 证明 被告 外遇 证明 夫妻 感情 破裂 孩子 完整 家庭 被告 诚实 态度 请求 原告 回 心 转 意 同意 离婚 审理 查明 原告 被告 2006年 夏天 自由 恋爱 ×××× 年 ×× 月 ×× 日 原 兖州市 民政局 办理 结婚 登记 手续 2011年 9月 16日 夫妻 矛盾 原 兖州市 民政局 协议 离婚 2013年 1月 27日 原 兖州市 民政局 办理 复婚 手续 2011年 3月 29日 婚 生 男孩 取名 王星元 原 被告 生活 期间 生活 琐事 发生 吵闹 2014年 3月 中旬 深夜 孩子 上学 意见 原告 家中 搬 2014年 4月 14日 原告 诉讼 院 称 被告 孩子 面向 原告 说 脏话 影响 孩子 健康 成长 小事 原告 原因 被告 外遇 伤害 夫妻 感情 原告 被告 生活 被告 离婚 被告 应诉 承认 第三者 原告 暧昧关系 证明 被告 暧昧关系 原告 向本庭 提交 光盘 1 张 内存 女子 龚 照片 21 张 原告 女子 合影 8 张 宗 照片 拍摄 2010年 4月 3日 系 原 被告 第一 次 婚姻 关系 存 续 期间 原告 提供 书写 书面 材料 4 张 4 张 书面 材料 内容 抄 被告 手机 中 短 信 内容 被告 女子 王 暧昧关系 被告 照片 真实性 异议 照片 拍摄 原 被告 第一 次 婚姻 关系 存 续 期间 本案 复婚 感情 照片 被告 外遇 被告 4 张 书面 材料 真实性 内容 异议 被告 手机 中 抄 内容 被告 外遇 被告 原告 外遇 原告 认可 被告 未 提供 证据 证明 事实 原 被告 陈述 提交 证据 认定 材料 收集 记录 卷";

        INDArray featuresFirstNegative = ((CnnSentenceDataSetIterator)trainIter).loadSingleSentence(contentsFirstPas);
        INDArray predictionsFirstNegative = net.outputSingle(featuresFirstNegative);
        List<String> labels = trainIter.getLabels();
        System.out.println("\n\nPredictions for first negative review:");
        System.out.println("labels size is : "+labels.size());
        double k = 0;
        int flag = 0;
        for( int i=0; i<labels.size(); i++ ){
            if (predictionsFirstNegative.getDouble(i) > k){
                k = predictionsFirstNegative.getDouble(i);
                flag = i;
            }
//            System.out.println("P(" + labels.get(i) + ") = " + );
        }
        System.out.println("nearest law is : "+labels.get(flag));
        System.out.println("The similar is : "+k);


    }

    private static DataSetIterator getDataSetIterator(boolean isTraining,
                                                      Word2Vec wordVectors,
                                                      int minibatchSize,
                                                      int maxSentenceLength,
                                                      Random random) {

        String path = isTraining ? DataPath:TestDataPath;

        LabeledSentenceProvider sentenceProvider = new LabeledSentence(path,random);

        return new CnnSentenceDataSetIterator.Builder()
                .sentenceProvider(sentenceProvider)
                .wordVectors(wordVectors)
                .minibatchSize(minibatchSize)
                .maxSentenceLength(maxSentenceLength)
                .useNormalizedWordVectors(false)
                .build();

    }


    //停用词
    private static Set getStopWords(){
        String stopwordsPath = "src\\main\\resources\\stopwords.txt";
        String system_charset = "utf-8";

        BufferedReader bufferedReader;
        Set stopWordSet = new HashSet<String>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(stopwordsPath)),system_charset));
            String stopword = null;
            for (;(stopword=bufferedReader.readLine())!=null;){
                stopword = unicodeToUtf8(stopword);
                stopWordSet.add(stopword);
//                System.out.println(stopword);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWordSet;
    }

    private static void word2vec(Collection<String> sentences) throws IOException {
        log.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        SentenceIterator iterator = new CollectionSentenceIterator(sentences);
        // Split on white spaces in the line to get words
        while (iterator.hasNext()){
            log.info(iterator.nextSentence());
        }


        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(2)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iterator)
                .build();

        log.info("Fitting Word2Vec model....");
        vec.fit();
        log.info("Writing word vectors to text file....");

        double a = vec.similarity("婚姻","感情");
        double b = vec.similarity("婚姻","合同");
        double c = vec.similarity("婚姻","结婚");
        System.out.println("婚姻和感情的相似度是： "+a);
        System.out.println("婚姻和合同的相似度是： "+b);
        System.out.println("婚姻和结婚的相似度是： "+c);
        // Write word vectors
        WordVectorSerializer.writeWordVectors(vec, WORD2VEC_MODEL_PATH);

    }

    //NLPIRresult
    private static String getNLPIRresult(String sInput,Set stopWordSet){
        String nativeBytes = "";
        try {
            nativeBytes = NLPIRword(sInput);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] resultArray =nativeBytes.split(" ");
        for(int i=0;i<resultArray.length;++i){
            if (resultArray[i].contains("/"))
                resultArray[i] = resultArray[i].substring(0,resultArray[i].indexOf("/"));
            if (stopWordSet.contains(resultArray[i]))
                resultArray[i] = null;
        }
        StringBuffer result = new StringBuffer();
        for (String tmp : resultArray){
            if (tmp!=null){
                if (!isContainNumber(tmp)){
                    result.append(tmp).append(" ");
                }
            }
        }
        return result.toString();
//        System.out.println("Final result: "+reault);
    }

    //NLPIRcall
    private static String NLPIRword(String sInput) throws UnsupportedEncodingException {
        String argu = "";
         String system_charset = "GBK";//GBK----0
//        String system_charset = "UTF-8";
//        int charset_type = 1;
         int charset_type = 0;
        // 调用printf打印信息
        if (!CLibrary.Instance.NLPIR_Init(argu.getBytes(system_charset),
                charset_type, "0".getBytes(system_charset))) {
            System.err.println("初始化失败！");
        }



        String nativeBytes = null;
        try {
            nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 3);
            // String nativeStr = new String(nativeBytes, 0,
            // nativeBytes.length,"utf-8");
            System.out.println("分词结果为： " + nativeBytes);
            // System.out.println("分词结果为： "
            // + transString(nativeBytes, system_charset, "UTF-8"));
            //
            // System.out.println("分词结果为： "
            // + transString(nativeBytes, "gb2312", "utf-8"));

            int nCountKey = 0;
            String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(sInput, 10,true);

            System.out.println("关键词提取结果是：" + nativeByte);

            String nativeBytenew  = CLibrary.Instance.NLPIR_GetNewWords(sInput,10,true);

            System.out.println("新词提取结果是：" + nativeByte);

            // int nativeElementSize = 4 * 6 +8;//size of result_t in native
            // code
            // int nElement = nativeByte.length / nativeElementSize;
            // ByteArrayInputStream(nativeByte));
            //
            // nativeByte = new byte[nativeByte.length];
            // nCountKey = testNLPIR30.NLPIR_KeyWord(nativeByte, nElement);
            //
            // Result[] resultArr = new Result[nCountKey];
            // DataInputStream dis = new DataInputStream(new
            // ByteArrayInputStream(nativeByte));
            // for (int i = 0; i < nCountKey; i++)
            // {
            // resultArr[i] = new Result();
            // resultArr[i].start = Integer.reverseBytes(dis.readInt());
            // resultArr[i].length = Integer.reverseBytes(dis.readInt());
            // dis.skipBytes(8);
            // resultArr[i].posId = Integer.reverseBytes(dis.readInt());
            // resultArr[i].wordId = Integer.reverseBytes(dis.readInt());
            // resultArr[i].word_type = Integer.reverseBytes(dis.readInt());
            // resultArr[i].weight = Integer.reverseBytes(dis.readInt());
            // }
            // dis.close();
            //
            // for (int i = 0; i < resultArr.length; i++)
            // {
            // System.out.println("start=" + resultArr[i].start + ",length=" +
            // resultArr[i].length + "pos=" + resultArr[i].posId + "word=" +
            // resultArr[i].wordId + "  weight=" + resultArr[i].weight);
            // }

            CLibrary.Instance.NLPIR_Exit();

        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

        return nativeBytes;
    }

}