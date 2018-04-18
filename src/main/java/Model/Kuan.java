package Model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by 69401 on 2018/3/18.
 */
@Document
public class Kuan {

    private String 内容;

    private List<String> 项;

    private String 关键词;

    public Kuan() {
    }

    public Kuan(Kuan kuan) {
        this.内容 = kuan.getContent();
        this.项 = kuan.getXiang();
        this.关键词 = kuan.getKeywords();
    }

    public String getContent() {
        return 内容;
    }

    public void setContent(String content) {
        this.内容 = content;
    }

    public List<String> getXiang() {
        return 项;
    }

    public void setXiang(List<String> xiang) {
        项 = xiang;
    }

    public String getKeywords() {
        return 关键词;
    }

    public void setKeywords(String keywords) {
        关键词 = keywords;
    }

    @Override
    public String toString() {
        return "Model.Kuan{" +
                "content='" + 内容 + '\'' +
                ", Xiang=" + 项 +
                ", Keywords='" + 关键词 + '\'' +
                '}';
    }

}
