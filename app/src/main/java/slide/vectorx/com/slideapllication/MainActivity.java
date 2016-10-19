package slide.vectorx.com.slideapllication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager mPager;

    @BindView(R.id.layoutDots)
    LinearLayout mLinear;

    @BindView(R.id.btn_next)
    Button mNext;

    @BindView(R.id.btn_skip)
    Button mSkip;

    private TextView[] dots;
    private MyViewPagerAdapter adapter;
    private int[] layouts = {R.layout.welcome1, R.layout.welcome2, R.layout.welcome3, R.layout.welcome4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (PrefManager.getBoolean(this, "first"))
            luanchHome();


        if (Build.VERSION.SDK_INT >= 21)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        addBottomDots(0);

        changeStatusBarColor();

        adapter = new MyViewPagerAdapter(this);
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(viewPager);


    }

    @OnClick({R.id.btn_next, R.id.btn_skip})
    void click(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    mPager.setCurrentItem(current);
                } else {
                    luanchHome();
                }
                break;
            default:
                luanchHome();
        }
    }

    private int getItem(int i) {
        return mPager.getCurrentItem() + i;
    }


    ViewPager.OnPageChangeListener viewPager = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                mNext.setText(getString(R.string.start));
                mSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                mNext.setText(getString(R.string.next));
                mSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void addBottomDots(int current) {
        dots = new TextView[layouts.length];

        int[] active = getResources().getIntArray(R.array.array_dot_active);
        int[] inactive = getResources().getIntArray(R.array.array_dot_inactive);

        mLinear.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextColor(inactive[current]);
            dots[i].setTextSize(35);
            mLinear.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[current].setTextColor(active[current]);


    }

    void luanchHome() {
       // PrefManager.setBoolean(this,"first",true);
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }
}
