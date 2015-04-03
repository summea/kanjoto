
package com.andrewsummers.otashu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * View a generated picture of a particular emotion's fingerprint.
 * <p>
 * This activity allows a user to see how an emotion's fingerprint appears.  
 * </p>
 */
public class DrawView extends View {
    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // 7. Plot root number reductions (the emofing)
        paint.setColor(Color.BLUE);
        paint.setAlpha(51);
        canvas.drawRect(0, 0, 50, 50, paint);
        paint.setColor(Color.RED);
        paint.setAlpha(102);
        canvas.drawRect(50, 0, 100, 50, paint);
        paint.setColor(Color.YELLOW);
        paint.setAlpha(153);
        canvas.drawRect(100, 0, 150, 50, paint);
        paint.setColor(Color.GREEN);
        paint.setAlpha(204);
        canvas.drawRect(150, 0, 200, 50, paint);
    }
}
