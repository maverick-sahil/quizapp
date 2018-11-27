package com.quiz.font;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by android-da on 5/23/18.
 */

public class EditTextMedium extends EditText {
    public EditTextMedium(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public EditTextMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public EditTextMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }


    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new LatoMedium(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
