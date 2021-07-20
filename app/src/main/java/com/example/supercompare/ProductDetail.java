package com.example.supercompare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.supercompare.Database.Database;
import com.example.supercompare.Model.Comparison;
import com.example.supercompare.Model.Product;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView product_description;
    TextView product_price_ramilevi,product_price_shufersal,product_price_yinotbitan,product_price_mega,product_price_victory;
    ImageView img_product;
    TextView pricesTitle;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String productID="";
    Product product;
    FirebaseDatabase database;
    DatabaseReference products;
    DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    Button openDrawerProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        drawer = findViewById(R.id.drawer_layout_details);
        NavigationView navigationView = findViewById(R.id.nav_view_details);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_products, R.id.nav_my_cart, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        setNavigationViewListener();
        View headerView = navigationView.getHeaderView(0);
        openDrawerProducts = findViewById(R.id.openDrawerDetails);
        //three dots button clicked : open navigation drawer
        openDrawerProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        database = FirebaseDatabase.getInstance();
        products = database.getReference("Product"); //use products from firebase

        numberButton = findViewById(R.id.number_button); //quantity
        btnCart = findViewById(R.id.btnCart);
        pricesTitle = findViewById(R.id.prices_title);
        pricesTitle.setTextSize(15);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Comparison(
                        productID,
                        product.getName(),
                        numberButton.getNumber(),
                        product.getPriceRamiLevi(),
                        product.getPriceShufersal(),
                        product.getPriceYinotBitan(),
                        product.getPriceVictory(),
                        product.getPriceMega()
                ));

                Toast.makeText(ProductDetail.this, "Added to Cart!",Toast.LENGTH_SHORT).show();
            }
        });
        product_description = findViewById(R.id.product_description);
        img_product = findViewById(R.id.img_product);
        product_price_ramilevi = findViewById(R.id.product_price_ramilevi);
        product_price_shufersal = findViewById(R.id.product_price_shufersal);
        product_price_mega = findViewById(R.id.product_price_mega);
        product_price_victory = findViewById(R.id.product_price_victory);
        product_price_yinotbitan = findViewById(R.id.product_price_yinotbitan);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpendedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        if (getIntent() != null){
            productID = getIntent().getStringExtra("productID");
        }
        if(!productID.isEmpty()){
            getDetailProduct(productID);
        }

    }

    private void getDetailProduct(String productID) {
        products.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                product = dataSnapshot.getValue(Product.class);
                Picasso.with(getBaseContext()).load(product.getImage()).into(img_product);
                collapsingToolbarLayout.setTitle(product.getName());

                //set all info on specific product by productID
                product_price_ramilevi.setText(product.getPriceRamiLevi());
                product_price_shufersal.setText(product.getPriceShufersal());
                product_price_yinotbitan.setText(product.getPriceYinotBitan());
                product_price_mega.setText(product.getPriceMega());
                product_price_victory.setText(product.getPriceVictory());
                product_description.setText(product.getDescription());
                product_description.setTextSize(25);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
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
                //cart option clicked : move to cart activity
                Intent intent = new Intent(ProductDetail.this,Cart.class);
                startActivity(intent);
                break;
            }

            case R.id.nav_logout:{
                //logout option clicked : move to main activity
                Intent loginScreen=new Intent(this,MainActivity.class);
                loginScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginScreen);
                break;
            }

            case R.id.nav_products:{
                Intent intent = new Intent(ProductDetail.this,Home.class);
                startActivity(intent);
                break;
            }
        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_details);
        navigationView.setNavigationItemSelectedListener(this);
    }

}
