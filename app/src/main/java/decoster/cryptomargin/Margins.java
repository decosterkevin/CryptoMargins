package decoster.cryptomargin;

import android.util.Log;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kevin on 12.12.17.
 */


public class Margins {
    public enum Currency {
        IOT,
        XRP,
        NEO,
        BCH,
        INIT, BTC
    }
    public static double[] getValueCurrency(Currency currency, double initValue, double initDollar, double initBTC) {
        if(currency == Currency.INIT) currency=Currency.BTC;

        String c2bc = Remote.getJSON(currency.name().toLowerCase() + "btc");
        String c2usd = Remote.getJSON(currency.name().toLowerCase() + "usd");
        double tmpBC = 0.0;
        double tmpUS =0.0;
        if(c2bc != null) {
            tmpBC = Double.parseDouble(c2bc)*initValue*1000.0;
        }
        else {
            tmpBC =initValue*1000.0;
        }
        if(c2usd != null) {
            tmpUS = Double.parseDouble(c2usd)*initValue;
        }
        else {
            tmpUS = initDollar;
        }

        return new double[] {initValue, initDollar, initBTC, tmpUS, tmpBC};

    }
}
