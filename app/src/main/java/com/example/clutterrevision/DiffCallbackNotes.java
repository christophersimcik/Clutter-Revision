package com.example.clutterrevision;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class DiffCallbackNotes extends DiffUtil.Callback {
    private final List<PojoNote> oldList;
    private final List<PojoNote> newList;
    public DiffCallbackNotes(List<PojoNote> oldList, List<PojoNote> newList){
        this.oldList = oldList;
        this.newList = newList;
    }
    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getNote_id() == newList.get(
                newItemPosition).getNote_id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final PojoNote oldChecklist = oldList.get(oldItemPosition);
        final PojoNote newChecklist = newList.get(newItemPosition);

        return oldChecklist.getContent() == newChecklist.getContent();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
