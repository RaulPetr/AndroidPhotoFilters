package com.example.filters.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.filters.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static SpeedDialView.OnActionSelectedListener getListener(Fragment fragment, Bitmap image, Context context) {
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
                        File file;
                        String path = Environment.getExternalStorageDirectory().toString() + "/Filters/";
                        String timeStamp = new SimpleDateFormat("h:mm:ss-dd.MM.yy").format(new Date()) + ".jpg";
                        file = new File(path, timeStamp);
                        try {
                            OutputStream stream = new FileOutputStream(file);
                            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            stream.flush();
                            stream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context, "Image " + timeStamp + " Saved", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        };
    }
}
