package fr.m2dl.japanairlines;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by mfaure on 22/01/15.
 */
public class GameOverActivity extends Activity

    {

        private Button boutonRetour;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(MainActivity.gagne){

            setContentView(R.layout.activity_win);
            boutonRetour = (Button) findViewById(R.id.buttonRetour);
        }else{

            setContentView(R.layout.activity_gameover);
            boutonRetour = (Button) findViewById(R.id.buttonRetour);
        }


        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), AccueilActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }
        });
    }
    }