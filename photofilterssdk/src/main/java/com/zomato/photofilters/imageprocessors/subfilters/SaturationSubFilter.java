package com.zomato.photofilters.imageprocessors.subfilters;

import android.graphics.Bitmap;

import com.zomato.photofilters.helpers.MathFn;
import com.zomato.photofilters.imageprocessors.ImageProcessor;
import com.zomato.photofilters.imageprocessors.SubFilter;


/**
 * @author varun on 28/07/15.
 */
public class SaturationSubFilter implements SubFilter {
    private static String tag = "";

    // The Level value is float, where level = 1 has no effect on the image
    private float level;

    public SaturationSubFilter(float level) {
        this.level = level;
    }

    @Override
    public Bitmap process(Bitmap inputImage) {
        return ImageProcessor.doSaturation(inputImage, level);
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public void setTag(Object tag) {
        SaturationSubFilter.tag = (String) tag;
    }

    @Override
    public Integer getValue() {
        return Math.round(MathFn.mapFloat(level, 0, 10, 0, 100));
    }

    @Override
    public void setValue(Integer value) {
        this.level = MathFn.mapFloat(value, 0, 100, 0, 10);
    }

    public void setLevel(float level) {
        this.level = level;
    }

    /**
     * Get the current saturation level
     */
    public float getSaturation() {
        return level;
    }
}
