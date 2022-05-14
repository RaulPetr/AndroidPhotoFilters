package com.example.filters.ui.filters;

import static com.example.filters.utils.SpeedDialUtils.getListener;
import static com.example.filters.utils.SpeedDialUtils.inflateMenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filters.R;

import com.example.filters.databinding.FragmentFiltersBinding;
import com.example.filters.ui.filters.thumbnail.ThumbnailCallback;
import com.example.filters.ui.filters.thumbnail.ThumbnailItem;
import com.example.filters.ui.filters.thumbnail.ThumbnailsAdapter;
import com.example.filters.ui.filters.thumbnail.ThumbnailsManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BlackAndWhiteSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.GrayscaleSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.NegativeSubfilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FilterFragment extends Fragment implements ThumbnailCallback {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    public static final String TAG = FilterFragment.class.getCanonicalName();
    private FragmentFiltersBinding binding;
    private RecyclerView thumbListView;
    private ImageView placeHolderImageView;
    private FilterFragment filterFragment;
    private SpeedDialView speedDialView;
    private Bitmap image;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFiltersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        filterFragment = this;
        speedDialView = binding.speedDial;
        thumbListView = (RecyclerView) binding.thumbnails;
        placeHolderImageView = (ImageView) binding.placeHolderImageview;
        preferences = getContext().getSharedPreferences("ImageURI", Context.MODE_PRIVATE);
        editor = preferences.edit();
        inflateMenu(speedDialView, R.menu.speed_dial_menu, getContext());
        speedDialView.setOnActionSelectedListener(getListener(filterFragment, placeHolderImageView.getDrawingCache(), getContext()));

        initUIWidgets();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();
            // Use Uri object instead of File to avoid storage permissions
            editor.putString("uri", uri.toString());
            editor.commit();
            Toast.makeText(this.getContext(), "yo", Toast.LENGTH_SHORT).show();
            initUIWidgets();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this.getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUIWidgets() {
        String uri = preferences.getString("uri", null);
        if (uri == null) {
            image = BitmapFactory.decodeResource(this.getResources(), R.drawable.photo);
        } else {
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), Uri.parse(uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        placeHolderImageView.setImageBitmap(image);
        initHorizontalList();
    }

    private void initHorizontalList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        thumbListView.setLayoutManager(layoutManager);
        thumbListView.setHasFixedSize(true);
        bindDataToAdapter();
    }

    private void bindDataToAdapter() {
        final Context context = this.getContext();
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                assert context != null;
                Bitmap thumbImage = image;
                ThumbnailItem t1 = new ThumbnailItem();
                ThumbnailItem t2 = new ThumbnailItem();
                ThumbnailItem t3 = new ThumbnailItem();
                ThumbnailItem t4 = new ThumbnailItem();
                ThumbnailItem t5 = new ThumbnailItem();
                ThumbnailItem t6 = new ThumbnailItem();
                ThumbnailItem t7 = new ThumbnailItem();
                ThumbnailItem t8 = new ThumbnailItem();
                ThumbnailItem t9 = new ThumbnailItem();

                t1.image = thumbImage;
                t2.image = thumbImage;
                t3.image = thumbImage;
                t4.image = thumbImage;
                t5.image = thumbImage;
                t6.image = thumbImage;
                t7.image = thumbImage;
                t8.image = thumbImage;
                t9.image = thumbImage;

                ThumbnailsManager.clearThumbs();
                ThumbnailsManager.addThumb(t1); // Original Image

                t2.filter = SampleFilters.getStarLitFilter();
                ThumbnailsManager.addThumb(t2);

                t3.filter = SampleFilters.getBlueMessFilter();
                ThumbnailsManager.addThumb(t3);

                t4.filter = SampleFilters.getAweStruckVibeFilter();
                ThumbnailsManager.addThumb(t4);

                t5.filter = SampleFilters.getLimeStutterFilter();
                ThumbnailsManager.addThumb(t5);

                t6.filter = SampleFilters.getNightWhisperFilter();
                ThumbnailsManager.addThumb(t6);

                t7.filter = new Filter();
                t7.filter.addSubFilter(new GrayscaleSubfilter());
                ThumbnailsManager.addThumb(t7);

                t8.filter = new Filter();
                t8.filter.addSubFilter(new NegativeSubfilter());
                ThumbnailsManager.addThumb(t8);

                t9.filter = new Filter();
                t9.filter.addSubFilter(new BlackAndWhiteSubfilter(127));
                ThumbnailsManager.addThumb(t9);

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(context);

                ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbs, (ThumbnailCallback) filterFragment);
                thumbListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    @Override
    public void onThumbnailClick(Filter filter) {
        placeHolderImageView.setImageBitmap(filter.processFilter(image.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}