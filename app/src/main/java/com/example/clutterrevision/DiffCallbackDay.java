package com.example.clutterrevision;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class DiffCallbackDay extends DiffUtil.Callback {

    private final List<PojoDay> oldList;
    private final List<PojoDay> newList;

    public DiffCallbackDay(List<PojoDay> oldList, List<PojoDay> newList) {
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
        return oldList.get(oldItemPosition).getDay_id() == newList.get(
                newItemPosition).getDay_id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final PojoDay oldChecklist = oldList.get(oldItemPosition);
        final PojoDay newChecklist = newList.get(newItemPosition);

        return oldChecklist.getDay_id() == newChecklist.getDay_id();

    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        return super.getChangePayload(oldItemPosition, newItemPosition);

    }
}
