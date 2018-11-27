package com.quiz.font;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by android-da on 5/23/18.
 */

public class ButtonMedium extends Button {
    public ButtonMedium(Context context) {
        super(context);
    }

    public ButtonMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ButtonMedium(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new LatoMedium(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
