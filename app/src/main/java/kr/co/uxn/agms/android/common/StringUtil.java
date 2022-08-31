package kr.co.uxn.agms.android.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static boolean isEmail(String input){
        boolean err = false;
        final String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        if(m.matches()) {
            err = true;
        }
        return err;
    }
}
