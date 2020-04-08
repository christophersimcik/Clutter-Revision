package com.example.clutterrevision;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

public class FragmentDates extends Fragment implements DateObserver{

    GotoRecyclerView recyclerView;
    FragmentManager fragmentManager;
    AdapterDay adapterDay;
    ViewModelDay viewModelDay;
    Observer observerDates;
    MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
        viewModelDay = ViewModelProviders.of(requireActivity()).get(ViewModelDay.class);
        adapterDay = new AdapterDay(this.getContext(), viewModelDay);
        mainActivity = (MainActivity) getActivity();
            initObserver();
            registerObservers();

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_date, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.dates_recycler_view);
        recyclerView.setAdapter(adapterDay);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.register(this);
        if(mainActivity.viewModelActivity.datesLiveData != null) {
            viewModelDay.listOfDays = mainActivity.viewModelActivity.datesLiveData;
        }
        return root;
    }

    private void initObserver() {
        observerDates = new Observer<List<PojoDay>>() {
            @Override
            public void onChanged(List<PojoDay> pojoDays) {
                    System.out.println("fragment_dates " + pojoDays.size());
                    int position = 0;
                    if(!pojoDays.isEmpty()) {
                        mainActivity.viewModelActivity.pojoDay = pojoDays.get(pojoDays.size()- 1);
                        position = mainActivity.viewModelActivity.getPosition();
                        recyclerView.myPosition = position;
                    }
                    mainActivity.viewModelActivity.inititiateFragments();
                    adapterDay.setData(pojoDays);
                    recyclerView.getLayoutManager().scrollToPosition(position);
            }
        };
    }

    private void registerObservers() {
        viewModelDay.listOfDays.observe(this, observerDates);
    }

    @Override
    public void onDateChanged(String date) {
        System.out.println("*** fragment dates, date changed to " + date );
        viewModelDay.getDaysNotes(date);
        mainActivity.viewModelActivity.setCurrentDay(date);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position",recyclerView.myPosition);
    }

}

