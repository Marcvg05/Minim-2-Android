package edu.upc.dsa.dsa_android;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FaqAssistantViewModelFactory implements ViewModelProvider.Factory {

    private final FaqAssistantRepository repository;

    public FaqAssistantViewModelFactory(FaqAssistantRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FaqAssistantViewModel.class)) {
            return (T) new FaqAssistantViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
