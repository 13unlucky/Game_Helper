/**Window containing a visual representation of a hand, with options
 * to change arrangement
 *
 */

package game.gamehelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import game.gamehelper.javaFiles.Domino;
import game.gamehelper.javaFiles.GameSet;
import game.gamehelper.javaFiles.Hand;


public class GameWindow extends ActionBarActivity implements
        ConfirmationFragment.ConfirmationListener,
        DrawFragment.DrawListener,
        EndSelectFragment.EndListener,
        AdapterView.OnItemClickListener{

    private Hand hand;
    private GridView listView;
    private ImageView trainHeadImage;
    private TextView text;
    private DominoAdapter adapter;
    private Bundle scoreHistory = new Bundle();
    private ArrayList<GameSet> setList = new ArrayList<GameSet>();
    private ArrayList<String> playerList = new ArrayList<String>();
    private int trainHead = 0;
    private Bundle handInformation;
    Domino[] data = new Domino[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_window);
        handInformation = getIntent().getExtras();

        if(playerList.size() == 0){
            playerList.add("Player 1");
        }

        text = (TextView)findViewById(R.id.remPoint);
        listView = (GridView) findViewById(R.id.gridViewMain);
        listView.setNumColumns(getResources().getConfiguration().orientation);
        trainHeadImage = (ImageView) findViewById(R.id.imageView2);

        text.setClickable(false);

//        DialogFragment endSelect = new EndSelectFragment();
//        endSelect.show(getSupportFragmentManager(), "Select_End");

        //use data passed from main window or camera to create a hand
        if(handInformation != null) {
            if (handInformation.getInt("dominoTotal") != 0) {
                createHand();
            }
            else
            {
                hand = new Hand(handInformation.getInt("maxDouble"));
                data = hand.toArray();
                text.setText(Integer.toString(hand.getTotalPointsHand()));
            }
        }

        //set ListView adapter to display list of dominos
        adapter = new DominoAdapter(this, R.layout.hand_display_grid, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        //THIS IS A HACK. REMOVE. USED FOR DEBUGGING PURPOSES.
//        Domino [] longestRun = hand.getLongestRun().toArray();
//        adapter = new DominoAdapter(this, R.layout.hand_display_grid, longestRun);
//        listView.setAdapter(adapter);

        addButtonBehavior();
    }

    public void createHand(){

        hand = new Hand((int[][]) handInformation.getSerializable("dominoList"),
                handInformation.getInt("dominoTotal"), handInformation.getInt("maxDouble"));

        //create domino array for adapter, set text and image to corresponding values
        Domino temp[] = hand.toArray();

        data = new Domino[(temp.length < MainWindow.MAX_DOMINO_DISPLAY) ? temp.length : MainWindow.MAX_DOMINO_DISPLAY];

        //generate bitmaps for hand
        for (int i = 0; i < data.length; i++) {
            data[i] = temp[i];
        }

        trainHeadImage.setImageBitmap(getSide(hand.getTrainHead()));
        text.setText(Integer.toString(hand.getTotalPointsHand()));

    }

    public void addButtonBehavior(){

        Button longestRun = (Button)findViewById(R.id.longestRunButton);
        Button highestScore = (Button)findViewById(R.id.highestScoreButton);
        Button draw = (Button)findViewById(R.id.drawButton);
        Button unsorted = (Button)findViewById(R.id.unsortedButton);
        Button undo = (Button)findViewById(R.id.undoButton);

        //Train head image behavior
        trainHeadImage.setOnClickListener(
                new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment endSelect = new EndSelectFragment();
                        endSelect.show(getSupportFragmentManager(), "Select_End");
                    }
                }
        );

        //longest run click handler
        longestRun.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        // TODO Add behavior

                        Domino temp[] = new Domino[1];
                        temp[0] = new Domino(0, 0);

                        adapter = new DominoAdapter(v.getContext(), R.layout.hand_display_grid, temp);
                        listView.setAdapter(adapter);

                        //set ListView adapter to display list of dominos
                        temp = hand.getLongestRun().toArray();

                        Domino viewLongestRun[] = new Domino[(temp.length < MainWindow.MAX_DOMINO_DISPLAY) ? temp.length : MainWindow.MAX_DOMINO_DISPLAY];

                        //generate bitmaps for hand (first 10 values, or memory crash).
                        for (int i = 0; i < viewLongestRun.length; i++) {
                            viewLongestRun[i] = temp[i];
                        }

                        adapter = new DominoAdapter(v.getContext(), R.layout.hand_display_grid, viewLongestRun);
                        listView.setAdapter(adapter);

                        //update point display
                        text.setText(Integer.toString(hand.getLongestRun().getPointVal()));
                    }
                }
        );

        //highest score click handler
        highestScore.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        // TODO Add behavior

                        Domino temp[] = new Domino[1];
                        temp[0] = new Domino(0, 0);
                        adapter = new DominoAdapter(v.getContext(), R.layout.hand_display_grid, temp);
                        listView.setAdapter(adapter);

                        //set ListView adapter to display list of dominos
                        temp = hand.getMostPointRun().toArray();

                        Domino viewMostPointRun[] = new Domino[(temp.length < MainWindow.MAX_DOMINO_DISPLAY) ? temp.length : MainWindow.MAX_DOMINO_DISPLAY];

                        //generate bitmaps for hand (first 10 values, or memory crash).
                        for (int i = 0; i < viewMostPointRun.length; i++) {
                            viewMostPointRun[i] = temp[i];
                        }

                        adapter = new DominoAdapter(v.getContext(), R.layout.hand_display_grid, viewMostPointRun);
                        listView.setAdapter(adapter);

                        //update point display
                        text.setText(Integer.toString(hand.getMostPointRun().getPointVal()));
                    }
                }
        );

        //draw click handler
        draw.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        DialogFragment newFragment = new DrawFragment();
                        newFragment.show(getSupportFragmentManager(), "Draw_Display");
                    }
                }
        );

        //unsorted click handler
        unsorted.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        Domino temp[] = new Domino[1];
                        temp[0] = new Domino(0, 0);
                        adapter = new DominoAdapter(v.getContext(), R.layout.hand_display_grid, temp);
                        listView.setAdapter(adapter);

                        //set ListView adapter to display list of dominos
                        temp = hand.toArray();
                        Domino viewHand[] = new Domino[(temp.length < MainWindow.MAX_DOMINO_DISPLAY) ? temp.length : MainWindow.MAX_DOMINO_DISPLAY];

                        //generate bitmaps for hand
                        for (int i = 0; i < viewHand.length; i++) {
                            viewHand[i] = temp[i];
                        }

                        adapter = new DominoAdapter(v.getContext(), R.layout.hand_display_grid, viewHand);
                        listView.setAdapter(adapter);

                        //update point display
                        text.setText(Integer.toString(hand.getTotalPointsHand()));
                    }
                }
        );

        //undo click handler
        undo.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        hand.undo();
                        adapter = new DominoAdapter(v.getContext(), R.layout.hand_display_grid, hand.toArray());
                        listView.setAdapter(adapter);
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_game_window, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_new_game:
                DialogFragment newFragment = new ConfirmationFragment();
                newFragment.show(getSupportFragmentManager(), getString(R.string.newGame));

                break;

            case R.id.action_score_board:
                //display score board
                scoreHistory.clear();
                scoreHistory.putSerializable("setList", setList);
                scoreHistory.putStringArrayList("playerList", playerList);
                startActivityForResult(new Intent(GameWindow.this, ScoreBoard.class).putExtras(scoreHistory),0);
//                startActivity(new Intent(GameWindow.this, ScoreBoard.class).putExtras(scoreHistory));

                break;

            case R.id.action_end_round:
                //write to scoreboard and wipe hand
                DialogFragment fragment = new ConfirmationFragment();
                fragment.show(getSupportFragmentManager(), getString(R.string.endSet));

                break;


            case R.id.action_camera:
                //camera call, overwrite hand
                startActivityForResult(new Intent(GameWindow.this, MainActivity.class),0);

                break;

            default:
                //TODO perform other

                break;
            }

        return true;
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

    public void newSet(){

        //create empty list
        DominoAdapter adapter;
        hand = new Hand(getIntent().getExtras().getInt("maxDouble"));
        Domino[] data = hand.toArray();
        adapter = new DominoAdapter(this, R.layout.hand_display_grid, data);
        listView.setAdapter(adapter);

        trainHeadImage.setImageBitmap(getSide(hand.getTrainHead()));
        text.setText(Integer.toString(hand.getTotalPointsHand()));
    }

    @Override
    public void onDialogPositiveClick(String tag) {
        //behavior for confirmation fragment (new game/ end set)

        if(tag.compareTo(getString(R.string.newGame)) == 0){
            //clear data and start new set
            scoreHistory.clear();
            setList.clear();
            playerList.clear();
            newSet();
        }
        else if(tag.compareTo(getString(R.string.endSet)) == 0){
            //create gameset from hand and add to scoreboard
            GameSet newSet = new GameSet(hand);

            //add rows for all current players
            for (int i = 1 ; i < playerList.size() ; i++)
                newSet.addPlayer();
            setList.add(newSet);
            newSet();
        }

    }

    @Override
    public void onClose(int var1, int var2) {
        //From draw button, use 2 integers to add a domino to hand
        //TODO handle domino added

        //add domino to hand
        hand.addDomino(new Domino(var1, var2));
        adapter = new DominoAdapter(this, R.layout.hand_display_grid, hand.toArray());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //update point display
        text.setText(Integer.toString(hand.getTotalPointsHand()));

    }

    @Override
    public void onClose(int var1) {
        //From end piece select, replace largest double value in hand
        //TODO handle train head changed

//        hand.setEndValue(var1);
        trainHeadImage.setImageBitmap(getSide(var1));
//        trainHead = var1;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle b;

        if( resultCode != RESULT_OK )
            return;
        switch( requestCode) {
            case 0:
                //data returned from scoreboard
                b = data.getExtras();

                if(b != null){
                    setList.clear();
                    setList = b.getParcelableArrayList("setList");
                    playerList.clear();
                    playerList = b.getStringArrayList("playerList");
                }
                return;

            case 1:
                //Data from camera
                b = data.getExtras();

                if(b != null){
                    handInformation = b;
                    newSet();
                    createHand();

                    //set ListView adapter to display list of dominos
                    adapter = new DominoAdapter(this, R.layout.hand_display_grid, this.data);
                    listView.setAdapter(adapter);
                    return;
                }

                Log.w("MainWindow", "No camera data found");
                return;

            default:
                //other
                return;      }
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO handle domino played
        Log.w("GameWindow", "Clicked " + position);
        hand.dominoPlayed(position);

        data = hand.toArray();
        adapter = new DominoAdapter(this, R.layout.hand_display_grid, this.data);
        listView.setAdapter(adapter);

        trainHeadImage.setImageBitmap(getSide(hand.getTrainHead()));
        text.setText(Integer.toString(hand.getTotalPointsHand()));

    }


}