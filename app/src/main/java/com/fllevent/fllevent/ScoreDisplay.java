package com.fllevent.fllevent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class ScoreDisplay extends AppCompatActivity {
    TextView textView;
    ArrayList<Integer> list;
    String teamName;
    int teamNum;
    String eventName;
    String matchScore1 = "20";
    String matchScore2 = "15";
    String matchScore3 = "10";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_display);

        Intent intent = getIntent();
        teamName = intent.getStringExtra(EventDisplay.TEAM_NAME);
        teamNum = intent.getIntExtra(EventDisplay.TEAM_NUM, 0);
        eventName = intent.getStringExtra(EventDisplay.EVENT_NAME);
        RequestQueue mRequestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024 * 128); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        String url ="http://fllevent.com:4000/api/v1/team/singleteam/";
        url = url.concat(Integer.toString(teamNum));
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                            try {
                                JSONObject object = response.getJSONObject(0);
                                    matchScore1 = object.getString("MatchScoreOne");

                                    //list.add(matchScore1);
                                    matchScore2 = object.getString("MatchScoreTwo");
                                    //list.add(matchScore2);
                                    matchScore3 = object.getString("MatchScoreThree");
                                    //list.add(matchScore3);
                                TextView textView = findViewById(R.id.textView3);
                                TextView textView2 = findViewById(R.id.textView4);
                                TextView textView3 = findViewById(R.id.textView5);
                                textView.setText(matchScore1);
                                textView2.setText(matchScore2);
                                textView3.setText(matchScore3);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("Something went wrong");
                        error.printStackTrace();
                    }
                });
        mRequestQueue.add(jsonArrayRequest);


    }

}
