package edu.upc.dsa.dsa_android;
import android.view.View;

public class ProgressBarActivity {
    public static void show(android.widget.ProgressBar bar) {
        if (bar != null)
            bar.setVisibility(View.VISIBLE);
    }

    public static void hide(android.widget.ProgressBar bar) {
        if (bar != null)
            bar.setVisibility(View.GONE);
    }
}
