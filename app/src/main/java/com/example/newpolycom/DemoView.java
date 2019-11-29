package com.example.newpolycom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DemoView extends View implements LongClickView {
    private OnViewLongEventListener callback;

    private Bitmap mBgBitmap; //背景圆的图标
    private Bitmap mCtlBitmap; //中心控制圆的图标

    private Bitmap mBitmapT;
    private Bitmap mBitmapD;
    private Bitmap mBitmapL;
    private Bitmap mBitmapR;
    private Bitmap mBitmapDT;
    private Bitmap mBitmapDD;
    private Bitmap mBitmapDL;
    private Bitmap mBitmapDR;

    private Rect mRectTop;
    private Rect mRectDown;
    private Rect mRectLeft;
    private Rect mRectRight;
    private Rect mRectH;
    private Rect mRectV;


    boolean isClickT = false;
    boolean isClickD = false;
    boolean isClickL = false;
    boolean isClickR = false;

    private String mLongNumberTop = "1";
    private String mLongNumberBottom = "2";
    private String mLongNumberLeft = "3";
    private String mLongNumberRight = "4";

    private String mNumberTop = "";
    private String mNumberBottom = "";
    private String mNumberLeft = "";
    private String mNumberRight = "";

    private Paint mBgPaint; //背景圆的画笔
    private Paint mCtlPaint; //中心控制圆的画笔

    //背景圆的宽高
    private float mBgCircleWidth = 360f;
    private float mBgCircleHeight = 360f;

    //中心控制圆的宽高
    private float mCtlCircleWidth = 137f;
    private float mCtlCircleHeight = 137f;

    //中心控制图片的宽高
    private float mCtlBitmaWidth = 200f;
    private float mCtlBitmaHeight = 200f;

    //画布中心点
    private float mCenterX = 180f;
    private float mCenterY = 180f;

    //中心控制圆心点
    private float mCtlCenterX = 180f;
    private float mCtlCenterY = 180f;//中心控制圆心点

    //中心控制图片的一半
    private float mCtlBitmipHaltX = 180f;
    private float mCtlBitmipHaltY = 180f;

    //中心控制圆心点
    private float mBgRradius = 180f;
    private float mCtRradius = 180f;

    //中心点到中心控制圆形的圆心的距离（即圆心的活动距离）
    private float mSpacingCircle = 180f;

    //手指在的点
    private float mCurrX = 0;
    private float mCurrY = 0;

    //这两个用于标记是否是象限变化
    private int mCurrIndex = -1;//标记当前是第几象限
    private int mMoveTo = -1; //记录移动到的象限

    private boolean isMoving = false;//是否移动
    private String mCurrNumber = "";//当前的点击码
    private String mCurrLongNumber = "";//当前的长按码

    public DemoView(Context context) {
        this(context, null);
    }

    public DemoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
/*        TypedArray array = attrs == null ? null : context.obtainStyledAttributes(attrs, R.styleable.PolycomNumber);
        if (array != null) {
            this.mLongNumberTop = array.getString(R.styleable.PolycomNumber_comfdLongNumberTop);
            this.mLongNumberBottom = array.getString(R.styleable.PolycomNumber_comfdLongNumberBottom);
            this.mLongNumberLeft = array.getString(R.styleable.PolycomNumber_comfdLongNumberLeft);
            this.mLongNumberRight = array.getString(R.styleable.PolycomNumber_comfdLongNumberRght);
            this.mNumberTop = array.getString(R.styleable.PolycomNumber_comfdNumberTop);
            this.mNumberBottom = array.getString(R.styleable.PolycomNumber_comfdNumberBottom);
            this.mNumberLeft = array.getString(R.styleable.PolycomNumber_comfdNumberLeft);
            this.mNumberRight = array.getString(R.styleable.PolycomNumber_comfdNumberRght);
        }*/

        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycim_bg);
        mCtlBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycim_ctl);

        mBitmapT = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycom_top);
        mBitmapD = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycom_down);
        mBitmapL = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycom_left);
        mBitmapR = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycom_right);
        mBitmapDT = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycom_def_top);
        mBitmapDD = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycom_def_down);
        mBitmapDL = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycom_def_left);
        mBitmapDR = BitmapFactory.decodeResource(getResources(), R.mipmap.img_polycom_def_right);

        mRectH = new Rect(0, 0, 132, 69);
        mRectV = new Rect(0, 0, 69, 132);

        mRectTop = new Rect(114, 30, 114 + mRectH.right, 30 + mRectH.bottom);
        mRectDown = new Rect(114, 261, 114 + mRectH.right, 261 + mRectH.bottom);
        mRectLeft = new Rect(30, 114, 30 + mRectV.right, 114 + mRectV.bottom);
        mRectRight = new Rect(30, 261, 30 + mRectV.right, 261 + mRectV.bottom);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(Color.RED);

        mCtlPaint = new Paint();
        mCtlPaint.setAntiAlias(true);
        mCtlPaint.setColor(Color.WHITE);

        mCenterX = mBgCircleWidth / 2;
        mCenterY = mBgCircleHeight / 2;
        mCtlCenterX = mCenterX;
        mCtlCenterY = mCenterY;

        mBgRradius = mBgCircleWidth / 2;
        mCtRradius = mCtlCircleHeight / 2;

        mCtlBitmipHaltX = mCtlBitmaWidth / 2;
        mCtlBitmipHaltY = mCtlBitmaHeight / 2;

        mSpacingCircle = mBgRradius - mCtRradius;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(mBgBitmap, 0, 0, mBgPaint);
        if (isClickT) {
            canvas.drawBitmap(mBitmapT, mRectH, mRectTop, mCtlPaint);
        } else {
            canvas.drawBitmap(mBitmapDT, mRectH, mRectTop, mCtlPaint);
        }

        if (isClickD) {
            canvas.drawBitmap(mBitmapD, mRectH, mRectDown, mCtlPaint);
        } else {
            canvas.drawBitmap(mBitmapDD, mRectH, mRectDown, mCtlPaint);
        }

        if (isClickL) {
            canvas.drawBitmap(mBitmapL, mRectV, mRectLeft, mCtlPaint);
        } else {
            canvas.drawBitmap(mBitmapDL, mRectV, mRectLeft, mCtlPaint);
        }

        if (isClickR) {
            canvas.drawBitmap(mBitmapR, mRectV, mRectRight, mCtlPaint);
        } else {
            canvas.drawBitmap(mBitmapDR, mRectV, mRectRight, mCtlPaint);
        }
        canvas.drawBitmap(mCtlBitmap, getPointCtlX(), getPointCtlY(), mCtlPaint);
    }

    private float getPointCtlX() {
        return mCtlCenterX - mCtlBitmipHaltX;
    }

    private float getPointCtlY() {
        return mCtlCenterY - mCtlBitmipHaltY;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            if (callback != null) {
                callback.onDisabled(this);
            }
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrX = event.getX();
                mCurrY = event.getY();

                //判断点击的点是否在中心圆形上
                float mSubCenter = getSpacing(mCurrX, mCurrY, mCenterX, mCenterY);
                if (mSubCenter < mCtRradius) {
                    isMoving = true;
                } else {
                    if (isClickT = isClick(mRectTop, mCurrX, mCurrY)) {
                        setBtnClickNumber(mLongNumberTop);
                    } else if (isClickD = isClick(mRectDown, mCurrX, mCurrY)) {
                        setBtnClickNumber(mLongNumberBottom);
                    } else if (isClickL = isClick(mRectLeft, mCurrX, mCurrY)) {
                        setBtnClickNumber(mLongNumberLeft);
                    } else if (isClickR = isClick(mRectRight, mCurrX, mCurrY)) {
                        setBtnClickNumber(mLongNumberRight);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoving) {
                    //计算偏移量
                    mCtlCenterX = event.getX();
                    mCtlCenterY = event.getY();
                    //计算到圆心的距离

                    float mSubRradius = getSpacing(mCtlCenterX, mCtlCenterY, mCenterX, mCenterY);
                    if (mSubRradius >= mSpacingCircle) {
                        mCtlCenterX = getValueXY(mCenterX, mSpacingCircle, mCtlCenterX, mSubRradius);
                        mCtlCenterY = getValueXY(mCenterY, mSpacingCircle, mCtlCenterY, mSubRradius);
                    }
                    if (mSubRradius > 5) {
                        int mAngle = getRotationBetweenLines(mCenterX, mCenterY, event.getX(), event.getY());
                        if ((mAngle > 315 && mAngle < 360) || (0 < mAngle && mAngle < 45)) {  //上
                            Log.e("TAG_LOG", "----------> 上");
                        } else if (mAngle > 45 && mAngle < 135) {//右
                            Log.e("TAG_LOG", "----------> 右");
                        } else if (mAngle > 135 && mAngle < 225) {  //下
                            Log.e("TAG_LOG", "----------> 下");
                        } else {//左
                            Log.e("TAG_LOG", "----------> 左");
                        }
                    }
                    if (mMoveTo != mCurrIndex) {
                        mCurrIndex = mMoveTo;
                        if (callback != null) {
                            callback.onLongClickDown(this);
                        }
                    }
                    mCurrX = event.getX();
                    mCurrY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (callback != null) {
                    callback.onLongClickUp(this);
                }
                isMoving = false;
                mCurrX = 0;
                mCurrY = 0;
                mCtlCenterX = mCenterX;
                mCtlCenterY = mCenterY;
                isClickT = false;
                isClickD = false;
                isClickL = false;
                isClickR = false;
                break;
        }

        invalidate();
        return true;
    }

    public float getValueXY(float mCenter, float mR, float mX, float mRX) {
        float value = mX - mCenter;
        float absValue = Math.abs(value);
        float result = (absValue * mR) / mRX;
        if (value > 0) {
            return mCenter + result;
        } else {
            return mCenter - result;
        }
    }

    /**
     * 要求的坐标
     * (((value1-value12)*value12)/value3)+value12
     *
     * @param value1  要求的坐标
     * @param value12 半斤
     * @param value3  距离
     * @return
     */
    float getvalue(float value1, float value12, float value3) {
        float v1 = value1 - value12;
        float v2 = v1 * value12;
        float v3 = v2 / value3;
        return v3 + value12;
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

    public void setBtnClickNumber(String number) {
        this.setLongClickNumber(number);
        if (callback != null) {
            callback.onLongClickDown(this);
        }
        invalidate();
    }

    /**
     * 计算两点间的距离
     *
     * @param mPointX1
     * @param mPointY1
     * @param mPointX2
     * @param mPointY2
     * @return
     */
    public float getSpacing(float mPointX1, float mPointY1, float mPointX2, float mPointY2) {
        return (float) Math.sqrt(Math.pow(mPointX1 - mPointX2, 2)
                + Math.pow(mPointY1 - mPointY2, 2));
    }


    /**
     * 根据坐标判断是否是在点击的有效范围内
     *
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

    /**
     * 获取两条线的夹角
     *
     * @param centerX
     * @param centerY
     * @param xInView
     * @param yInView
     * @return
     */
    public static int getRotationBetweenLines(float centerX, float centerY, float xInView, float yInView) {
        double rotation = 0;

        double k1 = (double) (centerY - centerY) / (centerX * 2 - centerX);
        double k2 = (double) (yInView - centerY) / (xInView - centerX);
        double tmpDegree = Math.atan((Math.abs(k1 - k2)) / (1 + k1 * k2)) / Math.PI * 180;

        if (xInView > centerX && yInView < centerY) {  //第一象限
            rotation = 90 - tmpDegree;
        } else if (xInView > centerX && yInView > centerY) //第二象限
        {
            rotation = 90 + tmpDegree;
        } else if (xInView < centerX && yInView > centerY) { //第三象限
            rotation = 270 - tmpDegree;
        } else if (xInView < centerX && yInView < centerY) { //第四象限
            rotation = 270 + tmpDegree;
        } else if (xInView == centerX && yInView < centerY) {
            rotation = 0;
        } else if (xInView == centerX && yInView > centerY) {
            rotation = 180;
        }
        return (int) rotation;
    }


    /**
     * 重写View属性
     */
    @Override
    public int getViewId() {
        return getId();
    }

    @Override
    public String getClickNumber() {
        return mCurrNumber;
    }

    @Override
    public void setClickNumber(String clickNumber) {
        mCurrNumber = clickNumber;
    }

    @Override
    public String getLongClickNumber() {
        return mCurrLongNumber;
    }

    @Override
    public void setLongClickNumber(String longClickNumber) {
        mCurrLongNumber = longClickNumber;
    }

    boolean isLongClick;

    @Override
    public boolean isLongClick() {
        return isLongClick;
    }

    @Override
    public void setLongClick(boolean longClick) {
        isLongClick = longClick;
    }

    @Override
    public void setOnViewLongEventListener(OnViewLongEventListener callback) {
        this.callback = callback;
    }


    @Override
    public String getTextString() {
        return "";
    }


    public String getmLongNumberTop() {
        return mLongNumberTop;
    }

    public void setmLongNumberTop(String mLongNumberTop) {
        this.mLongNumberTop = mLongNumberTop;
    }

    public String getmLongNumberBottom() {
        return mLongNumberBottom;
    }

    public void setmLongNumberBottom(String mLongNumberBottom) {
        this.mLongNumberBottom = mLongNumberBottom;
    }

    public String getmLongNumberLeft() {
        return mLongNumberLeft;
    }

    public void setmLongNumberLeft(String mLongNumberLeft) {
        this.mLongNumberLeft = mLongNumberLeft;
    }

    public String getmLongNumberRight() {
        return mLongNumberRight;
    }

    public void setmLongNumberRight(String mLongNumberRight) {
        this.mLongNumberRight = mLongNumberRight;
    }

    public String getmNumberTop() {
        return mNumberTop;
    }

    public void setmNumberTop(String mNumberTop) {
        this.mNumberTop = mNumberTop;
    }

    public String getmNumberBottom() {
        return mNumberBottom;
    }

    public void setmNumberBottom(String mNumberBottom) {
        this.mNumberBottom = mNumberBottom;
    }

    public String getmNumberLeft() {
        return mNumberLeft;
    }

    public void setmNumberLeft(String mNumberLeft) {
        this.mNumberLeft = mNumberLeft;
    }

    public String getmNumberRight() {
        return mNumberRight;
    }

    public void setmNumberRight(String mNumberRight) {
        this.mNumberRight = mNumberRight;
    }
}
