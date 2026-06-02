package edu.upc.dsa.dsa_android;

public class FaqAssistantUiState {
    private final boolean loading;
    private final String answer;
    private final String error;

    public FaqAssistantUiState(boolean loading, String answer, String error) {
        this.loading = loading;
        this.answer = answer;
        this.error = error;
    }

    public static FaqAssistantUiState idle() {
        return new FaqAssistantUiState(false, "", "");
    }

    public static FaqAssistantUiState loading() {
        return new FaqAssistantUiState(true, "", "");
    }

    public static FaqAssistantUiState success(String answer) {
        return new FaqAssistantUiState(false, answer, "");
    }

    public static FaqAssistantUiState error(String error) {
        return new FaqAssistantUiState(false, "", error);
    }

    public boolean isLoading() {
        return loading;
    }

    public String getAnswer() {
        return answer;
    }

    public String getError() {
        return error;
    }
}
