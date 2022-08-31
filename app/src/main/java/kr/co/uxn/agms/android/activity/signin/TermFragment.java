package kr.co.uxn.agms.android.activity.signin;

import android.os.Bundle;
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

public class TermFragment extends Fragment {

    MaterialCheckBox allTermsCheckbox;

    MaterialButton button_disagree;
    MaterialButton button_agree;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_term,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allTermsCheckbox = view.findViewById(R.id.checkbox_final_terms);
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
        ((LoginActivity)getActivity()).changeToEula();
    }
    private void doAgree(){

        if(allTermsCheckbox.isChecked()){
            ((LoginActivity)getActivity()).changeToInputBaseInfo();
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.alert_title)
                    .setMessage(R.string.dialog_message_need_agree_eula)
                    .setPositiveButton(android.R.string.ok, null).show();
        }
    }


}
