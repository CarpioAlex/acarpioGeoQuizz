package com.acarpio.GeoQuizz;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int currentIndex = 0;
    private Question[] questionBank;
    protected TextView questionTextView;
    Button nextButton;
    Button backButton;
    Button startTimer;
    TextView seconds;
    Handler handler = new Handler();
    Runnable runnable;
    int countDownTime;

    ImageView trueButton;
    ImageView falseButton;
    ArrayList<View> scoreViews;
    TextView correctAnswers;
    TextView failedAnswers;
    int correctAnswersnum;
    int failedAnsersnum;
    private static final String KEY_INDEX = "index";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        // Checking if there was anything saved between states
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        // Getting the views that I will use
        questionTextView = findViewById(R.id.questionView);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        trueButton = findViewById(R.id.trueButton);
        falseButton = findViewById(R.id.falseButton);
        startTimer = findViewById(R.id.Crono);
        correctAnswers = findViewById(R.id.correctAnswers);
        failedAnswers = findViewById(R.id.failedAnswers);

        // Getting the questions array
        Resources res = getResources();
        String[] questionArray = res.getStringArray(R.array.questions_array);
        int[] answerArray = res.getIntArray(R.array.questions_answer);
        questionBank = new Question[questionArray.length];

        // Score keeping
        // Getting the views that represent the scores

        GridLayout scoreContainer = findViewById(R.id.scoreContainer);
        scoreViews = new ArrayList<>();

        // Adding the views to the array
        for (int i = 0; i < scoreContainer.getChildCount(); i++) {
            scoreViews.add(scoreContainer.getChildAt(i));
        }
        addBorderToTheScore();


        // Generating question objects
        for (int i = 0; i < questionArray.length; i++) {
            Question question = new Question(questionArray[i], answerArray[i]);
            questionBank[i] = question;
        }
        updateQuestion();
        // Next button listener
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % questionBank.length;
                updateQuestion();
                resetAnswerButton();
            }
        });

        // Back button listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex - 1 + questionBank.length) % questionBank.length;
                updateQuestion();

            }
        });


        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAnswer(1);
                noMoreAnswers();
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(0);
                noMoreAnswers();
            }
        });

        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountdown();
            }
        });







    }





    private void updateQuestion() {
        // Getting the question value from the array
        String currentQuestion = questionBank[currentIndex].getQuestion();
        // Setting the question value to the textview
        questionTextView.setText(currentQuestion);
    }

    private void checkAnswer(int userPressedTrue) {
        int answerIsTrue = questionBank[currentIndex].isAnswerTrue();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        setScore(userPressedTrue);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d("LOGGING_LIFECYCLE", "OnSavedInstanceState");
            savedInstanceState.putInt(KEY_INDEX, currentIndex);
        }



    public void noMoreAnswers() {
        trueButton.setClickable(false);
        falseButton.setClickable(false);


    }

    public void resetAnswerButton() {
        trueButton.setClickable(true);
        falseButton.setClickable(true);
    }

    public void setScore(int trueAnswer) {
        if (trueAnswer == 1) {
            Drawable drawable = getResources().getDrawable(R.drawable.score_background_green);
            scoreViews.get(currentIndex).setBackground(drawable);
            correctAnswersnum++;
            correctAnswers.setText("Correct answers: " + correctAnswersnum + " / " + questionBank.length);;
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.score_background_red);
            scoreViews.get(currentIndex).setBackground(drawable);
            failedAnsersnum++;
            failedAnswers.setText("Failed answers: " + failedAnsersnum + " / " + questionBank.length);
        }
    }

    public void addBorderToTheScore() {
        Drawable borde = getResources().getDrawable(R.drawable.round_background_primary);
        for (View cuadrado : scoreViews) {
            cuadrado.setBackground(borde);
        }
    }


    private void resetScore() {
        for (int i = 0; i < scoreViews.size(); i++) {
            scoreViews.get(i).setBackground(getResources().getDrawable(R.drawable.round_background_primary));
        }
    }





    private void startCountdown() {
        resetScore();
        seconds = findViewById(R.id.seconds);
        countDownTime = 5;
        seconds.setText(String.valueOf(countDownTime));
        // Esconder boton de atrás:
        backButton.setVisibility(View.INVISIBLE);
        backButton.setClickable(false);

        runnable = new Runnable() {
            @Override
            public void run() {
                if (countDownTime > 0) {
                    countDownTime--;
                    seconds.setText("Time reamining: " + String.valueOf(countDownTime));
                    handler.postDelayed(this, 1000);
                //Al terminar el contador hacer:
                } else  {
                    currentIndex = 0;
                    backButton.setVisibility(View.VISIBLE);
                    backButton.setClickable(true);
                }
            }
        };
        handler.post(runnable);
    }



}





