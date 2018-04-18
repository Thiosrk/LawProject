package Model;

import java.util.List;

/**
 * Created by 69401 on 2018/3/28.
 */
public class Age {

    private String nian;

    private String yue;

    private List<String> content;

    public String getNian() {
        return nian;
    }

    public void setNian(String nian) {
        this.nian = nian;
    }

    public String getYue() {
        return yue;
    }

    public void setYue(String yue) {
        this.yue = yue;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Age{" +
                "nian='" + nian + '\'' +
                ", yue='" + yue + '\'' +
                ", content=" + content +
                '}';
    }
}
