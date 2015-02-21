/**Window containing a visual representation of a hand, with options
 * to change arrangement
 *
 */

package game.gamehelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import game.gamehelper.javaFiles.Domino;
import game.gamehelper.javaFiles.Hand;


public class GameWindow extends ActionBarActivity {
    private Hand hand;
    private ListView listView;
    private ImageView image;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_window);
        Bundle bundle = getIntent().getExtras();

        DominoAdapter adapter;
        Domino[] data = new Domino[0];

        text = (TextView)findViewById(R.id.remPoint);
        listView = (ListView) findViewById(R.id.listView);
        image = (ImageView) findViewById(R.id.imageView2);

        text.setClickable(false);

        //use data passed from main window or camera to create a hand
        if(bundle != null) {
            if (bundle.getInt("dominoTotal") != 0) {
                hand = new Hand((int[][]) bundle.getSerializable("dominoList"),
                        bundle.getInt("dominoTotal"), bundle.getInt("maxDouble"));

                //generate bitmaps for hand
                for (Domino a : hand.toArray())
                    buildDomino(a);

                //create domino array for adapter, set text and image to corresponding values
                data = hand.toArray();
                text.setText(Integer.toString(hand.getTotalPointsHand()));
                image.setImageBitmap(getSide(hand.getLargestDouble()));
            }
        }

        //set ListView adapter to display list of dominos
        adapter = new DominoAdapter(this, R.layout.hand_display_grid, data);
        listView.setAdapter(adapter);

        addButtonBehavior();
    }

    public void addButtonBehavior(){

        Button longestRun = (Button)findViewById(R.id.longestRunButton);
        Button highestScore = (Button)findViewById(R.id.highestScoreButton);
        Button draw = (Button)findViewById(R.id.drawButton);
        Button undo = (Button)findViewById(R.id.undoButton);

        //longest run click handler
        longestRun.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        // TODO Add behavior
                    }
                }
        );

        //highest score click handler
        highestScore.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        // TODO Add behavior
                    }
                }
        );

        //draw click handler
        draw.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        // TODO Add behavior
                    }
                }
        );

        //undo click handler
        undo.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        // TODO Add behavior
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_window, menu);
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

    //Load background and write each side on top
    public void buildDomino(Domino a){

        Bitmap side1;
        Bitmap side2;
        Bitmap bg;

        bg = BitmapFactory.decodeResource(getResources(), R.drawable.dom_bg);
        side1 = getSide(a.getVal1());
        side2 = getSide(a.getVal2());

        //copy immutable bitmap generated previously to a mutable bitmap and impose the sides
        bg = bg.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bg);
        canvas.drawBitmap(side1, 0, 0, null);
        canvas.drawBitmap(side2, side2.getWidth(), 0, null);

        a.setDominoPic(bg);
    }

    //Load image for domino side value
    private Bitmap getSide(int value){

        Bitmap side;

        switch(value){
            case 1:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_one);
                break;
            case 2:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_two);
                break;
            case 3:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_three);
                break;
            case 4:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_four);
                break;
            case 5:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_five);
                break;
            case 6:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_six);
                break;
            case 7:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_seven);
                break;
            case 8:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_eight);
                break;
            case 9:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_nine);
                break;
            case 10:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_ten);
                break;
            case 11:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_eleven);
                break;
            case 12:
                side = BitmapFactory.decodeResource(getResources(), R.drawable.dom_twelve);
                break;
            case 0:
            default:
                side = Bitmap.createBitmap(200,200,Bitmap.Config.ARGB_8888);
                break;
        }
        return side;
    }
}
