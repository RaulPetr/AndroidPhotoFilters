package com.zomato.photofilters.helpers;

import android.util.Log;

public class MathFn {
    public static Integer map (Integer value, int start1, int stop1, int start2, int stop2) {
        Log.d("mathlib", "map: " + value + " " + start1 + " " + stop1 + " " + start2 + " " + stop2 + " " + start2 + (int)((stop2 - start2) * ((float)(value - start1) / (stop1 - start1))));
        return start2 + (int)((stop2 - start2) * ((float)(value - start1) / (stop1 - start1)));
    }
    public static Float mapFloat(float value, float start1, float stop1, float start2, float stop2) {
        Log.d("mathlib", "map: " + value + " " + start1 + " " + stop1 + " " + start2 + " " + stop2 + " " + (start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1))));
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }
    public static Float mapFloatExponential(float value, float start1, float stop1, float start2, float stop2) {
        Log.d("mathlib", "map: " + value + " " + start1 + " " + stop1 + " " + start2 + " " + stop2 + " " + (start2 + (stop2 - start2) * ((float)Math.pow(value - start1, 2) / (float)Math.pow(stop1 - start1, 2))));
        return start2 + (stop2 - start2) * ((float)Math.pow(value - start1, 2) / (float)Math.pow(stop1 - start1, 2));
    }
}
