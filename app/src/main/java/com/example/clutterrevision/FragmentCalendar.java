package com.example.clutterrevision;

import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentCalendar extends Fragment implements CustomRelativeLayout.CallbackTouch {

    ViewModelCalendar viewModelCalendar;
    AdapterCalendar adapterCalendar;
    CustomRelativeLayout customRelativeLayout;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    TextView titleHeader;
    MainActivity mainActivity;
    Button homeButton;
    View main;
    float startX;
    float viewX;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        viewModelCalendar = ViewModelProviders.of(requireActivity()).get(ViewModelCalendar.class);
        viewModelCalendar.setViewModelActivity(mainActivity.viewModelActivity);
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        main = binding.getRoot();
        customRelativeLayout = main.findViewById(R.id.calendar_main_container);
        adapterCalendar = new AdapterCalendar(this.getContext(),this.mainActivity.getSupportFragmentManager(), viewModelCalendar, viewModelCalendar.getViewModelActivity());
        viewModelCalendar.repositoryCalendar.repositoryDay.register(adapterCalendar);
        customRelativeLayout.setCallbackTouch(this);
        linearLayout = main.findViewById(R.id.weekday_names);
        recyclerView = main.findViewById(R.id.calendar_recycler_view);
        titleHeader = main.findViewById(R.id.calendar_month_year);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 7, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapterCalendar);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recordPosition(recyclerView.getX());
            }
        });
        viewModelCalendar.setData(adapterCalendar);
        viewModelCalendar.setHeader(titleHeader);
        homeButton = main.findViewById(R.id.calendar_home_button);
        homeButton.setText(makeSpannableString(baseString()));
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeButton.setBackground(getResources().getDrawable(R.drawable.calendar_icon_hilite,null));
                homeButton.setTextColor(getResources().getColor(R.color.accent_red,null));
                recyclerView.setVisibility(View.INVISIBLE);
                titleHeader.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        homeButton.setBackground(getResources().getDrawable(R.drawable.calendar_icon,null));
                        homeButton.setTextColor(getResources().getColor(R.color.default_text,null));
                        recyclerView.setVisibility(View.VISIBLE);
                        titleHeader.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                },250);
                viewModelCalendar.reset();
                viewModelCalendar.setData(adapterCalendar);
                viewModelCalendar.setHeader(titleHeader);
            }
        });

        return main;
    }

    private int findNextLineChar(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '\n') {
                return i;
            }
        }
        return 0;
    }

    private String baseString(){
        String year = viewModelCalendar.dayHelper.getYear();
        String month = Constants.monthsOfYear.get(viewModelCalendar.dayHelper.getMonthAsInt());
        return year+"\n"+month;
    }
    private SpannableStringBuilder makeSpannableString(String string){
        int nextLine = findNextLineChar(string);
        RelativeSizeSpan halfSizeText = new RelativeSizeSpan(.75f);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        spannableStringBuilder.setSpan(halfSizeText,0,nextLine, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    public int getScreenWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int w = point.x;
        int h = point.y;
        int size = Math.min(w, h);
        return size;
    }

    private void moveView(float x) {
        float distance = x - startX;
        float position = viewX + distance;
        recyclerView.setX(position);
    }

    private void resetView(float x) {
        recyclerView.setX(x);
    }

    private void recordPosition(float x) {
        viewX = x;
    }

    @Override
    public void down(float downX) {
        startX = downX;
    }

    @Override
    public void move(float moveX) {
        moveView(moveX);
        titleHeader.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void up() {
        titleHeader.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        if (recyclerView.getX() <= 0 - getScreenWidth() * .40) {
            titleHeader.setVisibility(View.INVISIBLE);
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (recyclerView.getX() > 0 - recyclerView.getWidth()) {
                        recyclerView.setX(recyclerView.getX() - 50);
                        handler.post(this);
                    } else {
                        viewModelCalendar.next(true);
                        viewModelCalendar.setData(adapterCalendar);
                        viewModelCalendar.setHeader(titleHeader);
                        titleHeader.setVisibility(View.VISIBLE);
                        resetView(viewX);
                    }
                }
            });
        } else if (recyclerView.getX() > getScreenWidth() * .40) {
            titleHeader.setVisibility(View.INVISIBLE);
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (recyclerView.getX() < getScreenWidth()) {
                        recyclerView.setX(recyclerView.getX() + 50);
                        handler.post(this);
                    } else {
                        viewModelCalendar.next(false);
                        viewModelCalendar.setData(adapterCalendar);
                        viewModelCalendar.setHeader(titleHeader);
                        titleHeader.setVisibility(View.VISIBLE);
                        resetView(viewX);
                    }
                }
            });
        } else {
            resetView(viewX);
        }
    }
}
