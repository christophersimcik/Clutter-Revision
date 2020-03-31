package com.example.clutterrevision;

import java.util.List;

public interface ObserverNotes {
    void onDayRetrieved(List<PojoNote> pojoNotes);
}
