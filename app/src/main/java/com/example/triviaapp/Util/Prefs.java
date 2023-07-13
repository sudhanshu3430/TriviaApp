package com.example.triviaapp.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    public static final String HIGHEST_SCORE = "Highest Score";
    public static final String STATE = "CURRENT_STATE";
    private SharedPreferences preferences;

    public Prefs(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);

    }

    public void savedHighestScore(int score){
        int lastscore = preferences.getInt(HIGHEST_SCORE ,0);
      if(score >lastscore) preferences.edit().putInt(HIGHEST_SCORE, score).apply();

    }
    public int getHighestScore(){
        return preferences.getInt(HIGHEST_SCORE,0);
    }

    public void setState(int index){
        preferences.edit().putInt(STATE,index).apply();
    }
    public int getState(){
        return preferences.getInt(STATE,0);
    }
}
