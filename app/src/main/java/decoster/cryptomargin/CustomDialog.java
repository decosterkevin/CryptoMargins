package decoster.cryptomargin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;

/**
 * Created by kevin on 09.01.18.
 */

public class CustomDialog{
    private Callable finalCallback;
    public AlertDialog.Builder builder;
    public double[] result;
    private Activity activity;
    private ArrayList<Margins.Currency> cryptos;
    public LinkedHashMap<Margins.Currency, double[]> margs;
    public CustomDialog(Activity context, ArrayList<Margins.Currency> cryptos) {
        activity=context;
        this.cryptos=cryptos;
        this.margs= new LinkedHashMap<>();
    }
    public void init( Callable finalCallback) {
        this.finalCallback = finalCallback;
        createDialog(cryptos.get(0));
    }

    public  void createDialog(final Margins.Currency crypto) {
        builder = new AlertDialog.Builder(this.activity);
        // Get the layout inflater
        final LinearLayout layout = (LinearLayout) this.activity.getLayoutInflater().inflate(R.layout.currency_values, null);
        TextView text= layout.findViewById(R.id.crypto);

        if(crypto.equals("INIT")) {
            text.setText("BTC");
            builder.setTitle("Enter the inital BTC (before investment)");
        }
        else {
            text.setText(crypto.name());
            builder.setTitle("Enter your current wallet value");
        }


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        result = new double[3];
        final EditText nb1=layout.findViewById(R.id.nb1);
        final EditText nb2=layout.findViewById(R.id.nb2);
        final EditText nb3=layout.findViewById(R.id.nb3);
        builder.setView(layout)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        if(nb1.getText() == null || nb1.getText().toString().equals("")) {

                        }
                        else {
                            result[0] = Double.valueOf(nb1.getText().toString());
                            result[1]= (nb2.getText()==null || nb2.getText().toString().equals(""))?0.0:Double.valueOf(nb2.getText().toString());
                            result[2]= (nb3.getText()==null || nb3.getText().toString().equals(""))?0.0:Double.valueOf(nb3.getText().toString());
                            margs.put(crypto, result);
                            cryptos.remove(crypto);
                            Log.d("main", String.valueOf(cryptos.size()));
                            dialog.dismiss();


                        }
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(cryptos.size()>0) {
                            createDialog(cryptos.get(0));
                        }
                        else {
                            try {
                                finalCallback.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);
        // Now set the textchange listener for edittext
        nb1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });

    }
    public AlertDialog.Builder getBuilder(){
        return builder;
    }
    public LinkedHashMap<Margins.Currency, double[]> getResult(){
        return margs;
    }
}
