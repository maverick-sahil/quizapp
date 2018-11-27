package com.quiz.utils;

/**
 * Created by android-da on 5/19/18.
 */

public class QUIZSingleton {

    private static QUIZSingleton ourInstance;


    private QUIZSingleton() {
    }


    public static QUIZSingleton getInstance() {
        if (ourInstance == null) {
            ourInstance = new QUIZSingleton();
        }
        return ourInstance;

    }
}
