package com.example.stockwarden.mainpackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockwarden.R;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private List<Stock> stocks;

    public StockAdapter(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        public TextView stockName;
        public TextView stockPrice;

        public StockViewHolder(View view) {
            super(view);
            stockName = view.findViewById(R.id.stock_name);
            stockPrice = view.findViewById(R.id.stock_price);
        }
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_item, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock stock = stocks.get(position);
        holder.stockName.setText(stock.getName());
        holder.stockPrice.setText(stock.getPrice());
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public void updateStocks(List<Stock> newStocks) {
        this.stocks = newStocks;
        notifyDataSetChanged();
    }
}
