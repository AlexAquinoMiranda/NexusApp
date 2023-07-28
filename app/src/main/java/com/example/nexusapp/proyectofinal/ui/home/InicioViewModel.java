package com.example.nexusapp.proyectofinal.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InicioViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public InicioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Aqui ira la pagina principal o Home");
    }

    public LiveData<String> getText() {
        return mText;
    }
}