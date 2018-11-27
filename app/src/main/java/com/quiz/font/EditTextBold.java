package com.quiz.font;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Dharmani Apps on 1/18/2018.
 */

public class EditTextBold extends EditText {
    public EditTextBold(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public EditTextBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public EditTextBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new LatoBold(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
