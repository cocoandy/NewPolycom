package com.example.newpolycom;


public interface OnViewLongEventListener {
  void onDefaultClick(LongClickView view);
  void onLongClickDown(LongClickView view);
  void onLongClickUp(LongClickView view);
  void onDisabled(LongClickView view);
}
