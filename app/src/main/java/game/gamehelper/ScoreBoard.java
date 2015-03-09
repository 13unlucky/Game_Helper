package game.gamehelper;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.TableRow;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

import game.gamehelper.javaFiles.GameSet;

/*Class for reading scores recorded from MainWindow and generating table
 */

public class ScoreBoard extends ActionBarActivity implements FieldChangeFragment.FieldChangeListener {

    public static final int PLAYER_FIELD = 1;
    public static final int SET_FIELD = 2;
    public static final int DATA_FIELD = 3;
    public static final int TOTAL_FIELD = 4;

    public static final int MODE_NORMAL = 0;
    public static final int MODE_ADD = 1;
    public static final int MODE_REMOVE = 2;
    public static final int MODE_EDIT = 3;

    private TextView selectedField;
    private int fieldX;
    private int fieldY;
    private int mode = 0;
    private Menu menu;

    private HorizontalScrollView scrollHeader;
    private HorizontalScrollView scrollData;
    private TextView recyclableTextView;
    private int totalRows = 20;
    private int totalColumns = 20;
    private int rowWidthPercent = 20;
    private int fixedRowHeight = 90;
    private int fixedHeaderHeight = 60;
    private ArrayList<GameSet> setList;
    private ArrayList<String> playerList;
    private LinkedList<LinkedList<TextView>> setListView = new LinkedList<LinkedList<TextView>>();
    private LinkedList<TextView> playerListView;
    private LinkedList<TextView> setLabelListView;
    private LinkedList<TextView> totalListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        //read lists from MainWindow
        if (b != null) {
            setList = b.getParcelableArrayList("setList");
            playerList = b.getStringArrayList("playerList");
        }

        //create lists if empty
        if (setList == null) {
            setList = new ArrayList<GameSet>();
            setList.add(new GameSet());
            setList.get(0).addPlayer(0);
            playerList = new ArrayList<String>();
            playerList.add("Player 1");
        }

        setContentView(R.layout.activity_score_board);
        TableRow.LayoutParams tableParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        //blank space (fixed top left)
        TableLayout header = (TableLayout) findViewById(R.id.column_header);
        TableRow row = new TableRow(this);
        row.setLayoutParams(tableParams);
        row.setGravity(Gravity.CENTER);
        row.addView(makeTableRowWithText("", rowWidthPercent, fixedHeaderHeight, TOTAL_FIELD, -1, -1));
        header.addView(row);

        //set list (fixed vertically, scroll horizontally)
        header = (TableLayout) findViewById(R.id.scrollable_header);
        row = new TableRow(this);
        row.setLayoutParams(tableParams);
        row.setGravity(Gravity.CENTER);
        row.setBackgroundColor(Color.YELLOW);

        setLabelListView = new LinkedList<TextView>();
        for(int i = 1 ; i <= setList.size() ; i++ ) {
            TextView set = makeTableRowWithText("Round " + i, rowWidthPercent, fixedHeaderHeight, SET_FIELD, 0, i);
            setLabelListView.add(set);
            row.addView(set);
        }

        header.addView(row);

        //total label (fixed top right)
        header = (TableLayout) findViewById(R.id.column_header_total);
        row = new TableRow(this);
        row.setLayoutParams(tableParams);
        row.setGravity(Gravity.CENTER);
        row.setBackgroundColor(Color.GREEN);
        row.addView(makeTableRowWithText("Total", rowWidthPercent, fixedHeaderHeight, TOTAL_FIELD, -1, -1));
        header.addView(row);

        //Player name fields (fixed horizontally)
        TableLayout fixedColumn = (TableLayout) findViewById(R.id.fixed_column);

        //Total score fields (fixed horizontally)
        TableLayout totalColumn = (TableLayout) findViewById(R.id.total_column);

        //score data fields (within scroll view)
        TableLayout scrollableData = (TableLayout) findViewById(R.id.scrollable_data);

        //create linked lists for player and total columns
        playerListView = new LinkedList<TextView>();
        totalListView = new LinkedList<TextView>();

        for (int i = 0; i < playerList.size() ; i++) {
            //player name field
            TextView fixedView = makeTableRowWithText(playerList.get(i), rowWidthPercent, fixedRowHeight, PLAYER_FIELD, 0, i);
            fixedView.setBackgroundColor(Color.BLUE);
            playerListView.add(fixedView);
            fixedColumn.addView(fixedView);

            //score data field
            row = new TableRow(this);
            row.setLayoutParams(tableParams);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.WHITE);

            //fill column with scores
            LinkedList<TextView> dataColumn = new LinkedList<TextView>();
            for(int j = 0 ; j < setList.size() ; j++) {
                TextView data = makeTableRowWithText("" + setList.get(j).getScore(i), rowWidthPercent, fixedRowHeight, DATA_FIELD, j, i);
                dataColumn.add(data);
                row.addView(data);
            }

            setListView.add(dataColumn);
            scrollableData.addView(row);

            //total value field
            TextView fixedViewT = makeTableRowWithText( "" + getPlayerTotal(i), rowWidthPercent, fixedRowHeight, TOTAL_FIELD, -1, i);
            fixedViewT.setBackgroundColor(Color.WHITE);
            totalListView.add(fixedViewT);
            totalColumn.addView(fixedViewT);

        }

        //Lock data horizontal scroll to header horizontal scroll
        scrollHeader = (HorizontalScrollView)findViewById(R.id.scroll_header);
        scrollData = (HorizontalScrollView)findViewById(R.id.scroll_data);

        scrollData.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scrollHeader.setScrollX(scrollData.getScrollX());
            }
        });

    }

    public TextView makeTableRowWithText(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels,
                                         final int fieldType, final int x, final int y){
        //create text to set in table

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(this);
        recyclableTextView.setText(text);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setTextSize(10);
        recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setHeight(fixedHeightInPixels);

        //make text clickable
        recyclableTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedField = (TextView) v;
                fieldX = x;
                fieldY = y;
                DialogFragment newFragment;

                //add field information to bundle to be passed to fragment
                Bundle b = new Bundle();
                b.putString("field", selectedField.getText().toString());
                b.putInt("fieldType", fieldType);

                switch (fieldType) {

                    case PLAYER_FIELD:
                        newFragment = new FieldChangeFragment();
                        newFragment.setArguments(b);
                        newFragment.show(getSupportFragmentManager(), "fieldChange");
                        return true;

                    case SET_FIELD:
                        //TODO delete set popup
                        return true;

                    case DATA_FIELD:
                        newFragment = new FieldChangeFragment();
                        newFragment.setArguments(b);
                        newFragment.show(getSupportFragmentManager(), "fieldChange");

                        return true;

                    case TOTAL_FIELD:
                         //do nothing
                        return true;

                    case 0:
                    default:
                        Log.w("ScoreBoard", "unknown field selected: " + fieldX + "/" + fieldY);
                        Log.w("ScoreBoard", "see ScoreBoard method makeTableRowWithText");
                    return false;
                }
            }
        });
        return recyclableTextView;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score_board, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO add button behavior
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            //change icon state and icons

            case R.id.action_add:
                if (mode != MODE_ADD) {
                    mode = MODE_ADD;
                    resetIconState();
                    menu.getItem(MODE_ADD).setIcon(R.drawable.ic_action_add_active);
                } else {
                    mode = MODE_NORMAL;
                    resetIconState();
                }
                break;

            case R.id.action_remove:
                if (mode != MODE_REMOVE) {
                    mode = MODE_REMOVE;
                    resetIconState();
                    menu.getItem(MODE_REMOVE).setIcon(R.drawable.ic_action_remove_active);
                } else {
                    mode = MODE_NORMAL;
                    resetIconState();
                }
                break;

            case R.id.action_edit:
                if (mode != MODE_EDIT) {
                    mode = MODE_EDIT;
                    resetIconState();
                    menu.getItem(MODE_EDIT).setIcon(R.drawable.ic_action_edit_active);
                } else {
                    mode = MODE_NORMAL;
                    resetIconState();
                }
                break;

            case R.id.action_back:
                finish();
                break;

            default:
                Log.w("ScoreBoard", "Unknown actionbar icon clicked");
                break;



        }

        return super.onOptionsItemSelected(item);
    }

    private void resetIconState(){
        //set action bar icons to default state
        menu.getItem(MODE_ADD).setIcon(R.drawable.ic_action_add);
        menu.getItem(MODE_REMOVE).setIcon(R.drawable.ic_action_remove);
        menu.getItem(MODE_EDIT).setIcon(R.drawable.ic_action_edit);
    }

    public int getPlayerTotal(int location){
        //calculate total points for a row
        int total = 0;
        for(GameSet a : setList){
            total += a.getScore(location);
        }
        return total;
    }

    private void recalculateTotal(int x, int y) {
        //recalculate total column after data change
        totalListView.get(y).setText("" + getPlayerTotal(y));
    }


    @Override
    public void onDialogPositiveClick(String s, int fieldType) {
        int score = 0;

        switch(fieldType){
            case PLAYER_FIELD:
                //change name in listarray and textview object
                playerList.remove(fieldY);
                playerList.add(fieldY, s);
                playerListView.get(fieldY).setText(s);
                break;

            case DATA_FIELD:
                //change score in listarray and textview object if valid
                try{
                    score = Integer.parseInt(s);
                } catch (NumberFormatException e){
                    //don't change field if non numbers present
                    return;
                }

                setList.get(fieldX).changeScore(fieldY,score);
                setListView.get(fieldX).get(fieldY).setText(s);
                recalculateTotal(fieldX, fieldY);
                break;

            default:
                //do nothing
                Log.w("ScoreBoard", "unknown field returned from FieldChangeFragment");
                Log.w("ScoreBoard", "see ScoreBoard method onDialogPositiveClick");
                return;

        }

        //create bundle with changes to return to MainWindow
        Bundle b = new Bundle();
        Intent intent = getIntent();

        b.putParcelableArrayList("setList", setList);
        b.putStringArrayList("playerList", playerList);
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.w("ScoreBoard", "onSaveInstanceState called");
        outState.putParcelableArrayList("setList", setList);
        outState.putSerializable("playerList", playerList);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onPause() {
        Log.w("ScoreBoard", "onPause called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.w("ScoreBoard", "onStop called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.w("ScoreBoard", "onDestroy called");
        super.onDestroy();
    }
}
