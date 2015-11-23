package com.example.don.cutviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by don on 15/11/23.
 */
public class CutView extends ImageView {

    private int viewWidth, viewHeight;//cutView的宽高

    private Bitmap origBitmap;//需要的剪切的原始Bitmap
    private int bitmapLeft, bitmapTop, bitmapRight, bitmapBottom;//原始Bitmap的一些属性

    private int fitWidth, fitHeight;//原始Bitmap按屏幕宽度等比例缩放后的宽高

    private int cutRadius;//剪裁半径

    private boolean isNeedCut;//标志是否剪裁图片
    private CutListener cutListener;//剪切图片通过代理模式将剪裁结果返回

    //考虑到需要实现缩放功能，所以这里我们预先通过枚举设置集中模式
    enum CutMode{

      NONE,DRAG,ZOOM

    };
    private CutMode cutMode= CutMode.NONE;//默认是没有模式的


    public CutListener getCutListener() {
        return cutListener;
    }

    public void setCutListener(CutListener cutListener) {
        this.cutListener = cutListener;
    }

    public CutView(Context context) {
        super(context);

        init();
    }

    public CutView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    /**
     * 初始化方法，可在吃方法中做一些初始化设置。
     */
    private void init() {

        try {
            origBitmap = ((BitmapDrawable) getDrawable()).getBitmap();//获取在布局文件中设置的src图片
        } catch (Exception e) {

            origBitmap = null;
        }

        scale=1.0f;
        outScale=1.0f;


    }


    private PointF firstFinger= new PointF();
    private float offsetX,offsetY;//当前偏移量
    private float outOffsetX,outOffsetY;//上次偏移量

    private float dist;//两个手指之间的距离
    private float scale;
    private float outScale;
    //覆写onTouch方法获取触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 主点按下，只有一个手指
            case MotionEvent.ACTION_DOWN:

                cutMode = CutMode.DRAG;
                firstFinger.set(event.getX(), event.getY());

                break;
            // 副点按下,此时有两个手指
            case MotionEvent.ACTION_POINTER_DOWN:
                dist=spacing(event);

                if(dist>10f){
                    cutMode=CutMode.ZOOM;
                }

                break;
            //副点抬起
            case MotionEvent.ACTION_POINTER_UP:
                cutMode = CutMode.NONE;
                break;

            //移动
            case MotionEvent.ACTION_MOVE:

                if(cutMode==CutMode.DRAG){


                    offsetX=event.getX()-firstFinger.x+outOffsetX;
                    offsetY=event.getY()-firstFinger.y+outOffsetY;

                    int scaleHeight = (int) (viewWidth / (float) origBitmap.getWidth() * origBitmap.getHeight());
                    //边界控制X
                    if(offsetX>getFitWidth()/2-cutRadius)
                        offsetX=getFitWidth()/2-cutRadius;
                    else if(offsetX<-(getFitWidth()/2-cutRadius))
                        offsetX=-(getFitWidth()/2-cutRadius);
                    //边界控制Y
                    if(offsetY>getFitHeight()/2-cutRadius)
                        offsetY=getFitHeight()/2-cutRadius;
                    else if(offsetY<-(getFitHeight()/2-cutRadius))
                        offsetY=-(getFitHeight()/2-cutRadius);


                }else if(cutMode==CutMode.ZOOM){

                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        float tScale = (float) Math.sqrt(newDist / dist);
                        if (scale > 10)
                            scale = 10f;
                        if (scale < 0.1)
                            scale = 0.1f;
                        scale=tScale*outScale;
                    }

                }

                break;
            //抬起
            case MotionEvent.ACTION_UP:

                outOffsetX=offsetX;
                outOffsetY=offsetY;

                outScale=scale;

                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取控件宽高
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();

        cutRadius=viewWidth / 2 / 2;





    }

    @Override
    protected void onDraw(Canvas canvas) {


        if (origBitmap != null) {

            //将Bitmap设置为根据view的宽度按比例缩放
            int scaleHeight = (int) (viewWidth / (float) origBitmap.getWidth() * origBitmap.getHeight());

            //设置Bitmap的一些属性
            setBitmapLeft((int) ((0 + offsetX + (viewWidth-viewWidth*scale)/2)));
            setBitmapTop((int) (((viewHeight - scaleHeight) / 2+offsetY+(scaleHeight-scaleHeight*scale)/2)));
            setBitmapRight((int) ((getBitmapLeft()+viewWidth*scale)));
            setBitmapBottom((int)((getBitmapTop() + scaleHeight*scale)));

            setFitWidth(getBitmapRight() - getBitmapLeft());
            setFitHeight(getBitmapBottom() - getBitmapTop());

            //绘制Bitmap
            canvas.drawBitmap(origBitmap, new Rect(0, 0, origBitmap.getWidth(), origBitmap.getHeight()),
                    new Rect(getBitmapLeft(), getBitmapTop(), getBitmapRight(), getBitmapBottom()), new Paint());


            //绘制圆形遮罩
            Path path = new Path();
            path.addCircle(viewWidth / 2, viewHeight / 2, cutRadius,
                    Path.Direction.CCW);
            canvas.clipPath(path, Region.Op.DIFFERENCE);
            canvas.drawColor(0xDF222222);//0xDF222222


            //虚线
            DashPathEffect dashStyle = new DashPathEffect(new float[]{10,
                    5, 5, 5}, 2);
            Paint mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(0x006F8DD5);//0xFF6F8DD5
            mPaint.setStrokeWidth(1);
            mPaint.setPathEffect(dashStyle);

            canvas.drawPath(path, mPaint);

            if (isNeedCut) {//剪切图片

                if (cutListener != null)
                    cutListener.cutResultWithBitmap(getResultBitmap());

                isNeedCut = false;

            }


        } else
            try {
                origBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            } catch (Exception e) {

                origBitmap = null;
            }
        invalidate();//刷新界面
    }

    public void clipImage() {

        isNeedCut = true;


    }

    /**
     * 获取剪裁后的圆形Bitmap
     *
     * @return
     */
    private Bitmap getResultBitmap() {


        int circleWidth = cutRadius * 2;
        int circleHeight = cutRadius * 2;

        Bitmap output = Bitmap.createBitmap(circleWidth, circleHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(circleWidth / 2, circleHeight / 2, circleWidth / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        Rect src = new Rect(0, 0, origBitmap.getWidth(), origBitmap.getHeight());

        int dstLeft, dstTop, dstRight, dstBottom;

        dstLeft = (int)(-(getFitWidth() - circleWidth) / 2+offsetX);
        dstTop = (int)(-(getFitHeight() - circleHeight) / 2+offsetY);
        dstRight = dstLeft+circleWidth + (getFitWidth() - circleWidth);
        dstBottom =dstTop+ circleHeight + (getFitHeight() - circleHeight);

        Rect dst = new Rect(dstLeft, dstTop, dstRight, dstBottom);

        canvas.drawBitmap(origBitmap, src, dst, paint);

        return output;
    }

    public int getBitmapLeft() {
        return bitmapLeft;
    }

    public void setBitmapLeft(int bitmapLeft) {
        this.bitmapLeft = bitmapLeft;
    }

    public int getBitmapTop() {
        return bitmapTop;
    }

    public void setBitmapTop(int bitmapTop) {
        this.bitmapTop = bitmapTop;
    }

    public int getBitmapRight() {
        return bitmapRight;
    }

    public void setBitmapRight(int bitmapRight) {
        this.bitmapRight = bitmapRight;
    }

    public int getBitmapBottom() {
        return bitmapBottom;
    }

    public void setBitmapBottom(int bitmapBottom) {
        this.bitmapBottom = bitmapBottom;
    }

    public int getFitWidth() {
        return fitWidth;
    }

    public void setFitWidth(int fitWidth) {
        this.fitWidth = fitWidth;
    }

    public int getFitHeight() {
        return fitHeight;
    }

    public void setFitHeight(int fitHeight) {
        this.fitHeight = fitHeight;
    }

    /**
     * 两点的距离
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float)Math.sqrt((x * x + y * y));
    }


}
