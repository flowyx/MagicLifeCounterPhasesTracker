package com.example.lifecounter;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Player {

    //combat phases set up
    private ImageView upkeep;
    private ImageView mainPhase;
    private ImageView combatPhase;
    private ImageView pcMainPhase;
    private ImageView endStep;

    //Timer variables
    private ProgressBar progressBar;

    //count down Text
    private TextView countDownText;

    //Life
    private TextView lifeText;
    private int life;

    private Button buttonNextPhase;
    private Button buttonPassTurn;

    private long time;
    private long original = 30*60000;

    private CountDownTimer timer;

    private int playerID;

    public Player(int ID, ImageView[] images, Button[] buttons, TextView[] textViews, ProgressBar progress){
        time = original;

        upkeep = images[0];
        mainPhase = images[1];
        combatPhase = images[2];
        pcMainPhase = images[3];
        endStep = images[4];

        buttonNextPhase = buttons[0];
        buttonPassTurn = buttons[1];

        lifeText = textViews[0];
        countDownText = textViews[1];

        progressBar = progress;
        progressBar.setMax((int) time);
        progressBar.setProgress((int) time);
        countDownText.setText(updateText(time));
        playerID = ID;

        //set initial image state alphas
        upkeep.setAlpha(0.3f);
        mainPhase.setAlpha(0.3f);
        combatPhase.setAlpha(0.3f);
        pcMainPhase.setAlpha(0.3f);
        endStep.setAlpha(0.3f);

    }

    public void startTimer(View view){
        timer = new CountDownTimer(time, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                    time = (millisUntilFinished - 1);
                    countDownText.setText(updateText(time));
                    progressBar.setProgress((int) time);
            }
            @Override
            public void onFinish() {
                buttonNextPhase.setBackgroundColor(Color.parseColor("#C0392B"));
                buttonNextPhase.setTextColor(Color.parseColor("#ffffff"));
            }
        }.start();
    }

    public void resume(){
            buttonNextPhase.setTextColor(Color.parseColor("#ffffff"));
            buttonNextPhase.setBackgroundColor(Color.parseColor("#6d00c1"));
            timer = new CountDownTimer(time, 1) {
                @Override
                public void onTick(long millisUntilFinished) {
                        time = (millisUntilFinished - 1);
                        //player1.getCountDownText().setText(player1.updateText(player1.getTime(v)));
                        countDownText.setText(updateText(time));
                        progressBar.setProgress((int) time);
                }
                @Override
                public void onFinish() {
                    buttonNextPhase.setBackgroundColor(Color.parseColor("#C0392B"));
                    buttonNextPhase.setTextColor(Color.parseColor("#ffffff"));
                }
            }.start();
    }

    public void pause(){
        timer.cancel();
        buttonNextPhase.setBackgroundColor(Color.parseColor("#F9AE34"));
        buttonNextPhase.setTextColor(Color.parseColor("#ffffff"));
    }


    public void cancelTimer(View view){
        timer.cancel();
        countDownText.setText(updateText(time));
        progressBar.setProgress((int) time);
    }

    public void reset(View view){
        //if no timer object exists do nothing
        try {
            timer.cancel();
        }
        catch (Exception e){

        }
        progressBar.setMax((int) original);
        progressBar.setProgress((int) original);
        time = original;
        countDownText.setText(updateText(time));

        lifeText.setText("20");

        upkeep.setAlpha(0.3f);
        mainPhase.setAlpha(0.3f);
        combatPhase.setAlpha(0.3f);
        pcMainPhase.setAlpha(0.3f);
        endStep.setAlpha(0.3f);

        buttonNextPhase.setEnabled(true);
        buttonNextPhase.setBackgroundColor(Color.parseColor("#0067b3"));
        buttonNextPhase.setTextColor(Color.parseColor("#ffffff"));

        if (playerID == 1){
            buttonNextPhase.setText("START P2");
        }
        else {
            buttonNextPhase.setText("START P1");
        }
        buttonPassTurn.setEnabled(false);
        buttonPassTurn.setBackgroundColor(Color.parseColor("#D3D3D3"));
        buttonPassTurn.setTextColor(Color.parseColor("#000000"));
    }

    public void disableButtons(View view){
        buttonPassTurn.setEnabled(false);
        buttonPassTurn.setTextColor(Color.parseColor("#000000"));
        buttonPassTurn.setBackgroundColor(Color.parseColor("#D3D3D3"));
        buttonNextPhase.setEnabled(false);
        buttonNextPhase.setTextColor(Color.parseColor("#000000"));
        buttonNextPhase.setBackgroundColor(Color.parseColor("#D3D3D3"));
    }

    public void enableButtons(View view, double phaseTracker){
        if ((playerID == 1 && (phaseTracker < 5 && phaseTracker > 1)) || (playerID == 2 && phaseTracker >= 5)) {
                buttonPassTurn.setEnabled(true);
                buttonPassTurn.setTextColor(Color.parseColor("#ffffff"));
                buttonPassTurn.setBackgroundColor(Color.parseColor("#6d00c1"));
        }
        buttonNextPhase.setEnabled(true);
        buttonNextPhase.setTextColor(Color.parseColor("#ffffff"));
        buttonNextPhase.setBackgroundColor(Color.parseColor("#6d00c1"));
    }

    public void lifeUp(View view){
        Integer life = Integer.parseInt(lifeText.getText().toString());
        life++;
        lifeText.setText(life.toString());
    }
    public void lifeDown(View view){
        Integer life = Integer.parseInt(lifeText.getText().toString());
        life--;
        lifeText.setText(life.toString());
    }

    public String updateText(long milliseconds) {
        if (milliseconds >= 3600000) {   //Greater than 1 hour and less than 24
            long hours = milliseconds / 3600000;
            long minutes = (milliseconds / 60000) - (hours * 60);
            long seconds = (milliseconds / 1000) - (hours * 3600) - (minutes * 60);

            if (minutes >= 10 && seconds >= 10)
                return ("" + hours + ":" + minutes + ":" + seconds);
            else if (minutes < 10 && seconds >= 10)
                return ("" + hours + ":0" + minutes + ":" + seconds);
            else if (minutes >= 10)
                return ("" + hours + ":" + minutes + ":0" + seconds);
            else
                return ("" + hours + ":0" + minutes + ":0" + seconds);

        } else if (milliseconds >= 60000) {     //Greater than one minute and less than 1 hour
            long minutes = milliseconds / 60000;
            long seconds = (milliseconds/1000) - (minutes * 60);

            if (seconds >= 10)
                return ("" + minutes + ":" + seconds);
            else {
                return ("" + minutes + ":0" + seconds);
            }
        } else {  //Less than one minute
            long seconds = milliseconds / 1000;
            long decisecond = (milliseconds / 100) - (seconds * 10);
            return ("" + seconds + "." + decisecond);
        }
    }

    //setter methods
    public void setTime2(boolean isUp){
        time = isUp? time + 60000: time - 60000;
        countDownText.setText(updateText(time));
        progressBar.setMax((int) time);
        progressBar.setProgress((int) time);
    }

}

