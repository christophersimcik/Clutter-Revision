package com.example.clutterrevision;

public interface SubjectPath {
    void register(ObserverPathComplete observerPathComplete);
    void unregister(ObserverPathComplete observerPathComplete);
    void notifyPathCompleted();
}
