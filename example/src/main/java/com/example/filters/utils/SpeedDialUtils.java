package com.example.filters.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;

import com.example.filters.ui.ImageProvider;
import com.example.filters.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class SpeedDialUtils {
    public static void inflateMenu(SpeedDialView speedDialView,
                                   @MenuRes int menuRes, Context context) {
        speedDialView.clearActionItems();
        PopupMenu popupMenu = new PopupMenu(context, new View(context));
        popupMenu.inflate(menuRes);
        Menu menu = popupMenu.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SpeedDialActionItem actionItem = new SpeedDialActionItem.Builder(menuItem.getItemId(), menuItem.getIcon())
                    .setLabel(menuItem.getTitle() != null ? menuItem.getTitle().toString() : null)
                    .setFabImageTintColor(context.getResources().getColor(R.color.md_white_1000))
                    .create();
            speedDialView.addActionItem(actionItem);
        }
    }

    public static SpeedDialView.OnActionSelectedListener getListener(ImageProvider fragment, ImageView imageView, Context context) {
        return new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.fab_upload:
                        ImagePicker.with(fragment)
                                //.crop()                    //Crop image(Optional), Check Customization for more option
                                .start();
                        break;
                    case R.id.fab_save:
                        int status = ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (status != PackageManager.PERMISSION_GRANTED) {
                            Log.d("saveFunction", "permission not granted");
                            Toast.makeText(fragment.getActivity(), "Permission not granted", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                        String path = "DCIM/Filters/";
                        String timeStamp = new SimpleDateFormat("h:mm:ss").format(new Date()) + ".jpg";
                        String date = new SimpleDateFormat("dd.MM.yy").format(new Date());
                        Log.d("saveFunction", fragment.getBitmap().toString());
                        SpeedDialUtils.saveImageAndInsertIntoMediaStore(fragment.getContext(), path + date, timeStamp, fragment.getBitmap(), 100, 100);

                        Toast.makeText(context, "Image " + timeStamp + " Saved", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        };
    }


    public static boolean saveImageAndInsertIntoMediaStore(
            Context context, String fileFullPath, String fileName,
            Bitmap bitmap, int jpgQuality, int pngQuality) {
        try {
            Log.d("saveFunction", "trying to save at " + fileFullPath);
            Bitmap.CompressFormat format;
            int quality;
            String mimeType;

            if (fileName.toLowerCase().endsWith(".jpg") ||
                    fileName.toLowerCase().endsWith(".jpeg")) {
                format = Bitmap.CompressFormat.JPEG;
                quality = jpgQuality;
                mimeType = "image/jpg";
            } else if (fileName.toLowerCase().endsWith(".png")) {
                format = Bitmap.CompressFormat.PNG;
                quality = pngQuality;
                mimeType = "image/png";
            } else {
                return false;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, fileFullPath);

            Log.d("saveFunction", contentValues.toString());

            ContentResolver resolver = context.getContentResolver();
            Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Log.d("saveFunction", "Saving at" + uri);
            OutputStream out = resolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(format, quality, out);
            Objects.requireNonNull(out).close();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static String getShortName(String path) {
        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);

        int pos = path.lastIndexOf('/');

        if (pos == -1) return path;

        return path.substring(pos + 1);
    }
}
