package game.gamehelper;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.app.Activity;
import android.view.View;
import android.content.Intent;

import java.util.Random;


public class MainWindow extends ActionBarActivity {
    public static int[][] tileList = new int[100][2];
    public static int totalTiles = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        getSupportActionBar().hide();

        Button newGameButton = (Button)findViewById(R.id.newGameButton);
        Button cameraButton = (Button)findViewById(R.id.cameraButton);
        Button exitButton = (Button)findViewById(R.id.exitButton);

        randomDominos(9);

        newGameButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        startActivity(new Intent(MainWindow.this, GameWindow.class));
                    }
                }

        );

        cameraButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        startActivity(new Intent(MainWindow.this, MainActivity.class));
                    }
                }

        );

        exitButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        finish();
                        System.exit(0);
                    }
                }

        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_window, menu);
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

    static void randomDominos(int total){
        Random generator = new Random();
        boolean[][] used = new boolean[13][13];
        tileList[0][0] = generator.nextInt(12);
        tileList[0][1] = generator.nextInt(12);

        for(boolean[] a : used){
            for(boolean b : a)
                b = false;
        }

        for(int[] i : tileList){

            if(total-- <= 0)
                break;

            while(used[i[0]][i[1]] != false) {
                i[0] = generator.nextInt(12);
                i[1] = generator.nextInt(12);
            }
            used[i[0]][i[1]] = true;
            used[i[1]][i[0]] = true;
            totalTiles++;




        }
    }
}
