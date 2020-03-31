package com.example.clutterrevision;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterNote extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeToDelete {
    private DialogDelete dialogDelete;
    public PojoNote noteToDelete;
    public int positionOfDeletion;
    private Context context;
    private List<PojoNote> data = new ArrayList();
    ViewModelDay viewModelDay;
    FragmentManager fragmentManager;
    List<MediaPlayer> mediaPlayers = new ArrayList();
    RecyclerView recyclerView;

    public AdapterNote(Context context, ViewModelDay viewModelDay, FragmentManager fragmentManager) {
        this.context = context;
        this.viewModelDay = viewModelDay;
        this.fragmentManager = fragmentManager;
        dialogDelete = new DialogDelete();
        dialogDelete.setTargetFragment(fragmentManager.findFragmentById(R.id.fragment_main), 0);
        dialogDelete.getDialog();

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Constants.TYPE_AUDIO:
                view = LayoutInflater.from(context).inflate(R.layout.item_audio, parent, false);
                return new ViewHolderAudio(view);
            case Constants.TYPE_CHECKLIST:
                view = LayoutInflater.from(context).inflate(R.layout.item_checklist, parent, false);
                return new ViewHolderChecklist(view);
            case Constants.TYPE_NOTE:
                view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
                return new ViewHolderNote(view);
            case Constants.TYPE_REFERENCE:
                view = LayoutInflater.from(context).inflate(R.layout.item_reference, parent, false);
                return new ViewHolderReference(view);
            case Constants.TYPE_GOOGLE_BOOKS:
                view = LayoutInflater.from(context).inflate(R.layout.item_google_books, parent, false);
                return new ViewHolderBooks(view);
            default:
                view = LayoutInflater.from(context).inflate(R.layout.item_reference, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Constants.TYPE_AUDIO:
                ViewHolderAudio viewHolderAudio = ((ViewHolderAudio) holder);
                viewModelDay.getAudioImage(data.get(position).getImage(), viewHolderAudio.visualAudio);
                viewHolderAudio.setPojoNote(data.get(position));
                break;
            case Constants.TYPE_CHECKLIST:
                ViewHolderChecklist viewHolderChecklist = ((ViewHolderChecklist) holder);
                viewHolderChecklist.textView.setText(data.get(position).getImage());
                viewHolderChecklist.setPojoNote(data.get(position));
                break;
            case Constants.TYPE_NOTE:
                ViewHolderNote viewHolderNote = ((ViewHolderNote) holder);
                viewHolderNote.textView.setText(data.get(position).getImage());
                viewHolderNote.setPojoNote(data.get(position));
                break;
            case Constants.TYPE_REFERENCE:
                ViewHolderReference viewHolderReference = ((ViewHolderReference) holder);
                viewHolderReference.textView.setText(data.get(position).getImage());
                viewHolderReference.setPojoNote(data.get(position));
                break;
            case Constants.TYPE_GOOGLE_BOOKS:
                ViewHolderBooks viewHolderBooks = ((ViewHolderBooks) holder);
                viewHolderBooks.textView.setText(data.get(position).getContent());
                viewHolderBooks.setPojoNote(data.get(position));
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewSwiped(int position) {
        noteToDelete = data.get(position);
        positionOfDeletion = position;
        dialogDelete.setPosition(position);
        dialogDelete.show(fragmentManager, "delete");

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewHolder(@NonNull final View itemView) {
            super(itemView);
        }
    }

    void setData(List<PojoNote> notes) {
        final DiffCallbackNotes diffCallback = new DiffCallbackNotes(this.data, notes);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.data.clear();
        this.data.addAll(notes);
        diffResult.dispatchUpdatesTo(this);
    }

    class ViewHolderBooks extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        PojoNote pojoNote;

        public ViewHolderBooks(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentBooks fragmentBooks = new FragmentBooks();
                    Bundle bundle = new Bundle();
                    bundle.putString("query", textView.getText().toString());
                    fragmentBooks.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentBooks, "book")
                            .addToBackStack("book")
                            .commit();
                }
            });
        }

        public void setPojoNote(PojoNote pojoNote) {
            this.pojoNote = pojoNote;
        }
    }

    class ViewHolderAudio extends RecyclerView.ViewHolder {

        ImageView imageView;
        VisualAudio visualAudio;
        PojoNote pojoNote;
        Boolean active = false;

        public ViewHolderAudio(@NonNull View itemView) {
            super(itemView);
            visualAudio = itemView.findViewById(R.id.audio);
            imageView = itemView.findViewById(R.id.type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!active && mediaPlayers.size() < 2) {
                        active = true;
                        Uri uri = Uri.fromFile(new File(pojoNote.getContent()));
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayers.add(mediaPlayer);
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mediaPlayer.setDataSource(context, uri);
                            mediaPlayer.prepare();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        visualAudio.playback(mediaPlayer.getDuration());
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                active = false;
                                mediaPlayer.release();
                                mediaPlayers.remove(mediaPlayer);
                            }
                        });
                    }
                }
            });
        }

        public void setPojoNote(PojoNote pojoNote) {
            this.pojoNote = pojoNote;
        }


    }

    class ViewHolderNote extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        PojoNote pojoNote;

        public ViewHolderNote(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentNote fragmentNote = new FragmentNote();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", pojoNote.getNote_id());
                    bundle.putBoolean("new_note", false);
                    fragmentNote.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentNote, "note")
                            .addToBackStack("note")
                            .commit();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });
        }

        public void setPojoNote(PojoNote pojoNote) {
            this.pojoNote = pojoNote;
        }
    }

    class ViewHolderReference extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        PojoNote pojoNote;

        public ViewHolderReference(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, pojoNote.getContent()); // query contains search string
                    context.startActivity(intent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return true;
                }
            });
        }

        public void setPojoNote(PojoNote pojoNote) {
            this.pojoNote = pojoNote;
        }
    }

    class ViewHolderChecklist extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        PojoNote pojoNote;

        public ViewHolderChecklist(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentList fragmentList = new FragmentList();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", pojoNote.getNote_id());
                    bundle.putBoolean("new_list", false);
                    fragmentList.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentList, "list")
                            .addToBackStack("list")
                            .commit();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });
        }

        public void setPojoNote(PojoNote pojoNote) {
            this.pojoNote = pojoNote;
        }
    }

}
