package Word;

import com.sun.jna.Native;

import java.io.UnsupportedEncodingException;

public class NLPIR {

    private CLibrary Instance = (CLibrary) Native.loadLibrary(System.getProperty("user.dir")+"\\source\\NLPIR", CLibrary.class);

    private boolean initflag = false;

    public boolean init() throws UnsupportedEncodingException {
        String argu = "";
        //         String system_charset = "GBK";//GBK----0
        String system_charset = "UTF-8";
        int charset_type = 1;
//         int charset_type = 0;
        // 调用printf打印信息


        boolean init_flag = Instance.NLPIR_Init(argu.getBytes(system_charset),
                charset_type, "0".getBytes(system_charset));
        String nativeBytes = null;
        if (!init_flag){
            nativeBytes = Instance.NLPIR_GetLastErrorMsg();
            System.err.println("初始化失败！fail reason is "+nativeBytes);
        }
        initflag = true;
        return true;

    }

    public boolean unInit(){
        try {
            Instance.NLPIR_Exit();
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        initflag = false;
        return true;
    }

    public String parseSeq(String sInput) {
        String nativeBytes = null;
        try {
            nativeBytes = Instance.NLPIR_ParagraphProcess(sInput, 3);
//            System.out.println(++size+"");
//             String nativeStr = new String(nativeBytes, 0,
//             nativeBytes.length(),"utf-8");
//            System.out.println("分词结果为： " + nativeBytes);
//             System.out.println("分词结果为： "
//             + transString(nativeBytes, system_charset, "UTF-8"));
            //
            // System.out.println("分词结果为： "
            // + transString(nativeBytes, "gb2312", "utf-8"));

//            int nCountKey = 0;
//            String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(sInput, 10,true);

//            System.out.println("关键词提取结果是：" + nativeByte);

//            String nativeBytenew  = CLibrary.Instance.NLPIR_GetNewWords(sInput,10,true);

//            System.out.println("新词提取结果是：" + nativeByte);

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


        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        return nativeBytes;
    }

    public CLibrary getInstance() {
        return Instance;
    }

    public boolean isInitflag() {
        return initflag;
    }
}
