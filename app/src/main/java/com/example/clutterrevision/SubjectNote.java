package com.example.clutterrevision;

public interface SubjectNote {
    void register(ObserverNote observerNote);
    void unregister(ObserverNote observerNote);
    void notifyObservers(PojoNote pojoNote);
}
