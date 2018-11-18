package com.fllevent.fllevent;

        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.widget.TextView;
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
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.JsonRequest;
        import com.android.volley.toolbox.StringRequest;
        import org.json.*;

        import java.util.ArrayList;
        import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    MyRecyclerViewAdapter adapter;
    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
        }
        textView = findViewById(R.id.textView);

        RequestQueue mRequestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024 * 128); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        String url ="http://fllevent.com:4000/api/event/allevents";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        list = new ArrayList<String>();
                        for(int i = 0; i < response.length();i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                String eventName = object.getString("EventName");
                                list.add(eventName);
                                //textView.setText(list.toString());
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
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        textView.setText(list.toString());
    }
}