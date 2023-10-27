package com.sujeevuthayakumar.noteme;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedNoteViewModel extends ViewModel {
    private final MutableLiveData<NoteModel> data = new MutableLiveData<>();

    public void setData(NoteModel value) {
        data.setValue(value);
    }

    public LiveData<NoteModel> getData() {
        return data;
    }
}
