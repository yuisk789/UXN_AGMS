package kr.co.uxn.agms.android.api;

import java.util.HashMap;

import kr.co.uxn.agms.android.api.model.LoginBody;
import kr.co.uxn.agms.android.api.model.LoginResponse;
import kr.co.uxn.agms.android.api.model.SignupBody;
import kr.co.uxn.agms.android.api.model.SignupResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("/api/v1/user/sign-up/user/save")
    Call<SignupResponse> singnup(@Body SignupBody body);


    @POST("/api/v1/login/user")
    Call<String> login(@Body LoginBody body);
}
