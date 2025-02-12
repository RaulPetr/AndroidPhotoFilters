package com.zomato.photofilters.imageprocessors;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author Varun on 29/06/15.
 */
public final class ImageProcessor {
    public static final String TAG = ImageProcessor.class.getCanonicalName();

    private ImageProcessor() {
    }

    public static Bitmap applyCurves(int[] rgb, int[] red, int[] green, int[] blue, Bitmap inputImage) {
        // create output bitmap
        Bitmap outputImage = inputImage;

        // get image size
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        int[] pixels = new int[width * height];
        outputImage.getPixels(pixels, 0, width, 0, 0, width, height);

        if (rgb != null) {
            pixels = NativeImageProcessor.applyRGBCurve(pixels, rgb, width, height);
        }

        if (!(red == null && green == null && blue == null)) {
            pixels = NativeImageProcessor.applyChannelCurves(pixels, red, green, blue, width, height);
        }

        try {
            outputImage.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (IllegalStateException ise) {
        }
        return outputImage;
    }

    public static Bitmap doBrightness(int value, Bitmap inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        Log.i(TAG, String.valueOf(width) + String.valueOf(height));
        int[] pixels = new int[width * height];

        inputImage.getPixels(pixels, 0, width, 0, 0, width, height);
        NativeImageProcessor.doBrightness(pixels, value, width, height);
        inputImage.setPixels(pixels, 0, width, 0, 0, width, height);

        return inputImage;
    }

    public static Bitmap doContrast(float value, Bitmap inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] pixels = new int[width * height];

        inputImage.getPixels(pixels, 0, width, 0, 0, width, height);
        NativeImageProcessor.doContrast(pixels, value, width, height);
        inputImage.setPixels(pixels, 0, width, 0, 0, width, height);

        return inputImage;
    }


    public static Bitmap doColorOverlay(int depth, float red, float green, float blue, Bitmap inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] pixels = new int[width * height];

        inputImage.getPixels(pixels, 0, width, 0, 0, width, height);
        NativeImageProcessor.doColorOverlay(pixels, depth, red, green, blue, width, height);
        inputImage.setPixels(pixels, 0, width, 0, 0, width, height);

        return inputImage;
    }

    public static Bitmap doSaturation(Bitmap inputImage, float level) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] pixels = new int[width * height];

        inputImage.getPixels(pixels, 0, width, 0, 0, width, height);
        NativeImageProcessor.doSaturation(pixels, level, width, height);
        inputImage.setPixels(pixels, 0, width, 0, 0, width, height);
        return inputImage;
    }

    public static Bitmap doNegative(Bitmap inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] pixels = new int[width * height];

        inputImage.getPixels(pixels, 0, width, 0, 0, width, height);
        NativeImageProcessor.doNegative(pixels, width, height);
        inputImage.setPixels(pixels, 0, width, 0, 0, width, height);
        return inputImage;
    }

    public static Bitmap doGrayscale(Bitmap inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] pixels = new int[width * height];

        inputImage.getPixels(pixels, 0, width, 0, 0, width, height);
        NativeImageProcessor.doGrayscale(pixels, width, height);
        inputImage.setPixels(pixels, 0, width, 0, 0, width, height);
        return inputImage;
    }
    public static Bitmap doBlackAndWhite(Bitmap inputImage, int threshold) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] pixels = new int[width * height];

        inputImage.getPixels(pixels, 0, width, 0, 0, width, height);
        NativeImageProcessor.doBW(pixels, threshold, width, height);
        inputImage.setPixels(pixels, 0, width, 0, 0, width, height);
        return inputImage;
    }


}
