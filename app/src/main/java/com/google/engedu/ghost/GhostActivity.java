package com.google.engedu.ghost;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private SimpleDictionary simpleDictionary;
    private boolean userTurn = false;
    private boolean isWon = false;
    private Random random = new Random();

    private TextView ghostText;
    private TextView label;
    private Button challengeButton;
    private Button restartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        InputStream is = null;

        try {
            is = getAssets().open("words.txt");
            simpleDictionary = new SimpleDictionary(is);
        } catch (IOException e) {

            e.printStackTrace();
        }

        ghostText = (TextView) findViewById(R.id.ghostText);
        label = (TextView) findViewById(R.id.gameStatus);
        challengeButton = (Button) findViewById(R.id.challenge_button);
        restartButton = (Button) findViewById(R.id.restart_button);

        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChallengeButtonClick();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart(null);
            }
        });

        onStart(null);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(isWon){
            disableChallengeButton();
        }else{
            enableChallengeButton();
        }

        if(keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z){
            String fragment = (String) ghostText.getText();
            fragment = fragment.concat(String.valueOf(event.getDisplayLabel())).toLowerCase();
            ghostText.setText(fragment);
            userTurn = false;
            computerTurn();
            return true;
        }else {
            return super.onKeyUp(keyCode, event);
        }
    }

    public void onChallengeButtonClick(){
        String fragment = ghostText.getText().toString();
        if(fragment.length() > 3 && simpleDictionary.isWord(fragment)){
            label.setText("You won");
            isWon = true;
            disableChallengeButton();
        }else{
            String word = simpleDictionary.getAnyWordStartingWith(fragment);
            if(word != null){
                label.setText("Computer won \nWord is : " + word);
                isWon = true;
                disableChallengeButton();
            }else{
                label.setText("You can't bluff this computer!! You lose");
                isWon = true;
                disableChallengeButton();
            }
        }
    }

    public void disableChallengeButton(){
        challengeButton.setEnabled(false);
    }

    public void enableChallengeButton(){
        challengeButton.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        String word;
        String fragment  =  ghostText.getText().toString().toLowerCase();

        word = simpleDictionary.getAnyWordStartingWith(fragment);
        Log.d("word", fragment);

        if(fragment.length() >= 4 && simpleDictionary.isWord(fragment)){
            label.setText("Computer won!");
            isWon = true;
            disableChallengeButton();
        }else{
            if(word == null){
                label.setText("You can't bluff this computer!!  You Lose");
                isWon = true;
                disableChallengeButton();
            }else{
                Log.d("word", word);
                ghostText.setText(word.substring(0, fragment.length() + 1));
                userTurn = true;
                label.setText(USER_TURN);
            }
        }
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);

        String word = simpleDictionary.getAnyWordStartingWith("");
        text.setText(word.substring(0,3));

        TextView label = (TextView) findViewById(R.id.gameStatus);
        isWon = false;
        disableChallengeButton();

        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
}
