package decoster.cryptomargin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;


public class MainActivity extends AppCompatActivity {
    public Handler handler;
    public double[] initValue;
    private final int numberFinalRow = 2;
    private final int nbColumns =6;
    private final int initalOffset = 9;
    public LinkedHashMap<Margins.Currency, double[]> margs;
    public SharedPreferences prefs;
    public String hashMapKey = "map";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.main_activity);
        prefs = getPreferences(Context.MODE_PRIVATE);


        initializeVariable();

        FloatingActionButton myFab = (FloatingActionButton)findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                updateMargins(initalOffset,margs);

            }
        });
        FloatingActionButton logout = (FloatingActionButton)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.confirmation_msg);
                builder.setCancelable(false);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        prefs.edit().clear().commit();
                        margs = new LinkedHashMap<>();
                        finish();
                        startActivity(getIntent());
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();


            }
        });
    }

    public void initializeVariable() {


        if(prefs.getBoolean("isLogged", false)) {
            margs = getHashMap(hashMapKey);
            Log.d("main", String.valueOf(margs.size()));
            initGrid((margs.size()+numberFinalRow)*nbColumns);
            updateMargins(initalOffset,margs);
        }
        else{
            margs= new LinkedHashMap<>();
            createDialogList();
        }



//        if(res != null) {
//            double[] tmp = createDialogCrypto("INIT");
//            margs.put(Margins.Currency.INIT, tmp);
//            for(Margins.Currency crypto : res) {
//                tmp = createDialogCrypto(crypto.name());
//                margs.put(crypto, tmp);
//            }
//        }
//        margs.put(Margins.Currency.INIT, new double[]{0.022, 15.5,0.022});
//        margs.put(Margins.Currency.BTC, new double[]{0.00616, 8.4, 0.0});
//        margs.put(Margins.Currency.IOT, new double[]{11.77, 47.77, 2.89});
//        margs.put(Margins.Currency.XRP, new double[]{150.0, 36.075, 2.1});
//        margs.put(Margins.Currency.NEO, new double[]{2.0, 70.0, 4.0});
//        margs.put(Margins.Currency.BCH, new double[]{0.023, 35.1, 2.01});
//        margs.put(Margins.Currency.BTG, new double[]{0.0232, 7.1, 0.44});
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void updateMargins(final int start,final LinkedHashMap<Margins.Currency, double[]> margs) {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        new Thread(){
            public void run(){
                final LinkedHashMap res = getInfo(margs);
                handler.post(new Runnable(){
                        public void run(){
                            updateGrid(start, res);
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        }
                    });

            }
        }.start();
    }

    public LinkedHashMap getInfo(final LinkedHashMap<Margins.Currency, double[]> margs) {
        Iterator it = margs.entrySet().iterator();
        LinkedHashMap<Margins.Currency, double[]> result = new LinkedHashMap<>();
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
            titleText.setTextColor(getResources().getColor(R.color.colorAccent2));
            GridLayout.Spec row=   GridLayout.spec(GridLayout.UNDEFINED, 1);
            GridLayout.Spec col  = GridLayout.spec(GridLayout.UNDEFINED, 1);

            GridLayout.LayoutParams param =new GridLayout.LayoutParams(row, col);

            param.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            param.height= LinearLayout.LayoutParams.WRAP_CONTENT;
            param.setGravity(Gravity.CENTER);
            grid.addView(titleText, param);
        }
    }
    private void updateGrid(int start, LinkedHashMap<Margins.Currency, double[]> currencies) {

        GridLayout grid = (GridLayout)findViewById(R.id.grid);

        Iterator it = currencies.entrySet().iterator();
        double totalBC =0.0;
        double totalUSD = 0.0;
        int counter =start;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Margins.Currency curr = (Margins.Currency)pair.getKey();
            double[] res = (double[]) pair.getValue();
            if(curr != Margins.Currency.INIT) {
                totalBC += res[3];
                totalUSD += res[4];
            }

            for(int j=0; j < 6; j++) {


                TextView titleText= (TextView) grid.getChildAt(counter);
                if(j==0)
                {
                    titleText.setText(curr.name());
                    titleText.setTypeface(titleText.getTypeface(), Typeface.BOLD);
                }
                else {
                    if(res[j-1] < 0.01) {
                        titleText.setText(String.format("%.4f",res[j-1]));
                    }
                    else if(res[j-1] <1.0) {
                        titleText.setText(String.format("%.2f",res[j-1]));
                    }
                    else {
                        titleText.setText(String.format("%.1f",res[j-1]));
                    }

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
    public double[] createDialogCrypto(String crypto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Get the layout inflater
        final LinearLayout layout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.currency_values, null);
        TextView text= layout.findViewById(R.id.crypto);

        if(crypto.equals("INIT")) {
            text.setText("BTC");
            builder.setTitle("Enter the inital BTC (before investment)");
        }
        else {
            text.setText(crypto);
            builder.setTitle("Enter your current wallet value");
        }


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final double[] result = new double[3];
        builder.setView(layout)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText nb1=layout.findViewById(R.id.nb1);
                        EditText nb2=layout.findViewById(R.id.nb2);
                        EditText nb3=layout.findViewById(R.id.nb3);

                        if(nb1.getText() == null || nb1.getText().toString().equals("")) {
                            nb1.setBackgroundColor(Color.RED);
                        }
                        else {
                            result[0] = Double.valueOf(nb1.getText().toString());
                            result[1]= (nb2.getText()==null || nb2.getText().toString().equals(""))?0.0:Double.valueOf(nb2.getText().toString());
                            result[2]= (nb3.getText()==null || nb3.getText().toString().equals(""))?0.0:Double.valueOf(nb3.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });


        builder.show();
        return result;

    }
    public ArrayList createDialogList() {

        final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();  // Where we track the selected items

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Set the dialog title
        String[] currenciesStr = new String[Margins.Currency.values().length-1];
        final Margins.Currency[] currencies= new Margins.Currency[Margins.Currency.values().length-1];
        int i =0;
        for(Margins.Currency curr: Margins.Currency.values()) {
            if(curr != Margins.Currency.INIT) {
                currenciesStr[i] = curr.name();
                currencies[i] = curr;
                i++;
            }

        }
        builder.setTitle(R.string.list_view)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(currenciesStr, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(mSelectedItems.size() >= 1) {
                            ArrayList<Margins.Currency> res = new ArrayList<Margins.Currency>();
                            res.add(Margins.Currency.INIT);
                            for(int ii= 0; ii < mSelectedItems.size(); ii++) {
                                int index= mSelectedItems.get(ii);
                                res.add(currencies[index]);
                            }
                            final CustomDialog dial = new CustomDialog(MainActivity.this, res);
                            dial.init(new Callable() {
                                @Override
                                public Object call() throws Exception {
                                    margs=dial.getResult();
                                    prefs.edit().putBoolean("isLogged", true).commit();
                                    saveHashMap(hashMapKey, margs);

                                    initGrid((margs.size()+numberFinalRow)*nbColumns);
                                    updateMargins(initalOffset,margs);

                                    return null;
                                }
                            });
                            dialog.dismiss();
                        }

                    }
                });

        builder.show();

        return null;

    }
    public void saveHashMap(String key , Object obj) {
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key,json);
        editor.apply();     // This line is IMPORTANT !!!
    }


    public LinkedHashMap<Margins.Currency,double[]> getHashMap(String key) {
        Gson gson = new Gson();
        String json = prefs.getString(key,"");
        java.lang.reflect.Type type = new TypeToken<LinkedHashMap<Margins.Currency,double[]>>(){}.getType();
        LinkedHashMap<Margins.Currency,double[]> obj = gson.fromJson(json, type);
        return obj;
    }
}
