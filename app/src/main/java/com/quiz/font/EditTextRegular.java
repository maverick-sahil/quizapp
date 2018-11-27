package com.quiz.font;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Dharmani Apps on 1/18/2018.
 */

public class EditTextRegular extends EditText {
    public EditTextRegular(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public EditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public EditTextRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }


    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new LatoRegular(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
