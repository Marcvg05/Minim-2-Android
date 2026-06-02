package edu.upc.dsa.dsa_android;

public class FaqAssistantRequest {
    private String question;

    public FaqAssistantRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
