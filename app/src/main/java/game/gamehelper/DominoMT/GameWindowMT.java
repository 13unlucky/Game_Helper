/**Window containing a visual representation of a hand, with options
 * to change arrangement
 *
 */

package game.gamehelper.DominoMT;

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

import game.gamehelper.ConfirmationFragment;
import game.gamehelper.GameSet;
import game.gamehelper.MainActivity;
import game.gamehelper.MainWindow;
import game.gamehelper.R;
import game.gamehelper.ScoreBoard;


public class GameWindowMT extends ActionBarActivity implements
        ConfirmationFragment.ConfirmationListener,
        DrawFragment.DrawListener,
        EndSelectFragment.EndListener,
        AdapterView.OnItemClickListener{

    private static int DOUBLE_NINE = 9;
    private static int DOUBLE_TWELVE = 12;
    private static int DOUBLE_FIFTEEN = 15;
    private static int DOUBLE_EIGHTEEN = 18;

    private HandMT hand;
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
    private int maxDouble = 0;

    /**
     * Context of a play. Whether we played on the longest, the most points, or the unsorted screen.
     */
    public enum WindowContext {
        SHOWING_LONGEST, SHOWING_MOST_POINTS, SHOWING_UNSORTED
    }

    private WindowContext windowState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_window);
        handInformation = getIntent().getExtras();
        windowState = WindowContext.SHOWING_UNSORTED;


        text = (TextView)findViewById(R.id.remPoint);
        listView = (GridView) findViewById(R.id.gridViewMain);
        listView.setNumColumns(getResources().getConfiguration().orientation);
        trainHeadImage = (ImageView) findViewById(R.id.imageView2);

        text.setClickable(false);
        addButtonBehavior();

        listView.setOnItemClickListener(this);

        if(MainWindow.debug){
            newGameDebug();
            return;
        }

        newGame();


        //THIS IS A HACK. REMOVE. USED FOR DEBUGGING PURPOSES.
//        Domino [] longestRun = hand.getLongestRun().toArray();
//        adapter = new DominoAdapter(this, R.layout.hand_display_grid, longestRun);
//        listView.setAdapter(adapter);

    }

    public void createHand(){

        maxDouble = handInformation.getInt("maxDouble");
        hand = new HandMT((int[][]) handInformation.getSerializable("dominoList"),
                handInformation.getInt("dominoTotal"), handInformation.getInt("maxDouble"));

        //create domino array for adapter, set text and image to corresponding values
        Domino temp[] = hand.toArray();

        data = new Domino[(temp.length < MainWindow.MAX_DOMINO_DISPLAY) ? temp.length : MainWindow.MAX_DOMINO_DISPLAY];

        //generate bitmaps for hand
        for (int i = 0; i < data.length; i++) {
            data[i] = temp[i];
        }

        trainHeadImage.setImageBitmap(Domino.getSide(hand.getTrainHead(), getApplicationContext()));
        updatePointValueText();
    }

    private void updatePointValueText() {
        if (windowState == WindowContext.SHOWING_LONGEST) {
            text.setText("Junk Value: " + (hand.getTotalPointsHand() - hand.getLongestRun().getPointVal()));
        }
        else if (windowState == WindowContext.SHOWING_MOST_POINTS) {
            text.setText("Junk Value: " + (hand.getTotalPointsHand() - hand.getMostPointRun().getPointVal()));
        }
        else if (windowState == WindowContext.SHOWING_UNSORTED) {
            text.setText("Hand Value: " + (hand.getTotalPointsHand()));
        }
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
                        windowState = WindowContext.SHOWING_LONGEST;

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
                        updatePointValueText();
                    }
                }
        );

        //highest score click handler
        highestScore.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        windowState = WindowContext.SHOWING_MOST_POINTS;

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
                        updatePointValueText();
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
                        windowState = WindowContext.SHOWING_UNSORTED;

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
                        updatePointValueText();
                    }
                }
        );

        //undo click handler
        undo.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        hand.undo();

                        //update the pictures & score shown based on our current context
                        if (windowState == WindowContext.SHOWING_LONGEST) {
                            data = hand.getLongestRun().toArray();
                        }
                        else if (windowState == WindowContext.SHOWING_MOST_POINTS) {
                            data = hand.getMostPointRun().toArray();
                        }
                        else if (windowState == WindowContext.SHOWING_UNSORTED) {
                            data = hand.toArray();
                        }

                        updatePointValueText();

                        adapter = new DominoAdapter(v.getContext(), R.layout.hand_display_grid, data);
                        listView.setAdapter(adapter);

                        //re-sets the old train head.
                        trainHeadImage.setImageBitmap(Domino.getSide(hand.getTrainHead(), getApplicationContext()));
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
        DialogFragment newFragment;
        Bundle b;

        switch (item.getItemId()){
            case R.id.action_new_game:
                b = new Bundle();
                b.putString("positive", getString(R.string.yes));
                b.putString("negative", getString(R.string.cancel));
                b.putString("mainText", getString(R.string.newGameText));
                b.putString("callName", getString(R.string.newGame));
                newFragment = new ConfirmationFragment();
                newFragment.setArguments(b);
                newFragment.show(getSupportFragmentManager(), getString(R.string.newGame));

                break;

            case R.id.action_score_board:
                //display score board
                scoreHistory.clear();
                scoreHistory.putSerializable("setList", setList);
                scoreHistory.putStringArrayList("playerList", playerList);
                startActivityForResult(new Intent(GameWindowMT.this, ScoreBoard.class).putExtras(scoreHistory),0);
//                startActivity(new Intent(GameWindow.this, ScoreBoard.class).putExtras(scoreHistory));

                break;

            case R.id.action_end_round:
                //write to scoreboard and wipe hand
                b = new Bundle();
                b.putString("positive", getString(R.string.yes));
                b.putString("negative", getString(R.string.cancel));
                b.putString("mainText", getString(R.string.endSetText));
                b.putString("callName", getString(R.string.endSet));
                newFragment = new ConfirmationFragment();
                newFragment.setArguments(b);
                newFragment.show(getSupportFragmentManager(), getString(R.string.endSet));

                break;


            case R.id.action_camera:
                //camera call, overwrite hand
                startActivityForResult(new Intent(GameWindowMT.this, MainActivity.class),0);

                break;

            default:
                //TODO perform other

                break;
            }

        return true;
    }


    public void newGameDebug(){
        //use data passed from main window or camera to create a hand
        if(handInformation != null) {
            if (handInformation.getInt("dominoTotal") != 0) {
                createHand();
            }
            else
            {
                maxDouble = handInformation.getInt("maxDouble");
                hand = new HandMT(maxDouble);
                data = hand.toArray();
                updatePointValueText();
            }
        }
        if(playerList.size() == 0){
            playerList.add("Player 1");
        }
        createHand();
        updateUI();

    }

    public void newGame(){
        scoreHistory.clear();
        setList.clear();
        playerList.clear();
        playerList.add("Player 1");
        newSet();
    }

    public void newSet(){

        //create empty list
        hand = new HandMT(maxDouble);
        data = hand.toArray();
        updateUI();
    }

    @Override
    public void onDialogPositiveClick(String tag) {
        //behavior for confirmation fragment (new game/ end set)

        if(tag.compareTo(getString(R.string.newGame)) == 0){
            //clear data and start new set
            newGame();
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

        hand.addDomino(new Domino(var1, var2));
        updateUI();

    }

    @Override
    public void onClose(int var1) {
        //From end piece select, replace largest double value in hand

        hand.setTrainHead(var1);
        updateUI();
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

    //When each domino is clicked, we delete it from the hand, and update the pictures.
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.w("GameWindow", "Clicked " + position);
        hand.dominoPlayed(position, windowState);
        updateUI();
    }

    private void updateUI(){
        //update the screen to reflect changes to hand

        //update the pictures & score shown based on our current context
        if (windowState == WindowContext.SHOWING_LONGEST) {
            data = hand.getLongestRun().toArray();
        }
        else if (windowState == WindowContext.SHOWING_MOST_POINTS) {
            data = hand.getMostPointRun().toArray();
        }
        else if (windowState == WindowContext.SHOWING_UNSORTED) {
            data = hand.toArray();
        }

        updatePointValueText();

        //update the picture to our data array of dominoes.
        adapter = new DominoAdapter(this, R.layout.hand_display_grid, this.data);
        listView.setAdapter(adapter);

        //update the train head's image
        trainHeadImage.setImageBitmap(Domino.getSide(hand.getTrainHead(),getApplicationContext()));
    }
}