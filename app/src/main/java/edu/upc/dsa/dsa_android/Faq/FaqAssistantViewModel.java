package edu.upc.dsa.dsa_android.Faq;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FaqAssistantViewModel extends ViewModel {

    private final FaqAssistantRepository repository;
    private final MutableLiveData<FaqAssistantUiState> uiState = new MutableLiveData<>(FaqAssistantUiState.idle());

    public FaqAssistantViewModel(FaqAssistantRepository repository) {
        this.repository = repository;
    }

    public LiveData<FaqAssistantUiState> getUiState() {
        return uiState;
    }

    public void askQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            uiState.setValue(FaqAssistantUiState.error("Escribe una pregunta antes de enviar."));
            return;
        }

        uiState.setValue(FaqAssistantUiState.loading());

        repository.askFaq(question.trim(), new FaqAssistantRepository.FaqCallback() {
            @Override
            public void onSuccess(FaqAssistantResponse response) {
                String answer = response.getAnswer() != null ? response.getAnswer().trim() : "";
                if (answer.isEmpty()) {
                    uiState.postValue(FaqAssistantUiState.error("El asistente no devolvió una respuesta válida."));
                    return;
                }
                uiState.postValue(FaqAssistantUiState.success(answer));
            }

            @Override
            public void onError(String message) {
                uiState.postValue(FaqAssistantUiState.error(message));
            }
        });
    }
}
