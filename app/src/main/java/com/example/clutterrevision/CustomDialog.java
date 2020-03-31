package com.example.clutterrevision;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomDialog extends AlertDialog  {
    OnTouchOutsideListener onTouchOutsideListener;
    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            onTouchOutsideListener.onTouchOutside();
        }
        return super.onTouchEvent(event);
    }

    interface OnTouchOutsideListener{
        void onTouchOutside();
    }

    public void setListener(OnTouchOutsideListener onTouchOutsideListener){
        this.onTouchOutsideListener = onTouchOutsideListener;
    }
}
