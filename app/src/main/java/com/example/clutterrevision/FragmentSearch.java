package com.example.clutterrevision;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.emoji.widget.EmojiEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentSearch extends Fragment implements ViewModelSearch.LiveDataCheck {
    ViewModelSearch viewModelSearch;
    AdapterNote adapterNote;
    FragmentManager fragmentManager;
    MainActivity mainActivity;
    RecyclerView recyclerView;
    EmojiEditText emojiEditText;
    ImageButton submit;
    TextView textView;
    TextView statusTextView;
    ImageView imageResults;
    AnimationDrawable animationDrawable;
    Observer<List<PojoNote>> pojoNoteListObserver;


    public FragmentSearch() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        viewModelSearch = ViewModelProviders.of(this).get(ViewModelSearch.class);
        viewModelSearch.registerLiveDataCheck(this);
        mainActivity = (MainActivity) getActivity();
        fragmentManager = mainActivity.getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        View root = binding.getRoot();
        viewModelSearch.getLiveData(getArguments());
        textView = root.findViewById(R.id.search_header);
        imageResults = root.findViewById(R.id.no_results_animation);
        imageResults.setVisibility(View.INVISIBLE);
        animationDrawable = (AnimationDrawable) imageResults.getBackground();
        statusTextView = root.findViewById(R.id.status_text_view);
        emojiEditText = root.findViewById(R.id.search_bar);
        adapterNote = new AdapterNote(this.getContext(), viewModelSearch, fragmentManager);
        recyclerView = root.findViewById(R.id.search_recycler_view);
        submit = root.findViewById(R.id.search_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModelSearch.search(emojiEditText.getText().toString());
                emojiEditText.setText("");
                emojiEditText.clearFocus();
                viewModelSearch.hideKeyboard(getContext(), emojiEditText);
                submit.setBackground(getResources().getDrawable(R.drawable.basic_hilite,null));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        submit.setBackground(getResources().getDrawable(R.drawable.basic,null));
                    }
                },250);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapterNote);
        return root;
    }

    @Override
    public void liveDataInitiated() {
        pojoNoteListObserver = new Observer<List<PojoNote>>() {
            @Override
            public void onChanged(List<PojoNote> pojoNotes) {
                adapterNote.setData(pojoNotes);
                viewModelSearch.getMessage(pojoNotes.size(),statusTextView,imageResults,animationDrawable);

            }
        };
        viewModelSearch.listOfNotes.observe(this, pojoNoteListObserver);
    }

}





