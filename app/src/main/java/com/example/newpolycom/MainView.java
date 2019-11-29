package com.example.newpolycom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MainView extends View implements IMainView {

    private final int POSITION_TYPE_1 = 1;//横向
    private final int POSITION_TYPE_2 = 2;//竖向
    private final int POSITION_TYPE_3 = 3;//左下曲线
    private final int POSITION_TYPE_4 = 4;//右下曲线
    private final int POSITION_TYPE_5 = 5;//上曲线

    //状态  0 左到右边(上到下) 1右到左边(下到上)  2空
    private int mStatus = 2;

    //位置类型
    private int mPosition = POSITION_TYPE_3;

    //画布中点坐标
    private float centerX, centerY;

    //二阶贝塞尔曲线 三个点
    private float eventX, eventY;//这个是控制曲线弯曲度的点
    private float startX, startY;
    private float endX, endY;

    private Paint paint;
    private Path path;

    //画布的大小（控件大小）
    private float mWidth = 0;
    private float mHeight = 0;

    //图片的大小
    private float mBitmapWidth = 0;
    private float mBitmapHeight = 0;

    //四个方向的箭头
    private Bitmap mBitmapLeft;
    private Bitmap mBitmapRight;
    private Bitmap mBitmapTop;
    private Bitmap mBitmapDown;


    private float currentValue = 1;     // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度

    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmapCurrent;      // 箭头图片
    private Matrix mMatrix;             // 矩阵,用于对图片进行一些操作

    public MainView(Context context) {
        this(context, null);
    }

    public MainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = attrs == null ? null : context.obtainStyledAttributes(attrs, R.styleable.MainView);
        if (array != null) {
            this.mPosition = array.getInt(R.styleable.MainView_view_Position, 1);
            this.mStatus = array.getInt(R.styleable.MainView_view_Status, 2);
        }
        init(context);
    }

    private void setmBitmapCurrent(Bitmap mBitmap) {
        mBitmapCurrent = mBitmap;
        if (mBitmapCurrent != null) {
            mBitmapWidth = mBitmapCurrent.getWidth();
            mBitmapHeight = mBitmapCurrent.getHeight();
        } else {
            mBitmapWidth = 0;
            mBitmapHeight = 0;
        }

    }

    private void init(Context context) {
        post(new Runnable() {
            @Override
            public void run() {
                mWidth = getWidth();
                mHeight = getHeight();
                centerX = mWidth / 2;
                centerY = mHeight / 2;
                setmPosition(mPosition);
            }
        });

        pos = new float[2];
        tan = new float[2];
        mMatrix = new Matrix();

        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.color_FDD454));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setPathEffect(new DashPathEffect(new float[]{15, 10}, 0));

        Matrix matrix = new Matrix();
        matrix.postRotate(180); /*翻转180度*/
        mBitmapRight = BitmapFactory.decodeResource(getResources(), R.drawable.img_power_right);
        mBitmapLeft = Bitmap.createBitmap(mBitmapRight, 0, 0, mBitmapRight.getWidth(), mBitmapRight.getHeight(), matrix, true);
        mBitmapDown = BitmapFactory.decodeResource(getResources(), R.drawable.img_power_down);
        mBitmapTop = Bitmap.createBitmap(mBitmapDown, 0, 0, mBitmapDown.getWidth(), mBitmapDown.getHeight(), matrix, true);
        post(mRunnable);
    }

    //测量大小完成以后回调
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画二阶贝塞尔曲线
        path.reset();
        if (mPosition == POSITION_TYPE_3) {
            RectF mRectF = new RectF(mBitmapWidth, -mHeight + mBitmapHeight, mWidth * 2, mHeight - mBitmapHeight);
            canvas.drawOval(mRectF, paint);
            path.arcTo(mRectF, 90, 90);
        } else if (mPosition == POSITION_TYPE_4) {
            RectF mRectF = new RectF(-mWidth + mBitmapWidth, -mHeight + mBitmapHeight, mWidth - mBitmapWidth, mHeight - mBitmapHeight);
            canvas.drawOval(mRectF, paint);
            path.arcTo(mRectF, 0, 90);
        } else if (mPosition == POSITION_TYPE_5) {
            RectF mRectF = new RectF(mBitmapWidth, mBitmapHeight, mWidth - mBitmapWidth, (mHeight + mHeight * 0.2F) * 2);
            canvas.drawOval(mRectF, paint);
            path.arcTo(mRectF, 200, 140);
        } else {
            path.moveTo(startX, startY);
            path.quadTo(eventX, eventY, endX, endY);
        }
        if (mStatus != 2) {
            drawIcon(canvas, path);
        }
        canvas.drawPath(path, paint);
    }


    protected void drawIcon(Canvas canvas, Path path) {
        if (mBitmapCurrent == null) return;
        PathMeasure measure = new PathMeasure(path, false);     // 创建 PathMeasure
        // 计算当前的位置在总长度上的比例[0,1]
        if (mStatus == 0) {
            currentValue -= 0.1;
            if (currentValue < 0) {
                currentValue = 1;
            }
        } else {
            currentValue += 0.1;
            if (currentValue >= 1) {
                currentValue = 0;
            }
        }

        measure.getPosTan(measure.getLength() * currentValue, pos, tan);        // 获取当前位置的坐标以及趋势
        mMatrix.reset();                                                        // 重置Matrix
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI); // 计算图片旋转角度
        mMatrix.postRotate(degrees, mBitmapWidth / 2, mBitmapHeight / 2);   // 旋转图片
        mMatrix.postTranslate(pos[0] - mBitmapWidth / 2, pos[1] - mBitmapHeight / 2);   // 将图片绘制中心调整到与当前点重合
        canvas.drawBitmap(mBitmapCurrent, mMatrix, paint);                     // 绘制箭头
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
            if (mStatus == 2) {
                removeCallbacks(this);
            } else {
                postDelayed(this, 200);
            }
        }
    };


    public void setPosition() {
        removeCallbacks(mRunnable);
        switch (mPosition) {
            case POSITION_TYPE_1:
                setStatus(mBitmapLeft, mBitmapRight);
                setLineData(0, centerY, mWidth, centerY, centerX, centerY);
                break;
            case POSITION_TYPE_2:
                setStatus(mBitmapLeft, mBitmapRight);
                setLineData(centerX, 0, centerX, mHeight, centerX, centerY);
                break;
            case POSITION_TYPE_3:
                setStatus(mBitmapLeft, mBitmapRight);
                setLineData(mBitmapWidth, 0, mWidth, mHeight - mBitmapHeight, mWidth * 0.25F, mHeight * 0.75F);
                break;
            case POSITION_TYPE_4:
                setStatus(mBitmapLeft, mBitmapRight);
                setLineData(0, mHeight - mBitmapHeight, mWidth - mBitmapWidth, 0, mWidth * 0.75F, mWidth * 0.75F);
                break;
            case POSITION_TYPE_5:
                setStatus(mBitmapLeft, mBitmapRight);
                setLineData(0, mHeight - mBitmapHeight, mWidth, mHeight - mBitmapHeight, centerX, 0);
                break;
        }
        post(mRunnable);
    }

    /**
     * @param mBitmap1 正方向
     * @param mBitmap2 反方向
     */
    private void setStatus(Bitmap mBitmap1, Bitmap mBitmap2) {
        switch (mStatus) {
            case 0:
                paint.setColor(getResources().getColor(R.color.color_FDD454));
                setmBitmapCurrent(mBitmap1);
                break;
            case 1:
                paint.setColor(getResources().getColor(R.color.color_FDD454));
                setmBitmapCurrent(mBitmap2);
                break;
            case 2:
                paint.setColor(getResources().getColor(R.color.color_FDFDFE));
                setmBitmapCurrent(mBitmap1);
                break;
        }
    }

    private void setLineData(float startX, float startY, float endX, float endY, float eventX, float eventY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.eventX = eventX;
        this.eventY = eventY;
    }


    /* *************************暴露在外的接口 *********************** */
    
    public void setStatus(int status) {
        this.mStatus = status;
        setPosition();
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
        setPosition();
    }

}
