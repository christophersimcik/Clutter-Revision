package com.example.clutterrevision;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentCalendar extends Fragment {
    ViewModelCalendar viewModelCalendar;
    GridLayoutManager gridLayoutManager;
    AdapterCalendar adapterCalendar;
    RecyclerView recyclerView;
    TextView titleHeader;
    View main;
    GestureDetector gestureDetector;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        viewModelCalendar= ViewModelProviders.of(requireActivity()).get(ViewModelCalendar.class);
        gridLayoutManager = new GridLayoutManager(this.getContext(),7, RecyclerView.VERTICAL,false);
        gestureDetector = new GestureDetector(this.getContext(), new CustomGestureListener(this)){
        };


        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_calendar, container, false);
        main = binding.getRoot();
        adapterCalendar = new AdapterCalendar(this.getContext());
        recyclerView = main.findViewById(R.id.calendar_recycler_view);
        titleHeader = main.findViewById(R.id.calendar_month_year);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapterCalendar);
        viewModelCalendar.setData(adapterCalendar);
        viewModelCalendar.setHeader(titleHeader);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               return  gestureDetector.onTouchEvent(motionEvent);
            }
        });
        return main;
    }

    public void onFling(Boolean left){
        titleHeader.setTextColor(getResources().getColor(R.color.light_gray,null));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                titleHeader.setTextColor(getResources().getColor(R.color.default_text,null));
            }
        },250);
        viewModelCalendar.next(left);
        viewModelCalendar.setData(adapterCalendar);
        viewModelCalendar.setHeader(titleHeader);
    }



}
