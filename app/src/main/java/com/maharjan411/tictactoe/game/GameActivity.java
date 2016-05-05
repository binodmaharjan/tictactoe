package com.maharjan411.tictactoe.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.maharjan411.tictactoe.R;


public class GameActivity extends AppCompatActivity {

    private static final String TAG=GameActivity.class.getSimpleName();

    public static final String EXTRA_GAME="EXTRA_GAME";

    public static final int SINGLE_PLAYER=1;
    public static final int MULTIPLE_PLAYER=2;

    private int gameMode;
    private Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        gameMode=getIntent().getIntExtra(EXTRA_GAME,SINGLE_PLAYER);
         game=new Game(this);
        game.setGameMode(gameMode);
        setContentView(game);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                game.start();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
