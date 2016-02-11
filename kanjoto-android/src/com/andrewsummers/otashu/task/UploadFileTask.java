
package com.andrewsummers.otashu.task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

    // params[0] is server URL
    // params[1] is file to upload
    // params[2] is file type
    @Override
    protected Integer doInBackground(String... params) {
        if (params.length > 0) {
            try {
                // TODO: take out loop later... for some reason POSTs often take two tries to send
                // emofing successfully
                for (int i = 0; i < 2; i++) {
                    String serverUrl = params[0];
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(serverUrl);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    if (params[2] == "image") {
                        Bitmap bitmap = BitmapFactory.decodeFile(params[1]);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    } else {
                        File fileUpload = new File(params[1]);
                        InputStream is = new FileInputStream(fileUpload);
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                        is.close();
                    }
                    byte[] byte_array = bos.toByteArray();
                    String file = Base64.encodeToString(byte_array, Base64.DEFAULT);
                    ArrayList<NameValuePair> form = new ArrayList<NameValuePair>();
                    form.add(new BasicNameValuePair("file", file));
                    httpPost.setEntity(new UrlEncodedFormEntity(form));

                    HttpResponse response = httpClient.execute(httpPost);
                    Log.d("MYLOG", "> http response: " + response.getStatusLine());
                    response.getEntity().consumeContent();
                }
            } catch (Exception e) {
                Log.d("MYLOG", e.toString());
            }

            return 0;
        } else {
            return 1;
        }
    }
}
