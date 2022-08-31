package kr.co.uxn.agms.android.activity.signin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import kr.co.uxn.agms.android.R;

public class EulaFragment extends Fragment {

    MaterialCheckBox checkBox1;
    MaterialCheckBox allTermsCheckbox;

    MaterialButton button_disagree;
    MaterialButton button_agree;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eula,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkBox1 = view.findViewById(R.id.checkbox_eula_1);
        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllTerms();
            }
        });
        allTermsCheckbox = view.findViewById(R.id.checkbox_final_terms);
        allTermsCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllTerms(allTermsCheckbox.isChecked());
            }
        });
        button_disagree = view.findViewById(R.id.button_disagree);
        button_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                doDisagree();
            }
        });
        button_agree = view.findViewById(R.id.button_agree);
        button_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                doAgree();

            }
        });
    }
    private void doDisagree(){
        ((LoginActivity)getActivity()).changeToLogin();
    }
    private void doAgree(){
        checkAllTerms();
        Log.e("check","allTermsCheckbox:"+allTermsCheckbox.isChecked());
        if(allTermsCheckbox.isChecked()){
            Log.e("check","changeToTerms");
            ((LoginActivity)getActivity()).changeToTerms();
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.alert_title)
                    .setMessage(R.string.dialog_message_need_agree_eula)
                    .setPositiveButton(android.R.string.ok, null).show();
        }
    }

    private void checkAllTerms(){
        Log.e("check","checkBox1.isChecked():"+checkBox1.isChecked());
        Log.e("check","allTermsCheckbox.isChecked():"+allTermsCheckbox.isChecked());
        if(!checkBox1.isChecked()){
            allTermsCheckbox.setChecked(false);
            return;
        }
        allTermsCheckbox.setChecked(true);
    }
    private void setAllTerms(boolean checked){
        Log.e("check","setAllTerms:"+checked);
        Log.e("check","checkBox1.isChecked():"+checkBox1.isChecked());
        checkBox1.setChecked(checked);
        Log.e("check","checkBox1.isChecked():"+checkBox1.isChecked());
    }
}
