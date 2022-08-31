package kr.co.uxn.agms.android.activity.signup;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import kr.co.uxn.agms.android.R;
import kr.co.uxn.agms.android.activity.launcher.IntroActivity;
import kr.co.uxn.agms.android.activity.launcher.IntroFragment;

public class ManualActivity extends AppCompatActivity {

    ViewPager2 mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager2);
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(new IntroFragmentStateAdapter(this));
    }


    private long Timeback;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - Timeback > 1000) {
            Timeback = System.currentTimeMillis();
            Toast.makeText(this, R.string.back_press_to_exit, Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

    class IntroFragmentStateAdapter extends FragmentStateAdapter {
        public IntroFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return ManualFragment.getInstance(position);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    public void changeStep(int step){
        mViewPager.setCurrentItem(step);
    }
}
