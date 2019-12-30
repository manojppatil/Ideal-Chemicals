package com.bitsinfotec.idealchemicals;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitsinfotec.idealchemicals.Helper.L;
import com.bitsinfotec.idealchemicals.Helper.Routes;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;
import lecho.lib.hellocharts.model.SliceValue;

public class CheckAutocmplt extends AppCompatActivity implements AdapterView.OnItemClickListener {
    AutoCompleteTextView auto;
    TextView tvDisplay;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_autocmplt);
        auto = findViewById(R.id.autocc);
        tvDisplay = findViewById(R.id.tvDisplay);
        setTitle("AutocompleteTv");

        RequestParams requestParams = new RequestParams();
        requestParams.add("tbname", "Address_Book_Baroda");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(Routes.selectAllByQuery, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                L.L(responseBody + "");
                str = new String(responseBody);
                L.L(str);
                ArrayList party_names = new ArrayList();
                try {
                    JSONArray jr = new JSONArray(str);

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jo = jr.getJSONObject(i);
                        String name = jo.getString("Party_Name");
//                        L.L(name+"");
                        party_names.add(name+"");

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckAutocmplt.this, android.R.layout.simple_list_item_1, party_names);
                    auto.setAdapter(adapter);
                    auto.setOnItemClickListener(CheckAutocmplt.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // fetch the user selected value
        String item = parent.getItemAtPosition(position).toString();

        // create Toast with user selected value
        Toast.makeText(CheckAutocmplt.this, "Selected Item is: \t" + item, Toast.LENGTH_LONG).show();

        // set user selected value to the TextView
        tvDisplay.setText(item);


    }
}
