package com.quiz.font;

import android.content.Context;
import android.graphics.Typeface;



public class LatoMedium {

    String path = "Lato_Medium.ttf";
    Context mContext;
    public static Typeface mTypeface;
    public LatoMedium(Context context){
        mContext = context;
    }

    public Typeface getFontFamily(){
        try{
            if (mTypeface == null)
                mTypeface = Typeface.createFromAsset(mContext.getAssets(),path);
        }catch (Exception e){
            e.printStackTrace();
        }

        return mTypeface;
    }
}
