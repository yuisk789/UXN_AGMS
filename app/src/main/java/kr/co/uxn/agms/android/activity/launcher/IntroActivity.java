package kr.co.uxn.agms.android.activity.launcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import kr.co.uxn.agms.android.R;

public class IntroActivity extends AppCompatActivity {

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
            return IntroFragment.getInstance(position);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

}
