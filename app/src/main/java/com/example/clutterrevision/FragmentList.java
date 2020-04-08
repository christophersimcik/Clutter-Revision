package com.example.clutterrevision;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.emoji.widget.EmojiEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentList extends Fragment{

    FragmentManager fragmentManager;
    RecyclerView checklistView;
    private Button addItem;
    private AdapterCheckList recyclerViewChecklist;
    private EmojiEditText emojiEditText;
    private ViewModelList viewModelList;
    private Handler handler = new Handler();
    private Observer checklistObserver;
    private Observer noteObserver;
    private ItemTouchCallback itemTouchCallback;
    public Boolean newList = false;

    String name;

    private Boolean active = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().getSupportFragmentManager() != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        Bundle bundle = getArguments();
        viewModelList = ViewModelProviders.of(this).get(ViewModelList.class);
        recyclerViewChecklist = new AdapterCheckList(getContext(), viewModelList);
        recyclerViewChecklist.hasStableIds();
        itemTouchCallback = new ItemTouchCallback(recyclerViewChecklist);
        if(bundle != null && bundle.containsKey("new_list")){
            this.newList = bundle.getBoolean("new_list");
        }
        if (bundle != null && bundle.containsKey("id")) {
            viewModelList.checkListName = bundle.getString("id");
            viewModelList.getList();
            initObserver();
            registerObservers();
        }
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checklist, container, false);
        View root = binding.getRoot();
        emojiEditText = root.findViewById(R.id.checklist_title);
        emojiEditText.setText(viewModelList.checkListName);
        emojiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!viewModelList.getRunning()){
                    viewModelList.startListening();
                }
                viewModelList.changeNoteTitle(editable);
            }
        });
        checklistView = root.findViewById(R.id.checklist_recycler_view);
        checklistView.setAdapter(recyclerViewChecklist);
        checklistView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(checklistView);
        addItem = root.findViewById(R.id.button_add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!active) {
                    active = true;
                    addItem.setBackground(getResources().getDrawable(R.drawable.add_checklist_item_clicked, null));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addItem.setBackground(getResources().getDrawable(R.drawable.add_checklist_item, null));
                            viewModelList.addCheckBox();
                            checklistView.getLayoutManager().smoothScrollToPosition(checklistView, null, 0);
                            active = false;
                        }
                    }, 150);
                }
            }
        });
        return root;
    }

    private void initObserver() {
        checklistObserver = new Observer<List<PojoCheckList>>() {

            @Override
            public void onChanged(List<PojoCheckList> pojoCheckLists) {
                recyclerViewChecklist.setData(pojoCheckLists);
            }
        };

        noteObserver = new Observer<PojoNote>() {

            @Override
            public void onChanged(PojoNote pojoNote) {
                String title = pojoNote.getImage();
                viewModelList.noteTitle = title;
                viewModelList.setTitle(emojiEditText);
                viewModelList.setThisNote(pojoNote);
            }
        };
    }

    private void registerObservers() {
        viewModelList.checklist.observe(this, checklistObserver);
        viewModelList.noteContainingCheckList.observe(this,noteObserver);
    }

}
