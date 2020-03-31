package com.example.clutterrevision;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = PojoNote.class,
        parentColumns = "note_id",
        childColumns = "checklist_day",
        onDelete = CASCADE))

public class PojoCheckList {
    @PrimaryKey(autoGenerate = true)
    private int checkListItem_id;
    @ColumnInfo(name = "checklist_day", index = true)
    private String checklist_day;
    @ColumnInfo(name = "is_item_checked")
    private Boolean checked;
    @ColumnInfo(name = "item_description")
    private String description;


    public PojoCheckList(int checkListItem_id, Boolean checked, String description, String checklist_day ) {
        this.checkListItem_id = checkListItem_id;
        this.checklist_day = checklist_day;
        this.checked = checked;
        this.description = description;


    }

    public void setCheckListItem_id(int checkListItem_id) {
        this.checkListItem_id = checkListItem_id;
    }

    public int getCheckListItem_id() {
        return checkListItem_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecklist_day(String checklist_day) {
        this.checklist_day = checklist_day;
    }

    public String getChecklist_day() {
        return checklist_day;
    }
}
