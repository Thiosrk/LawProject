package Model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by 69401 on 2018/3/18.
 */
@Document
public class TiaoModel {

    @Field("款")
    private LinkedHashMap<String,KuanModel> kuan;

    public LinkedHashMap<String,KuanModel> getKuan() {
        return kuan;
    }

    public void setKuan(LinkedHashMap<String,KuanModel> kuan) {
        this.kuan = kuan;
    }

//    @Override
//    public String toString() {
//
//        String strkuan = "";
//        Set<String> keys = this.kuan.keySet();
//        for(String key :keys){
//            strkuan += key+":"+this.kuan.get(key).toString()+"，";
//        }
//        return "Model.Tiao{" +
//                ", kuan=" + strkuan +
//                '}';
//    }
}
