package com.fllevent.fllevent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventDisplay extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    TextView textView;
    String eventName;
    int eventNum;
    ArrayList<String> list;
    MyRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);
        textView = (TextView) findViewById(R.id.textView2);
        Intent intent = getIntent();
        eventName = intent.getStringExtra(MainActivity.EVENT_NAME);
        eventNum = intent.getIntExtra(MainActivity.EXTRA_ID, 0);
        textView.setText(eventName);

        RequestQueue mRequestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024 * 128); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        String url ="http://fllevent.com:4000/api/event/singleevent/";
        url = url.concat(eventName);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        list = new ArrayList<String>();
                        for(int i = 0; i < response.length();i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                JSONArray matchArray = object.getJSONArray("Match");
                                for(int j = 0; j < matchArray.length(); j++) {
                                    JSONObject teamNameObj = matchArray.getJSONObject(j);
                                    String teamName = teamNameObj.getString("TeamName");
                                    list.add(teamName);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        initViewAdapter(list);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("Something went wrong");
                    }
                });
        mRequestQueue.add(jsonArrayRequest);
    }

    public void initViewAdapter(ArrayList<String> dataList) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, dataList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
