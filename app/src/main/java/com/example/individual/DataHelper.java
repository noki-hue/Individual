package com.example.individual;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// 1. Extend SQLiteOpenHelper (Lab 3 Slide 12)
public class DataHelper extends SQLiteOpenHelper {

    // 2. Define Database Name and Version (Lab 3 Slide 12)
    private static final String DATABASE_NAME = "electricitybill.db";
    private static final int DATABASE_VERSION = 1;

    // 3. Constructor (Lab 3 Slide 12)
    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 4. Create Table in onCreate method (Lab 3 Slide 12)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE bill (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "month TEXT NULL, " +
                "unit REAL NULL, " +
                "rebate REAL NULL, " +
                "total_charge REAL NULL, " +
                "final_cost REAL NULL);";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
    }

    // 5. Upgrade Database method (Lab 3 Slide 12)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS bill");
        onCreate(db);
    }
}