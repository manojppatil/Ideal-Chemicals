package com.bitsinfotec.idealchemicals;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import com.bitsinfotec.idealchemicals.Adapter.challan_adapter;
import com.bitsinfotec.idealchemicals.Helper.DialogBox;
import com.bitsinfotec.idealchemicals.Helper.L;
import com.bitsinfotec.idealchemicals.Helper.Routes;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

public class Show_challan extends AppCompatActivity {
    ArrayList arr = new ArrayList();
    RequestParams requestParams = new RequestParams();
    private int inserted_id;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_challan);

        setTitle("Challan Details");
        requestParams.add("tbname", "challan_table");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#12c2e9")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(Routes.selectAll, requestParams, new AsyncHttpResponseHandler() {
            AlertDialog dialog = new SpotsDialog(Show_challan.this, R.style.Custom);

            public void onStart() {
                super.onStart();
                dialog.show();
            }

            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                dialog.dismiss();
                String str = new String(responseBody);
//                str = str.replace("<br>", "\n");
                JSONArray jr = null;
                JSONObject jo = null;
                L.L(str);
                try {
                    if (statusCode == 200) {
                        try {
                            jr = new JSONArray(str);
                            for (int i = 0; i < jr.length(); i++) {
                                jo = jr.getJSONObject(i);
                                HashMap hashMap = new HashMap();
                                if (jo != null) {
                                    hashMap.put("id", jo.get("id").toString());
                                    hashMap.put("chto", jo.get("name").toString());
                                    hashMap.put("receiver", jo.get("Receiver_name").toString());

                                }
                                arr.add(hashMap);
                            }
                            challan_adapter chAdapter = new challan_adapter(arr, Show_challan.this);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.challan_recycler);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Show_challan.this));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(chAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        L.L(statusCode + str);
                        new DialogBox(Show_challan.this, str).asyncDialogBox();
                    }
                } catch (Exception ex) {
                    L.L(ex.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error) {
                new DialogBox(Show_challan.this, responseBody.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                dialog = ProgressDialog.show(Show_challan.this, "none to say",
                        "Saving.Please wait...", true);
            }

        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        Intent intent = new Intent(Show_challan.this, AdminActivity.class);
        startActivity(intent);
        return true;
    }

}
