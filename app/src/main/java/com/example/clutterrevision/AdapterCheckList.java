package com.example.clutterrevision;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.emoji.widget.EmojiEditText;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterCheckList extends RecyclerView.Adapter<AdapterCheckList.ViewHolder> implements SwipeToDelete {
    private Context context;
    private List<PojoCheckList> data = new ArrayList();
    private ViewModelList viewModelList;

    public AdapterCheckList(Context context, ViewModelList viewModelList) {
        this.context = context;
        this.viewModelList = viewModelList;
    }

    @NonNull
    @Override
    public AdapterCheckList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_checklist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCheckList.ViewHolder holder, int position) {
        String description = data.get(position).getDescription();
        holder.emojiEditText.setText(description);
        holder.checkBox.setChecked(data.get(position).getChecked());
        holder.pojoChecklist = data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewSwiped(int position) {
        Toast.makeText(context,"CheckList Item Deleted",Toast.LENGTH_SHORT).show();
        viewModelList.delete(data.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private int position;
        EmojiEditText emojiEditText;
        CheckBox checkBox;
        Boolean touched = false;
        PojoCheckList pojoChecklist;

        private ViewHolder(@NonNull final View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.item_checkbox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    check(data.get(getAdapterPosition()));
                }
            });

            emojiEditText = itemView.findViewById(R.id.checkbox_description);
            emojiEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    touched = true;
                    return false;
                }
            });
           emojiEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b && touched) {
                        if (emojiEditText.getText().toString().equals("nuke")) {
                            PojoCheckList[] input = new PojoCheckList[data.size()];
                            data.toArray(input);
                            viewModelList.deleteAll(input);
                        }
                        updateText(pojoChecklist, emojiEditText.getText());
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        touched = false;
                    } else {
                        position = getAdapterPosition();
                    }
                }
            });
        }
    }

    private void check(PojoCheckList pojoCheckList) {
        if (pojoCheckList.getChecked()) {
            pojoCheckList.setChecked(false);
            viewModelList.update(pojoCheckList);

        } else {
            pojoCheckList.setChecked(true);
            viewModelList.update(pojoCheckList);
        }
    }

    private void updateText(PojoCheckList pojoCheckList, Editable editable) {
        pojoCheckList.setDescription(editable.toString());
        viewModelList.update(pojoCheckList);
    }



     void setData(List<PojoCheckList> checklist) {
        final DiffCallbackChecklist diffCallback = new DiffCallbackChecklist(this.data, checklist);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.data.clear();
        this.data.addAll(checklist);
        diffResult.dispatchUpdatesTo(this);

    }
}
