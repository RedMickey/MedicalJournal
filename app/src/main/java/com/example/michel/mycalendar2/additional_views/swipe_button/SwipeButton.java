package com.example.michel.mycalendar2.additional_views.swipe_button;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.R;

public class SwipeButton extends RelativeLayout{
    private ImageView slidingButton;
    private float initialX;
    private int state;
    private TextView centerText;
    private OnStateChangeListener onStateChangeListener;
    private OnActiveListener onActiveListener;

    private Drawable disabledDrawable;
    private Drawable enabledDrawable;

    public SwipeButton(Context context) {
        super(context);

        init(context, null, -1, -1);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, -1, -1);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, -1);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        RelativeLayout background = new RelativeLayout(context);

        LayoutParams layoutParamsView = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParamsView.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        background.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_rounded));

        addView(background, layoutParamsView);

        state = 0;
        /*final TextView centerText = new TextView(context);
        this.centerText = centerText;

        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        centerText.setText("SWIPE"); //добавьте текст, который вам нужен
        centerText.setTextColor(Color.WHITE);
        centerText.setPadding(65, 65, 65, 65);
        background.addView(centerText, layoutParams);*/
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                200);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        background.setLayoutParams(layoutParams);

        final ImageView acceptIV = new ImageView(context);
        LayoutParams layoutParamsAcceptIV = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsAcceptIV.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParamsAcceptIV.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        acceptIV.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_confirm));
        acceptIV.setPadding(0,0,50,0);
        addView(acceptIV, layoutParamsAcceptIV);

        final ImageView cancelIV = new ImageView(context);
        LayoutParams layoutParamsCancelIV = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsCancelIV.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParamsCancelIV.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        cancelIV.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_clear));
        cancelIV.setPadding(50,0,0,0);
        addView(cancelIV, layoutParamsCancelIV);

        final ImageView swipeButton = new ImageView(context);
        this.slidingButton = swipeButton;

        disabledDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_pill_c_96);
        enabledDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_pill_c_96);

        slidingButton.setImageDrawable(disabledDrawable);
        slidingButton.setPadding(20, 20, 20, 20);
        LayoutParams layoutParamsButton = new LayoutParams(
                200,
                200);

        layoutParamsButton.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layoutParamsButton.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        swipeButton.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_button));
        swipeButton.setImageDrawable(disabledDrawable);
        addView(swipeButton, layoutParamsButton);
        //initialCenterX = getWidth()/2- swipeButton.getWidth()/2;
        setOnTouchListener(getButtonTouchListener());
    }



    private OnTouchListener getButtonTouchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (initialX == 0) {
                            initialX = slidingButton.getX();
                        }

                        if (event.getX() > slidingButton.getWidth() / 2 &&
                                event.getX() + slidingButton.getWidth() / 2 < getWidth()) {
                            slidingButton.setX(event.getX() - slidingButton.getWidth() / 2);
                            //centerText.setAlpha(1 - 1.3f * (slidingButton.getX() + slidingButton.getWidth()) / getWidth());
                        }

                        if (event.getX() + slidingButton.getWidth() / 2 > getWidth() &&
                                slidingButton.getX() + slidingButton.getWidth() / 2 < getWidth()) {
                            slidingButton.setX(getWidth() - slidingButton.getWidth());
                        }

                        if (event.getX() < slidingButton.getWidth() / 2) {
                            slidingButton.setX(0);
                        }

                        return true;
                    case MotionEvent.ACTION_UP:
                        if(slidingButton.getX()<getWidth()*0.25){
                            expandButton(0);

                        } else if (slidingButton.getX() + slidingButton.getWidth() > getWidth() * 0.75){
                            expandButton(1);
                        } else {
                            moveButtonBack();
                        }

                        return true;
                }

                return false;
            }
        };
    }

    private void expandButton(final int direction){
        LayoutParams layoutParams = new LayoutParams(
                200,
                200);
        if (direction==0){
            slidingButton.setBackground(getResources().getDrawable(R.drawable.shape_button_rej));
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        }
        else {
            slidingButton.setBackground(getResources().getDrawable(R.drawable.shape_button_acc));
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        }

        slidingButton.setLayoutParams(layoutParams);

        final ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(slidingButton.getX(), 0);
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) positionAnimator.getAnimatedValue();
                slidingButton.setX(x);
            }
        });

        final ValueAnimator widthAnimator = ValueAnimator.ofInt(
                slidingButton.getWidth(),
                getWidth());

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params = slidingButton.getLayoutParams();
                params.width = (Integer) widthAnimator.getAnimatedValue();
                slidingButton.setLayoutParams(params);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (direction==0)
                    state=1;
                else
                    state=2;
                if (onStateChangeListener != null) {
                    onStateChangeListener.onStateChange(state);
                }

                if (onActiveListener != null) {
                    onActiveListener.onActive();
                }
                //slidingButton.setImageDrawable(enabledDrawable);
            }
        });

        animatorSet.playTogether(positionAnimator, widthAnimator);
        animatorSet.start();
    }

    private void moveButtonBack() {
        final ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(slidingButton.getX(), getWidth()/2- slidingButton.getWidth()/2);
        float p = getWidth();
        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) positionAnimator.getAnimatedValue();
                slidingButton.setX(x);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                centerText, "alpha", 1);

        positionAnimator.setDuration(200);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, positionAnimator);
        animatorSet.start();
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public void setOnActiveListener(OnActiveListener onActiveListener) {
        this.onActiveListener = onActiveListener;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
