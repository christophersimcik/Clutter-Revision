package com.example.clutterrevision;

import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FragmentNotes extends Fragment implements DialogCheckList.ChecklistDialogListener, DialogDelete.DeleteDialogListener {

    FragmentButton note;
    FragmentButton term;
    FragmentButton audio;
    FragmentButton list;

    ViewModelDay viewModelDay;
    AdapterNote adapterNote;
    Observer observerNotes;
    RecyclerView recyclerView;
    FragmentManager fragmentManager;
    MainActivity mainActivity;
    ItemTouchCallback itemTouchCallback;
    List<CancelPlayback> cancelPlaybackList = new ArrayList<>();
    private Boolean canPress;
    private DialogCheckList dialogCheckList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        fragmentManager = getActivity().getSupportFragmentManager();
        viewModelDay = ViewModelProviders.of(requireActivity()).get(ViewModelDay.class);
        viewModelDay.setViewModelActivity(mainActivity.viewModelActivity);
        adapterNote = new AdapterNote(this.getContext(), viewModelDay, fragmentManager);
        itemTouchCallback = new ItemTouchCallback(adapterNote);
        dialogCheckList = new DialogCheckList();
        dialogCheckList.setTargetFragment(this, 0);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        canPress = true;
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.notes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapterNote);
        ItemTouchHelper touchHelper = new ItemTouchHelper(itemTouchCallback);
        touchHelper.attachToRecyclerView(recyclerView);
        note = root.findViewById(R.id.note_button);
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canPress) {
                    canPress = false;
                    note.setBackground(getResources().getDrawable(R.drawable.note, null));
                    final AnimationDrawable animateTerm = (AnimationDrawable) note.getDrawable();
                    animateTerm.setOneShot(true);
                    animateTerm.start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animateTerm.stop();
                            animateTerm.selectDrawable(0);
                            note.setBackground(getResources().getDrawable(R.drawable.basic, null));
                            FragmentNote fragmentNote = new FragmentNote();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("new_note", true);
                            fragmentNote.setArguments(bundle);
                            String tag = getResources().getString(R.string.note);
                            fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentNote, tag)
                                    .addToBackStack(tag)
                                    .commit();
                            canPress = true;
                        }
                    }, 300);
                }
            }
        });
        term = root.findViewById(R.id.term_button);
        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canPress) {
                    canPress = false;
                    term.setBackground(getResources().getDrawable(R.drawable.referenece, null));
                    final AnimationDrawable animateTerm = (AnimationDrawable) term.getDrawable();
                    animateTerm.setOneShot(true);
                    animateTerm.start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animateTerm.stop();
                            animateTerm.selectDrawable(0);
                            term.setBackground(getResources().getDrawable(R.drawable.basic, null));
                            FragmentReference fragmentReference = new FragmentReference();
                            fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentReference, "term")
                                    .addToBackStack("term")
                                    .commit();
                            canPress = true;
                        }
                    }, 300);
                }

            }
        });
        audio = root.findViewById(R.id.audio_button);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canPress) {
                    audio.setBackground(getResources().getDrawable(R.drawable.audio, null));
                    final AnimationDrawable animateTerm = (AnimationDrawable) audio.getDrawable();
                    animateTerm.setOneShot(true);
                    animateTerm.start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animateTerm.stop();
                            animateTerm.selectDrawable(0);
                            audio.setBackground(getResources().getDrawable(R.drawable.basic, null));
                            FragmentAudio fragmentAudio = new FragmentAudio();
                            fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentAudio, "audio")
                                    .addToBackStack("audio")
                                    .commit();
                            canPress = true;
                        }
                    }, 300);
                }
            }
        });
        list = root.findViewById(R.id.list_button);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canPress) {
                    canPress = false;
                    list.setBackground(getResources().getDrawable(R.drawable.checklist, null));
                    final AnimationDrawable animateTerm = (AnimationDrawable) list.getDrawable();
                    animateTerm.setOneShot(true);
                    animateTerm.start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animateTerm.stop();
                            animateTerm.selectDrawable(0);
                            list.setBackground(getResources().getDrawable(R.drawable.basic, null));
                            dialogCheckList.show(getActivity().getSupportFragmentManager(), "dialog");
                        }
                    }, 300);

                }
            }

        });

        initObserver();
        registerObservers();
        viewModelDay.currentDay = mainActivity.viewModelActivity.getCurrentDay();
        viewModelDay.refreshNotes();
        return root;
    }

    private void initObserver() {
        observerNotes = new Observer<List<PojoNote>>() {
            @Override
            public void onChanged(List<PojoNote> pojoNotes) {
                if (pojoNotes != null) {
                    adapterNote.setData(pojoNotes);
                    notifyCancelPlayback();
                    System.out.println(" show notes size = " + pojoNotes.size());
                }
            }
        };
    }

    private void registerObservers() {
        viewModelDay.listOfNotes.observe(this, observerNotes);
    }

    @Override
    public void onSubmit(Dialog dialog) {
        String id = String.valueOf(System.currentTimeMillis());
        String content = dialogCheckList.emojiEditText.getText().toString();
        if (content == null || content.equals("")) {
            content = id;
        }
        String image = content;
        PojoNote pojoNote = new PojoNote(id, content, image, Constants.TYPE_CHECKLIST, viewModelDay.getDayId());
        viewModelDay.insertNote(pojoNote);
        FragmentList fragmentList = new FragmentList();
        Bundle bundle = new Bundle();
        bundle.putString("id", pojoNote.getNote_id());
        bundle.putBoolean("new_list", true);
        fragmentList.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentList, "list")
                .addToBackStack("list")
                .commit();
        dialog.dismiss();
    }

    @Override
    public void onCancelled() {
        canPress = true;
    }

    @Override
    public void onDismiss(Dialog dialog, int position) {
        adapterNote.notifyItemChanged(position);
        this.canPress = true;
        dialog.dismiss();
    }

    @Override
    public void onCanceled(Dialog dialog, int position) {
        adapterNote.notifyItemChanged(position);
        this.canPress = true;
        dialog.dismiss();
    }

    @Override
    public void onConfirm(Dialog dialog, int position) {
        PojoNote noteToDelete = adapterNote.noteToDelete;
        if (noteToDelete != null) {
            viewModelDay.repositoryNote.delete(noteToDelete);
            adapterNote.noteToDelete = null;
        }
        adapterNote.notifyItemChanged(position);
        dialog.dismiss();
    }

    public void registerCancelPlaybacks(CancelPlayback cancelPlayback) {
        cancelPlaybackList.add(cancelPlayback);
    }

    public void notifyCancelPlayback() {
        for (CancelPlayback cancellation : cancelPlaybackList) {
            cancellation.cancelPlayback();
        }
    }
}
