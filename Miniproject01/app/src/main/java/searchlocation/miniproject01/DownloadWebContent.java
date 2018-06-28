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
    ArrayList<Bitmap> icons = new ArrayList<>();
    ArrayList<LatLng> locations = new ArrayList<>();
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
                locations.add(new LatLng(Double.parseDouble(location.getString("lat")),
                        Double.parseDouble(location.getString("lng"))));

                //save names
                names.add(place.getString("name"));

                //save icons
                /*DownloadIcon downloadIcon = new DownloadIcon();
                try {
                    icons.add(downloadIcon.execute(place.getString("icon")).get());
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
            getAddresses();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void getAddresses(){
        /*for(int i=0;i<size;++i){
            Log.i("Address",addresses.get(i));
        }*/
        //the mode private just share data in app only
        SharedPreferences sharedPreferences =context.getSharedPreferences
                ("searchlocation.miniproject01", Context.MODE_PRIVATE);

        //convert array list to string so that we can save it in shape of json object
        try {
            sharedPreferences.edit().putString("addresses",ObjectSerializer.serialize(addresses)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getLocations(){
        for(int i=0;i<size;++i){
            Log.i("Latitude",Double.toString(locations.get(i).latitude));
            Log.i("Latitude",Double.toString(locations.get(i).longitude));
        }
    }

    public void getNames(){
        for(int i=0;i<size;++i){
            Log.i("Name",names.get(i));
        }
    }
}
