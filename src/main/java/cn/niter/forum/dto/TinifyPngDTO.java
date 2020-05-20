package cn.niter.forum.dto;

import lombok.Data;

@Data
public class TinifyPngDTO {
    String url;
    UserDTO user;
    String fileName;
}
