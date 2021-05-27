package com.example.seyedmohammadfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ShowResults extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Spinner spinner;
    EditText editTextName, editTextScore;
    Button btnBack;
    ListView listViewResult;
    ProgressBar psBar;

    int score;
    ArrayList<UserAnswer> results;
    ArrayList<UserAnswer> modifiedResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        initialize();
    }

    private void initialize() {

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        Serializable objSerializable = bundle.getSerializable("results");
        results = (ArrayList<UserAnswer>)objSerializable;

        //initializing the Spinner and it's adapter
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<SortOptions> spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, SortOptions.values());
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        editTextName = findViewById(R.id.editTextRegister);
        editTextScore = findViewById(R.id.editTextScore);

        // initializing back button and on click listener
        btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(this);

        // initializing the list view and arrayAdapter
        listViewResult = findViewById(R.id.listViewResult);
        ArrayAdapter<UserAnswer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);

        listViewResult.setAdapter(adapter);

        psBar = (ProgressBar)findViewById(R.id.progressBar);
        psBar.setMax(100);

        score = 0;
        calculatePercentage();

    }

    private void calculatePercentage() {
        int correct = 0;
        for(UserAnswer answer : results) {
            if(answer.result) correct++;
        }
        if(results.size() == 0) return;
        score = (correct * 100) / results.size();

        psBar.setProgress(score);
        editTextScore.setText(score + " %");

        if(score <= 40) {
            psBar.getProgressDrawable().setColorFilter(
                    getResources().getColor(R.color.orange), android.graphics.PorterDuff.Mode.SRC_IN);
        }else if (score < 60 && score > 40) {
            psBar.getProgressDrawable().setColorFilter(
                    getResources().getColor(R.color.yellowT), android.graphics.PorterDuff.Mode.SRC_IN);
        }else {
            psBar.getProgressDrawable().setColorFilter(
                    getResources().getColor(R.color.tealT), android.graphics.PorterDuff.Mode.SRC_IN);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("name", editTextName.getText().toString());
        intent.putExtra("score", editTextScore.getText().toString());
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        modifiedResults = new ArrayList<>();
        switch (position) {
            case 1:
                for(UserAnswer ans : results) {
                    if(ans.result) modifiedResults.add(ans);
                }
                setSpinnerAdapter(modifiedResults);
                break;
            case 2:
                for(UserAnswer ans : results) {
                    if(!ans.result) modifiedResults.add(ans);
                }
                setSpinnerAdapter(modifiedResults);
                break;
            case 3:
                modifiedResults = new ArrayList<>(results);
                Collections.sort(modifiedResults);
                setSpinnerAdapter(modifiedResults);
                break;
            case 4:
                modifiedResults = new ArrayList<>(results);
                Collections.sort(modifiedResults, Collections. reverseOrder());
                setSpinnerAdapter(modifiedResults);
                break;
            default:
                setSpinnerAdapter(results);

        }
    }

    private void setSpinnerAdapter(ArrayList<UserAnswer> res) {
        ArrayAdapter<UserAnswer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, res);

        listViewResult.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}