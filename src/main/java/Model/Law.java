package Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by 69401 on 2018/3/18.
 */
@Document
public class Law {

    @Id
    private String _id;

    private String 法律名称;//法律名称

    private List<Tiao> 法律内容;//法律条目

    private String 时效性;//法律时效性

    private String 效力级别;//法律效力级别；

    private String 发布日期;//发布日期

    private String 实施日期;//实施日期

    public Law() {
    }

    public Law(String name) {
        this.法律名称 = name;
    }

    public String getName() {
        return 法律名称;
    }

    public void setName(String name) {
        this.法律名称 = name;
    }

    public List<Tiao> getTiao() {
        return 法律内容;
    }

    public void setTiao(List<Tiao> tiao) {
        this.法律内容 = tiao;
    }

    public String getTimelimit() {
        return 时效性;
    }

    public void setTimelimit(String timelimit) {
        this.时效性 = timelimit;
    }

    public String getLevel() {
        return 效力级别;
    }

    public void setLevel(String level) {
        this.效力级别 = level;
    }

    public String getPublishtime() {
        return 发布日期;
    }

    public void setPublishtime(String publishtime) {
        this.发布日期 = publishtime;
    }

    public String getStarttime() {
        return 实施日期;
    }

    public void setStarttime(String starttime) {
        this.实施日期 = starttime;
    }

    @Override
    public String toString() {
        String strtiao = "";
        for(Tiao taio :法律内容){
            strtiao += taio.toString();
        }
        return "Model.Law{" +
                "name='" + 法律名称 + '\'' +
                ", tiao=" +strtiao+
                ", timelimit='" + 时效性 + '\'' +
                ", level='" + 效力级别 + '\'' +
                ", publishtime='" + 发布日期 + '\'' +
                ", starttime='" + 实施日期 + '\'' +
                '}';
    }
}