package com.epicture.aypicture;

import android.content.Context;
import android.util.AttributeSet;

/**
 * ImageView that always has the height set to the current width value.
 */
public class SquareImageView extends android.support.v7.widget.AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        // if our width doesn't match our height, set our height value to our width
        if (width != height) {
            setMeasuredDimension(width, width);
        }
    }
}