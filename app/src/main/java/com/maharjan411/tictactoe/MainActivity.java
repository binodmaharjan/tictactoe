package com.maharjan411.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.maharjan411.tictactoe.about.AboutActivity;
import com.maharjan411.tictactoe.game.GameActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSingle;
    Button btnMultiple;
    private Button btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnSingle=(Button)findViewById(R.id.btnSingle);
        btnMultiple=(Button)findViewById(R.id.btnMultiple);
        btnAbout=(Button)findViewById(R.id.btnAbout);


        btnSingle.setOnClickListener(this);
        btnMultiple.setOnClickListener(this);
        btnAbout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSingle:
                startGameActivity(GameActivity.SINGLE_PLAYER);
                break;
            case R.id.btnMultiple:
                startGameActivity(GameActivity.MULTIPLE_PLAYER);
                break;
            case R.id.btnAbout:
                startActivity(new Intent(this, AboutActivity.class));
                break;

        }
    }

    private void startGameActivity(int type){
        Intent i=new Intent(MainActivity.this,GameActivity.class);
        i.putExtra(GameActivity.EXTRA_GAME,type);
        startActivity(i);
    }

}
