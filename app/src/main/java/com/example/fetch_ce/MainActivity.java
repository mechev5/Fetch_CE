// Marcos Echevarria
// Fetch Rewards Coding Exercise - Software Engineering - Mobile
// 9/14/24

package com.example.fetch_ce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private ListView listView;
  private List<JSONObject> jsonData = new ArrayList<>();
  private DataAdapter dataAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	listView = findViewById(R.id.listView);
	GetData getData = new GetData();
	getData.start();
	dataAdapter = new DataAdapter(this, jsonData);
	listView.setAdapter(dataAdapter);
  }

  // Use a separate thread to read JSON file
  public class GetData extends Thread {
	@Override
	public void run() {
	  try {
		// Read in data from url
		URL url = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		StringBuilder buffer = new StringBuilder();
		while ((line = br.readLine()) != null) {
		  buffer.append(line+"\n");
		}

		if (buffer.length() > 0) {
		  JSONArray jsonArray = new JSONArray(buffer.toString().trim());
		  jsonData.clear();
		  // Populate jsonData array
		  for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject temp = jsonArray.getJSONObject(i);

			// Filter out any items where "name" is blank or null.
			if (!temp.getString("name").equals("null") && !temp.getString("name").isEmpty()) {
			  jsonData.add(temp);
			}
			// Sort by listID
			Collections.sort(jsonData, new Comparator<JSONObject>() {
			  @Override
			  public int compare(JSONObject o1, JSONObject o2) {
				try {
				  Integer left = o1.getInt("listId");
				  Integer right = o2.getInt("listId");
				  Integer left2 = o1.getInt("id");
				  Integer right2 = o2.getInt("id");
				  int ret = left.compareTo(right);
				  // Sort by name
				  // Name correlates to id, so compare by id
				  if (ret == 0) {
					return left2.compareTo(right2);
				  } else {
					return ret;
				  }
				} catch (JSONException e) {
				  e.printStackTrace();
				  return 0;
				}
			  }
			});
		  }
		}

		// Sort by
	  } catch (IOException e) {
		e.printStackTrace();
	  } catch (JSONException e) {
		e.printStackTrace();
		throw new RuntimeException(e);
	  } finally {
		Log.i("TAG", "Data read");
		new Handler(Looper.getMainLooper()).post(() -> {
		  // Populate listview by notifying the adapter
		  dataAdapter.notifyDataSetChanged();
		});
	  }
	}
  }

}