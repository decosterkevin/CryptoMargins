//package decoster.cryptomargin;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * Created by kevin on 12.12.17.
// */
//
//
//public class Margins_copy {
//    public enum Currency {
//        IOTA,
//        XRP,
//        NEO,
//        BCH,
//        BC
//    }
//    public final String init2dollar;
//    public final String bc2dollar;
//    public final String iota2dollar;
//    public final String iota2bc;
//    public final String xrp2dollar;
//    public final String xrp2bc;
//    public final String neo2dollar;
//    public final String neo2bc;
//    public final String total2dollar;
//    public final String total2bc;
//    public final String bch2dollar;
//    public final String bch2bc;
//    private final double initBC = 0.023;
//    private final double currentBC = 0.012;
//    private final double currentIOTA = 11.77;
//    private final double currentXRC = 150.0;
//    private final double currentBCH = 0.023;
//    private final double currentNEO = 2.0;
//    public Margins_copy(double initBC, HashMap<Currency,double[]> input){
//
//
//        Iterator it = input.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            Currency curr = (Currency)pair.getKey();
//            double cValue = ((double[])pair.getValue())[0];
//            double cBuyPrice = ((double[])pair.getValue())[1];
//
//            String c2bc = Remote.getJSON(curr.name().toLowerCase() + "btc");
//            String c2usd = Remote.getJSON(curr.name().toLowerCase() + "usd");
//            if(c2bc != null) {
//                tmpBCH2bc = Double.parseDouble(c2bc)*currentBCH;
//            }
//            if(c2usd != null) {
//                tmpBCH2usd = Double.parseDouble(c2usd)*currentBCH;
//            }
//            it.remove(); // avoids a ConcurrentModificationException
//        }
////        Exchange bitfinex = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class.getName());
////        MarketDataService marketDataService = bitfinex.getMarketDataService();
////
////        double tmpInit2usd = 0.0;
////        double tmpBc2usd = 0.0;
////        double tmpIota2bc = 0.0;
////        double tmpIota2usd = 0.0;
////        double tmpNeo2usd = 0.0;
////        double tmpneo2bc = 0.0;
////        double tmpBCH2usd = 0.0;
////        double tmpBCH2bc = 0.0;
////        double tmpXRP2usd = 0.0;
////        double tmpXRP2bc = 0.0;
////        try {
////            Ticker tickerBTC2USD = marketDataService.getTicker(CurrencyPair.BTC_USD);
////
////
////            tmpInit2usd =initBC*tickerBTC2USD.getLast().doubleValue();
////            tmpBc2usd = currentBC*tickerBTC2USD.getLast().doubleValue();
////            Ticker tickerIOTA2USD = marketDataService.getTicker(CurrencyPair.IOTA_USD);
////            Ticker tickerIOTA2BC = marketDataService.getTicker(CurrencyPair.IOTA_BTC);
////
////            tmpIota2bc = currentIOTA*tickerIOTA2BC.getLast().doubleValue();
////            tmpIota2usd =currentIOTA*tickerIOTA2USD.getLast().doubleValue();
////
////            Ticker tickerXRC2USD = marketDataService.getTicker(CurrencyPair.XRP_USD);
////            Ticker tickerXRC2BC = marketDataService.getTicker(CurrencyPair.XRP_BTC);
////
////            tmpXRP2bc = currentXRC*tickerXRC2BC.getLast().doubleValue();
////            tmpXRP2usd = currentXRC*tickerXRC2USD.getLast().doubleValue();
////
////
////            String bch2bc = Remote.getJSON(Remote.API_neo2BC);
////            String bch2usd = Remote.getJSON(Remote.API_neo2dollar);
////            if(bch2bc != null) {
////                tmpBCH2bc = Double.parseDouble(bch2bc)*currentBCH;
////            }
////            if(bch2usd != null) {
////                tmpBCH2usd = Double.parseDouble(bch2usd)*currentBCH;
////            }
////
////            String neo2bc = Remote.getJSON(Remote.API_neo2BC);
////            String neo2usd = Remote.getJSON(Remote.API_neo2dollar);
////            if(neo2bc != null) {
////                tmpneo2bc = Double.parseDouble(neo2bc)*currentNEO;
////            }
////            if(neo2usd != null) {
////                tmpNeo2usd = Double.parseDouble(neo2usd)*currentNEO;
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
////
////        init2dollar = String.format("%.2f",tmpInit2usd);
////        bc2dollar = String.format("%.2f",tmpBc2usd);
////        iota2dollar = String.format("%.2f",tmpIota2usd);
////        iota2bc = String.valueOf(tmpIota2bc);
////        xrp2dollar = String.format("%.2f",tmpXRP2usd);
////        xrp2bc = String.valueOf(tmpXRP2bc);
////        neo2bc = String.valueOf(tmpneo2bc);
////        neo2dollar =String.format("%.2f",tmpNeo2usd);
////        bch2dollar = String.format("%.2f",tmpBCH2usd);
////        bch2bc = String.valueOf(tmpBCH2bc);
////
////        total2dollar = String.format("%.2f",tmpNeo2usd+tmpXRP2usd+tmpIota2usd+tmpBc2usd+tmpBCH2usd);
////        total2bc = String.valueOf(tmpneo2bc+tmpBCH2bc+tmpIota2bc+tmpXRP2bc+currentBC);
//
//    }
//}
