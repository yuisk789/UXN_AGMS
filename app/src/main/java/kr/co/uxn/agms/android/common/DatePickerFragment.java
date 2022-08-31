package kr.co.uxn.agms.android.common;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String ARGS_YEAR = "year";
    private static final String ARGS_MONTH = "month";
    private static final String ARGS_DATE = "date";

    private OnDateSelected mListener;

    public static DatePickerFragment getInstance(OnDateSelected listener,int year,int month,int date){
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_YEAR, year);
        bundle.putInt(ARGS_MONTH, month);
        bundle.putInt(ARGS_DATE, date);
        fragment.setArguments(bundle);
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        int year = getArguments().getInt(ARGS_YEAR);
        int month = getArguments().getInt(ARGS_MONTH);
        int day = getArguments().getInt(ARGS_DATE);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog =  new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mListener = null;
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mListener = null;
            }
        });
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
        if(mListener!=null){
            mListener.onDateSet(year,month,date);
        }
        dismiss();
    }

    public interface OnDateSelected {
        void onDateSet(int year, int month, int date);
    }
}
