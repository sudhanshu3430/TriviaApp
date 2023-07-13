package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.triviaapp.Util.Prefs;
import com.example.triviaapp.data.Repository;
import com.example.triviaapp.databinding.ActivityMainBinding;
import com.example.triviaapp.model.Question;
import com.example.triviaapp.model.Score;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentQuestionIndex = 0;
    List<Question> questionList;

    private int scorecount = 0;
    private Score score;
    private Prefs prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         score = new Score();
         prefs = new Prefs(MainActivity.this);
         currentQuestionIndex = prefs.getState();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.highetScore.setText("HighestScore: "+prefs.getHighestScore());

        questionList = new Repository().getQuestion(questionArrayList ->
                binding.questionTextview.setText( questionArrayList.get(currentQuestionIndex).getAnswer()));

        binding.buttonNext.setOnClickListener(v -> {
            getNextQuestion();

        });
        binding.buttonTrue.setOnClickListener(v -> {
            checkanswer(true);
            updateCurrentQuestion();

        });

        binding.buttonFalse.setOnClickListener(v -> {
            checkanswer(false);
            updateCurrentQuestion();

        });
    }

    private void getNextQuestion() {
        currentQuestionIndex = (currentQuestionIndex+1) % questionList.size();
        updateCurrentQuestion();
    }

    private void checkanswer(boolean userChoseCorrect) {
        boolean answer = questionList.get(currentQuestionIndex).isAnsTrue();
        int snackmsg = 0;
        if(userChoseCorrect == answer){
            snackmsg = R.string.correct_answer;
            fadeAnimation();
            addpoints();

        }
        else{
            snackmsg = R.string.incorrect_answer;
            shakeAnimation();
            deductpoints();
        }
        Snackbar.make(binding.cardView, snackmsg, Snackbar.LENGTH_SHORT).show();

    }

    private void updateCounter(ArrayList<Question> questionArrayList) {
        binding.questionOutOf.setText(String.format(getString(R.string.text_formatted), currentQuestionIndex, questionArrayList.size()));
    }
    private void fadeAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.GREEN);


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
                getNextQuestion();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void updateCurrentQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        binding.questionTextview.setText(question);
        updateCounter((ArrayList<Question>) questionList);


    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        binding.cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
                getNextQuestion();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void addpoints(){
        scorecount += 100;
        score.setScore(scorecount);
        binding.scoreText.setText("Score: "+score.getScore());

    }
    private void deductpoints(){

        if(scorecount>0){
            scorecount -= 100;
           score.setScore(scorecount);
            binding.scoreText.setText("Score: "+score.getScore());
        }
        else{
            scorecount = 0;
            score.setScore(scorecount);

        }

    }

    @Override
    protected void onPause() {
        prefs.savedHighestScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }
}