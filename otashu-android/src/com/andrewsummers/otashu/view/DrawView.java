
package com.andrewsummers.otashu.view;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

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

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, SparseArray<SparseIntArray> emofing) {
        super(context);

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        // mCanvas = new Canvas(mBitmap);
        mEmofing = emofing;
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setStrokeWidth(5.0f);

        mBitmap = Bitmap.createBitmap(bitmap_width, bitmap_height, Config.ARGB_8888);
        // canvas.setBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);

        colors.put(1, "#ff4d4d");
        colors.put(2, "#ff9933");
        colors.put(3, "#ffff66");
        colors.put(4, "#73e600");
        colors.put(5, "#00b8b8");
        colors.put(6, "#ff33ff");
        colors.put(7, "#a64dff");
        colors.put(8, "#7777ff");
        colors.put(9, "#ffc6b3");
        colors.put(10, "#ffecb3");
        colors.put(11, "#ffffcc");
        colors.put(12, "#ecffb3");
    }

    @Override
    public void onDraw(Canvas canvas) {
        // 6. Loop through all found paths
        for (int i = 1; i <= mEmofing.size(); i++) {
            for (int j = 1; j <= 12; j++) {
                // 7. Plot root number reductions (the emofing)
                Random random = new Random();
                // int color = random.nextInt(255 - 0 + 1) + 0;
                // mPaint.setColor(Color.parseColor(colors.get(j)));

                switch (j) {
                    case 1:
                        mPaint.setColor(Color.rgb(255, 51, 102));
                        break;
                    case 2:
                        mPaint.setColor(Color.rgb(255, 102, 51));
                        break;
                    case 3:
                        mPaint.setColor(Color.rgb(255, 204, 51));
                        break;
                    case 4:
                        mPaint.setColor(Color.rgb(204, 255, 51));
                        break;
                    case 5:
                        mPaint.setColor(Color.rgb(102, 255, 51));
                        break;
                    case 6:
                        mPaint.setColor(Color.rgb(51, 255, 102));
                        break;
                    case 7:
                        mPaint.setColor(Color.rgb(51, 255, 204));
                        break;
                    case 8:
                        mPaint.setColor(Color.rgb(51, 204, 255));
                        break;
                    case 9:
                        mPaint.setColor(Color.rgb(51, 102, 255));
                        break;
                    case 10:
                        mPaint.setColor(Color.rgb(102, 51, 255));
                        break;
                    case 11:
                        mPaint.setColor(Color.rgb(204, 51, 255));
                        break;
                    case 12:
                        mPaint.setColor(Color.rgb(255, 51, 204));
                        break;
                }

                int value = mEmofing.get(i).get(j);
                Log.d("MYLOG", "emofing: getting value (" + i + ", " + j + "): " + value);
                // use alpha to display strength of a particular box in emofing
                switch (value) {
                    case 1:
                        // mPaint.setAlpha(51);
                        // break;
                    case 2:
                        // mPaint.setAlpha(102);
                        // break;
                    case 3:
                        // mPaint.setAlpha(153);
                        // break;
                    case 4:
                        // mPaint.setAlpha(204);
                        // break;
                    case 5:
                        // mPaint.setAlpha(255);
                        // break;
                    default:
                        mPaint.setAlpha(255);
                }

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

        // mCanvas = new Canvas(mBitmap);
        try {
            FileOutputStream fout = new FileOutputStream(bitmapSource);
            // delete old emofing if necessary
            // if (bitmapSource.exists()) {
            // bitmapSource.delete();
            // } else {
            // bitmapSource.createNewFile();
            // }
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();
            Log.d("MYLOG", "> saved bitmap...");
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }
}
