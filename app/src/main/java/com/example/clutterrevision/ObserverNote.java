package com.example.clutterrevision;

import java.util.List;

public interface ObserverNote {
    void onNoteRetrieved(PojoNote pojoNote);
    void onNoteInserted();
    void onNoteUpdated();
}
