package com.example.individual;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Step 1: Declare class-level objects (Lab 1 Slide 18 & Lab 3 Slide 21)
    private Spinner spinnerMonth;
    private EditText etUnit;
    private SeekBar seekBarRebate;
    private TextView tvRebateLabel, tvTotalCharge, tvFinalCost;
    private Button btnCalculate, btnSave, btnHistory, btnAbout;

    public static MainActivity ma; // Static reference (Lab 3 Slide 21)
    private DataHelper dbcenter;

    private double totalCharges = 0.0;
    private double finalCost = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ma = this; // Lab 3 Slide 23
        dbcenter = new DataHelper(this); // Lab 3 Slide 23

        // Step 2: Assign View objects with findViewById (Lab 1 Slide 21)
        spinnerMonth = findViewById(R.id.spinnerMonth);
        etUnit = findViewById(R.id.etUnit);
        seekBarRebate = findViewById(R.id.seekBarRebate);
        tvRebateLabel = findViewById(R.id.tvRebateLabel);
        tvTotalCharge = findViewById(R.id.tvTotalCharge);
        tvFinalCost = findViewById(R.id.tvFinalCost);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnSave = findViewById(R.id.btnSave);
        btnHistory = findViewById(R.id.btnHistory);
        btnAbout = findViewById(R.id.btnAbout);

        // Populate Month Spinner
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonth.setAdapter(adapter);

        // SeekBar listener for rebate percentage
        seekBarRebate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRebateLabel.setText("Rebate Percentage: " + progress + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Step 3: Button Action Handlers (Lab 1 Slide 15)
        btnCalculate.setOnClickListener(v -> calculateBill());
        btnSave.setOnClickListener(v -> saveRecord());

        // Intent Navigation (Lab 2 Slide 20 & 23)
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    // Tariff calculation based on official assignment rates
    private void calculateBill() {
        String unitInput = etUnit.getText().toString().trim(); // Lab 1 Slide 31

        // Validation & Error Notice (Good Design Practice requirement)
        if (TextUtils.isEmpty(unitInput)) {
            etUnit.setError("Please enter electricity unit!");
            return;
        }

        double unit = Double.parseDouble(unitInput);
        if (unit < 1 || unit > 1000) {
            etUnit.setError("Units must be between 1 and 1000 kWh!");
            return;
        }

        // Calculation based on official block rates
        if (unit <= 200) {
            totalCharges = unit * 0.218;
        } else if (unit <= 300) {
            totalCharges = (200 * 0.218) + ((unit - 200) * 0.334);
        } else if (unit <= 600) {
            totalCharges = (200 * 0.218) + (100 * 0.334) + ((unit - 300) * 0.516);
        } else {
            totalCharges = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((unit - 600) * 0.546);
        }

        double rebatePct = seekBarRebate.getProgress() / 100.0;
        finalCost = totalCharges - (totalCharges * rebatePct);

        // Update UI Outputs
        tvTotalCharge.setText(String.format("Total Charges: RM %.2f", totalCharges));
        tvFinalCost.setText(String.format("Final Cost: RM %.2f", finalCost));
    }

    // Database Insert Logic (Lab 3 Slide 16)
    private void saveRecord() {
        if (totalCharges == 0.0) {
            Toast.makeText(getApplicationContext(), "Please calculate first!", Toast.LENGTH_SHORT).show(); // Lab 1 Slide 24
            return;
        }

        String month = spinnerMonth.getSelectedItem().toString();
        double unit = Double.parseDouble(etUnit.getText().toString());
        double rebate = seekBarRebate.getProgress();

        SQLiteDatabase db = dbcenter.getWritableDatabase(); // Lab 3 Slide 16
        db.execSQL("INSERT INTO bill (month, unit, rebate, total_charge, final_cost) VALUES ('" +
                month + "', " + unit + ", " + rebate + ", " + totalCharges + ", " + finalCost + ")"); // Lab 3 Slide 16

        Toast.makeText(getApplicationContext(), "Data Successfully Added", Toast.LENGTH_SHORT).show(); // Lab 3 Slide 16
    }
}