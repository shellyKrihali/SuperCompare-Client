package com.example.supercompare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.supercompare.Interface.ItemClickListener;
import com.example.supercompare.Model.Product;
import com.example.supercompare.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class ProductsList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //ADD SEARCH TOOLBAR IN THIS ACTIVITY!
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ListView found_items;
    FirebaseDatabase database;
    DatabaseReference productList;
    String categoryID = "";
    ArrayList<String> listOfProductNames = new ArrayList<>();
    ArrayList<Product> listOfProducts = new ArrayList<>();
    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;
    MaterialSearchView searchView;
    ArrayList<String> found = new ArrayList<String>();
    ArrayList<Product> productsFound = new ArrayList<Product>();
    int posInOriginalList;
    DrawerLayout drawerProductList;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        found_items = findViewById(R.id.found_items);
        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Product");
        recyclerView = (RecyclerView)findViewById(R.id.recycler_products);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        drawerProductList = findViewById(R.id.drawer_layout_productList);
        NavigationView navigationView = findViewById(R.id.nav_view_productList);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_products, R.id.nav_my_cart, R.id.nav_logout)
                .setDrawerLayout(drawerProductList)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        setNavigationViewListener();
        View headerView = navigationView.getHeaderView(0);


        Toolbar toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search..");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitleTextAppearance(this,R.style.ToolBarTextAppearance);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        if (getIntent()!=null){
            categoryID = getIntent().getStringExtra("CategoryID");
        }
        if (!categoryID.isEmpty() && categoryID!=null){
            loadListProducts(categoryID);
        }
        searchView = findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                found_items.setVisibility(View.INVISIBLE);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //goto specific product on the search bar
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    productsFound = new ArrayList<Product>();
                    found = new ArrayList<String>();
                    Product tempP = new Product(newText,null,null,null,null,null,null,null,null);
                    for (Product product : listOfProducts) {
                       if(product.compareTo(tempP)==1){
                           productsFound.add(product);
                           found.add(product.getName());
                           removeDuplicatesProducts(productsFound);
                           removeDuplicatesStrings(found);
                       }
                    }

                    found_items.setVisibility(View.VISIBLE);
                    ArrayAdapter adapter = new ArrayAdapter(ProductsList.this, android.R.layout.simple_list_item_1, found);
                    found_items.setAdapter(adapter);
                } else {
                    found_items.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
        found_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product clicked;
                //check which item clicked
                clicked = productsFound.get(position);
                for (Product p: listOfProducts){
                    if (clicked == p)
                        posInOriginalList = listOfProducts.indexOf(p);
                }
                //move to specific detail about product
                Intent productDetail = new Intent(ProductsList.this,ProductDetail.class);
                productDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                productDetail.putExtra("productID",adapter.getRef(posInOriginalList).getKey()); //sent product id to new activity
                startActivity(productDetail);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }


    private void loadListProducts(String categoryID) {
        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productList.orderByChild("menuID").equalTo(categoryID)) {
            @Override
            protected void populateViewHolder(ProductViewHolder productViewHolder, Product product, int i) {
                productViewHolder.product_name.setText(product.getName());
                productViewHolder.product_name.setTextSize(30);

                listOfProductNames.add(product.getName());
                listOfProducts.add(product);
                Picasso.with(getBaseContext()).load(product.getImage()).into(productViewHolder.product_image);

                final Product local = product;
                productViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent productDetail = new Intent(ProductsList.this,ProductDetail.class);
                        productDetail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        productDetail.putExtra("productID",adapter.getRef(position).getKey()); //sent product id to new activity
                        startActivity(productDetail);

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<String> removeDuplicatesStrings(ArrayList<String> arrayList){
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        set.addAll(arrayList);
        arrayList.clear();
        arrayList.addAll(set);
        return arrayList;
    }

    private ArrayList<Product> removeDuplicatesProducts(ArrayList<Product> arrayList){
        LinkedHashSet<Product> set = new LinkedHashSet<Product>();
        set.addAll(arrayList);
        arrayList.clear();
        arrayList.addAll(set);
        return arrayList;
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
                Intent intent = new Intent(ProductsList.this,Cart.class);
                startActivity(intent);
                break;
            }

            case R.id.nav_logout:{
                Intent loginScreen=new Intent(this,MainActivity.class);
                loginScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginScreen);
                break;
            }

            case R.id.nav_products:{
                Intent intent = new Intent(ProductsList.this,Home.class);
                startActivity(intent);
                break;
            }
        }
        //close navigation drawer
        drawerProductList.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_productList);
        navigationView.setNavigationItemSelectedListener(this);
    }

}
