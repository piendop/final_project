package searchlocation.miniproject01;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//class to get web content
public class DownloadWebContent extends AsyncTask<String,Void,String> {
    JSONObject object;
    JSONArray array;
    int size=10;
    ArrayList<String> addresses = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> icons = new ArrayList<>();
    ArrayList<String> lats = new ArrayList<>();
    ArrayList<String> lngs = new ArrayList<>();
    public Context context;

    public DownloadWebContent(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);//urls[0] can be empty ==> need catch and try
            //we might not open the connection ==> need catch and try
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            int data = reader.read();
            String result = "";
            while (data != -1) {
                result += (char) data;
                data = reader.read();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed successfully";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            object=new JSONObject(result);
            String places = object.getString("results");
            array = new JSONArray(places);


            if(array.length()<size) size=array.length();
            //save top 10 places
            for (int i=0;i<size;++i){
                JSONObject place = array.getJSONObject(i);
                //save addresses
                addresses.add(place.getString("formatted_address"));

                //save locations
                JSONObject geometry = place.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                lats.add(location.getString("lat"));
                lngs.add(location.getString("lng"));

                //save names
                names.add(place.getString("name"));

                //save icons
                icons.add(place.getString("icon"));

            }
            getAddresses();
            getLocations();
            getNames();
            getIcons();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getIcons() {
        //the mode private just share data in app only
        SharedPreferences sharedPreferences =context.getSharedPreferences
                ("searchlocation.miniproject01", Context.MODE_PRIVATE);

        //convert array list to string so that we can save it in shape of json object
        try {
            sharedPreferences.edit().putString("icons",ObjectSerializer.serialize(icons)).apply();
            MapsActivity.isSearchActive = sharedPreferences.edit().putString("addresses",
                    ObjectSerializer.serialize(addresses)).commit();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getAddresses(){
        for(int i=0;i<size;++i){
            Log.i("Address",addresses.get(i));
        }
        //the mode private just share data in app only
        SharedPreferences sharedPreferences =context.getSharedPreferences
                ("searchlocation.miniproject01", Context.MODE_PRIVATE);



        //convert array list to string so that we can save it in shape of json object
        try {
            sharedPreferences.edit().putString("addresses",ObjectSerializer.serialize(addresses)).apply();
            MapsActivity.isSearchActive = sharedPreferences.edit().putString("addresses",
                    ObjectSerializer.serialize(addresses)).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getLocations(){
        for(int i=0;i<size;++i){
            Log.i("Latitude",lats.get(i));
            Log.i("Latitude",lngs.get(i));
        }
        //the mode private just share data in app only
        SharedPreferences sharedPreferences =context.getSharedPreferences
                ("searchlocation.miniproject01", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //convert array list to string so that we can save it in shape of json object
        try {
            editor.putString("latitudes",ObjectSerializer.serialize(lats)).apply();
            editor.putString("longitudes",ObjectSerializer.serialize(lngs)).apply();
            MapsActivity.isSearchActive=editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getNames(){
        for(int i=0;i<size;++i){
            Log.i("Name",names.get(i));
        }
        //the mode private just share data in app only
        SharedPreferences sharedPreferences =context.getSharedPreferences
                ("searchlocation.miniproject01", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //convert array list to string so that we can save it in shape of json object
        try {
            editor.putString("names",ObjectSerializer.serialize(names)).apply();
            MapsActivity.isSearchActive = editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
