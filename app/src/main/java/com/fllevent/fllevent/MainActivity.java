package com.fllevent.fllevent;

        import android.Manifest;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
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
        import org.json.*;

        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    TextView textView;
    MyRecyclerViewAdapter adapter;
    ArrayList<String> list;
    ArrayList<String> searchList;
    public static final String EVENT_NAME = "com.fllevent.fllevent.NAME";
    public static final String EXTRA_ID = "com.fllevent.fllevent.ID";
    boolean searched = false;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
        }
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        RequestQueue mRequestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024 * 128); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        String url ="http://fllevent.com:4000/api/v1/event/allevents";
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
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        //textView.setText(list.toString());
    }


    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, EventDisplay.class);
        intent.putExtra(EXTRA_ID,position);
        intent.putExtra(EVENT_NAME,adapter.getItem(position));
        startActivity(intent);
    }

    public void search(View view) {
        Button button = findViewById(R.id.button);
        if(searched) {
            button.setText("Search");
            editText.setText("");
            searched = false;
        }
        else if(!searched) {
            button.setText("Clear");
            searched = true;
        }
        String searchData = editText.getText().toString();
        searchList = new ArrayList<String>();
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).contains(searchData)) {
                searchList.add(list.get(i));
            }
        }
        if(!searchList.isEmpty()) {
            initViewAdapter(searchList);
        } else {
            searchList.add("No results");
            initViewAdapter(searchList);
        }
        if(searchList.size() == 1 && !searchList.get(0).equals("No results")) {
            Intent intent = new Intent(this, EventDisplay.class);
            intent.putExtra(EXTRA_ID,0);
            intent.putExtra(EVENT_NAME,adapter.getItem(0));
            startActivity(intent);
        }
    }
}