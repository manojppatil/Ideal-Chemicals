package com.bitsinfotec.idealchemicals;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class DeliveryOrder extends AppCompatActivity {
    EditText add, date1, hsn_no, quantity, vehicle_no, favour_add, gst_no, sign_lorry, date_lorry;
    private int inserted_id;
    Context context;
    EditText date;
    AutoCompleteTextView do_To, product, favour_name;
    String name, prod_name, endorsed_name;
    ArrayList party_names = new ArrayList();
    ArrayList ids = new ArrayList();
    ArrayList addresses = new ArrayList();
    ArrayList product_names = new ArrayList();
    ArrayList hsn_code = new ArrayList();
    ArrayList endorsed_party_names = new ArrayList();
    ArrayList endorsed_ids = new ArrayList();
    ArrayList endorsed_addresses = new ArrayList();
    ArrayList endorsed_gstin = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order);
        setTitle("Delivery Order");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#12c2e9")));
        actionBar.setDisplayHomeAsUpEnabled(true);


        RequestParams requestParams = new RequestParams();
        requestParams.add("tbname", "Address_Book_Ulhasnagar");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(Routes.selectAllByQuery, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                name = new String(responseBody);
                L.L(name);
                try {
                    JSONArray jr = new JSONArray(name);

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jo = jr.getJSONObject(i);
                        String name = jo.getString("Party_Name");
                        party_names.add(name + "");
                        party_names.indexOf(name);
                        String aid = jo.getString("id");
                        ids.add(aid + "");
                        addresses.add(jo.getString("Address"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DeliveryOrder.this, android.R.layout.simple_list_item_1, party_names);
                    do_To.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        RequestParams requestParams1 = new RequestParams();
        requestParams1.add("tbname", "HSN_Code");
        AsyncHttpClient asyncHttpClient1 = new AsyncHttpClient();
        asyncHttpClient1.post(Routes.selectAllByQuery, requestParams1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prod_name = new String(responseBody);
                L.L(prod_name);
                try {
                    JSONArray jr = new JSONArray(prod_name);

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jo = jr.getJSONObject(i);
                        String name = jo.getString("NAME_OF_THE_PRODUCT");
                        product_names.add(name + "");
                        product_names.indexOf(name);
                        String aid = jo.getString("id");
                        ids.add(aid + "");
                        hsn_code.add(jo.getString("HSN_CODE"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DeliveryOrder.this, android.R.layout.simple_list_item_1, product_names);
                    product.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        RequestParams requestParams3 = new RequestParams();
        requestParams3.add("tbname", "Address_Book_Baroda");
        AsyncHttpClient asyncHttpClient3 = new AsyncHttpClient();
        asyncHttpClient3.post(Routes.selectAllByQuery, requestParams3, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                name = new String(responseBody);
                L.L(name);
                try {
                    JSONArray jr = new JSONArray(name);

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jo = jr.getJSONObject(i);
                        String name = jo.getString("Party_Name");
                        endorsed_party_names.add(name + "");
                        endorsed_party_names.indexOf(name);
                        endorsed_ids.add(jo.getString("id"));
                        endorsed_addresses.add(jo.getString("Address"));
                        endorsed_gstin.add(jo.getString("GSTIN_NO"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DeliveryOrder.this, android.R.layout.simple_list_item_1, endorsed_party_names);
                    favour_name.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        do_To = findViewById(R.id.do_to);
        do_To.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String strid = parent.getItemAtPosition(position).toString();
                String selectItem = do_To.getText().toString();
                int partinamesid = party_names.indexOf(selectItem);

                String addr = (String) addresses.get(partinamesid);
                add.setText(addr);
            }
        });
        add = findViewById(R.id.do_add);
        date1 = findViewById(R.id.do_date);
        hsn_no = findViewById(R.id.hsn_no);
        product = findViewById(R.id.product);
        product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String strid = parent.getItemAtPosition(position).toString();
                String selectItem = product.getText().toString();
                int hsnid = product_names.indexOf(selectItem);

                String hsn = (String) hsn_code.get(hsnid);
                hsn_no.setText(hsn);
            }
        });
        quantity = findViewById(R.id.quantity);
        vehicle_no = findViewById(R.id.vehicle_no);
        favour_name = findViewById(R.id.endorsed_name);
        favour_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String strid = parent.getItemAtPosition(position).toString();
                String selectItem = favour_name.getText().toString();
                int partinamesid = endorsed_party_names.indexOf(selectItem);
                String add = (String) endorsed_addresses.get(partinamesid);
                favour_add.setText(add);

                String gst = (String) endorsed_gstin.get(partinamesid);
                gst_no.setText(gst);
            }
        });
        favour_add = findViewById(R.id.endorsed_add);
        gst_no = findViewById(R.id.gst_no);
        sign_lorry = findViewById(R.id.sign_do);
        date_lorry = findViewById(R.id.date_do);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        Intent intent = new Intent(DeliveryOrder.this, AdminActivity.class);
        startActivity(intent);
        return true;
    }

    public void do_submit(View view) {
        String Name = do_To.getText().toString();
        String Add = add.getText().toString();
        String Date1 = date1.getText().toString();
        String Hsn_no = hsn_no.getText().toString();
        String Product = product.getText().toString();
        String Quantity = quantity.getText().toString();
        String Vehicle_no = vehicle_no.getText().toString();
        String Fav_name = favour_name.getText().toString();
        String Fav_add = favour_add.getText().toString();
        String Gst_no = gst_no.getText().toString();
        String Sign = sign_lorry.getText().toString();
        String Date = date_lorry.getText().toString();

        RequestParams requestParams = new RequestParams();
        requestParams.add("name", Name);
        requestParams.add("address", Add);
        requestParams.add("dated", Date1);
        requestParams.add("hsn_no", Hsn_no);
        requestParams.add("product", Product);
        requestParams.add("quantity", Quantity);
        requestParams.add("vehicle_no", Vehicle_no);
        requestParams.add("endorsed_in_favour_of_name", Fav_name);
        requestParams.add("endorsed_in_favour_of_address", Fav_add);
        requestParams.add("gst_no", Gst_no);
        requestParams.add("sign", Sign);
        requestParams.add("date", Date);
        requestParams.add("tbname", "delivery_order");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Routes.insert2, requestParams, new AsyncHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onStart() {
                super.onStart();
                dialog = ProgressDialog.show(DeliveryOrder.this, "Working",
                        "Saving data...", true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                L.L(responseBody + "");
                String str = new String(responseBody);
                str = str.replace("<br>", "\n");
                JSONArray jr = null;
                JSONObject jo = null;
                L.L(str);
                try {
                    if (statusCode == 200) {
                        try {
                            jo = new JSONObject(str);
                            if (jo.getString("status").equals("success")) {
                                inserted_id = Integer.parseInt(jo.getString("recentinsertedid"));
                                new DialogBox(DeliveryOrder.this, jo.get("message").toString()).asyncDialogBox();
                                Toast.makeText(context, "Data inserted " + jo.getString("status"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Data not inserted " + jo.getString("status"), Toast.LENGTH_SHORT).show();
                                new DialogBox(DeliveryOrder.this, jo.get("message").toString()).asyncDialogBox();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        L.L(statusCode + str);
                        new DialogBox(DeliveryOrder.this, str).asyncDialogBox();
                    }
                } catch (Exception ex) {
                    L.L(ex.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(DeliveryOrder.this, "Data insertion failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                dialog = ProgressDialog.show(DeliveryOrder.this, "none to say",
                        "Saving.Please wait...", true);
            }
        });
    }

    public void selectDate(View view) {
        date = (EditText) view;
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}

