package edu.upc.dsa.dsa_android.Faq;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.upc.dsa.dsa_android.R;
import edu.upc.dsa.dsa_android.network.RetrofitClient;

public class FaqAssistantActivity extends AppCompatActivity {

    private EditText etQuestion;
    private ProgressBar progressBar;
    private TextView tvAnswer;
    private TextView tvError;
    private FaqAssistantViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_assistant);

        etQuestion = findViewById(R.id.etFaqQuestion);
        Button btnAsk = findViewById(R.id.btnAskFaq);
        Button btnBack = findViewById(R.id.btnFaqBack);
        progressBar = findViewById(R.id.progressBarFaq);
        tvAnswer = findViewById(R.id.tvFaqAnswer);
        tvError = findViewById(R.id.tvFaqError);

        FaqAssistantRepository repository = new FaqAssistantRepository(RetrofitClient.getInstance().getApi());
        FaqAssistantViewModelFactory factory = new FaqAssistantViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(FaqAssistantViewModel.class);

        observeUiState();

        btnAsk.setOnClickListener(v -> viewModel.askQuestion(etQuestion.getText().toString()));
        btnBack.setOnClickListener(v -> finish());
    }

    private void observeUiState() {
        viewModel.getUiState().observe(this, state -> {
            if (state == null) {
                return;
            }

            progressBar.setVisibility(state.isLoading() ? View.VISIBLE : View.GONE);

            if (state.getAnswer() != null && !state.getAnswer().isEmpty()) {
                tvAnswer.setVisibility(View.VISIBLE);
                tvAnswer.setText(state.getAnswer());
            } else {
                tvAnswer.setVisibility(View.GONE);
                tvAnswer.setText("");
            }

            if (state.getError() != null && !state.getError().isEmpty()) {
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(state.getError());
            } else {
                tvError.setVisibility(View.GONE);
                tvError.setText("");
            }
        });
    }
}
