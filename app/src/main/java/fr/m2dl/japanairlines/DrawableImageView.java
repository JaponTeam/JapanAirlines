package fr.m2dl.japanairlines;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by mfaure on 15/01/15.
 */
public class DrawableImageView extends ImageView {

    private boolean checked;

    public DrawableImageView(Context context) {
        super(context);
    }

    public DrawableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = canvas.getHeight();
        int width = canvas.getWidth();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        if (!isChecked()) {
            paint.setStyle(Paint.Style.STROKE);
        }

        canvas.drawRect(0, 0, width, height, paint);
    }

    public void changeState() {
        checked = !checked;
        invalidate();
    }

    public boolean isChecked() {
        return checked;
    }
}
