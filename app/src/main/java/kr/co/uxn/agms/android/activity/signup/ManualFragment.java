package kr.co.uxn.agms.android.activity.signup;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.co.uxn.agms.android.R;
import kr.co.uxn.agms.android.activity.signin.LoginActivity;

public class ManualFragment extends Fragment {
    private static final String ARGS_INDEX = "index";
    private int mIndex;

    public static ManualFragment getInstance(int index){
        ManualFragment fragment = new ManualFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mIndex = getArguments().getInt(ARGS_INDEX);
        int layout = R.layout.fragment_manual1;
        if(mIndex == 0){
            layout = R.layout.fragment_manual1;
        } else if(mIndex == 1){
            layout = R.layout.fragment_manual2;
        } else {
            layout = R.layout.fragment_manual3;
        }
        return inflater.inflate(layout,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mIndex == 0){


            view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setClickable(false);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setClickable(true);
                        }
                    },200);

                    try {
                        ((ManualActivity)getActivity()).changeStep(1);
                    }catch (Exception e){}
                }
            });
        } else if(mIndex == 1){
            view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setClickable(false);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setClickable(true);
                        }
                    },200);

                    try {
                        ((ManualActivity)getActivity()).changeStep(2);
                    }catch (Exception e){}
                }
            });
        } else {
            view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setClickable(false);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setClickable(true);
                        }
                    },200);

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finishAffinity();
                }
            });
        }
    }
}
