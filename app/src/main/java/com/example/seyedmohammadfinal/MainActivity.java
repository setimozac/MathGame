package com.example.seyedmohammadfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn1, btn2, btn3, btn4,btn5, btn6, btn7, btn8, btn9, btn0, btnDot,
            btnDash, btnGenerate, btnValidate, btnClear, btnScore, btnFinish;
    TextView txtViewQuestion, txtViewTitle, txtViewHard;
    EditText editTextAnswer;
    Switch difficulty, themeChange;

    String answer;
    double actualAnswer;
    boolean isNegative, isHard, isGenerated;
    int firstOperand, secondOperand;
    char operation;
    ArrayList<UserAnswer> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        // initializing all the buttons and adding click listeners
        btn0 = findViewById(R.id.button0);
        btn0.setOnClickListener(this);

        btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        btn4 = findViewById(R.id.button4);
        btn4.setOnClickListener(this);

        btn5 = findViewById(R.id.button5);
        btn5.setOnClickListener(this);

        btn6 = findViewById(R.id.button6);
        btn6.setOnClickListener(this);

        btn7 = findViewById(R.id.button7);
        btn7.setOnClickListener(this);

        btn8 = findViewById(R.id.button8);
        btn8.setOnClickListener(this);

        btn9 = findViewById(R.id.button9);
        btn9.setOnClickListener(this);

        btnDot = findViewById(R.id.buttonDot);
        btnDot.setOnClickListener(this);

        btn0 = findViewById(R.id.button0);
        btn0.setOnClickListener(this);

        btnDash = findViewById(R.id.buttonDash);
        btnDash.setOnClickListener(this);

        btnGenerate = findViewById(R.id.buttonGenerate);
        btnGenerate.setOnClickListener(this);

        btnValidate = findViewById(R.id.buttonValidate);
        btnValidate.setOnClickListener(this);

        btnClear = findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(this);

        btnScore = findViewById(R.id.buttonScore);
        btnScore.setOnClickListener(this);

        btnFinish = findViewById(R.id.buttonFinish);
        btnFinish.setOnClickListener(this);

        // initializing text views
        txtViewQuestion = findViewById(R.id.txtViewQuestion);
        txtViewQuestion.setText("----");
        txtViewTitle = findViewById(R.id.txtViewTitle);
        txtViewHard = findViewById(R.id.txtViewHard);

        //initializing EditText for user input
        editTextAnswer = findViewById(R.id.editTextAnswer);
        editTextAnswer.setEnabled(false);

        difficulty = findViewById(R.id.switchDifficulty);
        difficulty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHard = isChecked;
                txtViewQuestion.setText("----");
            }
        });

        // initialize and set on change listener for SWITCH (change the theme of the application )
        themeChange = findViewById(R.id.switchTheme);
        themeChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View myView = findViewById(R.id.layout);
                View root = myView.getRootView();

                if(isChecked) {
                    blueBlack(myView);
                }
                else {
                    colors(myView);

                }

            }
        });

        answer = "";
        isNegative = false;
        isGenerated = false;
        results = new ArrayList<>();

        btnValidate.setEnabled(false);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button0:
                if(answer.length() != 0) getNum(0);
                break;
            case R.id.button1:
                getNum(1);
                break;
            case R.id.button2:
                getNum(2);
                break;
            case R.id.button3:
                getNum(3);
                break;
            case R.id.button4:
                getNum(4);
                break;
            case R.id.button5:
                getNum(5);
                break;
            case R.id.button6:
                getNum(6);
                break;
            case R.id.button7:
                getNum(7);
                break;
            case R.id.button8:
                getNum(8);
                break;
            case R.id.button9:
                getNum(9);
                break;
            case R.id.buttonDot:
                addSignAndDecimal(".");
                break;
            case R.id.buttonDash:
                addSignAndDecimal("-");
                break;
            case R.id.buttonGenerate:
                generateQuestion();
                break;
            case R.id.buttonValidate:
                validateAnswer();
                break;
            case R.id.buttonClear:
                clear();
                break;
            case R.id.buttonScore:
                goToScore();
                break;
            default:
                finish();
        }

    }

    private void goToScore() {
        Intent intent = new Intent(this, ShowResults.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("results", results);
        intent.putExtra("bundle", bundle);

        startActivityForResult(intent, 1);
    }

    private void clear() {
        if(answer.isEmpty()) txtViewQuestion.setText("----");
        answer = "";
        editTextAnswer.setText("0");

        btnValidate.setEnabled(false);
    }

    private void validateAnswer() {
        if(!isGenerated) return;
        answer = editTextAnswer.getText().toString();
        if(answer.isEmpty()) answer = "0";
        UserAnswer userAnswer;
        if(isNegative) {
            answer = !answer.equals("-") ? answer.substring(1) : "0";
            userAnswer = new UserAnswer(firstOperand, secondOperand, this.actualAnswer, -Double.parseDouble(answer), isHard, operation);
        }else {
            userAnswer = new UserAnswer(firstOperand, secondOperand, this.actualAnswer, Double.parseDouble(answer), isHard, operation);
        }

        if(userAnswer.result) {
            txtViewQuestion.setText("Correct");
        }
        else {
            txtViewQuestion.setText("Wrong");
        }
        results.add(userAnswer);
        isGenerated = false;
        isNegative = false;
        clear();
    }

    private void setActualAnswer(int firstNum, int secondNum, char operation) {
        firstOperand = firstNum;
        secondOperand = secondNum;
        DecimalFormat df = new DecimalFormat("#.#");
        switch (operation) {
            case '*':
                actualAnswer = firstNum * secondNum;
                break;
            case '/':
                actualAnswer = (secondNum != 0) ? Double.parseDouble(df.format((double) firstNum / secondNum)) : 0;
                break;
            case '+':
                actualAnswer = firstNum + secondNum;
                break;
            case '-':
                actualAnswer = firstNum - secondNum;
                break;
        }
        this.operation = operation;
    }

    private void generateQuestion() {
        Random random = new Random();
        int firstNum, secondNum;
        if(isHard) {
            firstNum = random.nextInt(100);
            do{secondNum = random.nextInt(100);} while (secondNum == 0);
        }
        else {
            firstNum = random.nextInt(10);
            do{secondNum = random.nextInt(10);} while(secondNum == 0);
        }

        String operations = "+-*/";
        char operation = operations.charAt(random.nextInt(operations.length()));

        setActualAnswer(firstNum, secondNum, operation);

        txtViewQuestion.setText(firstNum + " " + operation + " " + secondNum);
        btnValidate.setEnabled(true);
        isGenerated = true;
        editTextAnswer.setText("0");
        answer = "";
    }

    private void addSignAndDecimal(String s) {
        if(s.equals(".") && !answer.contains(".")) {
            if(answer.length() == 0) answer+= "0.";
            else answer += ".";
        }else if(s.equals("-")) {
            if(isNegative) {
                isNegative = false;
                answer = (answer.length() > 1) ? answer.substring(1) : "";
                if(answer.length() == 0) {
                    editTextAnswer.setText("0");
                    return;
                }

            }else {
                isNegative = true;
                answer = "-"+answer;
            }
        }
        editTextAnswer.setText(answer);
    }

    private void getNum(int num) {
        answer += num;
        btnValidate.setEnabled(true);
        editTextAnswer.setText(answer);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == RESULT_OK)
                if(data.getExtras().getSerializable("name").toString().isEmpty()){
                    txtViewTitle.setText("Math Quiz");
                    txtViewQuestion.setText("----");
                    return;
                }
                txtViewTitle.setText(data.getExtras().getSerializable("name").toString());
                txtViewQuestion.setText(data.getExtras().getSerializable("score").toString());

        }
    }

    private void blueBlack(View myView) {
        myView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btn0.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btn1.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btn2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btn3.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btn4.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btn5.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btn6.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btn7.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btn8.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btn9.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btnClear.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btnGenerate.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btnValidate.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btnScore.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btnFinish.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btnDash.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btnDot.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        txtViewHard.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
    }


    private void colors(View myView) {
        myView.setBackground(getResources().getDrawable(R.drawable.back_calc));
        btn0.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.bluepink1));
        btn1.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.teal2));
        btn2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.yellow));
        btn3.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.orangeyellow));
        btn4.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.teal));
        btn5.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.pinkyellow));
        btn6.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.orange));
        btn7.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.teal_700));
        btn8.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.bluepink1));
        btn9.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.pink1));
        btnClear.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.pink));
        btnGenerate.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.teal_200));
        btnValidate.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.bluepink));
        btnScore.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
        btnFinish.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.pink));
        btnDash.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.pink1));
        btnDot.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.teal));
        txtViewHard.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.cyan));
    }
}