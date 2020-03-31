package com.example.clutterrevision;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentBooks extends Fragment implements RepositoryGoogleBooksAPI.EmptyListReturned {
    FragmentManager fragmentManager;
    ViewModelBook viewModelBook;
    RecyclerView recyclerView;
    ImageView progressBar;
    TextView updates;
    AdapterBooks adapterBooks;
    AnimationDrawable animationProgress;
    LinearLayout linearLayout;
    Activity activity;
    Observer<List<PojoBook>> observerBooks;
    ViewGroup viewGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        fragmentManager = getActivity().getSupportFragmentManager();
        activity = this.getActivity();
        fragmentManager = getActivity().getSupportFragmentManager();
        viewModelBook = ViewModelProviders.of(this).get(ViewModelBook.class);
        viewModelBook.getData(getArguments());
        viewModelBook.registerEmptyCallback(this);
        adapterBooks = new AdapterBooks(this.getContext(),viewModelBook);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book, container, false);
        viewGroup = container;
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.books_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapterBooks);
        progressBar = root.findViewById(R.id.google_books_progress_bar);
        animationProgress = (AnimationDrawable) progressBar.getDrawable();
        animationProgress.start();
        updates = root.findViewById(R.id.no_data_warning);
        viewModelBook.setTextView(updates);
        linearLayout = root.findViewById(R.id.view_containers);
        viewGroup = linearLayout;
        initObservers();
        registerObservers();
        return root;
    }

    private void initObservers() {

        observerBooks = new Observer<List<PojoBook>>() {
            @Override
            public void onChanged(List<PojoBook> pojoBooks) {
                System.out.println("size of pojoBooks = " + pojoBooks);
                animationProgress.stop();
                viewGroup.removeView(progressBar);
                adapterBooks.setData(pojoBooks);
            }
        };
    }

    private void registerObservers() {
        viewModelBook.pojoBooks.observe(this, observerBooks);
    }

    @Override
    public void onEmptyReturned() {
        updates.setText("No Books Found");
        animationProgress.stop();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager.popBackStack();
            }
        },750);
    }

    @Override
    public void onListReturned() {
        linearLayout.setBackground(null);
        viewGroup.removeView(updates);
    }
}
