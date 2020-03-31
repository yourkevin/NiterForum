package cn.niter.forum.provider;

import com.tinify.Source;
import com.tinify.Tinify;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class TinifyProvider {


    @Value("${tinify.key}")
    private String tinifyKey;

      public InputStream getStreamfromUrl(String url){
          Tinify.setKey(tinifyKey);
          Source source = null;
          InputStream inputStream=null;
          try {
              //source = Tinify.fromUrl("https://qcdn.niter.cn/demo/img-e0a3caa1dea1c79212f411b57f053c50.jpg");
              byte[] resultData = Tinify.fromUrl(url).toBuffer();
              inputStream = new ByteArrayInputStream(resultData);

              //byte[] resultData = Tinify.fromBuffer(sourceData).toBuffer();
              //source = Tinify.fromFile("unoptimized.jpg");
              //source.toFile("C:\\Users\\kuang\\Desktop\\1.png");
             // System.out.println("ok了");
          } catch (IOException e) {
              e.printStackTrace();
          }
          return inputStream;

      }

    public InputStream getStreamfromInputStream(InputStream inputStream){
        Tinify.setKey(tinifyKey);
        //Source source = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        try {
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                byteArrayOutputStream.write(buff, 0, rc);
            }
            byte[] sourceData = byteArrayOutputStream.toByteArray();
            byte[] resultData = Tinify.fromBuffer(sourceData).toBuffer();
            //source = Tinify.fromUrl("https://qcdn.niter.cn/demo/img-e0a3caa1dea1c79212f411b57f053c50.jpg");
            //byte[] resultData = Tinify.fromUrl("https://tinypng.com/images/panda-happy.png").toBuffer();
            inputStream = new ByteArrayInputStream(resultData);
            //byte[] resultData = Tinify.fromBuffer(sourceData).toBuffer();
            //source = Tinify.fromFile("unoptimized.jpg");
            //source.toFile("C:\\Users\\kuang\\Desktop\\1.png");
            // System.out.println("ok了");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;

    }


}
