package com.example.clutterrevision;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class DiffCallbackChecklist extends DiffUtil.Callback {
    private final List<PojoCheckList> oldList;
    private final List<PojoCheckList> newList;
    public DiffCallbackChecklist(List<PojoCheckList> oldList, List<PojoCheckList> newList){
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
        return oldList.get(oldItemPosition).getCheckListItem_id() == newList.get(
                newItemPosition).getCheckListItem_id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final PojoCheckList oldChecklist = oldList.get(oldItemPosition);
        final PojoCheckList newChecklist = newList.get(newItemPosition);

        return oldChecklist.getDescription() == newChecklist.getDescription();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
