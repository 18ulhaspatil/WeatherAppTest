package com.example.ulhas.weather.app.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WeatherReport extends Activity {

    ListView listView;
    GPS_Location gpsObj;
    double Latitude, Longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_report);
        System.out.println("Inside WeatherRport");
        listView = (ListView) findViewById(R.id.listView);
        gpsObj = new GPS_Location(this);
        Latitude = gpsObj.getLatitude();
        Longitude = gpsObj.getLongitude();
        String Url = "http://api.openweathermap.org/data/2.5/forecast/daily?lat="
                + Latitude + "&lon=" + Longitude + "&cnt=14&mode=json";
        new getJSON().execute(Url);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String exit = "exit";
        Intent intent = new Intent(this, NoInternetConnection.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("exit", exit);
        startActivity(intent);
    }


    private class getJSON extends AsyncTask<String, String, String> {
        String cityName;
        String Latitude, Longitude;
        ArrayList<String> humidity;
        ArrayList<String> speed;
        ArrayList<String> weather;
        ArrayList<String> tempMin;
        ArrayList<String> tempMax;
        ArrayList<String> description;
        ArrayList<String> icon;

        @Override
        protected String doInBackground(String... params) {
            String Url = params[0];
            humidity = new ArrayList<String>();
            speed = new ArrayList<String>();
            weather = new ArrayList<String>();
            tempMin = new ArrayList<String>();
            tempMax = new ArrayList<String>();
            icon = new ArrayList<String>();
            description = new ArrayList<String>();
            String data;
            try {
                HttpClient hClient = new DefaultHttpClient();
                HttpGet hGet = new HttpGet(Url);
                ResponseHandler<String> rHandler = new BasicResponseHandler();
                data = hClient.execute(hGet, rHandler);
                JSONObject jObj = new JSONObject(data);
                JSONObject jsonObject = jObj.getJSONObject("city");
                cityName = jsonObject.getString("name");
                JSONObject jObjCoOrd = jsonObject.getJSONObject("coord");
                Latitude = jObjCoOrd.getString("lat");
                Longitude = jObjCoOrd.getString("lon");

                JSONArray jsonArray = jObj.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    String humidityString = object.getString("humidity");
                    humidity.add(humidityString);
                    String speedString = object.getString("speed");
                    speed.add(speedString);

                    JSONArray weatherArray = object.getJSONArray("weather");
                    JSONObject weatherObj = weatherArray.getJSONObject(0);

                    String iconString = weatherObj.getString("icon");
                    final String iconURL = "http://openweathermap.org/img/w/"
                            + iconString + ".png";
                    icon.add(iconURL);
                    String descriptionString = weatherObj
                            .getString("description");
                    description.add(descriptionString);

                    JSONObject tempObj = object.getJSONObject("temp");

                    String minTemp = tempObj.getString("min");
                    tempMin.add(minTemp);

                    String maxTemp = tempObj.getString("max");
                    tempMax.add(maxTemp);
                }
            } catch (Exception e) {
                Log.w("Error", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            listView.setAdapter(new ListViewCustomAdapter(WeatherReport.this,
                    Latitude, Longitude, cityName, humidity, speed, icon,
                    description, tempMin, tempMax));
        }

        private class ListViewCustomAdapter extends BaseAdapter {
            Context context;
            public LayoutInflater inflater;
            String Latitude, Longitude, cityName;
            String[] Humidity, Speed, Description1, minTemp, maxTemp;
            ArrayList<String> bitmap;
            int totalDisplayDatasize = 0;

            public ListViewCustomAdapter(Context context, String latitude,
                                         String longitude, String cityName,
                                         ArrayList<String> humidity, ArrayList<String> speed,
                                         ArrayList<String> image, ArrayList<String> description,
                                         ArrayList<String> tempMin, ArrayList<String> tempMax) {
                this.context = context;
                this.Latitude = latitude;
                this.Longitude = longitude;
                this.cityName = cityName;
                this.bitmap = image;
                this.Humidity = humidity.toArray(new String[humidity.size()]);
                this.Speed = speed.toArray(new String[speed.size()]);
                this.Description1 = description.toArray(new String[description
                        .size()]);
                this.minTemp = tempMin.toArray(new String[tempMin.size()]);
                this.maxTemp = tempMax.toArray(new String[tempMax.size()]);

                if (this.Humidity != null)
                    totalDisplayDatasize = this.Humidity.length;
                System.out.println("Inside ListViewCustomAdapter ");
            }

            @Override
            public int getCount() {
                return totalDisplayDatasize;
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            private class Holder {
                TextView City, MinTemp, MaxTemp, Description, Speed, Latitude,
                        Longitude, Humidity;
                ImageView cloud;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Holder holder = null;
                View view = convertView;
                System.out.println("Inside getView");
                if (view == null) {
                    System.out.println("Inside if getView");
                    holder = new Holder();
                    convertView = LayoutInflater.from(this.context).inflate(
                            R.layout.weather_report_single_item, null);
                    holder.cloud = (ImageView) convertView.findViewById(R.id.cloud);
                    holder.Humidity = (TextView) convertView.findViewById(R.id.humidity);
                    holder.City = (TextView) convertView.findViewById(R.id.cityName);
                    holder.MinTemp = (TextView) convertView
                            .findViewById(R.id.minTemp);
                    holder.MaxTemp = (TextView) convertView
                            .findViewById(R.id.maxTemp);
                    holder.Description = (TextView) convertView
                            .findViewById(R.id.weather);
                    holder.Speed = (TextView) convertView
                            .findViewById(R.id.speed);
                    holder.Latitude = (TextView) convertView
                            .findViewById(R.id.latitude);
                    holder.Longitude = (TextView) convertView
                            .findViewById(R.id.longitude);

                    convertView.setTag(holder);
                } else {
                    System.out.println("Inside else getView");
                    holder = (Holder) convertView.getTag();
                }
                holder.City.setText(cityName);
                holder.Latitude.setText(Latitude);
                holder.Longitude.setText(Longitude);
                holder.MaxTemp.setText(maxTemp[position]);
                holder.Humidity.setText(Humidity[position]);
                holder.MinTemp.setText(minTemp[position]);
                holder.Speed.setText(Speed[position]);
                holder.Description.setText(Description1[position]);
                Picasso.with(context).load(bitmap.get(position)).into(holder.cloud);
                return convertView;
            }
        }
    }

}