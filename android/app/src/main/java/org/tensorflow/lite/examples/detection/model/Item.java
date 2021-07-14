package org.tensorflow.lite.examples.detection.model;

import android.graphics.Bitmap;

import org.tensorflow.lite.examples.detection.R;

public class Item {
    String description;
    Bitmap image;

    public Item() {
    }

    public Item(String description, Bitmap image) {
        this.description = description;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
