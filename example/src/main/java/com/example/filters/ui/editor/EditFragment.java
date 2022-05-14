package com.example.filters.ui.editor;

import static com.example.filters.utils.SpeedDialUtils.getListener;
import static com.example.filters.utils.SpeedDialUtils.inflateMenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filters.R;
import com.example.filters.databinding.FragmentEditingBinding;
import com.example.filters.ui.editor.selector.SelectorAdapter;
import com.example.filters.ui.editor.selector.SelectorCallback;
import com.example.filters.ui.editor.selector.SelectorItem;
import com.example.filters.ui.filters.FilterFragment;
import com.example.filters.ui.filters.thumbnail.ThumbnailCallback;
import com.example.filters.ui.filters.thumbnail.ThumbnailItem;
import com.example.filters.ui.filters.thumbnail.ThumbnailsAdapter;
import com.example.filters.ui.filters.thumbnail.ThumbnailsManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.leinardi.android.speeddial.SpeedDialView;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.SubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.BlackAndWhiteSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditFragment extends Fragment implements SelectorCallback {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    public static final String TAG = EditFragment.class.getCanonicalName();
    private FragmentEditingBinding binding;
    private RecyclerView selectorsView;
    private ImageView imageView;
    private EditFragment editFragment;
    private SpeedDialView speedDialView;
    private Bitmap image;
    private TextView filterName;
    private SeekBar seekBar;
    private Filter filter;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editFragment = this;
        speedDialView = binding.speedDialEdit;
        imageView = binding.editImageview;
        selectorsView = (RecyclerView) binding.selectors;
        filterName = binding.textViewEdit;
        seekBar = binding.seekBar;
        filter = new Filter();
        preferences = getContext().getSharedPreferences("ImageURI", Context.MODE_PRIVATE);
        editor = preferences.edit();
        inflateMenu(speedDialView, R.menu.speed_dial_menu, getContext());
        speedDialView.setOnActionSelectedListener(getListener(editFragment, imageView.getDrawingCache(), getContext()));

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

        imageView.setImageBitmap(image);
        seekBar.setMax(100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seekBar.setProgress(50, true);
        } else {
            seekBar.setProgress(50);
        }
        initHorizontalList();
    }

    private void initHorizontalList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        selectorsView.setLayoutManager(layoutManager);
        selectorsView.setHasFixedSize(true);
        bindDataToAdapter();
    }

    private void bindDataToAdapter() {
        final Context context = this.getContext();
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                assert context != null;
                Bitmap thumbImage = image;
                List<SelectorItem> selectors = new ArrayList<>();


                SelectorItem t1 = new SelectorItem("Brightness", new BrightnessSubFilter(0), -255, 255);
                SelectorItem t2 = new SelectorItem("Saturation", new SaturationSubFilter(1), 0, 10);
                SelectorItem t3 = new SelectorItem("Contrast", new ContrastSubFilter(1), 0, 10);
                SelectorItem t4 = new SelectorItem("Vignette", new VignetteSubFilter(getContext(), 0), 0, 255);
                SelectorItem t5 = new SelectorItem("Black\n &\n White", new BlackAndWhiteSubfilter(256), 0, 255);

                selectors.add(t1);
                selectors.add(t2);
                selectors.add(t3);
                selectors.add(t4);
                selectors.add(t5);

                selectors.stream().forEach(selector -> {
                    selector.filter.setTag(selector.name);
                    filter.addSubFilter(selector.filter);
                });

                SelectorAdapter adapter = new SelectorAdapter(selectors, (SelectorCallback) editFragment);
                selectorsView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    @Override
    public void onSelectorClick(SelectorItem item) {
        //set the current selector
        filterName.setText("Adjust " + item.name);
        SubFilter current = filter.getSubFilterByTag(item.name);
        seekBar.setProgress(current.getValue());
        Log.d(TAG, filter.toString());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean tracking = false;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser && !tracking) {
                    current.setValue(progress);
                    imageView.setImageBitmap(filter.processFilter(image.copy(Bitmap.Config.ARGB_8888, true)));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tracking = false;
                onProgressChanged(seekBar, seekBar.getProgress(), true);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}