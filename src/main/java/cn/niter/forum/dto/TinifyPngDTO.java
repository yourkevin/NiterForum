package cn.niter.forum.dto;

import cn.niter.forum.model.User;
import lombok.Data;

@Data
public class TinifyPngDTO {
    String url;
    User user;
    String fileName;
}
