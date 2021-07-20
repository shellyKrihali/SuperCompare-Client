package com.example.supercompare.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.supercompare.Model.Comparison;
import com.example.supercompare.R;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtCartName;
    public ImageView imgCartCount;

    public void setTxtCartName(TextView txtCartName) {
        this.txtCartName = txtCartName;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtCartName = itemView.findViewById(R.id.cart_item_name);
        imgCartCount = itemView.findViewById(R.id.cart_item_count);
    }

    @Override
    public void onClick(View v) {

    }
}
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Comparison> listData = new ArrayList<>();
    private Context context;
    private String supermarketName="";


    public CartAdapter(List<Comparison> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.cart_layout,parent,false);

        return new CartViewHolder(itemView);
    }

    //set the cart products details
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(position).getQuantity(), Color.RED);
        holder.imgCartCount.setImageDrawable(drawable); //set specific item quantity
        holder.txtCartName.setText(listData.get(position).getProductName()); //set specific item name
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
