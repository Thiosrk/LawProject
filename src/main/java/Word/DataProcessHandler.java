package Word;

import java.util.Collection;

//数据处理接口
public interface DataProcessHandler {

    void process(byte[] data,NLPIR nlpir);

    Collection<String> getresult();

}
