package com.prismsoftworks.pullimagesviarestexample.models;

import android.graphics.Bitmap;

import com.prismsoftworks.pullimagesviarestexample.list.Item;

public class PullItem implements Item {
    private String content;
    private Bitmap imageBmp;

    public PullItem(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getImageBmp(){
        return imageBmp;
    }

    public void setImageBmp(Bitmap bmp){
        imageBmp = bmp.copy(Bitmap.Config.ARGB_8888, false);
    }
}
