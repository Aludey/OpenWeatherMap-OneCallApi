import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

public class Weather {

    public static float avg(float [] args){ //find average
        float result = 0;
        for (int i = 0; i< args.length; ++i) result += args[i];
        return result/args.length;
    }
    public static float max(float [] args){ //find maximum
        float max = -10000;
        for (int i = 0; i< args.length; ++i) if (args[i]> max) max =args[i];
        return max;
    }

    public static void  main(String[] args) {
        String API_KEY = "e9cb980fd035a63a416c9041c568674a"; // PRIVATE api key
        // Saint-Petersburg
        String LATITUDE = "59.9342802";
        String LONGTITUDE = "30.3350986";
        String EXCLUDE = "minutely,hourly,alerts";
        // using https://openweathermap.org/api/one-call-api
        String urlString = "https://api.openweathermap.org/data/2.5/onecall?lat=" + LATITUDE + "&lon="+ LONGTITUDE + "&exclude="+ EXCLUDE + "&appid=" + API_KEY + "&units=metric";

        StringBuilder result = new StringBuilder();
        try{
            URL url = new URL(urlString); // API request
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null){
                result.append(line);
            }
            rd.close();

        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        try {
            JSONParser jsonParser = new JSONParser(); // JSON parsing
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());

            JSONArray daily= (JSONArray) jsonObject.get("daily");
            Iterator i = daily.iterator();
            float [] morningTempetures = {0, 0, 0, 0,0};

            for (int k = 0; k < 5; ++k){  // only 5 days; day 0 === current day
                if (i.hasNext()){
                    JSONObject innerObj = (JSONObject) i.next();
                    JSONObject temp = (JSONObject) innerObj.get("temp"); // Taking all temperatures per day
                    morningTempetures[k] = Float.parseFloat(temp.get("morn").toString()); // Taking morning temperatures
                    System.out.println("Morning temperature on day " + k + " = " + morningTempetures[k] + " C");
                }
            }

            float avgtemperature = avg(morningTempetures);
            float maxtemperature = max(morningTempetures);
            System.out.println("Average morning temperature = " + avgtemperature + " C");
            System.out.println("Maximum morning temperature = " + maxtemperature + " C");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
