package com.example.clutterrevision;

public interface SubjectNoteAdded {
    void register(ObserverNoteAdded observerNoteAdded);
    void unregister(ObserverNoteAdded observerNoteAdded);
    void notifyNoteAdded();
}
