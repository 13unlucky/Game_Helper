package game.gamehelper;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.TableRow;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ScoreBoard extends ActionBarActivity {
    //TODO read score data from gameWindow and replace dummy data
    //TODO add click behavior

    private HorizontalScrollView scrollHeader;
    private HorizontalScrollView scrollData;
    private TextView recyclableTextView;
    private int totalRows = 20;
    private int totalColumns = 20;
    private int rowWidthPercent = 20;
    private int fixedRowHeight = 90;
    private int fixedHeaderHeight = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        TableRow.LayoutParams tableParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        //header (fixed top left)
        TableLayout header = (TableLayout) findViewById(R.id.column_header);
        TableRow row = new TableRow(this);
        row.setLayoutParams(tableParams);
        row.setGravity(Gravity.CENTER);
        row.addView(makeTableRowWithText("", rowWidthPercent, fixedHeaderHeight));
        header.addView(row);

        //header (fixed vertically, scroll horizontally)
        header = (TableLayout) findViewById(R.id.scrollable_header);
        row = new TableRow(this);
        row.setLayoutParams(tableParams);
        row.setGravity(Gravity.CENTER);
        row.setBackgroundColor(Color.YELLOW);

        for(int i = 0 ; i < totalColumns ; i++ )
            row.addView(makeTableRowWithText("Round " + i, rowWidthPercent, fixedHeaderHeight));

        header.addView(row);

        //header (fixed top right: total)
        header = (TableLayout) findViewById(R.id.column_header_total);
        row = new TableRow(this);
        row.setLayoutParams(tableParams);
        row.setGravity(Gravity.CENTER);
        row.setBackgroundColor(Color.GREEN);
        row.addView(makeTableRowWithText("Total", rowWidthPercent, fixedHeaderHeight));
        header.addView(row);



        //header (fixed horizontally)
        TableLayout fixedColumn = (TableLayout) findViewById(R.id.fixed_column);

        //total Column (fixed horizontally)
        TableLayout totalColumn = (TableLayout) findViewById(R.id.total_column);

        //rest of table (within scroll view)
        TableLayout scrollableData = (TableLayout) findViewById(R.id.scrollable_data);

        for (int i = 0; i < totalRows ; i++) {
            TextView fixedView = makeTableRowWithText("Player " + i, rowWidthPercent, fixedRowHeight);
            fixedView.setBackgroundColor(Color.BLUE);
            fixedColumn.addView(fixedView);
            row = new TableRow(this);
            row.setLayoutParams(tableParams);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.WHITE);

            for(int j = 0 ; j < totalColumns ; j++)
               row.addView(makeTableRowWithText("Score " + j, rowWidthPercent, fixedRowHeight));

            scrollableData.addView(row);

            TextView fixedViewT = makeTableRowWithText("Total " + i, rowWidthPercent, fixedRowHeight);
            fixedViewT.setBackgroundColor(Color.WHITE);
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

    public TextView makeTableRowWithText(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels){
        //create text to set in table

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(this);
        recyclableTextView.setText(text);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setTextSize(10);
        recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setHeight(fixedHeightInPixels);
        return recyclableTextView;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score_board, menu);
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


}
