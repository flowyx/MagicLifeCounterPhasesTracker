package com.example.lifecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    //Life
    TextView lifePlayer1;
    TextView lifePlayer2;

    //combat phases set up
    private ImageView upkeepP1;
    private ImageView mainPhaseP1;
    private ImageView combatPhaseP1;
    private ImageView pcMainPhaseP1;
    private ImageView endStepP1;

    private ImageView upkeepP2;
    private ImageView mainPhaseP2;
    private ImageView combatPhaseP2;
    private ImageView pcMainPhaseP2;
    private ImageView endStepP2;

    private int phaseTracker;
    private double phaseTracker2 = 0;

    //dice set up
    private Random rng = new Random();
    private ImageView imageViewDicePlayer1;
    private ImageView imageViewDicePlayer2;
    Button buttonRollDice;


    //Timer variables
    private ProgressBar progressBarP1;
    private ProgressBar progressBarP2;
    private TextView countDownP1Text;
    private TextView countDownP2Text;
    public boolean timerRuns;
    Button button1;
    Button button2;
    Button buttonPause;
    Button buttonReset;
    Button buttonResume;
    Button buttonPassTurnP1;
    Button buttonPassTurnP2;
    ImageButton buttonTimerUp;
    ImageButton buttonTimerDown;
    private int turn = 0;

    private long p1Time;
    public int p1Minutes;
    public int p1Seconds;

    private long p2Time;
    public int p2Seconds;
    private int p2Minutes;

    private long original = 30*60000;

    private CountDownTimer p2Timer;
    private CountDownTimer p1Timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //phases set up
        upkeepP1 = findViewById(R.id.upkeep_p1);
        mainPhaseP1 = findViewById(R.id.main_phase_p1);
        combatPhaseP1 = findViewById(R.id.combat_phase_p1);
        pcMainPhaseP1 = findViewById(R.id.pc_main_phase_p1);
        endStepP1 = findViewById(R.id.end_step_p1);

        upkeepP2 = findViewById(R.id.upkeep_p2);
        mainPhaseP2 = findViewById(R.id.main_phase_p2);
        combatPhaseP2 = findViewById(R.id.combat_phase_p2);
        pcMainPhaseP2 = findViewById(R.id.pc_main_phase_p2);
        endStepP2 = findViewById(R.id.end_step_p2);

        upkeepP1.setAlpha(0.3f);
        mainPhaseP1.setAlpha(0.3f);
        combatPhaseP1.setAlpha(0.3f);
        pcMainPhaseP1.setAlpha(0.3f);
        endStepP1.setAlpha(0.3f);
        upkeepP2.setAlpha(0.3f);
        mainPhaseP2.setAlpha(0.3f);
        combatPhaseP2.setAlpha(0.3f);
        pcMainPhaseP2.setAlpha(0.3f);
        endStepP2.setAlpha(0.3f);


        //don't fall asleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //set up dice
        imageViewDicePlayer1 = findViewById(R.id.image_view_dice_player1);
        imageViewDicePlayer2 = findViewById(R.id.image_view_dice_player2);
        imageViewDicePlayer1.animate().alpha(0f);
        imageViewDicePlayer2.animate().alpha(0f);
        buttonRollDice = findViewById(R.id.button_roll_dice);

        //count down set up
        countDownP1Text = findViewById(R.id.count_down_p1);
        countDownP2Text = findViewById(R.id.count_down_p2);
        String s1 = countDownP1Text.getText().toString();
        String[] countDownP1Array = s1.split( ":");
        p1Minutes = Integer.parseInt(countDownP1Array[0]);
        p1Seconds = Integer.parseInt(countDownP1Array[1]);
        p1Time = p1Minutes * 60000;

        String s2 = countDownP2Text.getText().toString();
        String[] countDownP2Array = s2.split( ":");
        p2Minutes = Integer.parseInt(countDownP1Array[0]);
        p2Seconds = Integer.parseInt(countDownP1Array[1]);
        p2Time = p2Minutes * 60000;

        buttonTimerUp = findViewById(R.id.timerUp);
        buttonTimerDown = findViewById(R.id.timerDown);


        //Progress bar set up
        progressBarP1 = findViewById(R.id.progressBar_Player1);
        progressBarP1.setMax((int) p1Time);
        progressBarP1.setProgress((int) p1Time);
        progressBarP2 = findViewById(R.id.progressBar_Player2);
        progressBarP2.setMax((int) p2Time);
        progressBarP2.setProgress((int) p2Time);

        //set up control buttons
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        buttonPause = findViewById(R.id.button_Pause);
        buttonPause.setVisibility(View.INVISIBLE);
        buttonPause.setEnabled(false);
        button1.setBackgroundColor(Color.parseColor("#0067b3"));
        button2.setBackgroundColor(Color.parseColor("#0067b3"));
        buttonReset = findViewById(R.id.reset_button);
        buttonReset.setEnabled(false);
        buttonReset.setVisibility(View.INVISIBLE);
        buttonResume = findViewById(R.id.button_Resume);
        buttonResume.setVisibility(View.INVISIBLE);
        buttonPassTurnP1 = findViewById(R.id.pass_turn_p1);
        buttonPassTurnP1.setEnabled(false);
        buttonPassTurnP2 = findViewById(R.id.pass_turn_p2);
        buttonPassTurnP2.setEnabled(false);
        buttonPassTurnP1.setBackgroundColor(Color.parseColor("#D3D3D3"));
        buttonPassTurnP1.setTextColor(Color.parseColor("#000000"));
        buttonPassTurnP2.setBackgroundColor(Color.parseColor("#D3D3D3"));
        buttonPassTurnP2.setTextColor(Color.parseColor("#000000"));

        //life set up
        lifePlayer1 = (TextView) findViewById(R.id.lifePlayer1);
        lifePlayer2 = (TextView) findViewById(R.id.lifePlayer2);


        //Starts countdown for player1  (When player 2 clicks)
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonReset.setVisibility(View.VISIBLE);
                buttonReset.setEnabled(true);
                buttonTimerDown.setVisibility(View.INVISIBLE);
                buttonTimerUp.setVisibility(View.INVISIBLE);
                //if player 1 starts do the following
                if (turn == 0){
                    button2.setText("END PHASE");
                    button1.setText("END PHASE");
                    phaseTracker2 = 1;
                    proceedPhase2();
                    buttonPassTurnP2.setEnabled(false);
                    buttonPassTurnP1.setEnabled(true);
                    buttonPassTurnP1.setTextColor(Color.parseColor("#ffffff"));
                    buttonPassTurnP1.setBackgroundColor(Color.parseColor("#6d00c1"));
                    buttonPassTurnP2.setTextColor(Color.parseColor("#000000"));
                    buttonPassTurnP2.setBackgroundColor(Color.parseColor("#D3D3D3"));

                }
                //if game is going on do the following
                else {
                    phaseTracker2 = phaseTracker2 + 0.5;
                    proceedPhase2();
                }
                //if turn player 2
                if (turn != 0 && phaseTracker2 >= 5){
                    buttonPassTurnP2.setEnabled(false);
                    //buttonPassTurnP1.setEnabled(true);
                    //buttonPassTurnP1.setTextColor(Color.parseColor("#ffffff"));
                    //buttonPassTurnP1.setBackgroundColor(Color.parseColor("#6d00c1"));
                    buttonPassTurnP2.setTextColor(Color.parseColor("#000000"));
                    buttonPassTurnP2.setBackgroundColor(Color.parseColor("#D3D3D3"));
                }
                //if turn player 1
                if (turn != 0 && phaseTracker2 < 5){
                    buttonPassTurnP1.setEnabled(true);
                    buttonPassTurnP1.setTextColor(Color.parseColor("#ffffff"));
                    buttonPassTurnP1.setBackgroundColor(Color.parseColor("#6d00c1"));
                }

                    //switch button activity
                    button2.setEnabled(false);
                    button1.setEnabled(true);
                    buttonRollDice.setVisibility(View.INVISIBLE);
                    buttonPause.setEnabled(true);
                    buttonPause.setVisibility(View.VISIBLE);

                    button1.setTextColor(Color.parseColor("#ffffff"));
                    button1.setBackgroundColor(Color.parseColor("#6d00c1"));
                    button2.setTextColor(Color.parseColor("#000000"));
                    button2.setBackgroundColor(Color.parseColor("#D3D3D3"));

                    //start timer
                    if (turn == 0 || turn == 2) {

                        p1Timer = new CountDownTimer(p1Time, 1) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if (turn != 1) {    //No longer player 1's turn, stop timer
                                    cancel();
                                } else {    //Otherwise, deduct time

                                    p1Time = (millisUntilFinished - 1);
                                    countDownP1Text.setText(updateText(p1Time));
                                    progressBarP1.setProgress((int) p1Time);
                                    //button1.setText(updateText(p1Time));
                                }
                            }

                            @Override
                            public void onFinish() {
                                button1.setBackgroundColor(Color.parseColor("#C0392B"));
                                button1.setTextColor(Color.parseColor("#ffffff"));
                                //button1.setBackgroundResource(R.drawable.button_timeout);
                                //timeGone();
                            }
                        }.start();
                    }

                    if (turn == 2) {
                        p2Timer.cancel();
                        countDownP2Text.setText(updateText(p2Time));
                        progressBarP2.setProgress((int) p2Time);
                        //button2.setText(updateText(p2Time));
                    }

                    turn = 1;

            }
        });

        //Starts countdown for player2  (When player 1 clicks)
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonReset.setVisibility(View.VISIBLE);
                buttonReset.setEnabled(true);
                buttonTimerDown.setVisibility(View.INVISIBLE);
                buttonTimerUp.setVisibility(View.INVISIBLE);
                //if player 2 starts do the following
                if (turn == 0){
                    button2.setText("END PHASE");
                    button1.setText("END PHASE");
                    phaseTracker2 = 4.5;
                    proceedPhase2();
                    buttonPassTurnP1.setEnabled(false);
                    buttonPassTurnP2.setEnabled(true);
                    buttonPassTurnP2.setTextColor(Color.parseColor("#ffffff"));
                    buttonPassTurnP2.setBackgroundColor(Color.parseColor("#6d00c1"));
                    buttonPassTurnP1.setTextColor(Color.parseColor("#000000"));
                    buttonPassTurnP1.setBackgroundColor(Color.parseColor("#D3D3D3"));
                }
                //if game is going on do the following
                else {
                    phaseTracker2 = phaseTracker2 + 0.5;
                    proceedPhase2();
                }
                //if turn player 1
                if (turn != 0 && phaseTracker2 < 5){
                    buttonPassTurnP1.setEnabled(false);
                    //buttonPassTurnP2.setEnabled(true);
                    //buttonPassTurnP2.setTextColor(Color.parseColor("#ffffff"));
                    //buttonPassTurnP2.setBackgroundColor(Color.parseColor("#6d00c1"));
                    buttonPassTurnP1.setTextColor(Color.parseColor("#000000"));
                    buttonPassTurnP1.setBackgroundColor(Color.parseColor("#D3D3D3"));
                }
                //if turn player 2
                if (turn != 0 && phaseTracker2 >= 5){
                    buttonPassTurnP2.setEnabled(true);
                    buttonPassTurnP2.setTextColor(Color.parseColor("#ffffff"));
                    buttonPassTurnP2.setBackgroundColor(Color.parseColor("#6d00c1"));
                }

                if (turn != 0 && phaseTracker2 == 5.5){
                    buttonPassTurnP1.setEnabled(true);
                    buttonPassTurnP1.setTextColor(Color.parseColor("#ffffff"));
                    buttonPassTurnP1.setBackgroundColor(Color.parseColor("#6d00c1"));
                }
                    button1.setEnabled(false);
                    button2.setEnabled(true);
                    buttonRollDice.setVisibility(View.INVISIBLE);
                    buttonPause.setVisibility(View.VISIBLE);
                    buttonPause.setEnabled(true);

                    button2.setTextColor(Color.parseColor("#ffffff"));
                    button2.setBackgroundColor(Color.parseColor("#6d00c1"));
                    button1.setTextColor(Color.parseColor("#000000"));
                    button1.setBackgroundColor(Color.parseColor("#D3D3D3"));
                    //start timer
                    if (turn == 1 || turn == 0) {

                        p2Timer = new CountDownTimer(p2Time, 1) {
                            @Override
                            public void onTick(long millisUntilFinished) {


                                if (turn != 2) {    //No longer player 2's turn, stop timer
                                    cancel();
                                } else {    //Otherwise, deduct time

                                    p2Time = (millisUntilFinished - 1);
                                    countDownP2Text.setText(updateText(p2Time));
                                    progressBarP2.setProgress((int) p2Time);
                                    //button2.setText(updateText(p2Time));
                                }
                            }

                            @Override
                            public void onFinish() {
                                button2.setBackgroundColor(Color.parseColor("#C0392B"));
                                button2.setTextColor(Color.parseColor("#ffffff"));
                                //button2.setBackgroundResource(R.drawable.button_timeout);
                                //timeGone();
                            }
                        }.start();
                    }

                    if (turn == 1) {
                        p1Timer.cancel();
                        turn = 2;
                        countDownP1Text.setText(updateText(p1Time));
                        progressBarP1.setProgress((int) p1Time);
                        //button1.setText(updateText(p1Time));
                    }

                    turn = 2;


            }
        });


        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                p1Timer.cancel();
                p2Timer.cancel();
                //Sets the button orange (indicates which player's turn it still is)
                if (turn == 1) {
                    button1.setBackgroundColor(Color.parseColor("#F9AE34"));
                    button1.setTextColor(Color.parseColor("#ffffff"));
                } else if (turn == 2) {
                    button2.setBackgroundColor(Color.parseColor("#F9AE34"));
                    button2.setTextColor(Color.parseColor("#ffffff"));
                }
                buttonPause.setVisibility(View.INVISIBLE);
                buttonResume.setVisibility(View.VISIBLE);
            }
        });

        buttonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (turn == 1){
                    button1.setTextColor(Color.parseColor("#ffffff"));
                    button1.setBackgroundColor(Color.parseColor("#6d00c1"));
                    p1Timer = new CountDownTimer(p1Time, 1) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (turn != 1) {    //No longer player 1's turn, stop timer
                                cancel();
                            } else {    //Otherwise, deduct time

                                p1Time = (millisUntilFinished - 1);
                                countDownP1Text.setText(updateText(p1Time));
                                progressBarP1.setProgress((int) p1Time);
                                //button1.setText(updateText(p1Time));
                            }
                        }

                        @Override
                        public void onFinish() {
                            button1.setBackgroundColor(Color.parseColor("#C0392B"));
                            button1.setTextColor(Color.parseColor("#ffffff"));
                            //button1.setBackgroundResource(R.drawable.button_timeout);
                            //timeGone();
                        }
                    }.start();
                }
                if (turn == 2){
                    button2.setTextColor(Color.parseColor("#ffffff"));
                    button2.setBackgroundColor(Color.parseColor("#6d00c1"));
                    p2Timer = new CountDownTimer(p2Time, 1) {
                        @Override
                        public void onTick(long millisUntilFinished) {


                            if (turn != 2) {    //No longer player 2's turn, stop timer
                                cancel();
                            } else {    //Otherwise, deduct time

                                p2Time = (millisUntilFinished - 1);
                                countDownP2Text.setText(updateText(p2Time));
                                progressBarP2.setProgress((int) p2Time);
                                //button2.setText(updateText(p2Time));
                            }
                        }

                        @Override
                        public void onFinish() {
                            button2.setBackgroundColor(Color.parseColor("#C0392B"));
                            button2.setTextColor(Color.parseColor("#ffffff"));
                            //button2.setBackgroundResource(R.drawable.button_timeout);
                            //timeGone();
                        }
                    }.start();
                }
                buttonPause.setVisibility(View.VISIBLE);
                buttonResume.setVisibility(View.INVISIBLE);
            }
        });
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

    public MainActivity(){

        timerRuns = false;

    }

    public void rollDice(View view){
        //hides phases and shows dice
        hidePhases(view);
        imageViewDicePlayer1.animate().alpha(1f).setDuration(1000);
        imageViewDicePlayer2.animate().alpha(1f).setDuration(1000);

        int dicePlayer1 = 0;
        int dicePlayer2 = 0;
        //randomized until dice show different values
        while(dicePlayer1 == dicePlayer2){
            dicePlayer1 = rng.nextInt(6) + 1;
            dicePlayer2 = rng.nextInt(6) + 1;
        }

        switch (dicePlayer1){
            case 1:
                imageViewDicePlayer1.setImageResource(R.drawable.dice1);
                break;
            case 2:
                imageViewDicePlayer1.setImageResource(R.drawable.dice2);
                break;
            case 3:
                imageViewDicePlayer1.setImageResource(R.drawable.dice3);
                break;
            case 4:
                imageViewDicePlayer1.setImageResource(R.drawable.dice4);
                break;
            case 5:
                imageViewDicePlayer1.setImageResource(R.drawable.dice5);
                break;
            case 6:
                imageViewDicePlayer1.setImageResource(R.drawable.dice6);
                break;
        }

        switch (dicePlayer2){
            case 1:
                imageViewDicePlayer2.setImageResource(R.drawable.dice1);
                break;
            case 2:
                imageViewDicePlayer2.setImageResource(R.drawable.dice2);
                break;
            case 3:
                imageViewDicePlayer2.setImageResource(R.drawable.dice3);
                break;
            case 4:
                imageViewDicePlayer2.setImageResource(R.drawable.dice4);
                break;
            case 5:
                imageViewDicePlayer2.setImageResource(R.drawable.dice5);
                break;
            case 6:
                imageViewDicePlayer2.setImageResource(R.drawable.dice6);
                break;
        }
        //animation to switch between dice and phases
        new CountDownTimer(3000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                imageViewDicePlayer1.animate().alpha(0f).setDuration(2000);
                imageViewDicePlayer2.animate().alpha(0f).setDuration(2000);
                displayPhases(view);

            }
        }.start();
    }

    public void hidePhases(View view){

        upkeepP1.animate().alpha(0f).setDuration(1000);
        mainPhaseP1.animate().alpha(0f).setDuration(1000);
        combatPhaseP1.animate().alpha(0f).setDuration(1000);
        pcMainPhaseP1.animate().alpha(0f).setDuration(1000);
        endStepP1.animate().alpha(0f).setDuration(1000);
        upkeepP2.animate().alpha(0f).setDuration(1000);
        mainPhaseP2.animate().alpha(0f).setDuration(1000);
        combatPhaseP2.animate().alpha(0f).setDuration(1000);
        pcMainPhaseP2.animate().alpha(0f).setDuration(1000);
        endStepP2.animate().alpha(0f).setDuration(1000);
    }

    public void displayPhases(View view){

        upkeepP1.animate().alpha(0.3f).setDuration(2500);
        mainPhaseP1.animate().alpha(0.3f).setDuration(2500);
        combatPhaseP1.animate().alpha(0.3f).setDuration(2500);
        pcMainPhaseP1.animate().alpha(0.3f).setDuration(2500);
        endStepP1.animate().alpha(0.3f).setDuration(2500);
        upkeepP2.animate().alpha(0.3f).setDuration(2500);
        mainPhaseP2.animate().alpha(0.3f).setDuration(2500);
        combatPhaseP2.animate().alpha(0.3f).setDuration(2500);
        pcMainPhaseP2.animate().alpha(0.3f).setDuration(2500);
        endStepP2.animate().alpha(0.3f).setDuration(2500);
    }
    //is only available while one player is on turn and during phase
    public void passTurn(View view){
        if (phaseTracker2 < 5){
            phaseTracker2 = 4.5;
            proceedPhase2();
            turn = 2;
            button1.setEnabled(false);
            button2.setEnabled(true);
            buttonRollDice.setVisibility(View.INVISIBLE);
            buttonPause.setVisibility(View.VISIBLE);
            buttonPause.setEnabled(true);
            button2.setTextColor(Color.parseColor("#ffffff"));
            button2.setBackgroundColor(Color.parseColor("#6d00c1"));
            button1.setTextColor(Color.parseColor("#000000"));
            button1.setBackgroundColor(Color.parseColor("#D3D3D3"));
            buttonPassTurnP1.setEnabled(false);
            buttonPassTurnP1.setTextColor(Color.parseColor("#000000"));
            buttonPassTurnP1.setBackgroundColor(Color.parseColor("#D3D3D3"));
        }
        if (phaseTracker2 >=5){
            phaseTracker2 = 7.5;
            proceedPhase2();
            turn = 1;
            button2.setEnabled(false);
            button1.setEnabled(true);
            buttonRollDice.setVisibility(View.INVISIBLE);
            buttonPause.setEnabled(true);
            buttonPause.setVisibility(View.VISIBLE);
            button1.setTextColor(Color.parseColor("#ffffff"));
            button1.setBackgroundColor(Color.parseColor("#6d00c1"));
            button2.setTextColor(Color.parseColor("#000000"));
            button2.setBackgroundColor(Color.parseColor("#D3D3D3"));
            buttonPassTurnP2.setEnabled(false);
            buttonPassTurnP2.setTextColor(Color.parseColor("#000000"));
            buttonPassTurnP2.setBackgroundColor(Color.parseColor("#D3D3D3"));
        }
    }

    public void proceedPhase2(){

        //player 2 starts timer for player 1 and proceeds to upkeep player 1
        if (turn == 0 && phaseTracker2 == 1){
            upkeepP1.setAlpha(1f);
        }
        //proceed to main phase player 1
        if (turn == 2 && phaseTracker2 == 2){
            upkeepP1.setAlpha(0.3f);
            mainPhaseP1.setAlpha(1f);
        }
        //proceed to combat phase player 1
        if (turn == 2 && phaseTracker2 == 3){
            mainPhaseP1.setAlpha(0.3f);
            combatPhaseP1.setAlpha(1f);
        }
        //proceed to post combat main phase player 1
        if (turn == 2 && phaseTracker2 == 4){
            combatPhaseP1.setAlpha(0.3f);
            pcMainPhaseP1.setAlpha(1f);
        }
        //proceed to end step player 1
        if (turn == 1 && phaseTracker2 == 4.5){
            upkeepP1.setAlpha(0.3f);
            mainPhaseP1.setAlpha(0.3f);
            combatPhaseP1.setAlpha(0.3f);
            pcMainPhaseP1.setAlpha(1f);
            endStepP1.setAlpha(1f);
        }
        //proceed to upkeep player 2
        if (turn == 2 && phaseTracker2 == 5){
            pcMainPhaseP1.setAlpha(0.3f);
            endStepP1.setAlpha(0.3f);
            upkeepP2.setAlpha(1f);
        }
        //player 1 starts timer and proceeds to upkeep player 2
        if (turn == 0 && phaseTracker2 == 4.5){
            upkeepP2.setAlpha(1f);
        }
        //proceed to mainPhaseP2
        if (turn == 1 && phaseTracker2 == 5.5){
            mainPhaseP2.setAlpha(1f);
            upkeepP2.setAlpha(0.3f);
            phaseTracker2 = phaseTracker2 - 0.5;
        }
        //proceed to combat phase player 2
        if (turn == 1 && phaseTracker2 == 6){
            mainPhaseP2.setAlpha(0.3f);
            combatPhaseP2.setAlpha(1f);
        }
        //proceed to post combat main phase player 2
        if (turn == 1 && phaseTracker2 == 7){
            combatPhaseP2.setAlpha(0.3f);
            pcMainPhaseP2.setAlpha(1f);
        }
        //proceed to end step player 2
        if (turn == 2 && phaseTracker2 == 7.5){
            upkeepP2.setAlpha(0.3f);
            mainPhaseP2.setAlpha(0.3f);
            combatPhaseP2.setAlpha(0.3f);
            pcMainPhaseP2.setAlpha(1f);
            endStepP2.setAlpha(1f);
        }
        //proceed to upkeep player 1
        if (turn == 1 && phaseTracker2 == 8){
            pcMainPhaseP2.setAlpha(0.3f);
            endStepP2.setAlpha(0.3f);
            upkeepP1.setAlpha(1f);
            phaseTracker2 = 1.5;
        }

    }

    public void reset(View view){

        p1Timer.cancel();
        p2Timer.cancel();

        progressBarP1.setProgress((int) original);
        progressBarP2.setProgress((int) original);
        p1Time = original;
        p2Time = original;
        countDownP1Text.setText(updateText(p1Time));
        countDownP2Text.setText(updateText(p2Time));


        lifePlayer1.setText("20");
        lifePlayer2.setText("20");

        turn = 0;
        upkeepP1.setAlpha(0.3f);
        mainPhaseP1.setAlpha(0.3f);
        combatPhaseP1.setAlpha(0.3f);
        pcMainPhaseP1.setAlpha(0.3f);
        endStepP1.setAlpha(0.3f);
        upkeepP2.setAlpha(0.3f);
        mainPhaseP2.setAlpha(0.3f);
        combatPhaseP2.setAlpha(0.3f);
        pcMainPhaseP2.setAlpha(0.3f);
        endStepP2.setAlpha(0.3f);

        button1.setEnabled(true);
        button2.setEnabled(true);
        button1.setBackgroundColor(Color.parseColor("#0067b3"));
        button1.setTextColor(Color.parseColor("#ffffff"));
        button2.setBackgroundColor(Color.parseColor("#0067b3"));
        button2.setTextColor(Color.parseColor("#ffffff"));
        buttonPassTurnP1.setEnabled(false);
        buttonPassTurnP2.setEnabled(false);
        buttonPassTurnP1.setBackgroundColor(Color.parseColor("#D3D3D3"));
        buttonPassTurnP1.setTextColor(Color.parseColor("#000000"));
        buttonPassTurnP2.setBackgroundColor(Color.parseColor("#D3D3D3"));
        buttonPassTurnP2.setTextColor(Color.parseColor("#000000"));

        buttonPause.setVisibility(View.INVISIBLE);
        buttonRollDice.setVisibility(View.VISIBLE);

        buttonResume.setVisibility(View.INVISIBLE);

        buttonReset.setEnabled(false);
        buttonReset.setVisibility(View.INVISIBLE);

        buttonTimerDown.setVisibility(View.VISIBLE);
        buttonTimerUp.setVisibility(View.VISIBLE);
    }
    //up and down by 1 minute for both players
    public void player1TimeUp(View view){
        p1Time = p1Time + 60000;
        p2Time = p2Time + 60000;
        countDownP1Text.setText(updateText(p1Time));
        countDownP2Text.setText(updateText(p2Time));
        progressBarP1.setMax((int) p1Time);
        progressBarP2.setMax((int) p2Time);
        progressBarP1.setProgress((int) p1Time);
        progressBarP2.setProgress((int) p2Time);
    }
    public void player1TimeDown(View view){
        p1Time = p1Time - 60000;
        p2Time = p2Time - 60000;
        countDownP1Text.setText(updateText(p1Time));
        countDownP2Text.setText(updateText(p2Time));
        progressBarP1.setMax((int) p1Time);
        progressBarP2.setMax((int) p2Time);
        progressBarP1.setProgress((int) p1Time);
        progressBarP2.setProgress((int) p2Time);
    }

    public void player1LifeUp(View view){

        TextView player1LifeTextView = (TextView) findViewById(R.id.lifePlayer1);

        Integer player1Life = Integer.parseInt(player1LifeTextView.getText().toString());

        player1Life++;

        player1LifeTextView.setText(player1Life.toString());

    }

    public void player1LifeDown(View view){

        TextView player1LifeTextView = (TextView) findViewById(R.id.lifePlayer1);

        Integer player1Life = Integer.parseInt(player1LifeTextView.getText().toString());

        player1Life = player1Life-1;

        player1LifeTextView.setText(player1Life.toString());

    }

    public void player2LifeUp(View view){

        TextView player2LifeTextView = (TextView) findViewById(R.id.lifePlayer2);

        Integer player2Life = Integer.parseInt(player2LifeTextView.getText().toString());

        player2Life++;

        player2LifeTextView.setText(player2Life.toString());

    }

    public void player2LifeDown(View view){

        TextView player2LifeTextView = (TextView) findViewById(R.id.lifePlayer2);

        Integer player2Life = Integer.parseInt(player2LifeTextView.getText().toString());

        player2Life = player2Life-1;

        player2LifeTextView.setText(player2Life.toString());

    }

}