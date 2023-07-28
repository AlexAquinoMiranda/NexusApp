package com.example.nexusapp.proyectofinal.ui.paneles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PanelesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PanelesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Esta es la pagina de los paneles");
    }

    public LiveData<String> getText() {
        return mText;
    }
}