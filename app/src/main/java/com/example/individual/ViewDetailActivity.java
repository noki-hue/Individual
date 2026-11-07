package com.example.individual;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewDetailActivity extends AppCompatActivity {

    protected Cursor cursor;
    DataHelper dbHelper;
    TextView tvMonth, tvUnit, tvRebate, tvTotal, tvFinal;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);

        dbHelper = new DataHelper(this); // Lab 3 Slide 32

        tvMonth = findViewById(R.id.tvDetailMonth);
        tvUnit = findViewById(R.id.tvDetailUnit);
        tvRebate = findViewById(R.id.tvDetailRebate);
        tvTotal = findViewById(R.id.tvDetailTotal);
        tvFinal = findViewById(R.id.tvDetailFinal);
        btnBack = findViewById(R.id.buttonBack); // Lab 3 Slide 32

        int recordId = getIntent().getIntExtra("id", 0); // Lab 2 Slide 41 & Lab 3 Slide 32

        // Query record by ID
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Lab 3 Slide 32
        cursor = db.rawQuery("SELECT * FROM bill WHERE id = " + recordId, null); // Lab 3 Slide 32

        if (cursor.moveToFirst()) {
            tvMonth.setText("Month: " + cursor.getString(1));
            tvUnit.setText("Electricity Used: " + cursor.getDouble(2) + " kWh");
            tvRebate.setText("Rebate: " + cursor.getDouble(3) + "%");
            tvTotal.setText(String.format("Total Charges: RM %.2f", cursor.getDouble(4)));
            tvFinal.setText(String.format("Final Cost: RM %.2f", cursor.getDouble(5))); // Lab 3 Slide 32
        }
        cursor.close(); // Lab 3 Slide 32

        btnBack.setOnClickListener(v -> finish()); // Lab 3 Slide 32
    }
}