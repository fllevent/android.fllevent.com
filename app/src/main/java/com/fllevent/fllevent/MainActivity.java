package com.fllevent.fllevent;

        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
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
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.StringRequest;

        import org.json.*;

        import java.net.HttpURLConnection;
        import java.net.URL;

public class MainActivity extends AppCompatActivity {
    URL url;
    HttpURLConnection urlConnection = null;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
        }
        textView = findViewById(R.id.textView);
        initRequestQueue();
    }
    public void initRequestQueue() {
        RequestQueue mRequestQueue;
    // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
    // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
    // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
    // Start the queue
        mRequestQueue.start();
        String url ="http://fllevent.com:4000/api/event/singleevent/testevent";
    // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        JSONObject JSONResponse = new JSONObject();

                        textView.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        textView.setText("Something went wrong");
                    }
                });
        mRequestQueue.add(stringRequest);
        }
    }