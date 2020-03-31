package com.example.clutterrevision;

public interface Subject {
    void register(DateObserver dateObserver);
    void unregister(DateObserver dateObserver);
    void notifyObservers();
}
