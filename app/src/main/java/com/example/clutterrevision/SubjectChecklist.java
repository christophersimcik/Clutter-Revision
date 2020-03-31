package com.example.clutterrevision;

public interface SubjectChecklist {
    void register(ObserverChecklist observerChecklist);
    void unregister(ObserverChecklist observerChecklist);
    void notifyChecklistObservers();
}
