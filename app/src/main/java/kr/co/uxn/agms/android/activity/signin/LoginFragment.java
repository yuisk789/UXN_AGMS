package kr.co.uxn.agms.android.activity.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.button.MaterialButton;

import kr.co.uxn.agms.android.CommonConstant;
import kr.co.uxn.agms.android.R;
import kr.co.uxn.agms.android.activity.device.DeviceConnectActivity;
import kr.co.uxn.agms.android.api.ApiUtils;
import kr.co.uxn.agms.android.api.model.LoginBody;
import kr.co.uxn.agms.android.api.model.LoginResponse;
import kr.co.uxn.agms.android.api.model.SignupResponse;
import kr.co.uxn.agms.android.common.StringUtil;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    EditText emailEditText;
    EditText passwordEditText;
    AppCompatCheckBox checkBoxSaveId;
    AppCompatCheckBox checkBoxAutologin;
    MaterialButton loginButton;
    MaterialButton findPasswordButton;

    private boolean useSaveEmail = true;
    private boolean useAutoLogin = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(emailEditText.getText()) && !TextUtils.isEmpty(passwordEditText.getText())){
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
            }
        };
        emailEditText = view.findViewById(R.id.edit_text_email);
        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText = view.findViewById(R.id.edit_text_password);
        passwordEditText.addTextChangedListener(textWatcher);

        checkBoxSaveId = view.findViewById(R.id.checkbox_save_id);
        checkBoxSaveId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(useSaveEmail != b){
                    useSaveEmail = b;
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putBoolean(CommonConstant.PREF_SAVE_EMAIL,useSaveEmail).apply();
                }
            }
        });
        checkBoxAutologin = view.findViewById(R.id.checkbox_auto_login);
        checkBoxAutologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(useAutoLogin != b){
                    useAutoLogin = b;
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putBoolean(CommonConstant.PREF_AUTO_LOGIN,useAutoLogin).apply();
                }
            }
        });

        loginButton = view.findViewById(R.id.button_do_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                doLogin();
            }
        });
        findPasswordButton = view.findViewById(R.id.button_find_password);
        findPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                goFindPassword();
            }
        });

        loadFromPref();
    }

    private void loadFromPref(){
        useSaveEmail = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean(CommonConstant.PREF_SAVE_EMAIL,true);
        useAutoLogin = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean(CommonConstant.PREF_AUTO_LOGIN,true);
        String savedEmail = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(CommonConstant.PREF_USER_EMAIL,null);
        String password = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(CommonConstant.PREF_USER_PASSWORD,null);
        if(useSaveEmail){
            emailEditText.setText(savedEmail);
            checkBoxSaveId.setChecked(true);
        } else {
            checkBoxSaveId.setChecked(false);
        }

        if(useAutoLogin){
            checkBoxAutologin.setChecked(true);
            passwordEditText.setText(password);
            if(!TextUtils.isEmpty(savedEmail) && !TextUtils.isEmpty(password)){
                doLogin();
            }
        } else {
            checkBoxAutologin.setChecked(false);
        }
    }

    private void goFindPassword(){

//        Intent intent = new Intent(getContext(), FindPasswordActivity.class);
//        startActivity(intent);
    }
    private void showSimpleAlert(int resId){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.alert_title)
                .setMessage(resId)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
    private void doLogin(){
        Log.e("check","doLogin!");
        if(TextUtils.isEmpty(emailEditText.getText()) || !StringUtil.isEmail(emailEditText.getText().toString())){
            showSimpleAlert(R.string.dialog_message_input_email);
            return;
        }
        if(TextUtils.isEmpty(passwordEditText.getText())){
            showSimpleAlert(R.string.dialog_message_input_password);
            return;
        }

        ApiUtils.getApiInterface(getContext().getApplicationContext())
                .login(new LoginBody(emailEditText.getText().toString(), passwordEditText.getText().toString()))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e("check","login result:"+response.isSuccessful() + " / " + response.code() + ","+response.message());
                        if(response.isSuccessful()){
                            Headers headers = response.headers();
                            if(headers!=null){
                                for(String name : headers.names()){
//                                    Log.e("check","headers:"+name+" / " + headers.get(name));
                                    if(name.equalsIgnoreCase("auth_token")){
                                        Log.e("auth_token:",headers.get(name));
                                    } else if(name.equalsIgnoreCase("refresh_token")){
                                        Log.e("refresh_token:",headers.get(name));
                                    }
                                }
                            }
                            doWhenLogin();
                        } else {
                            showSimpleAlert(R.string.dialog_message_login_fail);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                        showSimpleAlert(R.string.dialog_message_login_error);
                    }
                });

    }
    private void doWhenLogin(){
        if(useSaveEmail){
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .edit().putString(CommonConstant.PREF_USER_EMAIL, emailEditText.getText().toString()).apply();
        } else {
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .edit().remove(CommonConstant.PREF_USER_EMAIL).apply();
        }
        if(useAutoLogin){
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .edit().putString(CommonConstant.PREF_USER_PASSWORD, passwordEditText.getText().toString()).apply();
        } else {
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .edit().remove(CommonConstant.PREF_USER_PASSWORD).apply();
        }

        try {
            if(getActivity()!=null){
                getActivity().finish();
                Intent intent = new Intent(getContext(), DeviceConnectActivity.class);
                startActivity(intent);
            }
        }catch (Exception e){}
    }

}
