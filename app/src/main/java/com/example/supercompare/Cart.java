package com.example.supercompare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.supercompare.Database.Database;
import com.example.supercompare.Model.Comparison;
import com.example.supercompare.ViewHolder.CartAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Cart extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference request;
    FloatingActionButton cleanCart_cartActivity;

    TextView txtTotalPrice;
    DrawerLayout drawerCart;
    private AppBarConfiguration mAppBarConfiguration;
    Button openDrawerCart;

    List<Comparison> cart = new ArrayList<>();
    CartAdapter adapter;

    public void setSupermarketName(String supermarketName) {
        this.supermarketName = supermarketName;
    }

    String supermarketName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        request = database.getReference("Request"); //use sql
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);

        drawerCart = findViewById(R.id.drawer_layout_cart);
        NavigationView navigationView = findViewById(R.id.nav_view_cart);
        openDrawerCart = findViewById(R.id.openDrawerCart);
        openDrawerCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerCart.openDrawer(GravityCompat.START);
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_products, R.id.nav_my_cart, R.id.nav_logout)
                .setDrawerLayout(drawerCart)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        setNavigationViewListener();
        View headerView = navigationView.getHeaderView(0);

        cleanCart_cartActivity = findViewById(R.id.cleanCart_cartActivity);
        cleanCart_cartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).cleanCart(new Comparison("","","","","","","",""));
                Toast.makeText(Cart.this, "Cart cleaned!", Toast.LENGTH_SHORT).show();
                txtTotalPrice.setText("The Cart is Empty.");
                recyclerView.setVisibility(View.INVISIBLE);
            }
        });

        loadListProducts();
    }

    private void loadListProducts() { //load the products from the cart that built
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        double totalRamiLevi=0, totalShufersal=0, totalVictory=0, totalYinotBitan=0, totalMega=0;
        for (Comparison comparison: cart){
            totalRamiLevi += (Double.parseDouble(comparison.getPriceRamiLevi()))*(Double.parseDouble(comparison.getQuantity()));
            totalShufersal += (Double.parseDouble(comparison.getPriceShufersal()))*(Double.parseDouble(comparison.getQuantity()));
            totalVictory += (Double.parseDouble(comparison.getPriceVictory()))*(Double.parseDouble(comparison.getQuantity()));
            totalYinotBitan += (Double.parseDouble(comparison.getPriceYinotBitan()))*(Double.parseDouble(comparison.getQuantity()));
            totalMega += (Double.parseDouble(comparison.getPriceMega()))*(Double.parseDouble(comparison.getQuantity()));
        }
        Locale locale = new Locale("he","IL");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        //find the best price in total from all stores
        Map<String,Double> pricesMap = new HashMap<>();
        pricesMap.put("Rami Levi", totalRamiLevi);
        pricesMap.put("Shufersal", totalShufersal);
        pricesMap.put("Victory", totalVictory);
        pricesMap.put("Yinot Bitan", totalYinotBitan);
        pricesMap.put("Mega", totalMega);

        Set<Map.Entry<String, Double>> entries = pricesMap.entrySet();
        Comparator<Map.Entry<String, Double>> valueComparator = new Comparator<Map.Entry<String,Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> e1, Map.Entry<String, Double> e2) {
                Double v1 = e1.getValue();
                Double v2 = e2.getValue();
                if (v2<v1)
                    return 1;
                else if (v1<v2)
                    return -1;
                else
                    return 0;
            }
        };
        List<Map.Entry<String, Double>> listOfEntries = new ArrayList<Map.Entry<String, Double>>(entries);
        Collections.sort(listOfEntries, valueComparator);
        LinkedHashMap<String, Double> sortedByValue = new LinkedHashMap<String, Double>(listOfEntries.size());
        for(Map.Entry<String, Double> entry : listOfEntries){
            sortedByValue.put(entry.getKey(), entry.getValue());
        }
        ArrayList<Double> pricesTotal = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        Set<Map.Entry<String, Double>> entrySetSortedByValue = sortedByValue.entrySet();
        for (Map.Entry<String, Double> mapping : entrySetSortedByValue){
            pricesTotal.add(mapping.getValue());
            names.add(mapping.getKey());
        }
        if (pricesTotal.get(0)==0.00 || pricesTotal.get(0) == null)
            txtTotalPrice.setText("The Cart is Empty.");
        else {
            txtTotalPrice.setText(names.get(0) + " :" + numberFormat.format(pricesTotal.get(0)));
            setSupermarketName(names.get(0));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_my_cart: {
                drawerCart.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.nav_logout:{
                Intent loginScreen=new Intent(this,MainActivity.class);
                loginScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginScreen);
                break;
            }

            case R.id.nav_products:{
                Intent intent = new Intent(Cart.this,Home.class);
                startActivity(intent);
                break;
            }
        }
        //close navigation drawer
        drawerCart.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_cart);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

}
