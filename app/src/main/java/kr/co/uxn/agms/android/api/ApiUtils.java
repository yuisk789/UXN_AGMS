package kr.co.uxn.agms.android.api;

import android.content.Context;

import java.net.CookieHandler;
import java.net.CookieManager;

import kr.co.uxn.agms.android.CommonConstant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiUtils {
    public static final String SERVER_URL = CommonConstant.TEST_SERVER_IP + CommonConstant.API_PORT;

    private static Retrofit retrofit = null;
    private static OkHttpClient mHttpClient = null;

    public static OkHttpClient getHttpClient(final Context applicationContext){
        if(mHttpClient==null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            mHttpClient = httpClient.build();
        }
        return mHttpClient;
    }

    public static ApiInterface getApiInterface(final Context applicationContext) {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient(applicationContext))
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}
