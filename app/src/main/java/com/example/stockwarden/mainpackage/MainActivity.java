package com.example.stockwarden.mainpackage;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.example.stockwarden.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private List<Stock> stocks = new ArrayList<>();
    private List<Stock> filteredStocks = new ArrayList<>();
    private RecyclerView recyclerView;
    private StockAdapter viewAdapter;
    private RecyclerView.LayoutManager viewManager;
    private SearchView searchView;
    private Handler handler = new Handler();
    private AlphaVantageAPI alphaVantageAPI;
    private static final long REFRESH_INTERVAL_MS = 60000; // Обновление каждую минуту

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация RecyclerView
        viewManager = new LinearLayoutManager(this);
        viewAdapter = new StockAdapter(filteredStocks);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(viewManager);
        recyclerView.setAdapter(viewAdapter);

        // Инициализация SearchView
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterStocks(newText);
                return true;
            }
        });

        // Кнопка для ручного обновления данных
        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> fetchStockData());

        // Создание экземпляра Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AlphaVantageAPI.BASE_URL)
                .client(new OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        alphaVantageAPI = retrofit.create(AlphaVantageAPI.class);

        // Первоначальная загрузка данных
        fetchStockData();
    }

    private void fetchStockData() {
        List<String> stockSymbols = List.of(
                "AAPL", "MSFT", "GOOGL", "AMZN", "FB", // наименование акций компаний
                "BABA", "BRK.A", "BRK.B", "JPM", "XOM",
                "V", "BAC", "WFC", "JNJ", "PG",
                "INTC", "CVX", "PFE", "HD", "UNH",
                "MA", "MCD", "DIS", "T", "VZ",
                "CSCO", "PEP", "KO", "MRK", "NKE",
                "IBM", "C", "CMCSA", "WMT", "HD",
                "TGT", "MGM", "GE", "DOW", "UTX",
                "BKNG", "CVS", "SPY", "XLV", "XLE",
                "XLF", "XLK", "XLP", "XLY", "GLD"
        );

        for (String symbol : stockSymbols) {
            new Thread(() -> {
                try {
                    StockResponse stockResponse = alphaVantageAPI.getStockPrices(symbol).execute().body();
                    if (stockResponse != null && stockResponse.getStock() != null) {
                        updateStockData(stockResponse.getStock());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // Планируем следующее обновление
        handler.postDelayed(this::fetchStockData, REFRESH_INTERVAL_MS);
    }

    private void updateStockData(Stock newStock) {
        runOnUiThread(() -> {
            // Обновляем существующую запись или добавляем новую
            boolean found = false;
            for (int i = 0; i < stocks.size(); i++) {
                if (stocks.get(i).getName().equals(newStock.getName())) {
                    stocks.set(i, newStock);
                    found = true;
                    break;
                }
            }
            if (!found) {
                stocks.add(newStock);
            }

            // Обновляем отфильтрованный список
            filterStocks(searchView.getQuery().toString());
        });
    }

    private void filterStocks(String searchText) {
        filteredStocks.clear();
        if (searchText.isEmpty()) {
            filteredStocks.addAll(stocks);
        } else {
            for (Stock stock : stocks) {
                if (stock.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredStocks.add(stock);
                }
            }
        }
        viewAdapter.updateStocks(filteredStocks);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Удаляем все сообщения и колбэки, чтобы избежать утечек памяти
        handler.removeCallbacksAndMessages(null);
    }
}

