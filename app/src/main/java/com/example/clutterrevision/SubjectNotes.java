package com.example.clutterrevision;

import java.util.List;

public interface SubjectNotes {
    void register(ObserverNotes observerNotes);
    void unregister(ObserverNotes observerNotes);
    void notifyNotesRetrieved(List<PojoNote> listOfPojoNotes);
    void notifyNoteInserted();
    void notifyNoteUpdated();
}
