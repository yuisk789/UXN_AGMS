package kr.co.uxn.agms.android.activity.signin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.uxn.agms.android.CommonConstant;
import kr.co.uxn.agms.android.R;
import kr.co.uxn.agms.android.api.ApiUtils;
import kr.co.uxn.agms.android.api.model.Gender;
import kr.co.uxn.agms.android.api.model.SignupBody;
import kr.co.uxn.agms.android.api.model.SignupResponse;
import kr.co.uxn.agms.android.common.StringUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class CreateAccountFragment extends Fragment {


    MaterialButton buttonNext;
    MaterialButton buttonCancel;

    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPasswordConfirm;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_account,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonNext = view.findViewById(R.id.button_do_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                doNext();
            }
        });
        buttonCancel = view.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                doCancel();
            }
        });
        editTextEmail = view.findViewById(R.id.edit_text_email);
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TextUtils.isEmpty(charSequence)){
                    editTextEmail.setError(getString(R.string.dialog_message_input_email));
                } else {
                    if(StringUtil.isEmail(editTextEmail.getText().toString())){
                        editTextEmail.setError(null);
                    } else {
                        editTextEmail.setError(getString(R.string.dialog_message_input_email));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTextPassword = view.findViewById(R.id.edit_text_password);
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TextUtils.isEmpty(charSequence)){
                    editTextPassword.setError(getString(R.string.dialog_message_input_password_pattern));
                } else {
                    if(isValid(charSequence.toString())){
                        editTextPassword.setError(null);
                        if(!TextUtils.isEmpty(editTextPasswordConfirm.getText())
                                && editTextPassword.getText().toString().equals(editTextPasswordConfirm.getText().toString())){
                            editTextPasswordConfirm.setError(null);
                        }
                    } else {
                        editTextPassword.setError(getString(R.string.dialog_message_input_password_pattern));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTextPasswordConfirm = view.findViewById(R.id.edit_text_password_confirm);
        editTextPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TextUtils.isEmpty(charSequence)){
                    editTextPasswordConfirm.setError(getString(R.string.dialog_message_input_password_pattern));
                } else {
                    if(isValid(charSequence.toString())){
                        Log.e("check","password:"+editTextPassword.getText());
                        Log.e("check","passwordConfirm:"+editTextPasswordConfirm.getText());
                        if(!TextUtils.isEmpty(editTextPassword.getText())
                                && editTextPassword.getText().toString().equals(editTextPasswordConfirm.getText().toString())){
                            editTextPasswordConfirm.setError(null);
                        } else {
                            editTextPasswordConfirm.setError(getString(R.string.dialog_message_input_same_password));
                        }

                    } else {
                        editTextPasswordConfirm.setError(getString(R.string.dialog_message_input_password_pattern));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void doCancel(){
        new AlertDialog.Builder(getContext()).setTitle(R.string.alert_title)
                .setMessage(R.string.alert_message_exit_sign_up)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((LoginActivity)getActivity()).changeToEula();
                    }
                }).show();
    }
    private void doNext(){
        Log.e("check","doNext");
        if(TextUtils.isEmpty(editTextEmail.getText()) ){
            showSimpleAlert(R.string.dialog_message_input_email);
            editTextEmail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(editTextPassword.getText()) ){
            showSimpleAlert(R.string.dialog_message_input_password);
            editTextPassword.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(editTextPasswordConfirm.getText()) ){
            showSimpleAlert(R.string.dialog_message_input_password_confirm);
            editTextPasswordConfirm.requestFocus();
            return;
        }
        if(!editTextPasswordConfirm.getText().toString().equals(editTextPassword.getText().toString())){
            Log.e("check","check password -----");
            Log.e("check",editTextPassword.getText().toString());
            Log.e("check",editTextPasswordConfirm.getText().toString());
            showSimpleAlert(R.string.dialog_message_input_password_not_equal);
            return;
        }
        SiginUpInformation info = ((LoginActivity)getActivity()).getSignUpInfo();
        info.setEmail(editTextEmail.getText().toString());
        info.setPassword(editTextPassword.getText().toString());

        SignupBody body = new SignupBody();
        body.setBirth(info.getBirthDate());
        body.setGender(info.getGender());
        body.setEmail(info.getEmail());
        body.setPassword(info.getPassword());
        body.setUser_id(info.getEmail());
        body.setUser_name(info.getName());

        ApiUtils.getApiInterface(getContext().getApplicationContext())
                .singnup(body).enqueue(new Callback<SignupResponse>() {
                    @Override
                    public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                        if(response!=null && response.isSuccessful() && response.body()!=null){
                            ((LoginActivity)getActivity()).changeToAccountCreated();
                        } else {
                            new AlertDialog.Builder(getContext())
                                    .setTitle(R.string.alert_title)
                                    .setMessage(R.string.dialog_error_signup)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SignupResponse> call, Throwable t) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.alert_title)
                                .setMessage(R.string.dialog_error_signup_fail)
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    }
                });

    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    //대소문자포함,숫자포함,특수문자포함  패턴
//    private static final String PASSWORD_PATTERN =
//            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    //숫자포함, 영문자 포함, 특수문자 포함 패턴
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    private void showSimpleAlert(int resId){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.alert_title)
                .setMessage(resId)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
