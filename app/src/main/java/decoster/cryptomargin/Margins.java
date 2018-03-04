package decoster.cryptomargin;

import android.util.Log;

/**
 * Created by kevin on 12.12.17.
 */


public class Margins {
    public static double[] getValueCurrency(Currency currency, double initValue, double initDollar, double initBTC) {
        if (currency == Currency.INIT) currency = Currency.BTC;
        Log.d("main", String.valueOf(initValue));
        String c2bc = Remote.getJSON(currency.name().toLowerCase() + "btc");
        String c2usd = Remote.getJSON(currency.name().toLowerCase() + "usd");
        double tmpBC = 0.0;
        double tmpUS = 0.0;
        if (c2bc != null) {
            tmpBC = Double.parseDouble(c2bc) * initValue * 1000.0;
        } else {
            tmpBC = initValue * 1000.0;
        }
        if (c2usd != null) {
            tmpUS = Double.parseDouble(c2usd) * initValue;
        } else {
            tmpUS = initDollar;
        }

        return new double[]{initValue, initDollar, initBTC, tmpUS, tmpBC};

    }

    public enum Currency {
        IOT,
        XRP,
        NEO,
        BCH,
        INIT,
        BTC,
        BTG,
        LTC,
        ETH,
        ETC,
        XMR,
        RRT,
        ZEC,
        EOS,
        SAN,
        OMG,
        DAT,
        DSH,
        EDO
    }
}
