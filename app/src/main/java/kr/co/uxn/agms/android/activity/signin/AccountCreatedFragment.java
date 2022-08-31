package kr.co.uxn.agms.android.activity.signin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.uxn.agms.android.R;

public class AccountCreatedFragment extends Fragment {


    MaterialButton buttonNext;

    MaterialButton buttonRegisterHospital;
    MaterialButton buttonInputHospitalDirect;

    String mHospital;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_created,null);
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
        buttonInputHospitalDirect = view.findViewById(R.id.button_input_hospital);
        buttonInputHospitalDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                doInputHospital();
            }
        });
        buttonRegisterHospital = view.findViewById(R.id.button_request_hospital);
        buttonRegisterHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                registerHospital();
            }
        });

    }
    private void registerHospital(){
        //todo add function
    }

    private void doInputHospital(){
        //todo add function
    }

    private void doNext(){

//        if(!TextUtils.isEmpty(mHospital)){
//            showSimpleAlert(R.string.dialog_message_input_hospital);
//            return;
//        }
//        SiginUpInformation info = ((LoginActivity)getActivity()).getSignUpInfo();
//        info.setHospitalName(mHospital);
//        //todo call api
        ((LoginActivity)getActivity()).changeToSetting();
    }



    private void showSimpleAlert(int resId){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.alert_title)
                .setMessage(resId)
                .setPositiveButton(android.R.string.ok, null);
    }
}
