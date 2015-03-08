
package com.andrewsummers.otashu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);
    }

    /**
     * <pre>
     * Example:
     *   1 2 3 4 5 6 7 8 9 10 11 12
     * 0 x   x   x   x
     * 1   x   x x     x
     * 2     x   x   x   x
     * 3 x   x   x         x
     * 4 x   x   x               x
     * 5   x   x   x   x
     * </pre>
     */
    @Override
    public void onDraw(Canvas canvas) {
        // 7. Plot root number reductions (the emofing)
        paint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, 50, 50, paint);
        paint.setColor(Color.RED);
        canvas.drawRect(50, 0, 100, 50, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(100, 0, 150, 50, paint);
        paint.setColor(Color.GREEN);
        canvas.drawRect(150, 0, 200, 50, paint);
    }
}
