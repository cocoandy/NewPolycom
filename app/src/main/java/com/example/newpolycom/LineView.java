package com.example.newpolycom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LineView extends View {

    private final int ORIENTATION_LEFT = 0;//
    private final int ORIENTATION_RIGHT = 1;//
    private final int ORIENTATION_TOP = 2;//
    private final int ORIENTATION_DOWN = 3;//
    private final int ORIENTATION_EMPTY = 4;//

    private float mWidth = 0;
    private float mHeight = 0;
    private Bitmap mBitmapTop;
    private Bitmap mBitmapDown;
    private Bitmap mBitmapLeft;
    private Bitmap mBitmapRight;
    private Bitmap mBitmapCurrent = null;
    private float mBitmapCurrentWidth = 0;
    private float mBitmapCurrentHeight = 0;
    private Paint mPaint; //虚线
    private int mColorRes = R.color.colorAccent; //虚线颜色

    private int mOrientation = ORIENTATION_LEFT; //方位

    private int strokeWidth = 6;


    private final float mSpace = 50;//移动距离

    private float mCurrentX = 0;//当前水平位置
    private float mCurrentY = 0;//当前垂直位置

    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        post(new Runnable() {
            @Override
            public void run() {
                mWidth = getWidth();
                mHeight = getHeight();

            }
        });

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(mColorRes));
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setPathEffect(new DashPathEffect(new float[]{15, 10}, 0));

        Matrix matrix = new Matrix();
        matrix.postRotate(180); /*翻转180度*/
        mBitmapRight = BitmapFactory.decodeResource(getResources(), R.mipmap.img_power_right);
        mBitmapLeft = Bitmap.createBitmap(mBitmapRight, 0, 0, mBitmapRight.getWidth(), mBitmapRight.getHeight(), matrix, true);
        mBitmapDown = BitmapFactory.decodeResource(getResources(), R.mipmap.img_power_down);
        mBitmapTop = Bitmap.createBitmap(mBitmapDown, 0, 0, mBitmapDown.getWidth(), mBitmapDown.getHeight(), matrix, true);
        mBitmapCurrent = mBitmapTop;
        mBitmapCurrentWidth = mBitmapCurrent.getWidth();
        mBitmapCurrentHeight = mBitmapCurrent.getHeight();
        post(mRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);

        if (mBitmapCurrent != null) {
            canvas.drawBitmap(mBitmapCurrent, mCurrentX, mCurrentY, mPaint);
        }

    }

    public void drawLine(Canvas canvas) {
        mPaint.setColor(getResources().getColor(mColorRes));
        switch (mOrientation) {
            case ORIENTATION_LEFT:
            case ORIENTATION_RIGHT:
                canvas.drawLine(0, (mHeight + strokeWidth) / 2, mWidth, (mHeight + strokeWidth) / 2, mPaint);
                break;
            case ORIENTATION_TOP:
            case ORIENTATION_DOWN:
                canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, mPaint);
                break;
            case ORIENTATION_EMPTY:
                canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, mPaint);
                break;
        }
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            switch (mOrientation) {
                case ORIENTATION_LEFT:
                    mCurrentY = (mHeight - mBitmapCurrentHeight) / 2;
                    mCurrentX = mCurrentX - mSpace;
                    if (mCurrentX <= 0) {
                        mCurrentX = mWidth + mCurrentX;
                    }
                    mCurrentX = mCurrentX % mWidth;
                    break;
                case ORIENTATION_RIGHT:
                    mCurrentY = (mHeight - mBitmapCurrentHeight) / 2;
                    mCurrentX = Math.abs((mCurrentX + mSpace) % mWidth);
                    break;

                case ORIENTATION_TOP:
                    mCurrentX = (mWidth - mBitmapCurrentWidth) / 2;
                    mCurrentY = mCurrentY - mSpace;
                    if (mCurrentY <= 0) {
                        mCurrentY = mHeight + mCurrentY;
                    }
                    mCurrentY = mCurrentY % mHeight;
                    break;
                case ORIENTATION_DOWN:
                    mCurrentX = (mWidth - mBitmapCurrentWidth) / 2;
                    mCurrentY = Math.abs((mCurrentY + mSpace) % mHeight);
                    break;
            }
            Log.e("TAG_OO", "mCurrentX-->" + mCurrentX);
            postInvalidate();
            postDelayed(this, 200);
        }
    };

    public void setmOrientation(int mOrientation) {
        this.mOrientation = mOrientation;
        removeCallbacks(mRunnable);
        switch (mOrientation) {
            case ORIENTATION_LEFT:
                mBitmapCurrent = mBitmapLeft;
                break;
            case ORIENTATION_TOP:
                mBitmapCurrent = mBitmapTop;
                break;
            case ORIENTATION_RIGHT:
                mBitmapCurrent = mBitmapRight;
                break;
            case ORIENTATION_DOWN:
                mBitmapCurrent = mBitmapDown;
                break;
            case ORIENTATION_EMPTY:
                mBitmapCurrent = null;
                break;
        }
        post(mRunnable);
    }
}
