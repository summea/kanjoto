
package com.andrewsummers.otashu.view;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    SparseArray<String> colors = new SparseArray<String>();

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, SparseArray<SparseIntArray> emofing) {
        super(context);
        mEmofing = emofing;
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
        paint.setStrokeWidth(5.0f);
        // 6. Loop through all found paths
        for (int i = 1; i <= mEmofing.size(); i++) {
            for (int j = 1; j <= 12; j++) {
                // 7. Plot root number reductions (the emofing)
                Random random = new Random();
                int color = random.nextInt(255 - 0 + 1) + 0;
                paint.setColor(Color.parseColor(colors.get(j)));
                int value = mEmofing.get(i).get(j);
                // use alpha to display strength of a particular box in emofing
                switch (value) {
                    case 1:
                        paint.setAlpha(51);
                        break;
                    case 2:
                        paint.setAlpha(102);
                        break;
                    case 3:
                        paint.setAlpha(153);
                        break;
                    case 4:
                        paint.setAlpha(204);
                        break;
                    case 5:
                        paint.setAlpha(255);
                        break;
                    default:
                        paint.setAlpha(25);
                }
                canvas.drawRect((j - 1) * 40, (i - 1) * 40, ((j - 1) * 40) + 40,
                        ((i - 1) * 40) + 40, paint);
            }
        }
    }
}
