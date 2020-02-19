package cn.niter.forum.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TimeUtils {
    public static String timeStamp2Date(Long seconds,String format) {
              if(seconds == null || seconds==0 || seconds.equals("null")){
                       return "";
                 }
                if(format == null || format.isEmpty()){
                        format = "yyyy-MM-dd HH:mm:ss";
                    }
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                 return sdf.format(new Date(seconds));
            }

    public static Long date2TimeStamp(String date_str,String format){
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

}
