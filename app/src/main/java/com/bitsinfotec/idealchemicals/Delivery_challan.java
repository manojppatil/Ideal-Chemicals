package com.bitsinfotec.idealchemicals;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.Spinner;
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

public class Delivery_challan extends AppCompatActivity {
    Spinner select_office;
    EditText to_address, order_no, challan_date1, receiver_name, invoice_no, challan_date2, quantity,
            hsn_code, remark, quantity_in_kgs, desc_of_goods, challan_tankerno, challan_sign, challan_date3;
    private int inserted_id;
    Context context;
    AutoCompleteTextView to_name, challan_product;
    EditText date;
    String name, prod_name;
    ArrayList party_names = new ArrayList();
    ArrayList ids = new ArrayList();
    ArrayList addresses = new ArrayList();
    ArrayList product_names = new ArrayList();
    ArrayList hsn_codes = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_challan);
        setTitle("Delivery Challan");
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Delivery_challan.this, android.R.layout.simple_list_item_1, party_names);
                    to_name.setAdapter(adapter);

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
                        hsn_codes.add(jo.getString("HSN_CODE"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Delivery_challan.this, android.R.layout.simple_list_item_1, product_names);
                    challan_product.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        select_office = findViewById(R.id.select_office);
        to_name = findViewById(R.id.challan_name);
        to_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String strid = parent.getItemAtPosition(position).toString();
                String selectItem = to_name.getText().toString();
                int partinamesid = party_names.indexOf(selectItem);

                String addr = (String) addresses.get(partinamesid);
                to_address.setText(addr);
            }
        });
        to_address = findViewById(R.id.challan_add);
        order_no = findViewById(R.id.order_no);
        challan_date1 = findViewById(R.id.order_date);
        receiver_name = findViewById(R.id.material_receive);
        invoice_no = findViewById(R.id.invoice_no);
        challan_date2 = findViewById(R.id.invoice_date);
        quantity = findViewById(R.id.challan_quantity);
        challan_product = findViewById(R.id.product_name);
        challan_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String strid = parent.getItemAtPosition(position).toString();
                String selectItem = challan_product.getText().toString();
                int hsnid = product_names.indexOf(selectItem);

                String hsn = (String) hsn_codes.get(hsnid);
                hsn_code.setText(hsn);
            }
        });
        hsn_code = findViewById(R.id.hsn_code);
        remark = findViewById(R.id.challan_remark);
        quantity_in_kgs = findViewById(R.id.quantity_in_kgs);
        desc_of_goods = findViewById(R.id.desc_goods);
        challan_tankerno = findViewById(R.id.tanker_no);
        challan_sign = findViewById(R.id.sign_challan);
        challan_date3 = findViewById(R.id.date_challan);

        Spinner spinner2 = findViewById(R.id.select_office);
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add("Ulhasnagar");
        arrayList2.add("Baroda");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter2);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        Intent intent = new Intent(Delivery_challan.this, AdminActivity.class);
        startActivity(intent);
        return true;
    }

    public void submit_challan(View view) {
        String Select_office = select_office.getSelectedItem().toString();
        String Name = to_name.getText().toString();
        String Address = to_address.getText().toString();
        String Order_no = order_no.getText().toString();
        String Challan_date1 = challan_date1.getText().toString();
        String Receiver_name = receiver_name.getText().toString();
        String Invoice_no = invoice_no.getText().toString();
        String Challan_date2 = challan_date2.getText().toString();
        String Quantity = quantity.getText().toString();
        String Challan_product = challan_product.getText().toString();
        String Hsn_code = hsn_code.getText().toString();
        String Remark = remark.getText().toString();
        String Quantity_in_kgs = quantity_in_kgs.getText().toString();
        String Desc_of_goods = desc_of_goods.getText().toString();
        String Challan_tankerno = challan_tankerno.getText().toString();
        String Challan_sign = challan_sign.getText().toString();
        String Challan_date3 = challan_date3.getText().toString();

        RequestParams requestParams = new RequestParams();
        requestParams.add("Office", Select_office);
        requestParams.add("name", Name);
        requestParams.add("address", Address);
        requestParams.add("order_no", Order_no);
        requestParams.add("order_date", Challan_date1);
        requestParams.add("Receiver_name", Receiver_name);
        requestParams.add("Invoice_no", Invoice_no);
        requestParams.add("Invoice_date", Challan_date2);
        requestParams.add("Quantity", Quantity);
        requestParams.add("product", Challan_product);
        requestParams.add("HSN_Code", Hsn_code);
        requestParams.add("Remark", Remark);
        requestParams.add("Quantity_in_kgs", Quantity_in_kgs);
        requestParams.add("Desc_of_goods", Desc_of_goods);
        requestParams.add("tanker_no", Challan_tankerno);
        requestParams.add("Sign", Challan_sign);
        requestParams.add("_date", Challan_date3);
        requestParams.add("tbname", "challan_table");

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(Routes.insert2, requestParams, new AsyncHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onStart() {
                dialog = ProgressDialog.show(Delivery_challan.this, "Working",
                        "Saving data...", true);
            }

            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
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
                                new DialogBox(Delivery_challan.this, jo.get("message").toString()).asyncDialogBox();
                                Toast.makeText(context, "Data inserted " + jo.getString("status"), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, "Data not inserted " + jo.getString("status"), Toast.LENGTH_SHORT).show();
                                new DialogBox(Delivery_challan.this, jo.get("message").toString()).asyncDialogBox();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        L.L(statusCode + str);
                        new DialogBox(Delivery_challan.this, str).asyncDialogBox();
                    }
                } catch (Exception ex) {
                    L.L(ex.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

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

