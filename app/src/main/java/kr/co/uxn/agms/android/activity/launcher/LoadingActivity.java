package kr.co.uxn.agms.android.activity.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import kr.co.uxn.agms.android.CommonConstant;
import kr.co.uxn.agms.android.R;
import kr.co.uxn.agms.android.activity.signin.LoginActivity;
import kr.co.uxn.agms.android.databinding.ActivityLoadingBinding;


public class LoadingActivity extends AppCompatActivity {

    private ActivityLoadingBinding binding;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.circleCenter.animate().rotation(720f).setDuration(1000).start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                     goNextActivity();
            }
        },1000);
        Log.e("check","end");
    }

    private void goNextActivity(){
        Log.e("check","go to laucher");
        Intent intent = null;
        if(isSkipIntro()){
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, IntroActivity.class);
        }

        startActivity(intent);
        finish();
    }
    private boolean isSkipIntro(){
        return PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(CommonConstant.PREF_SKIP_INTRO,false);
    }

    private long Timeback;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - Timeback > 1000) {
            Timeback = System.currentTimeMillis();
            Toast.makeText(this, R.string.back_press_to_exit, Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }
}
