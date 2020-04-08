package com.example.clutterrevision;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class FragmentReference extends Fragment {

    EditText body;
    TextView title;
    FragmentManager fragmentManager;
    ViewModelReference viewModelReference;
    Observer<CharSequence> titleObserver;
    Observer<PojoNote> referenceObserver;
    CustomSubmit submit;
    ImageView api;
    MainActivity mainActivity;
    Boolean canPress = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
        viewModelReference = ViewModelProviders.of(this).get(ViewModelReference.class);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("id")) {
            viewModelReference.id = bundle.getString("id");
            viewModelReference.getNote();
        }
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reference, container, false);
        View root = binding.getRoot();
        body = root.findViewById(R.id.body_reference);
        title = root.findViewById(R.id.title_reference);
        submit = root.findViewById(R.id.button_submit_reference);
        mainActivity = (MainActivity)getActivity();
        viewModelReference.setPojoDay(mainActivity.viewModelActivity.pojoDay);
        body.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModelReference.setReferenceTitle(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        titleObserver = new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                title.setText(charSequence);
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canPress) {
                    canPress = true;
                    submit.setBackground(getResources().getDrawable(R.drawable.button_inactive, null));
                    submit.setTextColor(getResources().getColor(R.color.light_gray, null));
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (viewModelReference.pojoNote == null) {
                                viewModelReference.insert(viewModelReference.createNote(), getContext());
                            } else {
                                viewModelReference.update();
                            }
                            jumpToToday(mainActivity);
                            fragmentManager.popBackStack();
                        }
                    }, 125);
                }
            }

        });

        api = root.findViewById(R.id.button_submit_api_test);

        final AnimationDrawable animation = assignAnimation(viewModelReference.type);
        animation.setOneShot(true);
        api.setImageDrawable(animation);
        animation.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.stop();
                animation.selectDrawable(0);
            }
        }, 300);

        api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModelReference.pojoNote == null){
                    if (viewModelReference.type == Constants.TYPE_REFERENCE) {
                        viewModelReference.type = Constants.TYPE_GOOGLE_BOOKS;
                        api.setBackground(getResources().getDrawable(R.drawable.google_books,null));

                    } else {
                        viewModelReference.type = Constants.TYPE_REFERENCE;
                        api.setBackground(getResources().getDrawable(R.drawable.referenece,null));
                    }
                }else{
                    if (viewModelReference.pojoNote.getType() == Constants.TYPE_REFERENCE) {
                        viewModelReference.pojoNote.setType(Constants.TYPE_GOOGLE_BOOKS);
                        api.setBackground(getResources().getDrawable(R.drawable.google_books,null));
                    } else {
                        viewModelReference.pojoNote.setType(Constants.TYPE_REFERENCE);
                        api.setBackground(getResources().getDrawable(R.drawable.referenece,null));
                    }
                }

                final AnimationDrawable animation = assignAnimation(viewModelReference.type);
                animation.setOneShot(true);
                api.setImageDrawable(animation);
                animation.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animation.stop();
                        animation.selectDrawable(0);
                        api.setBackground(getResources().getDrawable(R.drawable.basic,null));
                    }
                }, 300);

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
                if(viewModelReference.pojoNote != null) {
                    viewModelReference.pojoNote.setImage(charSequence.toString());
                }
            }
        };

        referenceObserver = new Observer<PojoNote>() {
            @Override
            public void onChanged(PojoNote pojoNote) {
                viewModelReference.pojoNote = pojoNote;
                viewModelReference.title = pojoNote.getImage();
                viewModelReference.content = pojoNote.getContent();
                viewModelReference.type = pojoNote.getType();
                title.setText(pojoNote.getImage());
                body.setText(pojoNote.getContent());
            }
        };
    }

    private void registerObservers() {
        viewModelReference.titleText.observe(this, titleObserver);
        if(viewModelReference.thisNote!=null) {
            viewModelReference.thisNote.observe(this, referenceObserver);
        }
    }

    private AnimationDrawable assignAnimation (int type){
        AnimationDrawable animationDrawable;
        if(type == Constants.TYPE_REFERENCE){
            animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.animation_google_search,null);
        }else{
            animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.animation_google_books,null);
        }
        return animationDrawable;
    }
    private void jumpToToday(MainActivity mainActivity){
        ViewModelActivity vma = mainActivity.viewModelActivity;
        vma.setPosition(vma.datesLiveData.getValue().size()-1);
        vma.setCurrentDay(DayHelper.getInstance().getDateAsString());
    }

}
