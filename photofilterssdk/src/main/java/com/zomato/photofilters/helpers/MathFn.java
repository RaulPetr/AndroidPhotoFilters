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
    public static Float mapFloat100to10(float value) {
        Log.d("mathlib", "map100to10: " + value + " " + (((0.1)*Math.pow(1.0471, value) - 0.1)));
        return (float)(((0.1)*Math.pow(1.0471, value) - 0.1));
    }
    public static Float mapFloat10to100(float value) {
        Log.d("mathlib", "map10to100: " + value + " " + (Math.log(value*10)/Math.log(1.0471)));
        return (float)(Math.log(value*10)/Math.log(1.0471));
    }
}
