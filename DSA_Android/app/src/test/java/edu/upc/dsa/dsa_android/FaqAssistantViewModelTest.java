package edu.upc.dsa.dsa_android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;

public class FaqAssistantViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void askQuestion_withEmptyQuestion_setsValidationError() {
        FakeFaqAssistantRepository repository = new FakeFaqAssistantRepository();
        FaqAssistantViewModel viewModel = new FaqAssistantViewModel(repository);

        viewModel.askQuestion("   ");

        FaqAssistantUiState state = viewModel.getUiState().getValue();
        assertFalse(repository.wasCalled);
        assertTrue(state != null && !state.getError().isEmpty());
    }

    @Test
    public void askQuestion_withSuccess_updatesAnswer() {
        FakeFaqAssistantRepository repository = new FakeFaqAssistantRepository();
        repository.nextResponse = createResponse("Usa torres de ralentizacion y dano en area.");
        FaqAssistantViewModel viewModel = new FaqAssistantViewModel(repository);

        viewModel.askQuestion("Como paso una pantalla dificil?");

        FaqAssistantUiState state = viewModel.getUiState().getValue();
        assertTrue(repository.wasCalled);
        assertTrue(state != null);
        assertEquals("Usa torres de ralentizacion y dano en area.", state.getAnswer());
        assertEquals("", state.getError());
        assertFalse(state.isLoading());
    }

    @Test
    public void askQuestion_withRepositoryError_updatesError() {
        FakeFaqAssistantRepository repository = new FakeFaqAssistantRepository();
        repository.nextError = "No se pudo conectar con el servidor.";
        FaqAssistantViewModel viewModel = new FaqAssistantViewModel(repository);

        viewModel.askQuestion("Pregunta");

        FaqAssistantUiState state = viewModel.getUiState().getValue();
        assertTrue(repository.wasCalled);
        assertTrue(state != null);
        assertEquals("No se pudo conectar con el servidor.", state.getError());
        assertEquals("", state.getAnswer());
    }

    private FaqAssistantResponse createResponse(String answer) {
        FaqAssistantResponse response = new FaqAssistantResponse();
        response.setQuestion("Q");
        response.setAnswer(answer);
        return response;
    }

    private static class FakeFaqAssistantRepository extends FaqAssistantRepository {
        boolean wasCalled = false;
        FaqAssistantResponse nextResponse;
        String nextError;

        FakeFaqAssistantRepository() {
            super(null);
        }

        @Override
        public void askFaq(String question, FaqCallback callback) {
            wasCalled = true;
            if (nextError != null) {
                callback.onError(nextError);
                return;
            }
            callback.onSuccess(nextResponse != null ? nextResponse : new FaqAssistantResponse());
        }
    }
}
