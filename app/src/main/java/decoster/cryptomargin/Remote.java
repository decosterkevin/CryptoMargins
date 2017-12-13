package decoster.cryptomargin;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

/**
 * Created by kevin on 12.12.17.
 */

public class Remote {

        public static final String API =
                "https://api.bitfinex.com/v1/pubticker/";
    public static final String API_neo2dollar =
            "https://api.bitfinex.com/v1/pubticker/neousd";

        public static String getJSON(String uri){
            try {
                URL url = new URL(API + uri);
                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();

                JSONObject data = new JSONObject(json.toString());
                if(data != null) {
                    return data.getString("last_price");
                }
                else {
                    return null;
                }
            }catch(Exception e){
                return null;
            }
        }
}
