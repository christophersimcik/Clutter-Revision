package com.example.clutterrevision;

import java.util.List;
import androidx.lifecycle.LiveData;

public interface SubjectDates {
    void register(ObserverDates observerDates);
    void unregister(ObserverDates observerDates);
    void notifyEmptiesDeleted();
    void notifyDateChecked(PojoDay pojoDay);
    void notifyDateInserted();
    void notifyDatesRetrieved(List<PojoDay> listPojoDays);
}
