package com.example.lifecounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

    private double phaseTracker = 0;

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
    Button buttonNextPhase1;
    Button buttonNextPhase2;
    Button buttonPassTurnP1;
    Button buttonPassTurnP2;
    Button buttonPause;
    Button buttonReset;
    Button buttonResume;

    ImageButton buttonTimerUp;
    ImageButton buttonTimerDown;
    private int turn = 0;

    public Player player1;
    public Player player2;

    ImageView[] imageViewsP1;
    ImageView[] imageViewsP2;

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

        imageViewsP1 = new ImageView[5];
        imageViewsP1[0] = upkeepP1;
        imageViewsP1[1] = mainPhaseP1;
        imageViewsP1[2] = combatPhaseP1;
        imageViewsP1[3] = pcMainPhaseP1;
        imageViewsP1[4] = endStepP1;

        upkeepP2 = findViewById(R.id.upkeep_p2);
        mainPhaseP2 = findViewById(R.id.main_phase_p2);
        combatPhaseP2 = findViewById(R.id.combat_phase_p2);
        pcMainPhaseP2 = findViewById(R.id.pc_main_phase_p2);
        endStepP2 = findViewById(R.id.end_step_p2);

        imageViewsP2 = new ImageView[5];
        imageViewsP2[0] = upkeepP2;
        imageViewsP2[1] = mainPhaseP2;
        imageViewsP2[2] = combatPhaseP2;
        imageViewsP2[3] = pcMainPhaseP2;
        imageViewsP2[4] = endStepP2;

        //don't fall asleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //no dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //set up dice
        imageViewDicePlayer1 = findViewById(R.id.image_view_dice_player1);
        imageViewDicePlayer2 = findViewById(R.id.image_view_dice_player2);
        imageViewDicePlayer1.setAlpha(0f);
        imageViewDicePlayer2.setAlpha(0f);
        buttonRollDice = findViewById(R.id.button_roll_dice);

        //count down set up
        countDownP1Text = findViewById(R.id.count_down_p1);
        countDownP2Text = findViewById(R.id.count_down_p2);

        //--> findViewElementsById()
        buttonTimerUp = findViewById(R.id.timerUp);
        buttonTimerDown = findViewById(R.id.timerDown);

        //Progress bar set up
        progressBarP1 = findViewById(R.id.progressBar_Player1);
        progressBarP2 = findViewById(R.id.progressBar_Player2);

        //set up control buttons
        buttonNextPhase1 = findViewById(R.id.button1);
        buttonNextPhase2 = findViewById(R.id.button2);
        buttonNextPhase1.setBackgroundColor(Color.parseColor("#0067b3"));
        buttonNextPhase2.setBackgroundColor(Color.parseColor("#0067b3"));
        buttonPause = findViewById(R.id.button_Pause);
        buttonPause.setVisibility(View.INVISIBLE);
        buttonPause.setEnabled(false);
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

        Button[] buttonsP1 = new Button[2];
        buttonsP1[0] = buttonNextPhase1;
        buttonsP1[1] = buttonPassTurnP1;

        Button[] buttonsP2 = new Button[2];
        buttonsP2[0] = buttonNextPhase2;
        buttonsP2[1] = buttonPassTurnP2;

        //life set up
        lifePlayer1 = (TextView) findViewById(R.id.lifePlayer1);
        lifePlayer2 = (TextView) findViewById(R.id.lifePlayer2);

        TextView[] textViewsP1 = new TextView[2];
        textViewsP1[0] = lifePlayer1;
        textViewsP1[1] = countDownP1Text;

        TextView[] textViewsP2 = new TextView[2];
        textViewsP2[0] = lifePlayer2;
        textViewsP2[1] = countDownP2Text;

        player1 = new Player(1, imageViewsP1, buttonsP1, textViewsP1, progressBarP1);
        player2 = new Player(2, imageViewsP2, buttonsP2, textViewsP2, progressBarP2);

        //Starts countdown for player1  (When player 2 clicks)
        buttonNextPhase2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareBattlefieldOnStart(v);
                //if player 1 starts do the following
                if (turn == 0){
                    buttonNextPhase2.setText("END PHASE");
                    buttonNextPhase1.setText("END PHASE");
                    phaseTracker = 1;
                }
                //if game is going on do the following
                else {
                    prepareBattlefieldOnResume(v);
                    phaseTracker = phaseTracker + 0.5;
                }
                proceedPhase();
                player2.disableButtons(v);
                player1.enableButtons(v, phaseTracker);

                    //start timer
                    if (turn % 2 == 0) {
                        player1.startTimer(v);
                    }
                    if (turn == 2) {
                        player2.cancelTimer(v);
                    }
                    turn = 1;
            }
        });

        //Starts countdown for player2  (When player 1 clicks)
        buttonNextPhase1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareBattlefieldOnStart(v);
                //if player 2 starts do the following
                if (turn == 0){
                    buttonNextPhase2.setText("END PHASE");
                    buttonNextPhase1.setText("END PHASE");
                    phaseTracker = 4.5;
                }
                //if game is going on do the following
                else {
                    prepareBattlefieldOnResume(v);
                    phaseTracker = phaseTracker + 0.5;
                }
                proceedPhase();
                player1.disableButtons(v);
                player2.enableButtons(v, phaseTracker);

                    //start timer
                    if (turn == 1 || turn == 0) {
                        player2.startTimer(v);
                    }
                    if (turn == 1) {
                        player1.cancelTimer(v);
                    }
                    turn = 2;
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (turn == 1){
                    player1.pause();
                }
                if (turn == 2){
                    player2.pause();
                }
                buttonPause.setVisibility(View.INVISIBLE);
                buttonResume.setVisibility(View.VISIBLE);
                buttonRollDice.setVisibility(View.VISIBLE);
            }
        });

        buttonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (turn == 1){
                    player1.resume();
                }
                if (turn == 2){
                    player2.resume();
                }
                prepareBattlefieldOnResume(v);
            }
        });
    }

    public void prepareBattlefieldOnStart(View view){
        buttonReset.setVisibility(View.VISIBLE);
        buttonReset.setEnabled(true);
        buttonTimerDown.setVisibility(View.INVISIBLE);
        buttonTimerUp.setVisibility(View.INVISIBLE);
        buttonResume.setVisibility(View.INVISIBLE);
        buttonRollDice.setVisibility(View.INVISIBLE);
        buttonPause.setVisibility(View.VISIBLE);
        buttonPause.setEnabled(true);
    }

    public void prepareBattlefieldOnResume(View view){
        buttonPause.setVisibility(View.VISIBLE);
        buttonResume.setVisibility(View.INVISIBLE);
        buttonRollDice.setVisibility(View.INVISIBLE);
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

    //--> put in array and loop over it
    public void hidePhases(View view){
        for (ImageView imageView : imageViewsP1) {
            imageView.animate().alpha(0f).setDuration(1000);
        }
        for (ImageView imageView : imageViewsP2){
            imageView.animate().alpha(0f).setDuration(1000);
        }
    }

    public void displayPhases(View view){
        for (ImageView imageView : imageViewsP1) {
            imageView.animate().alpha(0.3f).setDuration(2500);
        }
        for (ImageView imageView : imageViewsP2){
            imageView.animate().alpha(0.3f).setDuration(2500);
        }
    }

    //is only available while player is on turn and during phase
    public void passTurn(View view){
        prepareBattlefieldOnResume(view);
        if (phaseTracker < 5){
            phaseTracker = 4.5;
            proceedPhase();
            turn = 2;
            player1.disableButtons(view);
            player2.enableButtons(view, phaseTracker);
            player1.cancelTimer(view);
            player2.startTimer(view);
        }
        else {
            phaseTracker = 7.5;
            proceedPhase();
            turn = 1;
            player1.enableButtons(view, phaseTracker);
            player2.disableButtons(view);
            player1.startTimer(view);
            player2.cancelTimer(view);
        }
    }

    public void proceedPhase(){
        //first turn
        if (turn == 0) {
            //player 2 starts timer for player 1 and proceeds to upkeep player 1
            if (phaseTracker == 1) {
                upkeepP1.setAlpha(1f);
            }
            //player 1 starts timer and proceeds to upkeep player 2
            if (turn == 0 && phaseTracker == 4.5) {
                upkeepP2.setAlpha(1f);
            }
        }
        if (turn == 1){
            //proceed to end step player 1
            if (phaseTracker == 4.5){
                upkeepP1.setAlpha(0.3f);
                mainPhaseP1.setAlpha(0.3f);
                combatPhaseP1.setAlpha(0.3f);
                pcMainPhaseP1.setAlpha(1f);
                endStepP1.setAlpha(1f);
            }
            //proceed to mainPhaseP2
            if (phaseTracker == 5.5){
                mainPhaseP2.setAlpha(1f);
                upkeepP2.setAlpha(0.3f);
                phaseTracker = phaseTracker - 0.5;
            }
            //proceed to combat phase player 2
            if (phaseTracker == 6){
                mainPhaseP2.setAlpha(0.3f);
                combatPhaseP2.setAlpha(1f);
            }
            //proceed to post combat main phase player 2
            if (phaseTracker == 7){
                combatPhaseP2.setAlpha(0.3f);
                pcMainPhaseP2.setAlpha(1f);
            }
            //proceed to upkeep player 1
            if (phaseTracker == 8){
                pcMainPhaseP2.setAlpha(0.3f);
                endStepP2.setAlpha(0.3f);
                upkeepP1.setAlpha(1f);
                phaseTracker = 1.5;
            }
        }
        if (turn == 2){
            //proceed to main phase player 1
            if (phaseTracker == 2){
                upkeepP1.setAlpha(0.3f);
                mainPhaseP1.setAlpha(1f);
            }
            //proceed to combat phase player 1
            if (phaseTracker == 3){
                mainPhaseP1.setAlpha(0.3f);
                combatPhaseP1.setAlpha(1f);
            }
            //proceed to post combat main phase player 1
            if (phaseTracker == 4){
                combatPhaseP1.setAlpha(0.3f);
                pcMainPhaseP1.setAlpha(1f);
            }
            //proceed to upkeep player 2
            if (phaseTracker == 5){
                pcMainPhaseP1.setAlpha(0.3f);
                endStepP1.setAlpha(0.3f);
                upkeepP2.setAlpha(1f);
            }
            //proceed to end step player 2
            if (turn == 2 && phaseTracker == 7.5){
                upkeepP2.setAlpha(0.3f);
                mainPhaseP2.setAlpha(0.3f);
                combatPhaseP2.setAlpha(0.3f);
                pcMainPhaseP2.setAlpha(1f);
                endStepP2.setAlpha(1f);
            }
        }
    }

    public void reset(View view){
        player1.reset(view);
        player2.reset(view);
        turn = 0;

        buttonPause.setVisibility(View.INVISIBLE);
        buttonRollDice.setVisibility(View.VISIBLE);
        buttonResume.setVisibility(View.INVISIBLE);
        buttonReset.setEnabled(false);
        buttonReset.setVisibility(View.INVISIBLE);
        buttonTimerDown.setVisibility(View.VISIBLE);
        buttonTimerUp.setVisibility(View.VISIBLE);
    }

    //up and down by 1 minute for both players
    public void playersTimeUp(View view){
        player1.setTime2(true);
        player2.setTime2(true);
    }

    public void playersTimeDown(View view){
        player1.setTime2(false);
        player2.setTime2(false);
    }

    public void player1LifeUp(View view){
        player1.lifeUp(view);
    }

    public void player1LifeDown(View view){
        player1.lifeUp(view);
    }

    public void player2LifeUp(View view){
        player2.lifeUp(view);
    }

    public void player2LifeDown(View view){
        player2.lifeDown(view);
    }
}