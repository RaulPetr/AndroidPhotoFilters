package com.example.filters.ui.editor.selector;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.SubFilter;

/**
 * @author Varun on 01/07/15.
 */
public class SelectorItem {
    public String name;
    public SubFilter filter;
    public Integer minValue, maxValue;

    public SelectorItem(String name, SubFilter filter, Integer minValue, Integer maxValue) {
        this.name = name;
        this.filter = filter;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
}
