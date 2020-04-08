package com.example.clutterrevision;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.PipedOutputStream;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class FragmentNote extends Fragment {

    EditText body;
    EditText title;
    Button submit;
    FragmentManager fragmentManager;
    ViewModelNote viewModelNote;
    Observer<PojoNote> noteObserver;
    Observer<CharSequence> titleObserver;
    MainActivity mainActivity;
    TextWatcher titleWatcher;
    TextView dateText;
    Boolean canPress = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
        viewModelNote = ViewModelProviders.of(this).get(ViewModelNote.class);
        titleWatcher = viewModelNote.createTitleWatcher();
        viewModelNote.unBundler(getArguments());
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        viewModelNote.setPojoDay(mainActivity.viewModelActivity.pojoDay);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note, container, false);
        View root = binding.getRoot();
        dateText = root.findViewById(R.id.date_id_text);
        dateText.setText(viewModelNote.parseDate());
        body = root.findViewById(R.id.body_note);
        body.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModelNote.setNoteTitle(charSequence);
                viewModelNote.pojoNote.setContent(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        title = root.findViewById(R.id.title_note);
        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    title.addTextChangedListener(titleWatcher);
                } else {
                    title.removeTextChangedListener(titleWatcher);
                    viewModelNote.defaultTitle = false;
                }
            }
        });

        submit = root.findViewById(R.id.button_submit_note);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (canPress) {
                    canPress = false;
                    submit.setTextColor(getResources().getColor(R.color.light_gray, null));
                    submit.setBackground(getResources().getDrawable(R.drawable.button_inactive, null));
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (viewModelNote.newNote) {
                                    viewModelNote.insert(viewModelNote.pojoNote, getContext());
                            } else {
                                viewModelNote.update();
                            }
                            jumpToToday(mainActivity);
                            fragmentManager.popBackStack();
                        }
                    }, 250);
                }
            }

        });

        initObservers();
        registerObservers();
        return root;
    }

    private void initObservers() {
        titleObserver = new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                title.setText(charSequence);
                viewModelNote.pojoNote.setImage(charSequence.toString());
                viewModelNote.title = charSequence.toString();
            }
        };

        noteObserver = new Observer<PojoNote>() {
            @Override
            public void onChanged(PojoNote pojoNote) {
                viewModelNote.pojoNote = pojoNote;
                body.setText(pojoNote.getContent());
                title.setText(pojoNote.getImage());
            }
        };
    }

    private void registerObservers() {
        viewModelNote.titleText.observe(this, titleObserver);
        if (viewModelNote.thisNote != null) {
            viewModelNote.thisNote.observe(this, noteObserver);
        }
    }

    private void jumpToToday(MainActivity mainActivity) {
        ViewModelActivity vma = mainActivity.viewModelActivity;
        vma.setPosition(vma.datesLiveData.getValue().size() - 1);
        vma.setCurrentDay(DayHelper.getInstance().getDateAsString());
    }



}
