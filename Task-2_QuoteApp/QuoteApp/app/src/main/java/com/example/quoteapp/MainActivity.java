package com.example.quoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tvQuote, tvAuthor, tvCategory, tvDate;
    Button btnNew, btnFavourite, btnShare;
    LinearLayout llFavourites;

    int currentIndex = 0;
    List<Quote> favourites = new ArrayList<>();
    List<Quote> quotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuote = findViewById(R.id.tvQuote);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvCategory = findViewById(R.id.tvCategory);
        tvDate = findViewById(R.id.tvDate);
        btnNew = findViewById(R.id.btnNew);
        btnFavourite = findViewById(R.id.btnFavourite);
        btnShare = findViewById(R.id.btnShare);
        llFavourites = findViewById(R.id.llFavourites);

        loadQuotes();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(new Date()));

        currentIndex = (int) (System.currentTimeMillis() / 86400000L) % quotes.size();
        showQuote(currentIndex);

        btnNew.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % quotes.size();
            showQuote(currentIndex);
        });

        btnFavourite.setOnClickListener(v -> toggleFavourite());
        btnShare.setOnClickListener(v -> shareQuote());
    }

    void loadQuotes() {
        quotes.add(new Quote("The only way to do great work is to love what you do.", "Steve Jobs", "Work"));
        quotes.add(new Quote("In the middle of difficulty lies opportunity.", "Albert Einstein", "Resilience"));
        quotes.add(new Quote("It does not matter how slowly you go as long as you do not stop.", "Confucius", "Perseverance"));
        quotes.add(new Quote("Believe you can and you're halfway there.", "Theodore Roosevelt", "Motivation"));
        quotes.add(new Quote("The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt", "Dreams"));
        quotes.add(new Quote("Success is not final, failure is not fatal: it is the courage to continue that counts.", "Winston Churchill", "Courage"));
        quotes.add(new Quote("You must be the change you wish to see in the world.", "Mahatma Gandhi", "Change"));
        quotes.add(new Quote("Happiness is not something ready-made. It comes from your own actions.", "Dalai Lama", "Happiness"));
        quotes.add(new Quote("The best time to plant a tree was 20 years ago. The second best time is now.", "Chinese Proverb", "Action"));
        quotes.add(new Quote("Everything you have ever wanted is on the other side of fear.", "George Addair", "Courage"));
        quotes.add(new Quote("Start where you are. Use what you have. Do what you can.", "Arthur Ashe", "Action"));
        quotes.add(new Quote("Dream big and dare to fail.", "Norman Vaughan", "Dreams"));
        quotes.add(new Quote("Keep your face always toward the sunshine and shadows will fall behind you.", "Walt Whitman", "Positivity"));
        quotes.add(new Quote("Life is what happens when you are busy making other plans.", "John Lennon", "Life"));
        quotes.add(new Quote("What you get by achieving your goals is not as important as what you become.", "Henry David Thoreau", "Growth"));
    }

    void showQuote(int index) {
        Quote q = quotes.get(index);
        tvQuote.setText(getString(R.string.quote_format, q.getText()));
        tvAuthor.setText(getString(R.string.author_format, q.getAuthor()));
        tvCategory.setText(q.getCategory());
        updateFavButton();
    }

    boolean isFavourite() {
        String current = quotes.get(currentIndex).getText();
        for (Quote f : favourites) {
            if (f.getText().equals(current)) return true;
        }
        return false;
    }

    void toggleFavourite() {
        if (isFavourite()) {
            String current = quotes.get(currentIndex).getText();
            favourites.removeIf(f -> f.getText().equals(current));
            Toast.makeText(this, R.string.removed_toast, Toast.LENGTH_SHORT).show();
        } else {
            favourites.add(quotes.get(currentIndex));
            Toast.makeText(this, R.string.saved_toast, Toast.LENGTH_SHORT).show();
        }
        updateFavButton();
        renderFavourites();
    }

    void updateFavButton() {
        btnFavourite.setText(isFavourite() ? R.string.btn_saved : R.string.btn_save);
    }

    void shareQuote() {
        Quote q = quotes.get(currentIndex);
        String shareText = getString(R.string.share_format, q.getText(), q.getAuthor());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, getString(R.string.share_chooser)));
    }

    void renderFavourites() {
        llFavourites.removeAllViews();

        if (favourites.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText(R.string.no_favourites);
            empty.setTextSize(13f);
            empty.setTextColor(0xFF888888);
            empty.setPadding(8, 16, 8, 16);
            llFavourites.addView(empty);
            return;
        }

        for (int i = 0; i < favourites.size(); i++) {
            llFavourites.addView(buildFavouriteRow(i));
        }
    }

    private LinearLayout buildFavouriteRow(int index) {
        Quote q = favourites.get(index);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setBackgroundColor(0xFFF0F0F0);
        row.setPadding(16, 12, 16, 12);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.setMargins(0, 0, 0, 8);
        row.setLayoutParams(rowParams);

        TextView tv = new TextView(this);
        tv.setText(getString(R.string.fav_item_format, q.getText(), q.getAuthor()));
        tv.setTextSize(13f);
        tv.setTextColor(0xFF333333);
        tv.setLineSpacing(4f, 1f);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        tv.setLayoutParams(tvParams);

        Button btnRemove = new Button(this);
        btnRemove.setText("✕");
        btnRemove.setTextSize(14f);
        btnRemove.setTextColor(0xFFE24B4A);
        btnRemove.setBackgroundColor(0x00000000);
        btnRemove.setPadding(8, 0, 8, 0);
        btnRemove.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        btnRemove.setOnClickListener(v -> {
            favourites.remove(index);
            renderFavourites();
            updateFavButton();
            Toast.makeText(this, R.string.removed_toast, Toast.LENGTH_SHORT).show();
        });

        row.addView(tv);
        row.addView(btnRemove);
        return row;
    }
}