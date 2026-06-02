package edu.upc.dsa.dsa_android.Faq;

import java.io.IOException;
import java.net.SocketTimeoutException;

import edu.upc.dsa.dsa_android.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqAssistantRepository {

    public interface FaqCallback {
        void onSuccess(FaqAssistantResponse response);
        void onError(String message);
    }

    private final ApiService apiService;

    public FaqAssistantRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void askFaq(String question, FaqCallback callback) {
        Call<FaqAssistantResponse> call = apiService.askFaq(new FaqAssistantRequest(question));
        call.enqueue(new Callback<FaqAssistantResponse>() {
            @Override
            public void onResponse(Call<FaqAssistantResponse> call, Response<FaqAssistantResponse> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 502) {
                        callback.onError("El asistente no está disponible temporalmente. Inténtalo de nuevo.");
                    } else if (response.code() >= 500) {
                        callback.onError("El servidor ha fallado. Vuelve a intentarlo más tarde.");
                    } else if (response.code() >= 400) {
                        callback.onError("La pregunta no pudo procesarse. Revisa el texto e inténtalo de nuevo.");
                    } else {
                        callback.onError("Error inesperado del servidor.");
                    }
                    return;
                }

                FaqAssistantResponse body = response.body();
                if (body == null) {
                    callback.onError("No se recibió respuesta del asistente.");
                    return;
                }

                if (body.getAnswer() == null || body.getAnswer().trim().isEmpty()) {
                    callback.onError("El asistente no devolvió una respuesta válida.");
                    return;
                }

                callback.onSuccess(body);
            }

            @Override
            public void onFailure(Call<FaqAssistantResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    callback.onError("La solicitud tardó demasiado. Comprueba tu conexión.");
                } else if (t instanceof IOException) {
                    callback.onError("No se pudo conectar con el servidor.");
                } else {
                    callback.onError("Ha ocurrido un error inesperado.");
                }
            }
        });
    }
}
