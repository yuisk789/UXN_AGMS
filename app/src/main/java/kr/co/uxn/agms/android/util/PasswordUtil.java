package kr.co.uxn.agms.android.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {
    private static final String PASSWORD_PATTERN = "^" +
            "(?=.*[0-9])" +         //at least 1 digit
//            "(?=.*[a-z])" +         //at least 1 lower case letter
//            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[!*~@#$%^+=?])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$";


    public enum ValidationResult {
        SUCCESS, ENTER_PASSWORD, ENTER_PASSWORD_CONFIRM, PASSWORD_CONFIRM_NOT_EQUAL,
        ERROR_WHEN_VERY_SHORT,
        SHORTER_THEN_REQUIRED_LENGTH,
        HAS_SPACE
        , ENTER_DIGIT,ENTER_ALPHABET, ENTER_LOWER_CASE,ENTER_UPPER_CASE,ENTER_SPECIAL_CHARACTER, REMOVE_WHITE_SPACES
        , ERROR
    }

    private static final int REQUIRED_LENGTH = 8;
    private static final int VERY_SHORT_SCORE = 2;

    private PasswordUtil(){}

    public static String getEncrypt(String password){
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static boolean verify(String password, String bcryptHashString){
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);
        return result.verified;
    }
    public static ValidationResult passwordValidation(CharSequence password, CharSequence passwordConfirm){
        if(TextUtils.isEmpty(password)){
            return ValidationResult.ENTER_PASSWORD;
        }

        ValidationResult result =  passwordValidation(password);
        if(result.equals(ValidationResult.SUCCESS)){
            if(TextUtils.isEmpty(passwordConfirm)){
                return ValidationResult.ENTER_PASSWORD_CONFIRM;
            } else {
                if(password.equals(passwordConfirm)){
                    return ValidationResult.SUCCESS;
                } else {
                    return ValidationResult.PASSWORD_CONFIRM_NOT_EQUAL;
                }
            }
        } else {
            return result;
        }
    }

    public static ValidationResult passwordValidation(CharSequence password){

        if(TextUtils.isEmpty(password)){
            return ValidationResult.ENTER_PASSWORD;
        }

        boolean isMatch = false;
        try{
            Pattern pattern;
            Matcher matcher;

            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);
            isMatch = matcher.matches();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(isMatch){
            return ValidationResult.SUCCESS;
        }

        int currentScore = 0;
        boolean sawUpper = false;
        boolean sawLower = false;
        boolean sawDigit = false;
        boolean sawSpecial = false;
        boolean hasEmpty = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if(Character.isSpaceChar(c)){
                hasEmpty = true;
                continue;
            }
            if (!sawSpecial && !Character.isLetterOrDigit(c)) {
                sawSpecial = true;
                currentScore += 1;
            } else {
                if (!sawDigit && Character.isDigit(c)) {
                    sawDigit = true;
                    currentScore += 1;
                } else {
                    if(!sawLower && Character.isAlphabetic(c)){
                        sawLower = true;
                        currentScore += 1;
                    }
                    /* if (!sawUpper && Character.isUpperCase(c)){
                        sawUpper = true;
                        currentScore += 1;
                    } else if(!sawLower && Character.isLowerCase(c)){
                        sawLower = true;
                        currentScore += 1;
                    } */
                }
            }
        }


        if(currentScore < VERY_SHORT_SCORE){
            return ValidationResult.ERROR_WHEN_VERY_SHORT;
        } else {
            if(hasEmpty){
                return ValidationResult.HAS_SPACE;
            }
            if(sawDigit && sawSpecial && sawLower
//                    && sawUpper
            ){
                if(password.length()< REQUIRED_LENGTH){
                    return ValidationResult.SHORTER_THEN_REQUIRED_LENGTH;
                }
                return ValidationResult.ENTER_LOWER_CASE;
            } else {
                if(!sawSpecial){
                    return ValidationResult.ENTER_SPECIAL_CHARACTER;
                }
                if(!sawDigit){
                    return ValidationResult.ENTER_DIGIT;
                }
                if(!sawLower){
                    return ValidationResult.ENTER_LOWER_CASE;
                }
//                if(!sawUpper){
//                    return ValidationResult.ENTER_UPPER_CASE;
//                }
            }
        }



        return ValidationResult.ERROR;
    }
}
