package utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant
{
    public static final String URL = "http://site3.bidbch.com/api/";
    public static String TOTAL_BALANCE = "";
    public static String MEMBER_ID = "";
    public static String IMG_URL = "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";


    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
