package com.example.newpolycom;

public interface LongClickView {
    int getViewId();

    String getClickNumber();

    void setClickNumber(String clickNumber);

    String getLongClickNumber();

    void setLongClickNumber(String longClickNumber);

    boolean isLongClick();

    void setLongClick(boolean longClick);

    void setOnViewLongEventListener(OnViewLongEventListener callback);

    String getTextString();
}
