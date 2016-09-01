package com.wujie.coordinatorlayout;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wujie on 2016/9/1.
 */
public class AvatarImageBehavior extends CoordinatorLayout.Behavior<CircleImageView> {

    private final static float MIN_AVATAR_PERCENTAGE_SIZE = 0.3f;
    private final static float EXTRA_FINAL_AVATAR_PADDING = 80;

    private int mStartYPosition;
    private int mFinalYPosition;
    private int mStartHeight;
    private int mFinalHeight;
    private int mStartXPosition;
    private int mFinalXPosition;
    private float mStartToolbarPosition;

    private final Context mContext;
    private float mAvatarMaxSize;


    public AvatarImageBehavior(Context context, AttributeSet attributeSet) {
        mContext = context;
        init();
    }

    private void init() {
        bingDimensions();
    }

    private void bingDimensions() {
        mAvatarMaxSize = mContext.getResources().getDimension(R.dimen.image_width);

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
        shouldInitProperties(child, dependency);
        final int maxScroolDistance = (int) (mStartToolbarPosition - getStatusBarHeight());

        float expendedPercentageFactor = dependency.getY() / maxScroolDistance;

        float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                * (1f - expendedPercentageFactor)) + (child.getHeight() / 2);

        float distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
                * (1f - expendedPercentageFactor)) + (child.getWidth() / 2);

        float heightToSubtract = ((mStartHeight - mFinalHeight) * (1f - expendedPercentageFactor));

        child.setX(mStartYPosition - distanceYToSubtract);
        child.setY(mStartXPosition - distanceXToSubtract);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)child.getLayoutParams();
        lp.width = (int) (mStartHeight - heightToSubtract);
        lp.height = (int) (mStartHeight - heightToSubtract);
        child.setLayoutParams(lp);

        return super.onDependentViewChanged(parent, child, dependency);
    }

    private void shouldInitProperties(CircleImageView child, View dependency) {
        if(mStartYPosition == 0) {
            mStartHeight = (int) (child.getY()) + (child.getHeight() / 2);
        }
        if(mFinalYPosition == 0) {
            mFinalYPosition = (dependency.getHeight() / 2);
        }
        if(mStartHeight == 0) {
            mStartHeight = child.getHeight();
        }
        if(mFinalHeight == 0) {
            mFinalHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.image_final_width);
        }
        if(mStartXPosition == 0) {
            mStartXPosition = (int) (child.getX() + (child.getWidth()) / 2);
        }
        if(mFinalXPosition == 0) {
            mFinalXPosition = mContext.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + (mFinalHeight / 2);
        }
        if(mStartToolbarPosition == 0) {
            mStartToolbarPosition = dependency.getY() + (dependency.getHeight() / 2);
        }
    }

    public float getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelOffset(resourceId);
        }
        return result;
    }
}
