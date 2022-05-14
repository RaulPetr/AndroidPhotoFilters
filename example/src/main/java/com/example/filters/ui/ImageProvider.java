package com.example.filters.ui;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public abstract class ImageProvider extends Fragment {
    public abstract Bitmap getBitmap();
}
