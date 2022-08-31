package kr.co.uxn.agms.android.activity.launcher;

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

public class IntroFragment extends Fragment {
    private static final String ARGS_INDEX = "index";
    private int mIndex;

    public static IntroFragment getInstance(int index){
        IntroFragment fragment = new IntroFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mIndex = getArguments().getInt(ARGS_INDEX);
        int layout = R.layout.fragment_intro1;
        if(mIndex == 0){
            layout = R.layout.fragment_intro1;
        } else if(mIndex == 1){
            layout = R.layout.fragment_intro2;
        } else {
            layout = R.layout.fragment_intro3;
        }
        return inflater.inflate(layout,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mIndex == 0){
            try {
                PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
                String version = pInfo.versionName;
                TextView textView = view.findViewById(R.id.app_version);
                textView.setText(getResources().getString(R.string.app_version) + " " + version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            view.findViewById(R.id.buttonSkip).setOnClickListener(new View.OnClickListener() {
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
                    getActivity().finish();
                }
            });
        } else if(mIndex == 1){
            view.findViewById(R.id.buttonSkip).setOnClickListener(new View.OnClickListener() {
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
                    getActivity().finish();
                }
            });
        } else {
            view.findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {
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
                    getActivity().finish();
                }
            });
        }
    }
}
