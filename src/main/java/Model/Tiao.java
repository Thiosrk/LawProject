package Model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by 69401 on 2018/3/18.
 */
@Document
public class Tiao {

    private String 内容;

    private List<Kuan> 款;

    public String getContent() {
        return 内容;
    }

    public void setContent(String content) {
        this.内容 = content;
    }

    public List<Kuan> getKuan() {
        return 款;
    }

    public void setKuan(List<Kuan> kuan) {
        this.款 = kuan;
    }

    @Override
    public String toString() {
        return "Model.Tiao{" +
                "content='" + 内容 + '\'' +
                ", kuan=" + 款 +
                '}';
    }
}
