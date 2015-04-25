
package com.andrewsummers.otashu.task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class UploadFileTask extends AsyncTask<String, Void, Integer> {
    File path = Environment.getExternalStorageDirectory();
    String externalDirectory = path.toString() + "/otashu/";
    File bitmapSource = new File(externalDirectory + "emofing.png");

    @Override
    protected Integer doInBackground(String... urls) {
        if (urls.length > 0) {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeFile(urls[0]);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bout);
            byte[] byte_array = bout.toByteArray();
            String image = Base64.encodeToString(byte_array, Base64.DEFAULT);

            ArrayList<NameValuePair> form = new ArrayList<NameValuePair>();
            form.add(new BasicNameValuePair("image", image));
            Log.d("MYLOG", image);

            try {
                String serverUrl = "";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(serverUrl);
                httpPost.setEntity(new UrlEncodedFormEntity(form));
                HttpResponse response = httpClient.execute(httpPost);

            } catch (Exception e) {
                Log.d("MYLOG", e.toString());
            }

            return 0;
        } else {
            return 1;
        }
    }
}
