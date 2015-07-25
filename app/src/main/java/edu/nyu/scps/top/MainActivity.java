package edu.nyu.scps.top;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "3263f9d73ef1b9a1bd4a66174b1e832f:14:71881873";
        String urlString = "http://api.nytimes.com/svc/topstories/v1/home.json?api-key=" + apiKey;

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(urlString);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urlString) {
            //Must be declared outside of try block,
            //so we can mention them in finally block.
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(urlString[0]);
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    return null;
                }
                return buffer.toString();
            } catch (IOException exception) {
                Log.e("myTag", "doInBackground ", exception);
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException exception) {
                        Log.e("myTag", "doInBackground ", exception);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String json) {
            useTheResult(json);
        }
    }

    private void useTheResult(String json) {
        TextView empty = (TextView)findViewById(android.R.id.empty);
        int n;  //number of articles
        String[] title;
        String[] abs;  //"abstract" is Java keyword
        final String[] url;

        if (json == null) {
            empty.setText("Couldn't get JSON string from server.");
            return;
        }

        try {
            JSONObject jSONObject = new JSONObject(json);
            JSONArray results = jSONObject.getJSONArray("results");
            n = results.length();
            title = new String[n];
            abs = new String[n];
            url = new String[n];

            for (int i = 0; i < n; ++i) {
                JSONObject result = results.getJSONObject(i);
                title[i] = result.getString("title");
                abs[i] = result.getString("abstract");
                url[i] = result.getString("url");
            }
        } catch (JSONException exception) {
            empty.setText(exception.toString());
            return;
        }

        ArticleAdapter adapter = new ArticleAdapter(this, title, abs);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setEmptyView(empty);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(url[position]);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri); //find web browser
                startActivity(intent); //To return to here, touch Android back button.
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
