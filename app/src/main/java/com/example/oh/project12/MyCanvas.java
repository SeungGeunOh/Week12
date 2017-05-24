package com.example.oh.project12;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by OH on 2017-05-18.
 */

public class MyCanvas extends View {
    static Bitmap mBitmap;
    Canvas mCanvas;
    Paint mPaint = new Paint();
    float[] matrixArray = {
            2f, 0f, 0f, 0f, -25f,
            0f, 2f, 0f, 0f, -25f,
            0f, 0f, 2f, 0f, -25f,
            0f, 0f, 0f, 1f, 0f
    };
    float[] originArray = {
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
    };

    static boolean scale = false;
    static boolean rotate = false;
    static boolean move = false;
    static boolean skew = false;
    int clickX = -1, clickY = -1;
    int oldX = -1, oldY = -1;
    public MyCanvas(Context context) {
        super(context);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3f);
    }

    public MyCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3f);
    }

    private void drawStamp(){
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (scale == true) {
            Bitmap imgBig = Bitmap.createScaledBitmap(img, img.getWidth() * 3 / 2, img.getHeight() * 3 / 2, false);
            mCanvas.drawBitmap(imgBig, clickX - imgBig.getWidth() / 2, clickY - imgBig.getHeight() / 2, mPaint);
            imgBig.recycle();
        }
        else if (rotate == true) {
            Matrix matrixRotate = new Matrix();
            matrixRotate.postRotate(30);
            Bitmap imgRotate = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrixRotate, false);
            mCanvas.drawBitmap(imgRotate,clickX - imgRotate.getWidth() / 2, clickY - imgRotate.getHeight() / 2, mPaint);
            imgRotate.recycle();
        }
        else if (move == true){
            mCanvas.drawBitmap(img, (clickX - img.getWidth() / 2) + 100, (clickY - img.getHeight() / 2) + 100, mPaint);
            img.recycle();
        }
        else if (skew == true){
            Matrix matrixSkew = new Matrix();
            matrixSkew.postSkew(0.2f, 0);
            Bitmap imgSkew = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrixSkew, false);
            mCanvas.drawBitmap(imgSkew, clickX - imgSkew.getWidth() / 2, clickY - imgSkew.getHeight() / 2, mPaint);
            imgSkew.recycle();
        }
        else {
            mCanvas.drawBitmap(img, clickX - img.getWidth() / 2, clickY - img.getHeight() / 2, mPaint);
            img.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.YELLOW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null)
            canvas.drawBitmap(mBitmap, clickX, clickY, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int X = (int)event.getX();
        int Y = (int)event.getY();
        if (MainActivity.checkStamp == true) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                clickX = X;
                clickY = Y;
                drawStamp();
                invalidate();
            }
        }
        else {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                oldX = X;
                oldY = Y;
            }
            else if (event.getAction() == MotionEvent.ACTION_MOVE){
                if (oldX != -1) {
                    mCanvas.drawLine(oldX, oldY, X, Y, mPaint);
                    oldX = X;
                    oldY = Y;
                }

            }
            else if (event.getAction() == MotionEvent.ACTION_UP){
                if (oldX != -1) {
                    mCanvas.drawLine(oldX, oldY, X, Y, mPaint);
                    invalidate();
                }
                oldX = -1;
                oldY = -1;
            }
        }
        clickX = -1;
        clickY = -1;
        return true;
    }

    public void clear(){
        mBitmap.eraseColor(Color.YELLOW);
        invalidate();
    }

    public void scale(){
        if (scale == false) // 1.5배로 커짐
            scale = true;
        else // 다시 작아짐
            scale = false;
    }
    public void rotate(){
        if (rotate == false)
            rotate = true;
        else
            rotate = false;
    }
    public void move(){
        if (move == false)
            move = true;
        else
            move = false;
    }
    public void skew(){
        if (skew == false)
            skew = true;
        else
            skew = false;
    }
    public void bluring(){
        if (MainActivity.checkBluring == true) {
            BlurMaskFilter blur = new BlurMaskFilter(150, BlurMaskFilter.Blur.INNER);
            mPaint.setMaskFilter(blur);
            Toast.makeText(getContext(), "BLUR 효과 적용", Toast.LENGTH_SHORT).show();
        }
        else {
            BlurMaskFilter blur = new BlurMaskFilter(0.1f, BlurMaskFilter.Blur.INNER);
            mPaint.setMaskFilter(blur);
            Toast.makeText(getContext(), "BLUR 효과 적용안됨", Toast.LENGTH_SHORT).show();
        }
        invalidate();
    }
    public void coloring(){
        if (MainActivity.checkColoring == true) {
            ColorMatrix matrix1 = new ColorMatrix(matrixArray);
            mPaint.setColorFilter(new ColorMatrixColorFilter(matrix1));
            Toast.makeText(getContext(), "COLOR 효과 적용", Toast.LENGTH_SHORT).show();
        }
        else {
            ColorMatrix matrix2 = new ColorMatrix(originArray);
            mPaint.setColorFilter(new ColorMatrixColorFilter(matrix2));
            Toast.makeText(getContext(), "COLOR 효과 적용안됨", Toast.LENGTH_SHORT).show();
        }
    }

    public void setStorokeWidth(){
        if (MainActivity.checkPenWidth == true) {
            mPaint.setStrokeWidth(5f);
        }
        else {
            mPaint.setStrokeWidth(3f);
        }

    }
    public void setPenColor(){
        if (MainActivity.color == 1) {
            mPaint.setColor(Color.RED);
        }
        else if (MainActivity.color == 2){
            mPaint.setColor(Color.BLUE);
        }
        invalidate();
    }

    public void openPaint(){
        try {
            String path = "data/data/com.example.oh.project12/test.jpg";
            Bitmap bm = BitmapFactory.decodeFile(path);
            mCanvas.drawBitmap(bm, bm.getWidth(), bm.getHeight(), mPaint);
            Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(getContext(), "no", Toast.LENGTH_SHORT).show();
        }
    }
}
