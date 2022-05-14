package com.zomato.photofilters.imageprocessors.subfilters;

import android.graphics.Bitmap;

import com.zomato.photofilters.helpers.MathFn;
import com.zomato.photofilters.imageprocessors.ImageProcessor;
import com.zomato.photofilters.imageprocessors.SubFilter;


/**
 * @author varun
 * subfilter used to tweak brightness of the Bitmap
 */
public class BrightnessSubFilter implements SubFilter {
    private static String tag = "";
    // Value is in integer
    private int brightness = 0;

    /**
     * Takes Brightness of the image
     *
     * @param brightness Integer brightness value {value 0 has no effect}
     */
    public BrightnessSubFilter(int brightness) {
        this.brightness = brightness;
    }

    @Override
    public Bitmap process(Bitmap inputImage) {
        return ImageProcessor.doBrightness(brightness, inputImage);
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(Object tag) {
        BrightnessSubFilter.tag = (String) tag;
    }

    @Override
    public Integer getValue() {
        return MathFn.map(brightness, -255, 255, 0, 100);
    }

    @Override
    public void setValue(Integer value) {
        this.setBrightness(MathFn.map(value, 0, 100, -255, 255));
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    /**
     * Changes the brightness by the value passed as parameter
     */
    public void changeBrightness(int value) {
        this.brightness += value;
    }

    /**
     * Get current Brightness level
     */
    public int getBrightness() {
        return brightness;
    }
}
