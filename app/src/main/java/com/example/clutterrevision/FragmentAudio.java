package com.example.clutterrevision;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class FragmentAudio extends Fragment {

    FragmentManager fragmentManager;
    ViewModelAudio vmAudio;
    VisualAudioRealtime visualAudioRealtime;
    VisualAudio visualAudio;
    Observer<short[]> observer;
    Observer<String> timeObserver;
    Observer<Boolean> canStart;
    Observer<Boolean> canStop;
    Observer<Boolean> canSubmit;
    Button start;
    Button stop;
    Button submit;
    Boolean playing = false;
    Boolean clicked = false;
    MainActivity activity;
    int activeColor, inactiveColor;
    Drawable buttonActive, buttonInactive, buttonRed, buttonGreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        assignDrawables();
        assignColors();
        activity = (MainActivity) this.getActivity();

        fragmentManager = getActivity().getSupportFragmentManager();
        vmAudio = ViewModelProviders.of(this).get(ViewModelAudio.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_audio, container, false);
        View root = binding.getRoot();
        start = root.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!playing) {
                    if(vmAudio.fileHelper.temporaryFile == null){
                        vmAudio.createTemporaryFile(getActivity());
                    }

                    vmAudio.start();
                    playing = true;
                }
            }
        });
        stop = root.findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playing) {
                    vmAudio.stop(getActivity());
                    playing = false;
                }
            }
        });
        submit = root.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(vmAudio.fileHelper.audioFile == null){
                        try {
                            vmAudio.writeFile(getActivity());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    vmAudio.submit(getActivity());
                    playing = false;
                    vmAudio.getData();
                vmAudio.insert(vmAudio.createNote());
                jumpToToday(activity);
                fragmentManager.popBackStack();

            }
        });
        visualAudioRealtime = root.findViewById(R.id.visualizer);
        canStart = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    start.setClickable(true);
                    start.setBackground(buttonGreen);
                    start.setTextColor(activeColor);
                }else{
                    start.setClickable(false);
                    start.setBackground(buttonInactive);
                    start.setTextColor(inactiveColor);
                }
            }
        };
        canStop = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    stop.setClickable(true);
                    stop.setBackground(buttonRed);
                    stop.setTextColor(activeColor);
                }else{
                    stop.setClickable(false);
                    stop.setBackground(buttonInactive);
                    stop.setTextColor(inactiveColor);
                }
            }
        };
        canSubmit = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    submit.setClickable(true);
                    submit.setBackground(buttonActive);
                    submit.setTextColor(activeColor);
                }else{
                    submit.setClickable(false);
                    submit.setBackground(buttonInactive);
                    submit.setTextColor(inactiveColor);
                }
            }
        };

        timeObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                visualAudioRealtime.time = s;
            }
        };
        observer = new Observer<short[]>() {
            @Override
            public void onChanged(short[] o) {
                if(playing) {
                    vmAudio.getTime();
                }
                visualAudioRealtime.pnts = o;
                visualAudioRealtime.invalidate();
            }
        };

        vmAudio.values.observe(this, observer);
        vmAudio.time.observe(this, timeObserver);
        vmAudio.canStart.observe(this,canStart);
        vmAudio.canStop.observe(this,canStop);
        vmAudio.canSubmit.observe(this,canSubmit);


        return root;
    }

    private void assignDrawables(){
        buttonActive = getResources().getDrawable(R.drawable.button_active,null);
        buttonInactive = getResources().getDrawable(R.drawable.button_inactive,null);
        buttonRed = getResources().getDrawable(R.drawable.button_red,null);
        buttonGreen = getResources().getDrawable(R.drawable.button_green,null);
    }

    private void assignColors(){
        activeColor = getResources().getColor(R.color.default_text,null);
        inactiveColor = getResources().getColor(R.color.light_gray,null);
    }

    private void jumpToToday(MainActivity mainActivity){
        ViewModelActivity vma = mainActivity.viewModelActivity;
        vma.setPosition(vma.datesLiveData.getValue().size()-1);
        vma.setCurrentDay(vma.dayHelper.getDateAsString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vmAudio.release();
        vmAudio.stopTimer();
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }



    @Override
    public void onPause(){
        super.onPause();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
