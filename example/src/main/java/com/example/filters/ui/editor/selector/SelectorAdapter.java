package com.example.filters.ui.editor.selector;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.filters.R;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

/**
 * @author Varun on 01/07/15.
 */
public class SelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "SELECTOR_ADAPTER";
    private static int lastPosition = -1;
    private SelectorCallback selectorCallback;
    private List<SelectorItem> dataSet;

    public SelectorAdapter(List<SelectorItem> dataSet, SelectorCallback selectorCallback) {
        Log.v(TAG, "Thumbnails Adapter has " + dataSet.size() + " items");
        this.dataSet = dataSet;
        this.selectorCallback = selectorCallback;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.v(TAG, "On Create View Holder Called");
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_selector_item, viewGroup, false);
        return new SelectorsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        final SelectorItem selectorItem = dataSet.get(i);
        Log.v(TAG, "On Bind View Called");
        SelectorsViewHolder selectorsViewHolder = (SelectorsViewHolder) holder;
        selectorsViewHolder.name.setText(selectorItem.name);
        setAnimation(selectorsViewHolder.name, i);
        selectorsViewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPosition != i) {
                    selectorCallback.onSelectorClick(selectorItem.filter);
                    lastPosition = i;
                }
            }

        });
    }

    private void setAnimation(View viewToAnimate, int position) {
        {
            ViewHelper.setAlpha(viewToAnimate, .0f);
            com.nineoldandroids.view.ViewPropertyAnimator.animate(viewToAnimate).alpha(1).setDuration(250).start();
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class SelectorsViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public SelectorsViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.selector);
        }
    }
}
