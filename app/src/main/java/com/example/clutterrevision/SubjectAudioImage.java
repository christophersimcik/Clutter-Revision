package com.example.clutterrevision;

public interface SubjectAudioImage {
    void register(ObserverAudioImage observerAudioImage);
    void unregister(ObserverAudioImage observerAudioImage);
    void notifyObservers();
}
