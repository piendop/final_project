package searchlocation.miniproject01.Utilis;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadIcon extends AsyncTask<String,Void,Bitmap> {
    @Override
    protected Bitmap doInBackground(String... urls){
        try{
            URL url = new URL(urls[0]);//get url from string
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();//open a connection

            connection.connect();//to connect to our url

            InputStream inputStream = connection.getInputStream();//create a stream to implement

            Bitmap myBitmap= BitmapFactory.decodeStream(inputStream);//convert image from stream to bitmap

            return myBitmap;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }
}
