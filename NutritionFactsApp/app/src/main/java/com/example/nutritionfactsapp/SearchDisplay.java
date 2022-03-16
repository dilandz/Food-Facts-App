package com.example.nutritionfactsapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


// This activity is the search activity with search bar and the buttons.
public class SearchDisplay extends AppCompatActivity {

    // button and edit text variable declaration
    EditText barcode;
    Button search;
    Button scan;
    String barcodeText;
    String barcodeTextScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        barcode = findViewById(R.id.search_bar);
        search = findViewById(R.id.search_button);
        scan = findViewById(R.id.scan_button);


        search.setOnClickListener(new View.OnClickListener() { // click on search and operation begins to search
            @Override
            public void onClick(View v) {
                barcodeText = barcode.getText().toString();

                if (TextUtils.isEmpty(barcodeText)) {
                    Toast.makeText(SearchDisplay.this, "Enter Data ", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(SearchDisplay.this, Confirm.class);

                    intent.putExtra("id", barcodeText);
                    startActivity(intent);

                }
            }
        });

        scan.setOnClickListener(new View.OnClickListener() { // open the scanner to scan the barcode
            @Override
            public void onClick(View v) {
                scan();
            }
        });

    }

    public void scan() {
        IntentIntegrator integrator = new IntentIntegrator(SearchDisplay.this); //setting the scanner
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setPrompt("Scanning Code");
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Getting the results from the barcode
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            barcodeTextScan = result.getContents();
            Intent intent = new Intent(SearchDisplay.this, Confirm.class);

            intent.putExtra("id", barcodeTextScan);
            startActivity(intent);

        } else {
            Toast.makeText(this, "NO Data", Toast.LENGTH_LONG).show();
            super.onActivityResult(requestCode, resultCode, data);

        }


    }


}