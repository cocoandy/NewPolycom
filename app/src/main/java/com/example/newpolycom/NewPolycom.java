package com.example.newpolycom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class NewPolycom extends View {
    private Bitmap mBitmapBg;
    private Bitmap mBitmap;
    private Paint mPaint;

    private int mWidth = 360;
    private int mHeight = 360;

    private int mCenterWidth = 200;//中心按钮宽
    private int mCenterHeight = 200;//中心按钮高

    private int mCenterX = 111;//中心按钮位置
    private int mCenterY = 111;//中心按钮位置
    private int mCenterHaltX = 0;//中心按钮位置
    private int mCenterHaltY = 0;//中心按钮位置

    private Rect mBgRect;
    private Rect mCenterRect;

    private String mNumberTop = "";
    private String mNumberBottom = "";
    private String mNumberLeft = "";
    private String mNumberRight = "";

    private int mLimitCenterX = 137;//圆球中心区域（实际圆球的大小）
    private int mLimitCenterY = 137;//圆球中心区域（实际圆球的大小）
    private int mLimitMinX;
    private int mLimitMaxX;
    private int mLimitMinY;
    private int mLimitMaxY;


    public NewPolycom(Context context) {
        this(context, null);
    }

    public NewPolycom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewPolycom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBitmapBg = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycim_bg);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycim_ctl);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        mCenterHaltX = mCenterX / 2;
        mCenterHaltY = mCenterY / 2;
        mBgRect = new Rect(0, 0, mWidth, mHeight);
        mCenterRect = new Rect(0, 0, mCenterWidth, mCenterHeight);

        mLimitMinX = mLimitCenterX / 2;
        mLimitMaxX = mWidth - mLimitCenterX / 2;
        mLimitMinY = mLimitCenterY / 2;
        mLimitMaxY = mWidth - mLimitCenterY / 2;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setAlpha(1.f);
        } else {
            setAlpha(0.4f);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(mBitmapBg, mBgRect, mBgRect, mPaint);
        int mCenterX = getBitmapRectX(this.mCenterX);
        int mCenterY = getBitmapRectY(this.mCenterY);
        canvas.drawBitmap(mBitmap, mCenterRect, new Rect(mCenterX - mCenterHaltX, mCenterY - mCenterHaltY, mCenterX + mCenterHaltX, mCenterY + mCenterHaltY), mPaint);
    }

    public int getBitmapRectX(int mCenterX) {
        if (mCenterX < mLimitMinX) {
            return mLimitMinX;
        } else if (mCenterX > mLimitMaxX) {
            return mLimitMaxX;
        } else {
            return mCenterX;
        }
    }

    public int getBitmapRectY(int mCenterY) {
        if (mCenterY < mLimitMinY) {
            return mLimitMinY;
        } else if (mCenterY > mLimitMaxY) {
            return mLimitMaxY;
        } else {
            return mCenterY;
        }
    }

    float mDownX = 0;
    float mDownY = 0;
    float mMoveX = 0;
    float mMoveY = 0;

    int mMoveTo = -1; //0:左右 1：上下

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mMoveX = event.getX();
                mMoveY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mSubX = event.getX() - mMoveX;
                float mSubY = event.getY() - mMoveY;
                mCenterX += mSubX;
                mCenterY += mSubY;
                mMoveX = event.getX();
                mMoveY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mMoveTo = -1;
                mDownX = 0;
                mDownY = 0;
                mCenterX = mWidth / 2;
                mCenterY = mHeight / 2;
                mMoveX = 0;
                mMoveY = 0;
                break;
        }
        invalidate();
        return true;
    }


    /**
     * 计算两点间距离
     *
     * @param mMoveX
     * @param mMoveY
     * @return
     */
    public float getTwoPointSpacing(float mMoveX, float mMoveY) {
        //点击位置x坐标与圆心的x坐标的距离
        float distanceX = Math.abs(mMoveX);
        //点击位置y坐标与圆心的y坐标的距离
        float distanceY = Math.abs(mMoveY);
        //点击位置与圆心的直线距离
        return (float) Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
    }

    public float getTwoPointSpacing(float mMoveX, float mMoveY, float mCenterX, float mCenterY) {
        //点击位置x坐标与圆心的x坐标的距离
        float distanceX = (int) Math.abs(mWidth / 2 - mMoveX);
        //点击位置y坐标与圆心的y坐标的距离
        float distanceY = (int) Math.abs(mHeight / 2 - mMoveY);
        //点击位置与圆心的直线距离
        return (float) Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
    }


    public float getTwoPointSpacing(float mMove, float radius, float mSpacing) {
        if (mSpacing == 0) {
            return 0;
        }
        return (mMove / mSpacing) * radius;
    }

    /**
     * 根据坐标判断是否是在点击的有效范围内
     * @param mRect
     * @param mClickX
     * @param mClickY
     * @return
     */
    public boolean isClick(Rect mRect, float mClickX, float mClickY) {
        if ((mClickX > mRect.left && mClickX < mRect.right)
                && (mClickY > mRect.top && mClickY < mRect.bottom)) {
            return true;
        }
        return false;
    }
}
