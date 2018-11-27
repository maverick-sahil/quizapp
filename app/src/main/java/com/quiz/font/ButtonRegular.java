package com.quiz.font;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Dharmani Apps on 1/18/2018.
 */

public class ButtonRegular extends Button {
    public ButtonRegular(Context context) {
        super(context);
    }

    public ButtonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ButtonRegular(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new LatoRegular(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
