package com.example.supercompare.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.supercompare.Model.Comparison;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "SuperCompareSQL.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Comparison> getCarts() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        //use the table from sql
        String[] sqlSelect = {"productName", "productID", "quantity",
                "priceRamiLevi", "priceShufersal", "priceYinotBitan", "priceVictory", "priceMega"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
        final List<Comparison> res = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                res.add(new Comparison(c.getString(c.getColumnIndex("productID")),
                        c.getString(c.getColumnIndex("productName")),
                        c.getString(c.getColumnIndex("quantity")),
                        c.getString(c.getColumnIndex("priceRamiLevi")),
                        c.getString(c.getColumnIndex("priceShufersal")),
                        c.getString(c.getColumnIndex("priceYinotBitan")),
                        c.getString(c.getColumnIndex("priceVictory")),
                        c.getString(c.getColumnIndex("priceMega"))));
            } while (c.moveToNext());
        }
        return res;
    }

    public void addToCart(Comparison comparison){
        //add items to cart
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(productID,productName,quantity,priceRamiLevi,priceShufersal,priceYinotBitan,priceVictory,priceMega) VALUES('%s','%s','%s','%s','%s','%s','%s','%s');",
                comparison.getProductID(),comparison.getProductName(),comparison.getQuantity(),
                comparison.getPriceRamiLevi(),
                comparison.getPriceShufersal(),
                comparison.getPriceYinotBitan(),
                comparison.getPriceVictory(),
                comparison.getPriceMega());
        db.execSQL(query);
    }

    public void cleanCart(Comparison comparison){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }
}
