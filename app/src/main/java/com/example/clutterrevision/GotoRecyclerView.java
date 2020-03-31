package com.example.clutterrevision;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GotoRecyclerView extends RecyclerView implements Subject{
    LinearLayoutManager linearLayoutManager;
    List<DateObserver> observers;
    MainActivity activity;
    Fragment fragment;
    int myPosition;
    int prevPosition;
    int myLeft, myRight;
    int firstLeft, firstRight;
    int lastLeft, lastRight;


    public GotoRecyclerView(@NonNull Context context) {
        super(context);
        activity = (MainActivity) context;
        observers = new ArrayList<>();
    }

    public GotoRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        activity = (MainActivity) context;
        observers = new ArrayList<>();
    }

    public GotoRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        activity = (MainActivity) context;
        observers = new ArrayList<>();
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        prevPosition = adapter.getItemCount()-1;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        linearLayoutManager = (LinearLayoutManager) this.getLayoutManager();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        System.out.println("velocity x = " + velocityX);
        // views on the screen
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        View firstView = linearLayoutManager.findViewByPosition(firstVisibleItemPosition);
        View lastView = linearLayoutManager.findViewByPosition(lastVisibleItemPosition);
        if (lastView != null) {
            lastLeft = lastView.getLeft();
            lastRight = lastView.getRight();
        }
        if (firstView != null) {
            firstLeft = firstView.getLeft();
            firstRight = firstView.getRight();
        }

        if (lastLeft >= 0 && lastRight <= screenWidth) {
            myPosition = lastVisibleItemPosition;
        }

        if (firstLeft >= 0 && firstRight <= screenWidth) {
            myPosition = firstVisibleItemPosition;
        }

            myLeft = linearLayoutManager.findViewByPosition(myPosition).getLeft();
            myRight = linearLayoutManager.findViewByPosition(myPosition).getRight();



        if (Math.abs(velocityX) < 1000) {
            if (myLeft > screenWidth / 2) {
                if (myPosition  > 0) {
                    myPosition--;
                }
            } else if (myRight < screenWidth / 2) {
                if (myPosition < linearLayoutManager.getItemCount()-1) {
                    myPosition++;
                }
            } else {
            }
            smoothScrollToPosition(myPosition);
            if(myPosition!=prevPosition) {
                notifyObservers();
            }
            prevPosition = myPosition;
            notifyObservers();
            return true;
        } else {
            if (velocityX > 0) {
                if (myPosition < linearLayoutManager.getItemCount()-1) {
                    myPosition ++;
                }

            } else {
                if (myPosition  > 0) {
               myPosition--;
                }
            }
            smoothScrollToPosition(myPosition);
            if(myPosition!=prevPosition) {
                notifyObservers();
            }
            prevPosition = myPosition;
            return true;
        }
    }

    @Override
    public void register(DateObserver dateObserver) {
            observers.add(dateObserver);
    }

    @Override
    public void unregister(DateObserver dateObserver) {
        observers.remove(dateObserver);

    }

    @Override
    public void notifyObservers() {
        for(DateObserver dateObserver : observers){
            AdapterDay adapterDay = (AdapterDay) this.getAdapter();
            PojoDay pojoDay = adapterDay.data.get(myPosition);
            activity.viewModelActivity.setPosition(myPosition);
            dateObserver.onDateChanged(pojoDay.getDay_id());
        }
    }
}