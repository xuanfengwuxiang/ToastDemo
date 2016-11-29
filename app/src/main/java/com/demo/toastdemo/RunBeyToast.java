package com.demo.toastdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.duration;

/**
 * Created by 朱建华 on 2016/11/28.
 */

public class RunBeyToast {
    private Context mContext;
    String mText;
    private int mBackgroundColor = Color.WHITE;
    private int mAlpha = 255;
    private int mImageResource = 0;
    private int showTime;
    private Toast mToast;
    private RelativeLayout mRl_toast_withimage;
    private ImageView mIv_toast_withimage;
    private TextView mTv_toast_withimage;
    private RelativeLayout mRl_toast_noimage;
    private TextView mTv_toast_noimage;
    private View mView;
    private Handler mHandler = new Handler();
    private boolean canceled = true;
    private TimeCount timeCount;
    private int mRl_toast_withimage_width;
    private int mRl_toast_withimage_height;


    //构造方法多种参数重载
    public RunBeyToast(Context context, int
            showTime, String text) {
        mContext = context;
        this.showTime = showTime;
        mText = text;
    }

    public RunBeyToast(Context context, int
            showTime, String text, int backgroundColor) {
        mContext = context;
        mBackgroundColor = backgroundColor;
        this.showTime = showTime;
        mText = text;
    }


    public RunBeyToast(Context context, int
            showTime, String text, int backgroundColor, int alpha) {
        mContext = context;
        mAlpha = alpha;
        this.showTime = showTime;
        mText = text;
        mBackgroundColor = backgroundColor;

    }

    public RunBeyToast(Context context, int
            showTime, String text,int backgroundColor, int alpha ,int imageResource) {
        mContext = context;
        mBackgroundColor = backgroundColor;
        mAlpha = alpha;
        mImageResource = imageResource;
        this.showTime = showTime;
        mText = text;
    }

    //show吐司
    public void show() {
        initView();
        initData();
    }

    //初始化View
    private void initView() {
        mView = View.inflate(mContext, R.layout.layout_toast, null);
        mRl_toast_withimage = (RelativeLayout) mView.findViewById(R.id.rl_toast_withimage);
        mIv_toast_withimage = (ImageView) mView.findViewById(R.id.iv_toast_withimage);
        mTv_toast_withimage = (TextView) mView.findViewById(R.id.tv_toast_withimage);
        mRl_toast_noimage = (RelativeLayout) mView.findViewById(R.id.rl_toast_noimage);
        mTv_toast_noimage = (TextView) mView.findViewById(R.id.tv_toast_noimage);
    }

    //设置吐司的数据
    private void initData() {
        mToast = new Toast(mContext);
        //默认不显示图片
        if (mImageResource==0){
            showTextToast();
        }else{
            showImageAndTextToast();
        }

    }


    //show文字吐司
    private void showTextToast() {
        mRl_toast_withimage.setVisibility(View.GONE);
        mRl_toast_noimage.setVisibility(View.VISIBLE);
        setViewCornersRadiusAndBackground(mRl_toast_noimage);
        mRl_toast_noimage.getBackground().setAlpha(mAlpha);
        mTv_toast_noimage.setText(mText);
        //动态获取mRl_toast_noimage的宽高
        mRl_toast_withimage_width = getDynamicWidthOrHeight(mTv_toast_noimage,"width");
        mRl_toast_withimage_height = getDynamicWidthOrHeight(mTv_toast_noimage,"height");
        //设置mRl_toast_noimage最大宽度
        if (mRl_toast_withimage_width>dip2px(mContext,200)){
            mRl_toast_withimage_width = dip2px(mContext,200);
            initViewSize(mTv_toast_noimage,mRl_toast_withimage_width,mRl_toast_withimage_height);
        }
        //设置mRl_toast_noimage最大高度
        if (mRl_toast_withimage_height>dip2px(mContext,98)){
            mRl_toast_withimage_height = dip2px(mContext,98);
            initViewSize(mTv_toast_noimage,mRl_toast_withimage_width,mRl_toast_withimage_width);
        }
        mToast.setView(mView);
        mToast.setGravity(Gravity.CENTER,0,0);
        //Toast显示定时
        timeCount = new TimeCount(showTime, 1000);
        if (canceled) {
            timeCount.start();
            canceled = false;
            showUntilCancel();
        }
    }

    //show图片吐司
    private void showImageAndTextToast() {
        mRl_toast_withimage.setVisibility(View.VISIBLE);
        mRl_toast_noimage.setVisibility(View.GONE);
        setViewCornersRadiusAndBackground(mRl_toast_withimage);
        mRl_toast_withimage.getBackground().setAlpha(mAlpha);
        mIv_toast_withimage.setImageResource(mImageResource);
        mTv_toast_withimage.setText(mText);
        mToast.setView(mView);
        mToast.setGravity(Gravity.CENTER,0,0);
        timeCount = new TimeCount(showTime, 1000);
        if (canceled) {
            timeCount.start();
            canceled = false;
            showUntilCancel();
        }
    }

    /**
     *  自定义计时器
     */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); //millisInFuture总计时长，countDownInterval时间间隔(一般为1000ms)
        }

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            hide();
        }
    }

    private void showUntilCancel() {
        if (canceled) { //如果已经取消显示，就直接return
            return;
        }
        mToast.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showUntilCancel();
            }
        }, Toast.LENGTH_LONG);
    }
    /**
     * 隐藏toast
     */
    private void hide() {
        if (mToast != null) {
            mToast.cancel();
        }
        canceled = true;

    }
    //动态获取宽高的方法
    private int getDynamicWidthOrHeight(View tv_toast_noimage,String widthOrHeight) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv_toast_noimage.measure(spec,spec);
        if ("width".equals(widthOrHeight)){
            return tv_toast_noimage.getMeasuredWidth();
        }
        if ("height".equals(widthOrHeight)){
            return tv_toast_noimage.getMeasuredHeight();
        }
        return tv_toast_noimage.getMeasuredWidth();
    }

    //设置控件的宽高
    public void initViewSize(View view, int width, int height) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
//        params.leftMargin = 0;
//        params.topMargin = 0;
        view.setLayoutParams(params);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置圆角和背景颜色
     *
     * @param view
     */
    private void setViewCornersRadiusAndBackground(View view) {
        if (view != null) {
//        int strokeWidth = 5; // 边框宽度
            int roundRadius = (int) mContext.getResources().getDimension(R.dimen.tip_corners_radius); // 圆角半径
//        int strokeColor = Color.parseColor("#2E3135");//边框颜色
//        int fillColor = Color.parseColor("#DFDFE0");//内部填充颜色
            GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
            gradientDrawable.setColor(mBackgroundColor);//内部填充颜色
            gradientDrawable.setCornerRadius(roundRadius);
            gradientDrawable.setCornerRadii(new float[]{roundRadius, roundRadius, roundRadius, roundRadius, roundRadius, roundRadius, roundRadius,
                    roundRadius});//左上和右上圆角
//        gd.setStroke(strokeWidth, strokeColor);
            view.setBackgroundDrawable(gradientDrawable);
        }
    }

}
