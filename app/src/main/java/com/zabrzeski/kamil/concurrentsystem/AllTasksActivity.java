package com.zabrzeski.kamil.concurrentsystem;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllTasksActivity extends ListActivity {

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_START = "start";
    private static final String TAG_END = "end";
    private static final String TAG_ISFSINISHED = "is_finished";
    // url to get all products list
    private static String url_all_products = "http://zuku7.idl.pl/android/get_tasks.php";
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList;
    // products JSONArray
    JSONArray products = null;
    int id = 0;
    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tasks);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            id = extras.getInt("id");

        }
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllProducts().execute();

        // Get listview
        ListView lv = getListView();

        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();


            }
        });

    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllTasksActivity.this);
            pDialog.setMessage("Loading tasks. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json;
            if (id == 0)
                json = jParser.makeHttpRequest(url_all_products, "GET", params);
            else {
                List<NameValuePair> paramsU = new ArrayList<NameValuePair>();
                paramsU.add(new BasicNameValuePair("id", Integer.toString(id)));
                json = jParser.makeHttpRequest(url_all_products, "GET", paramsU);
            }


            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        String start = c.getString(TAG_START);
                        String end = c.getString(TAG_END);
                        String isFinished = c.getString(TAG_ISFSINISHED);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_START, start);
                        map.put(TAG_END, end);
                        if (isFinished.equals("1")) {
                            map.put(TAG_ISFSINISHED, "Finished");
                        } else {
                            map.put(TAG_ISFSINISHED, "Ongoing");
                        }


                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                } else {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllTasksActivity.this, productsList,
                            R.layout.list_item, new String[]{TAG_PID,
                            TAG_NAME, TAG_START, TAG_END, TAG_ISFSINISHED},
                            new int[]{R.id.pid, R.id.name, R.id.start, R.id.end, R.id.is_finished});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}
