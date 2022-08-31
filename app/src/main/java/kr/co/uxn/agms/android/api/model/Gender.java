package kr.co.uxn.agms.android.api.model;

public enum Gender {
    MALE,FEMALE;
    public String getKorean(){
        if(this.ordinal() == 0){
            return "남성";
        } else {
            return "여성";
        }
    }
}
