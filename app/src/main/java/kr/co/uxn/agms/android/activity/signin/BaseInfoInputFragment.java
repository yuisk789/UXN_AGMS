package kr.co.uxn.agms.android.activity.signin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kr.co.uxn.agms.android.R;
import kr.co.uxn.agms.android.api.model.Gender;
import kr.co.uxn.agms.android.common.DatePickerFragment;

public class BaseInfoInputFragment extends Fragment {


    MaterialButton buttonNext;

    EditText editTextName;
    EditText editTextGender;
    EditText editTextBirthdate;

    int mYear;
    int mMonth;
    int mDate;
    int mGenderSelectedIndex=0;
    int currentSelectedGenderIndex=0;
    Calendar mCalendar = null;
    Gender mGender = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_base_info,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mYear = 1990;
        mMonth = Calendar.JANUARY;
        mDate = 1;

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

        editTextName = view.findViewById(R.id.edit_text_name);
        editTextGender = view.findViewById(R.id.edit_text_gender);
        editTextGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                showGenderSelector();
            }
        });
        editTextBirthdate = view.findViewById(R.id.edit_text_date);
        editTextBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                showDatePicker();
            }
        });

    }
    private void showDatePicker(){
        DatePickerFragment.getInstance(new DatePickerFragment.OnDateSelected() {
            @Override
            public void onDateSet(int year, int month, int date) {
                mYear = year;
                mMonth = month;
                mDate = date;
                mCalendar = Calendar.getInstance();
                mCalendar.set(mYear,mMonth,mDate);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
                editTextBirthdate.setText(format.format(mCalendar.getTime()));
            }
        }, mYear, mMonth, mDate).show(getChildFragmentManager(), "picker");
    }
    private void showGenderSelector(){
        new AlertDialog.Builder(getContext()).setTitle(R.string.alert_title_select_gender)
                .setSingleChoiceItems(R.array.gender_array, mGenderSelectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentSelectedGenderIndex = i;
                    }
                }).setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mGenderSelectedIndex = currentSelectedGenderIndex;
                        if(mGenderSelectedIndex==0){
                            mGender = Gender.MALE;
                        } else {
                            mGender = Gender.FEMALE;
                        }
                        editTextGender.setText(mGender.getKorean());
                    }
                }).show();
    }
    private void doNext(){
        if(TextUtils.isEmpty(editTextName.getText())){
            showSimpleAlert(R.string.dialog_message_input_name);
            return;
        }
        if(mGender==null){
            showSimpleAlert(R.string.dialog_message_select_gender);
            return;
        }
        if(mCalendar == null){
            showSimpleAlert(R.string.dialog_message_select_birth_date);
            return;
        }
        SiginUpInformation info = ((LoginActivity)getActivity()).getSignUpInfo();
        info.setName(editTextName.getText().toString());
        info.setYear(mYear);
        info.setMonth(mMonth);
        info.setDate(mDate);
        info.setBirthDate(editTextBirthdate.getText().toString());
        info.setGender(mGender.name());

        ((LoginActivity)getActivity()).changeToCreateAccount();
    }
    private void showSimpleAlert(int resId){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.alert_title)
                .setMessage(resId)
                .setPositiveButton(android.R.string.ok, null);
    }


}
