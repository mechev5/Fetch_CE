package com.example.fetch_ce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

// DataAdapter.java
// Custom adapter for listview displaying JSON data
public class DataAdapter extends ArrayAdapter<JSONObject> {
  public DataAdapter(@NonNull Context context, List<JSONObject> data) {
	super(context, 0, data);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
	JSONObject jsonObject = getItem(position);

	if (convertView == null) {
	  convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_row, parent, false);
	}
	TextView id = convertView.findViewById(R.id.tv_id);
	TextView listId = convertView.findViewById(R.id.tv_listid);
	TextView name = convertView.findViewById(R.id.tv_name);

	try {
	  assert jsonObject != null;
	  id.setText(jsonObject.getString("id"));
	  listId.setText(jsonObject.getString("listId"));
	  name.setText(jsonObject.getString("name"));
	} catch (JSONException e) {
	  throw new RuntimeException(e);
	}
	return convertView;
  }
}
