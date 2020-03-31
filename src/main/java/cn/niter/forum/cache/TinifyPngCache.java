package cn.niter.forum.cache;

import cn.niter.forum.dto.TinifyPngDTO;
import cn.niter.forum.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class TinifyPngCache {
    List<TinifyPngDTO> images=new ArrayList<>();

    public List get(){
        return images;
    }
    public void clear(){
        images.clear();
    }
    public void add(String url, User user, String fileName){
        if(images.size()<10){//控制最大容量
            TinifyPngDTO png = new TinifyPngDTO();
            png.setUrl(url);
            png.setUser(user);
            png.setFileName(fileName);
            images.add(png);
        }
    }



}
