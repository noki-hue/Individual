package com.example.individual;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    String[] register;
    int[] idList;
    ListView ListView01;
    protected Cursor cursor;
    DataHelper dbcenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbcenter = new DataHelper(this); // Lab 3 Slide 23
        RefreshList(); // Lab 3 Slide 23
    }

    // Refresh list view with SQLite data (Lab 3 Slide 22)
    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase(); // Lab 3 Slide 22
        cursor = db.rawQuery("SELECT * FROM bill", null); // Lab 3 Slide 22

        register = new String[cursor.getCount()];
        idList = new int[cursor.getCount()];
        cursor.moveToFirst();

        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            idList[cc] = cursor.getInt(0);

            // ASSIGNMENT MANDATE: Display Month and Final Cost ONLY in ListView
            String month = cursor.getString(1);
            double finalCost = cursor.getDouble(5);
            register[cc] = month + " - RM " + String.format("%.2f", finalCost);
        }

        ListView01 = (ListView) findViewById(R.id.listView1); // Lab 3 Slide 22
        ListView01.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, register)); // Lab 3 Slide 22
        ListView01.setSelected(true);

        // Alert Dialog menu handling (Lab 3 Slide 27)
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final int selectedId = idList[arg2];
                final CharSequence[] dialogitem = {"View Detail", "Delete Record"}; // Lab 3 Slide 27

                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this); // Lab 3 Slide 27
                builder.setTitle("Selection"); // Lab 3 Slide 27
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                // Open Detail Activity using Explicit Intent (Lab 2 Slide 17 & Lab 3 Slide 27)
                                Intent i = new Intent(getApplicationContext(), ViewDetailActivity.class);
                                i.putExtra("id", selectedId); // Pass record ID
                                startActivity(i);
                                break;
                            case 1:
                                // Delete record directly via SQL (Lab 3 Slide 27)
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("DELETE FROM bill WHERE id = " + selectedId);
                                Toast.makeText(getApplicationContext(), "Data Successfully Removed", Toast.LENGTH_SHORT).show(); // Lab 3 Slide 27
                                RefreshList(); // Refresh list after deletion
                                break;
                        }
                    }
                });
                builder.create().show(); // Lab 3 Slide 27
            }
        });

        ((ArrayAdapter) ListView01.getAdapter()).notifyDataSetInvalidated(); // Lab 3 Slide 22
    }
}