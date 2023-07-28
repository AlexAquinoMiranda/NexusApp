package com.example.nexusapp.proyectofinal.ui.perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PerfilViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PerfilViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Esto deberia ser los perfiles");
    }

    public LiveData<String> getText() {
        return mText;
    }
}