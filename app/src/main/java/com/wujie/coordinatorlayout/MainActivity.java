package com.wujie.coordinatorlayout;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    @BindView(R.id.main_iv_placeholder)
    ImageView mIvPlaceholder;

    @BindView(R.id.main_ll_title_container)
    LinearLayout mLltelContainer;

    @BindView(R.id.main_fl_title)
    FrameLayout mFlTlTitleContainer;

    @BindView(R.id.main_abl_app_bar)
    AppBarLayout mAblAppbar;

    @BindView(R.id.main_tv_toolbar_title)
    TextView mTvToolbarTitle;

    @BindView(R.id.main_tb_toolbar)
    Toolbar mTbToobar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTbToobar.setTitle("");
        
        mAblAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) /(float) maxScroll;
                handleAlphaOnTitle(percentage);
                hadleToolbarTitleVisibility(percentage);
            }
        });
        
        initParallaxValues();
    }

    private void initParallaxValues() {

        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) mIvPlaceholder.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mFlTlTitleContainer.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);


        mIvPlaceholder.setLayoutParams(petDetailsLp);
        mFlTlTitleContainer.setLayoutParams(petBackgroundLp);
    }

    private void hadleToolbarTitleVisibility(float percentage) {
        if(percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if(mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void startAlphaAnimation(View v, int alphaAnimationsDuration, int visible) {
        AlphaAnimation alphaAnimation = (visible == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(alphaAnimationsDuration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);

    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLltelContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLltelContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }
}
