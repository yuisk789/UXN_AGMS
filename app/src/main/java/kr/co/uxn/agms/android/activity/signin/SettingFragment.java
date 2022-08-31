package kr.co.uxn.agms.android.activity.signin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.button.MaterialButton;

import kr.co.uxn.agms.android.CommonConstant;
import kr.co.uxn.agms.android.MainActivity;
import kr.co.uxn.agms.android.R;
import kr.co.uxn.agms.android.activity.signup.WelcomeActivity;
import kr.co.uxn.agms.android.api.model.Gender;

public class SettingFragment extends Fragment {


    MaterialButton buttonComplete;

    View wrapUnit;
    View wrapTarget;
    View wrapAlarm;

    TextView unitValue;
    TextView targetValue;
    TextView alarmValue;

    private int currentAlarmIndex = 0;
    private int mAlarmIndex = -1;
    private int mTargetMin = CommonConstant.MIN_TARGET_VALUE;
    private int mTargetMax = CommonConstant.MAX_TARGET_VALUE;

    private int currentMin = 0;
    private int currentMax = 0;

    private int mUnitIndex = -1;
    private int currentUnitIndex = 0;
    private int currentTargetIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unitValue = view.findViewById(R.id.value_unit);
        targetValue = view.findViewById(R.id.target_value);
        alarmValue = view.findViewById(R.id.alarm_value);

        buttonComplete = view.findViewById(R.id.button_complete);
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                doComplete();
            }
        });
        wrapAlarm = view.findViewById(R.id.wrap_alarm);
        wrapAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                alarmClick();
            }
        });
        wrapTarget = view.findViewById(R.id.wrap_target);
        wrapTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                targetClick();
            }
        });
        wrapUnit = view.findViewById(R.id.wrap_unit);
        wrapUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                },200);
                unitClick();
            }
        });
        loadFromPref();
    }
    private void loadFromPref(){
        mAlarmIndex = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getInt(CommonConstant.PREF_SETTING_ALARM,0);
        currentAlarmIndex = mAlarmIndex;
        mUnitIndex = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getInt(CommonConstant.PREF_SETTING_UNIT,0);
        currentUnitIndex = mUnitIndex;
        mTargetMax = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getInt(CommonConstant.PREF_SETTING_TARGET_MAX,CommonConstant.MAX_TARGET_VALUE);

        mTargetMin = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getInt(CommonConstant.PREF_SETTING_TARGET_MIN,CommonConstant.MIN_TARGET_VALUE);
        currentMin = mTargetMin;
        currentMax = mTargetMax;
    }

    private void unitClick(){
        new AlertDialog.Builder(getContext()).setTitle(R.string.alert_title_select_unit)
                .setSingleChoiceItems(R.array.unit_setting, mUnitIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentUnitIndex = i;
                    }
                }).setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mUnitIndex = currentUnitIndex;
                        unitValue.setText(getResources().getStringArray(R.array.unit_setting)[mUnitIndex]);
                    }
                }).show();

    }
    private void showMinSelectDialog(){

        String[] array = new String[200];
        for(int i=0;i<array.length;i++){
            array[i] = String.valueOf(i+1);
        }
        new AlertDialog.Builder(getContext()).setTitle(R.string.alert_title_select_target_min)
                .setSingleChoiceItems(array, mTargetMin-1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentTargetIndex = i;
                    }
                }).setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mTargetMin = currentTargetIndex;
                        showMaxSelectDialog();
                    }
                }).show();
    }

    private void showMaxSelectDialog(){
        String[] array = new String[200];
        for(int i=0;i<array.length;i++){
            array[i] = String.valueOf(i+1);
        }
        new AlertDialog.Builder(getContext()).setTitle(R.string.alert_title_select_target_max)
                .setSingleChoiceItems(array, mTargetMax-1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentTargetIndex = i;
                    }
                }).setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mTargetMax = currentTargetIndex;
                        targetValue.setText(mTargetMin + "-" + mTargetMax);
                    }
                }).show();
    }

    private void targetClick(){
        showMinSelectDialog();

    }

    private void alarmClick(){
        new AlertDialog.Builder(getContext()).setTitle(R.string.alert_title_select_alarm)
                .setSingleChoiceItems(R.array.alarm_setting, mAlarmIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentAlarmIndex = i;
                    }
                }).setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAlarmIndex = currentAlarmIndex;
                        alarmValue.setText(getResources().getStringArray(R.array.alarm_setting)[mAlarmIndex]);
                    }
                }).show();
    }
    private void savePref(){
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit()
                .putInt(CommonConstant.PREF_SETTING_ALARM, mAlarmIndex)
                .putInt(CommonConstant.PREF_SETTING_TARGET_MIN, mTargetMin)
                .putInt(CommonConstant.PREF_SETTING_TARGET_MAX, mTargetMax)
                .putInt(CommonConstant.PREF_SETTING_UNIT, mUnitIndex)
                .apply();
    }

    private void doComplete(){
        savePref();
        SiginUpInformation info = ((LoginActivity)getActivity()).getSignUpInfo();
        //todo call api
        getActivity().finish();
        Intent intent = new Intent(getContext(), WelcomeActivity.class);
        startActivity(intent);
    }



    private void showSimpleAlert(int resId){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.alert_title)
                .setMessage(resId)
                .setPositiveButton(android.R.string.ok, null);
    }
}
