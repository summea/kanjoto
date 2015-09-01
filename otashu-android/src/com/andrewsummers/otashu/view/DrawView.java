
package com.andrewsummers.otashu.view;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

/**
 * View a generated picture of a particular emotion's fingerprint.
 * <p>
 * This activity allows a user to see how an emotion's fingerprint appears.
 * </p>
 */
public class DrawView extends View {
    Paint mPaint;
    SparseArray<SparseIntArray> mEmofing = new SparseArray<SparseIntArray>();
    SparseArray<String> colors = new SparseArray<String>();
    File path = Environment.getExternalStorageDirectory();
    String externalDirectory = path.toString() + "/otashu/";
    File bitmapSource = new File(externalDirectory + "emofing.png");
    int bitmap_width = 12;
    int bitmap_height = 4;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint;
    private FileOutputStream fout;
    private int[] emofingColors = {
            Color.rgb(0, 0, 0),
            Color.rgb(255, 51, 102), Color.rgb(255, 102, 51), Color.rgb(255, 204, 51),
            Color.rgb(204, 255, 51), Color.rgb(102, 255, 51), Color.rgb(51, 255, 102),
            Color.rgb(51, 255, 204), Color.rgb(51, 204, 255), Color.rgb(51, 102, 255),
            Color.rgb(102, 51, 255), Color.rgb(204, 51, 255), Color.rgb(255, 51, 204)
    };

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, SparseArray<SparseIntArray> emofing) {
        super(context);

        setmBitmapPaint(new Paint(Paint.DITHER_FLAG));
        mEmofing = emofing;
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setStrokeWidth(5.0f);

        mBitmap = Bitmap.createBitmap(bitmap_width, bitmap_height, Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        try {
            fout = new FileOutputStream(bitmapSource);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // 6. Loop through all found paths
        for (int i = 1; i <= mEmofing.size(); i++) {
            for (int j = 1; j <= 12; j++) {
                // 7. Plot root number reductions (the emofing)
                mPaint.setColor(emofingColors[j]);

                int value = mEmofing.get(i).get(j);

                // TODO: use alpha to display strength of a particular box in emofing
                mPaint.setAlpha(255);

                if (value <= 0) {
                    mPaint.setColor(Color.BLACK);
                }

                canvas.drawRect((j - 1) * 40, (i - 1) * 40, ((j - 1) * 40) + 40,
                        ((i - 1) * 40) + 40, mPaint);

                mCanvas.drawRect((j - 1) * 1, (i - 1) * 1, ((j - 1) * 1) + 1,
                        ((i - 1) * 1) + 1, mPaint);

                canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            }
        }

        try {
            // save bitmap
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }

    public Paint getmBitmapPaint() {
        return mBitmapPaint;
    }

    public void setmBitmapPaint(Paint mBitmapPaint) {
        this.mBitmapPaint = mBitmapPaint;
    }
}
