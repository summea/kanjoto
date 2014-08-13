package com.andrewsummers.otashu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * GetRemoteNotesetActivity is used to get a remote source of noteset data
 * which can then be used in a variety of ways.
 */
public class GetRemoteNotesetActivity extends Activity {
	private TextView notesetData;

	/**
	 * onCreate override used to start async task that makes a remote REST GET
	 * call in order to obtain remote noteset data.
	 * 
	 * @param savedInstanceState	Current application state data.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get specific layout for content view
		setContentView(R.layout.activity_get_remote_noteset);
		
		notesetData = (TextView)findViewById(R.id.remote_noteset);
		
		// call async task and execute REST GET request on remote server
		new HttpAsyncTask().execute("http://screenplays.herokuapp.com/welcome/get_notesets.json");
	}
	
	/**
	 * HttpAsyncTask is used as an HTTP wrapper for AsyncTask.
	 * 
	 * HttpAsyncTask originally based on a code tutorial which can be found at:
	 * http://hmkcode.com/android-parsing-json-data/
	 */
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		/**
		 * doInBackground implements the HTTP wrapper for this particular
		 * asynchronous task.
		 * 
		 * @param urls	Incoming URLs (though only the first URL is used).
		 */
		@Override
		protected String doInBackground(String... urls) {
			InputStream inputStream = null;
			String result = "";
			String url = urls[0];
			
			try {
				// create HTTP client
				HttpClient httpClient = new DefaultHttpClient();
				
				// make GET request to given URL
				HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
				
				// receive response as input stream
				inputStream = httpResponse.getEntity().getContent();
				
				// convert input stream to string
				if (inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = getResources().getString(R.string.http_request_failed);
			} catch (Exception e) {
				Log.d("MYLOG", e.getLocalizedMessage());
			}
			
			return result;
		}
		
		/**
		 * convertInputStreamToString turns an input stream into a string. 
		 * 
		 * @param inputStream	The incoming input stream to be converted.
		 * @return	<String> of converted input stream.
		 */
		private String convertInputStreamToString(InputStream inputStream) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String result = "";
			String line = "";
			
			try {
				while ((line = bufferedReader.readLine()) != null) {
					result += line;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// close input stream
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return result;
		}
		
		/**
		 * onPostExecute displays incoming, remote data once received.
		 * 
		 * @param result	Incoming, remote data source.
		 */
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Received HTTP get data!", Toast.LENGTH_SHORT).show();
			
			try {
				// parse incoming HTTP data as JSON objects and JSON arrays
				JSONObject jsonObj = new JSONObject(result);
				String jsonTextResult = "";
				
				JSONArray emotions = jsonObj.getJSONArray("emotion");
				JSONArray notevalues = jsonObj.getJSONArray("notevalues");
				
				jsonTextResult += emotions.join(",");
				jsonTextResult += notevalues.join(",");

				// display received JSON data
				notesetData.setText(jsonTextResult);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}