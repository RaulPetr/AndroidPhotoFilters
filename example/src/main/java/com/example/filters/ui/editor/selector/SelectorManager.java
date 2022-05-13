package com.example.filters.ui.editor.selector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Varun on 30/06/15.
 *         <p/>
 *         Singleton Class Used to Manage filters and process them all at once
 */
public final class SelectorManager {
    private static List<SelectorItem> selectorItems = new ArrayList<SelectorItem>(10);

    private SelectorManager() {
    }

    public static void addThumb(SelectorItem selectorItem) {
        selectorItems.add(selectorItem);
    }

    public static void clearThumbs() {
        selectorItems = new ArrayList<>();
    }
}
