package decoster.cryptomargin;

import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import decoster.cryptomargin.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {
    public Handler handler;
    public double[] initValue;
    private final int numberFinalRow = 2;
    private final int nbColumns =6;
    private final int initalOffset = 9;
    public  HashMap<Margins.Currency, double[]> margs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.main_activity);
        initializeVariable();

        initGrid((margs.size()+numberFinalRow)*nbColumns);

        updateMargins(initalOffset,this.margs);

        FloatingActionButton myFab = (FloatingActionButton)findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateMargins(initalOffset,margs);
            }
        });
    }

    public void initializeVariable() {
        margs= new HashMap<>();
        margs.put(Margins.Currency.BTC, new double[]{0.022, 15.5,0.022});
        margs.put(Margins.Currency.BTC, new double[]{0.012, 8.4, 0.0});
        margs.put(Margins.Currency.IOT, new double[]{11.77, 47.77, 2.89});
        margs.put(Margins.Currency.XRP, new double[]{150.0, 36.075, 2.1});
        margs.put(Margins.Currency.NEO, new double[]{2.0, 70.0, 4.0});
        margs.put(Margins.Currency.BCH, new double[]{0.023, 35.1, 2.01});
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void updateMargins(final int start,final HashMap<Margins.Currency, double[]> margs) {
        new Thread(){
            public void run(){
                final HashMap res = getInfo(margs);
                handler.post(new Runnable(){
                        public void run(){
                            updateGrid(start, res);
                        }
                    });

            }
        }.start();
    }

    public HashMap getInfo(final HashMap<Margins.Currency, double[]> margs) {
        Iterator it = margs.entrySet().iterator();
        HashMap<Margins.Currency, double[]> result = new HashMap<>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Margins.Currency curr = (Margins.Currency) pair.getKey();
            double[] values = (double[]) pair.getValue();

            double[] res = Margins.getValueCurrency(curr, values[0], values[1], values[2]);
            result.put(curr, res);
        }
        return result;

    }
    private void initGrid(int nb) {
        GridLayout grid = (GridLayout)findViewById(R.id.grid);
        for(int i =0; i < nb; i++) {
            TextView titleText= new TextView(this);
            titleText.setText("-");

            GridLayout.Spec row=   GridLayout.spec(GridLayout.UNDEFINED, 1);
            GridLayout.Spec col  = GridLayout.spec(GridLayout.UNDEFINED, 1);

            GridLayout.LayoutParams param =new GridLayout.LayoutParams(row, col);

            param.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            param.height= LinearLayout.LayoutParams.WRAP_CONTENT;
            param.setGravity(Gravity.CENTER);
            grid.addView(titleText, param);
        }
    }
    private void updateGrid(int start, HashMap<Margins.Currency, double[]> currencies) {

        GridLayout grid = (GridLayout)findViewById(R.id.grid);

        Iterator it = currencies.entrySet().iterator();
        double totalBC =0.0;
        double totalUSD = 0.0;
        int counter =start;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Margins.Currency curr = (Margins.Currency)pair.getKey();
            double[] res = (double[]) pair.getValue();

            totalBC += res[3];
            totalUSD += res[4];
            for(int j=0; j < 6; j++) {


                TextView titleText= (TextView) grid.getChildAt(counter);
                if(j==0)
                {
                    titleText.setText(curr.name());
                    titleText.setTypeface(titleText.getTypeface(), Typeface.BOLD);
                }
                else {
                    titleText.setText(String.format("%.2f",res[j-1]));
                }
                counter ++;

            }

        }
        counter += 6;
        addTotalRow(counter,grid, totalBC, totalUSD);


    }
    private void addTotalRow(int start, GridLayout grid,double valueBTC,double valueUSD) {

        String[] finals = new String[]{"X", "X", "X", String.format("%.2f",valueBTC), String.format("%.2f",valueUSD)};

        for(int i =0; i<6; i++) {
            TextView titleText = (TextView) grid.getChildAt(start);;
            if(i==0){
                titleText.setText("Final");
            }
            else {
                titleText.setText(finals[i-1]);
            }
            titleText.setTypeface(titleText.getTypeface(), Typeface.BOLD);
            start++;

        }

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
