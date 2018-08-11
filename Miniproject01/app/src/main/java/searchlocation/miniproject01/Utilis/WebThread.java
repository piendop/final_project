package searchlocation.miniproject01.Utilis;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import searchlocation.miniproject01.Search.MapsActivity;

public class WebThread extends HandlerThread {

    private Handler mHandler;
    private Handler mResponeHandler;
    private static final String TAG = WebThread.class.getSimpleName();
    private Callback mCallback;
    String result;
    long startTime ;
    long endTime   ;
    long totalTime ;


    public interface Callback{
        public void onWebContentDownloaded();
    }

    public WebThread(Handler responeHandler,  Callback callback){
        super(TAG);
        mResponeHandler=responeHandler;
        mCallback=callback;
    }


    public void queueTask(String url, int code){
        result=url;
        mHandler.obtainMessage(code).sendToTarget();//obtain message code and send to messeage queue
    }

    public void handleRequest(){//create separate thread to load content
        if(mHandler==null){
            mHandler = new Handler(getLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if(msg.what==0){
                        try {
                            startTime=System.nanoTime();
                            startTime=System.nanoTime();
                            if(!result.isEmpty()) {
                                JSONObject object = new JSONObject(result);
                                String places = object.getString("results");
                                JSONArray array = new JSONArray(places);

                                int size = 5;
                                if (array.length() < size) size = array.length();
                                //save top 10 places
                                for (int i = 0; i < size; ++i) {
                                    JSONObject place = array.getJSONObject(i);
                                    //save addresses
                                    MapsActivity.addresses.add(place.getString("formatted_address"));

                                    //save locations
                                    JSONObject geometry = place.getJSONObject("geometry");
                                    JSONObject location = geometry.getJSONObject("location");
                                    MapsActivity.locations.add(new LatLng(Double.parseDouble(location.getString("lat")),
                                            Double.parseDouble(location.getString("lng"))));

                                    //save names
                                    MapsActivity.names.add(place.getString("name"));

                                    if (i == 0) {
                                        //save icons
                                        String icons;
                                        icons = place.getString("icon");
                                        DownloadIcon downloadIcon = new DownloadIcon();
                                        try {
                                            MapsActivity.icon = downloadIcon.execute(icons).get();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                                mResponeHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mCallback.onWebContentDownloaded();
                                    }
                                });
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    endTime=System.nanoTime();
                    totalTime=(endTime-startTime)/1000000000;
                    Log.i("Time save",Long.toString(totalTime));
                    return true;
                }
            });
        }
    }
}
