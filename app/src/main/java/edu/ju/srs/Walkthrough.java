package edu.ju.srs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.awalkthrough.AWalkthroughActivity;
import com.example.awalkthrough.AWalkthroughCard;

import java.util.ArrayList;
import java.util.List;

public class Walkthrough extends AWalkthroughActivity {
    String title[]={"My Grades","If you like My grades"};
    String detail[]={"Academic status on the GO!!","RATE 5 STARS"};
     int drawable[]={R.drawable.easy,R.drawable.rate};
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences=getSharedPreferences("SRS",0);
        if(preferences.getBoolean("start",false)){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            return;
        }
        List<AWalkthroughCard> pages=new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            AWalkthroughCard card=new AWalkthroughCard(title[i],detail[i],drawable[i]);
            pages.add(card);
        }
        setColorBackground(R.color.colorPrimary);
        setOnboardPages(pages);
        setFinishButtonTitle("Get Started");
    }
    @Override
    public void onFinishButtonPressed() {
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("start",true);
        editor.apply();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
