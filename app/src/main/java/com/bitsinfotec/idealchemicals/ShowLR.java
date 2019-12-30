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

import com.bitsinfotec.idealchemicals.Adapter.*;
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

public class ShowLR extends AppCompatActivity {
    ArrayList arr = new ArrayList();
    RequestParams requestParams = new RequestParams();
    private int inserted_id;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lr);

        setTitle("LR Details");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#12c2e9")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        requestParams.add("tbname", "lorry_receipt");

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(Routes.selectAll, requestParams, new AsyncHttpResponseHandler() {
            AlertDialog dialog = new SpotsDialog(ShowLR.this, R.style.Custom);

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
                                    hashMap.put("transporter", jo.get("Transporter").toString());
                                    hashMap.put("lrfrom", jo.get("_From").toString());
                                    hashMap.put("lrto", jo.get("_To").toString());
                                }
                                arr.add(hashMap);
                                L.L(arr + "---------------+++");
                            }
                            lr_adapter lrAdapter = new lr_adapter(arr, ShowLR.this);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lr_recycler);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ShowLR.this));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(lrAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        L.L(statusCode + str);
                        new DialogBox(ShowLR.this, str).asyncDialogBox();
                    }
                } catch (Exception ex) {
                    L.L(ex.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error) {
                new DialogBox(ShowLR.this, responseBody.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                dialog = ProgressDialog.show(ShowLR.this, "none to say",
                        "Saving.Please wait...", true);
            }

        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        Intent intent = new Intent(ShowLR.this, AdminActivity.class);
        startActivity(intent);
        return true;
    }
}
