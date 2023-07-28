package com.example.nexusapp.proyectofinal.ui.busqueda;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusquedaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BusquedaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
