package com.app.pipelinesurvey.location;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.app.pipelinesurvey.R;
import com.supermap.data.Point;

import java.io.InputStream;


/**
 * Created by puguan.luo on 2018/2/2 0002.
 * z自定义view
 */

public class NavigationPanelView extends View {
    private Bitmap[] mBitmapArray = new Bitmap[4];

    InputStream is;
    private float[] mValues;
    int[] mBitmapWidth = new int[4];
    int[] mBitmapHeight = new int[4];

    private Point point;
    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }
    public NavigationPanelView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }
    public NavigationPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Options opts = new Options();
        opts.inJustDecodeBounds = false;
        setBitMapArray(context, 0, opts, R.drawable.loc_gravity);
        setBitMapArray(context, 1, opts, R.drawable.loc_arrow);
    }

    /**
     * 设置bitmap数组个下标的值
     *
     * @param index
     * @param opts
     * @param resid
     */
    private void setBitMapArray(Context context, int index,
                                Options opts, int resid) {
        try {
            is = context.getResources().openRawResource(resid);
            mBitmapArray[index] = BitmapFactory.decodeStream(is);
            mBitmapWidth[index] = mBitmapArray[index].getWidth();
            mBitmapHeight[index] = mBitmapArray[index].getHeight();

            mBitmapArray[index + 2] = BitmapFactory.decodeStream(is, null, opts);
//            mBitmapArray[index + 2] = BitmapFactory.decodeStream(is);
            mBitmapHeight[index + 2] = mBitmapArray[index].getHeight();
            mBitmapWidth[index + 2] = mBitmapArray[index].getWidth();
            opts.inJustDecodeBounds = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        int cx;
        int cy;
        if (point != null) {
            cx = point.getX();
            cy = point.getY();
        } else {
            cx = w / 2;
            cy = h / 2;
        }
        canvas.translate(cx, cy);
        drawPictures(canvas, 0);
    }

    private void drawPictures(Canvas canvas, int idDelta) {
        if (mValues != null) {
            // Log.d(TAG, "mValues[0] = "+ mValues[0]);
            canvas.rotate(-mValues[0]);
            canvas.drawBitmap(mBitmapArray[0 + idDelta],
                    -mBitmapWidth[0 + idDelta] / 2,
                    -mBitmapHeight[0 + idDelta] / 2, null);
            canvas.rotate(360 + mValues[0]);
            canvas.drawBitmap(mBitmapArray[1 + idDelta],
                    -mBitmapWidth[1 + idDelta] / 2,
                    -mBitmapHeight[1 + idDelta] / 2, null);
        } else {
            canvas.drawBitmap(mBitmapArray[0 + idDelta],
                    -mBitmapWidth[0 + idDelta] / 2,
                    -mBitmapHeight[0 + idDelta] / 2, null);

            canvas.drawBitmap(mBitmapArray[1 + idDelta],
                    -mBitmapWidth[1 + idDelta] / 2,
                    -mBitmapHeight[1 + idDelta] / 2, null);
        }
    }

    public void setValue(float[] value) {
        this.mValues = value;
    }


}
