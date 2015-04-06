
package com.andrewsummers.otashu.view;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    Paint paint = new Paint();
    SparseArray<SparseIntArray> mEmofing = new SparseArray<SparseIntArray>();

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, SparseArray<SparseIntArray> emofing) {
        super(context);
        mEmofing = emofing;
    }

    @Override
    public void onDraw(Canvas canvas) {

        Log.d("MYLOG", "emofing received: " + mEmofing.toString());
        Log.d("MYLOG", "emofing size: " + mEmofing.size());

        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(5.0f);
        // 6. Loop through all found paths

        for (int i = 1; i <= mEmofing.size(); i++) {
            for (int j = 1; j <= 12; j++) {
                // 7. Plot root number reductions (the emofing)
                // TODO: make emofing out of boxes
                // TODO: add colors
                Random random = new Random();
                int color = random.nextInt(255 - 0 + 1) + 0;
                paint.setColor(Color.rgb(color, color, color));
                canvas.drawRect(j * 20, i * 20, j * 40, i * 40, paint);
            }
        }
    }
}
