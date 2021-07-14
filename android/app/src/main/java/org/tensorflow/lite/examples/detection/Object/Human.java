package org.tensorflow.lite.examples.detection.Object;

import android.graphics.Bitmap;

public class Human {
    private Bitmap avt;
    private  String info;

    public Human(Bitmap img, String name) {
        avt = img;
        info = name;
    }

    public Bitmap getAvt() {
        return avt;
    }

    public String getInfo() {
        return info;
    }


}
