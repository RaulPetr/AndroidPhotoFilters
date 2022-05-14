package com.zomato.photofilters.imageprocessors.subfilters;

import android.graphics.Bitmap;

import com.zomato.photofilters.helpers.MathFn;
import com.zomato.photofilters.imageprocessors.ImageProcessor;
import com.zomato.photofilters.imageprocessors.SubFilter;

public class BlackAndWhiteSubfilter implements SubFilter {
    private String tag = "";
    private int treshold;
    private boolean active = false;

    //use treshold 256 to keep the original image until input is received
    public BlackAndWhiteSubfilter(int treshold) {
        active = true;
        if(treshold == 256) {
            treshold = 128;
            active = false;
        }
        this.treshold = treshold;
    }

    @Override
    public Bitmap process(Bitmap inputImage) {
        if(!active) {
            return inputImage;
        }
        return ImageProcessor.doBlackAndWhite(inputImage, treshold);
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public void setTag(Object tag) {
        this.tag = tag.toString();
    }

    @Override
    public Integer getValue() {
        return MathFn.map(treshold, 0, 255, 0, 100);
    }

    @Override
    public void setValue(Integer value) {
        treshold = MathFn.map(value, 0, 100, 0, 255);
        active = true;
    }
}
