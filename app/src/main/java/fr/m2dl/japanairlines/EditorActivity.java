package fr.m2dl.japanairlines;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Random;

import fr.m2dl.japanairlines.domain.Obstacle;


public class EditorActivity extends ActionBarActivity implements View.OnTouchListener {

    private static long LEVEL_LENGTH = 100;
    private int screenHeight;
    private int screenWidth;

    ArrayList<DrawableImageView[]> cells;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        cells = new ArrayList<>();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

        TypedValue tv = new TypedValue();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                screenHeight -= TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            screenHeight -= getResources().getDimensionPixelSize(resourceId);

        for(int i = 0; i < LEVEL_LENGTH; i++) {
            LinearLayout repeatedLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    screenWidth/3,
                    screenHeight/5
            );
            repeatedLayout.setOrientation(LinearLayout.HORIZONTAL);

            DrawableImageView left = new DrawableImageView(this);
            left.setLayoutParams(layoutParams);
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DrawableImageView view = (DrawableImageView) v;
                    view.changeState();
                }
            });
            DrawableImageView middle = new DrawableImageView(this);
            middle.setLayoutParams(layoutParams);
            middle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DrawableImageView view = (DrawableImageView) v;
                    view.changeState();
                }
            });
            DrawableImageView right = new DrawableImageView(this);
            right.setLayoutParams(layoutParams);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DrawableImageView view = (DrawableImageView) v;
                    view.changeState();
                }
            });

            repeatedLayout.addView(left);
            repeatedLayout.addView(middle);
            repeatedLayout.addView(right);

            DrawableImageView[] line = new DrawableImageView[3];
            line[0] = left;
            line[1] = middle;
            line[2] = right;
            cells.add(line);

            linearLayout.addView(repeatedLayout);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_start:
                startLevel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private ArrayList<Obstacle> generateObstacles(){
        ArrayList<Obstacle> ret = new ArrayList<>();

        for(DrawableImageView[] tab : cells) {
            for (int i = 0; i < 3; ++i) {
                Obstacle obstacle = null;
                if(tab[i].isChecked()) {
                    obstacle = new Obstacle(i, cells.indexOf(tab));
                } else {
                    obstacle = new Obstacle(-1, -1);
                }
                ret.add(obstacle);
            }
        }

        return ret;
    }

    private void startLevel() {
        generateObstacles();
        // Launch intent
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("", "Touch event ! View : " + v );
        Log.d("", "Touch event ! Event : " + event.getX() + " // " + event.getY() );

        return true;
    }
}
