package com.example.assignment_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseManager {
    static final String DB_NAME = "SnacksDB";
    static final int DB_VERSION = 1;
    static final String TABLE_SNACKS = "snacks";
    static final String COLUMN_ID = "id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_IMG_SRC = "img_src";
    static final String COLUMN_PRICE = "price";
    static final String COLUMN_DETAILS = "details";

    Context c;
    DBHelper helper;
    public DatabaseManager(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }

    private void insertSnack(SQLiteDatabase db, int img, String name, String details, float price) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DETAILS, details);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_IMG_SRC, img);

        db.insert(TABLE_SNACKS, null, cv);
    }

    public ArrayList<Snack> getAllSnacks(){
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Snack> result = new ArrayList<>();
        Cursor cursor = db.query(TABLE_SNACKS,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()){
            do {
                int index_id = cursor.getColumnIndex(COLUMN_ID);
                int index_name = cursor.getColumnIndex(COLUMN_NAME);
                int index_price = cursor.getColumnIndex(COLUMN_PRICE);
                int index_details = cursor.getColumnIndex(COLUMN_DETAILS);
                int index_img_src = cursor.getColumnIndex(COLUMN_IMG_SRC);

                Snack s = new Snack(cursor.getInt(index_img_src), cursor.getString(index_name), cursor.getString(index_details), cursor.getFloat(index_price));
                result.add(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return result;
    }


    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_SNACKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DETAILS + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_IMG_SRC + " INTEGER" +
                    ")";

            db.execSQL(createTable);

            insertInitialSnacks(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
            onCreate(db);
        }
        private void insertInitialSnacks(SQLiteDatabase db) {

            insertSnack(db, R.drawable.snacks_popcorn, "Popcorn", "Large / Buttered", 8.99f);
            insertSnack(db, R.drawable.snacks_nachos, "Nachos", "With Cheese Dip", 7.99f);
            insertSnack(db, R.drawable.snacks_soft_drink, "Soft Drink", "Large / Any Flavor", 5.99f);
            insertSnack(db, R.drawable.snacks_lays, "Lays", "Family Pack", 1.99f);
        }
    }
}
