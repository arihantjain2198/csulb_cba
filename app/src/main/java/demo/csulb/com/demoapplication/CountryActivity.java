package demo.csulb.com.demoapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CountryActivity extends AppCompatActivity {

    private ListView country_list;
    private static final String TAG = "CountryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
        country_list = (ListView) findViewById(R.id.country_list);

        new AsyncTaskRunner().execute();
    }

    private void updateCountires(String[] countries) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, countries);
        country_list.setAdapter(adapter);
    }

    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                logoutUser();
                return true;
            case R.id.uninstall:
                Uri packageUri = Uri.parse("package:demo.csulb.com.demoapplication");
                startActivity(new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        String[] countries = null;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("http://opentable.herokuapp.com/api/countries");
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "doInBackground: Server Response: " + server_response);
                    try {
                        JSONObject jsonObject = new JSONObject(server_response);
                        JSONArray jsonArray = jsonObject.getJSONArray("countries");
                        int length = jsonArray.length();
                        countries = new String[length];
                        for (int i = 0; i < length; i++) {
                            countries[i] = jsonArray.getString(i);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "doInBackground: Error while parsing JSON data", e);
                    }
                } else {
                    showMessage("Error while fetching data, Server Response Code: " + responseCode);
                }
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: Exception while fetching data", e);
                showMessage("Error while fetching data");
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            updateCountires(countries);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CountryActivity.this, "Fetching Data", "Please wait...", false, false);
        }
    }
}
