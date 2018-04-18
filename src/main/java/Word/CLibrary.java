package Word;

/**
 * Created by 69401 on 2018/4/2.
 */
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface CLibrary extends Library {

    // 定义并初始化接口的静态变量
    CLibrary Instance = (CLibrary) Native.loadLibrary(System.getProperty("user.dir")+"\\source\\NLPIR", CLibrary.class);

    // printf函数声明
    public boolean NLPIR_Init(byte[] sDataPath, int encoding,
                              byte[] sLicenceCode);
    //初始化
//    public int NLPIR_Init(String sDataPath, int encoding, String sLicenceCode);
    //对字符串进行分词
    public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
    //对TXT文件内容进行分词
    public double NLPIR_FileProcess(String sSourceFilename,String sResultFilename, int bPOStagged);
    //从字符串中提取关键词
    public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,boolean bWeightOut);
    //从TXT文件中提取关键词
    public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,boolean bWeightOut);
    //添加单条用户词典
    public int NLPIR_AddUserWord(String sWord);
    //删除单条用户词典
    public int NLPIR_DelUsrWord(String sWord);
    //从TXT文件中导入用户词典
    public int NLPIR_ImportUserDict(String sFilename);
    //将用户词典保存至硬盘
    public int NLPIR_SaveTheUsrDic();
    //从字符串中获取新词
    public String NLPIR_GetNewWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
    //从TXT文件中获取新词
    public String NLPIR_GetFileNewWords(String sTextFile,int nMaxKeyLimit, boolean bWeightOut);
    //获取一个字符串的指纹值
    public long NLPIR_FingerPrint(String sLine);
    //设置要使用的POS map
    public int NLPIR_SetPOSmap(int nPOSmap);
    //获取报错日志
    public String NLPIR_GetLastErrorMsg();
    //退出
    public void NLPIR_Exit();
}
