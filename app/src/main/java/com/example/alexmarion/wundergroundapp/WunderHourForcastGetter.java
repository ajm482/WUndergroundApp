package com.example.alexmarion.wundergroundapp;

import android.os.AsyncTask;
import android.text.Layout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Alex Marion on 8/12/2015.
 */
public class WunderHourForcastGetter extends AsyncTask<Integer, Integer, String[][]> {
    @Override
    protected String[][] doInBackground(Integer... params) {
        System.out.println("WAT");
        String locationURL = "http://api.wunderground.com/api/1496919d14a2864c/geolookup/q/autoip.json";
        HttpClient locationHttpclient = new DefaultHttpClient();
        String zip = null;
        try {
            HttpResponse response = locationHttpclient.execute(new HttpGet(locationURL));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                out.close();
                //System.out.println(responseString);

                //System.out.println("PREPARSE");
                JsonParser jsonParser = new JsonParser();
                JsonObject rootobj = (JsonObject)jsonParser.parse(responseString);
                zip = rootobj.get("location").getAsJsonObject().get("zip").getAsString();
                System.out.println(zip);

            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch(IOException e) {
            System.out.print(e);
        }

        //========================================================================
        //////////////////////////////////////////////////////////////////////////
        //========================================================================

        //========================== Get Hourly Foracst =============================
        System.out.println("WATDO");
        String forecastURL = "http://api.wunderground.com/api/1496919d14a2864c/hourly/q/" + zip + ".json";
        HttpClient forecastHttpclient = new DefaultHttpClient();
        String[][] elementArray = null;
        try {
            HttpResponse response = forecastHttpclient.execute(new HttpGet(forecastURL));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                out.close();
                System.out.println(responseString);

                //System.out.println("PREPARSE");
                JsonParser jsonParser = new JsonParser();
                JsonObject rootobj = (JsonObject)jsonParser.parse(responseString);

                JsonArray hourlyForecastArray = rootobj.get("hourly_forecast").getAsJsonArray();

                String month;
                String day;
                String hour;
                String minute;

                String date;
                String previousDate = null;
                String time;

                String condition;
                String temp_f;
                String humidity;

                String icon_url;

                String[] dateArray = new String[hourlyForecastArray.size()];
                elementArray = new String[hourlyForecastArray.size()][6];

                for(int i = 0; i < hourlyForecastArray.size(); i++) {
                    // Getting Date and Time
                    month = hourlyForecastArray.get(i).getAsJsonObject().get("FCTTIME").getAsJsonObject().get("month_name").getAsString();
                    day = hourlyForecastArray.get(i).getAsJsonObject().get("FCTTIME").getAsJsonObject().get("mday").getAsString();
                    hour = hourlyForecastArray.get(i).getAsJsonObject().get("FCTTIME").getAsJsonObject().get("hour").getAsString();
                    minute = hourlyForecastArray.get(i).getAsJsonObject().get("FCTTIME").getAsJsonObject().get("min").getAsString();

                    date = month + " " + day;
                    time = hour + ":" + minute;

                    // Getting weather elements
                    condition = hourlyForecastArray.get(i).getAsJsonObject().get("condition").getAsString();
                    temp_f = hourlyForecastArray.get(i).getAsJsonObject().get("temp").getAsJsonObject().get("english").getAsString() + "\u00b0" + "F";
                    humidity = "Humidity: " + hourlyForecastArray.get(i).getAsJsonObject().get("humidity").getAsString() + "%";

                    // Getting icon url
                    icon_url = hourlyForecastArray.get(i).getAsJsonObject().get("icon_url").getAsString();

                    // Order for array:
                    // Icon Image > Date > Time > Temp > Condition > Humidity
                    // 0 = Icon Image
                    // 1 = Date
                    // 2 = Time
                    // 3 = Temp
                    // 4 = Condition
                    // 5 = Humidity
                    dateArray[i] = date;
                    elementArray[i][0] = icon_url;

                    if(!date.equals(previousDate)) {
                        elementArray[i][1] = date;
                    } else {
                        elementArray[i][1] = "";
                    }

                    elementArray[i][2] = time;
                    elementArray[i][3] = temp_f;
                    elementArray[i][4] = condition;
                    elementArray[i][5] = humidity;

                    previousDate = date;
                }


                //..more logic

            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch(IOException e) {
            System.out.print(e);
        }
        return elementArray;
    }
}
