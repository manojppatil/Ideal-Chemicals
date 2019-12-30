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
import android.widget.Button;
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

import static com.bitsinfotec.idealchemicals.Helper.Routes.selectOne;

public class LorryActivity extends AppCompatActivity {
    EditText date;
    EditText lorry_from, lorry_to, consigner_address, consignee_address, gstno1,
            gstno2, tanker_no, gross_loaded, gross_unloaded, tare_loaded, tare_unloaded, net_loaded, net_unloaded,
            strength_loaded, strength_unloaded, wt_loaded, wt_unloaded, freight_amount, gpi_no, challan_no, remark_date, lorry_sign, lorry_date;
    AutoCompleteTextView consigner_name, consignee_name, lorry_product, select_transport;
    Button submit;
    private int inserted_id;
    Context context;
    int trans_id, tran_id;
    String name, prod_name, transport_name;
    ArrayList party_names = new ArrayList();
    ArrayList ids = new ArrayList();
    ArrayList addresses = new ArrayList();
    ArrayList gstin = new ArrayList();
    ArrayList product_names = new ArrayList();
    ArrayList transporter_names = new ArrayList();
    ArrayList t_ids = new ArrayList();
    ArrayList t_addresses = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lorry);
        setTitle("Lorry Receipt");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#12c2e9")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        RequestParams requestParams2 = new RequestParams();
        requestParams2.add("tbname", "Transporter_Address");
        AsyncHttpClient asyncHttpClient2 = new AsyncHttpClient();
        asyncHttpClient2.post(Routes.selectAllByQuery, requestParams2, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                transport_name = new String(responseBody);
                L.L(transport_name);
                try {
                    JSONArray jr = new JSONArray(transport_name);

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jo = jr.getJSONObject(i);
                        String name = jo.getString("Name_of_the_Transporter");
                        transporter_names.add(name + "");
                        transporter_names.indexOf(name);
                        String aid = jo.getString("id");
                        t_ids.add(aid + "");
                        L.L(t_ids + "++++++--");
                        t_addresses.add(jo.getString("Address"));

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(LorryActivity.this, android.R.layout.simple_list_item_1, transporter_names);
                    select_transport.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


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
                        gstin.add(jo.getString("GSTIN_NO"));

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(LorryActivity.this, android.R.layout.simple_list_item_1, party_names);
                    consigner_name.setAdapter(adapter);
                    consignee_name.setAdapter(adapter);

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
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(LorryActivity.this, android.R.layout.simple_list_item_1, product_names);
                    lorry_product.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        select_transport = findViewById(R.id.select_transporter);
        select_transport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String strid = parent.getItemAtPosition(position).toString();
                String selectItem = select_transport.getText().toString();
                trans_id = transporter_names.indexOf(selectItem);
                String t_id = (String) t_ids.get(trans_id);

            }
        });
        lorry_from = findViewById(R.id.consigner_from);
        lorry_to = findViewById(R.id.consigner_to);
        consigner_name = findViewById(R.id.consigner_name);
        consigner_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String strid = parent.getItemAtPosition(position).toString();
                String selectItem = consigner_name.getText().toString();
                int partinamesid = party_names.indexOf(selectItem);

                L.L(partinamesid + "------partinamesid-------");

                String add = (String) addresses.get(partinamesid);
                L.L(add + "+++++add++++++++");
                consigner_address.setText(add);

                String gst = (String) gstin.get(partinamesid);
                L.L(add + "+++++gst++++++++");
                gstno1.setText(gst);
            }
        });
        consignee_name = findViewById(R.id.consignee_name);
        consignee_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String strid = parent.getItemAtPosition(position).toString();
                String selectItem = consignee_name.getText().toString();
                int partinamesid = party_names.indexOf(selectItem);
                L.L(partinamesid + "------partinamesid-------");

                String add = (String) addresses.get(partinamesid);
                L.L(add + "+++++add++++++++");
                consignee_address.setText(add);

                String gst = (String) gstin.get(partinamesid);
                L.L(add + "+++++gst++++++++");
                gstno2.setText(gst);
            }
        });
        consigner_address = findViewById(R.id.consigner_address);
        consignee_address = findViewById(R.id.consignee_address);
        gstno1 = findViewById(R.id.consigner_gst_no);
        gstno2 = findViewById(R.id.consignee_gst_no);
        tanker_no = findViewById(R.id.consigner_tanker_no);
        lorry_product = findViewById(R.id.consignee_product);
        lorry_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String strid = parent.getItemAtPosition(position).toString();
                String selectItem = lorry_product.getText().toString();
                int hsnid = product_names.indexOf(selectItem);
            }
        });
        gross_loaded = findViewById(R.id.loaded_gross);
        gross_unloaded = findViewById(R.id.unloaded_gross);
        tare_loaded = findViewById(R.id.loaded_tare);
        tare_unloaded = findViewById(R.id.unloaded_tare);
        net_loaded = findViewById(R.id.loaded_nett);
        net_unloaded = findViewById(R.id.unloaded_nett);
        strength_loaded = findViewById(R.id.loaded_strength);
        strength_unloaded = findViewById(R.id.unloaded_strength);
        wt_loaded = findViewById(R.id.loaded_wt);
        wt_unloaded = findViewById(R.id.unloaded_wt);
        freight_amount = findViewById(R.id.freight_amount);
        gpi_no = findViewById(R.id.gpi_no);
        challan_no = findViewById(R.id.challan_no);
        remark_date = findViewById(R.id.date_lorry);
        lorry_sign = findViewById(R.id.sign_lorry);
        lorry_date = findViewById(R.id.date_lorry2);
        submit = findViewById(R.id.lorry_submit);

//        Spinner spinner2 = findViewById(R.id.select_transporter);
//        ArrayList<String> arrayList2 = new ArrayList<>();
//        arrayList2.add("Raj Transport");
//        arrayList2.add("Singh Tranport Services");
//        arrayList2.add("Jai Ambe Goods Carrier");
//        arrayList2.add("Jay deep Carrier");
//        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList2);
//        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner2.setAdapter(arrayAdapter2);
//
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        Intent intent = new Intent(LorryActivity.this, AdminActivity.class);
        startActivity(intent);
        return true;
    }

    public void lorry_submit(View view) {
        String Select_tranporter = select_transport.getText().toString();
        String From = lorry_from.getText().toString();
        String To = lorry_to.getText().toString();
        String Consigner_name = consigner_name.getText().toString();
        String Consigner_add = consigner_address.getText().toString();
        String Consigner_gst = gstno1.getText().toString();
        String Tanker_no = tanker_no.getText().toString();
        String Consignee_name = consignee_name.getText().toString();
        String Consignee_add = consignee_address.getText().toString();
        String Consignee_gst = gstno2.getText().toString();
        String Product = lorry_product.getText().toString();
        String Gross_loaded = gross_loaded.getText().toString();
        String Gross_unloaded = gross_unloaded.getText().toString();
        String Tare_loaded = tare_loaded.getText().toString();
        String Tare_unloaded = tare_unloaded.getText().toString();
        String Nett_loaded = net_loaded.getText().toString();
        String Nett_unloaded = net_unloaded.getText().toString();
        String Strength_loaded = strength_loaded.getText().toString();
        String Strength_unloaded = strength_unloaded.getText().toString();
        String Wt_loaded = wt_loaded.getText().toString();
        String Wt_unloaded = wt_unloaded.getText().toString();
        String Freight_amount = freight_amount.getText().toString();
        String Gpi_no = gpi_no.getText().toString();
        String Challan_no = challan_no.getText().toString();
        String Remark_date = remark_date.getText().toString();
        String lorry_Sign = lorry_sign.getText().toString();
        String lorry_Date = lorry_date.getText().toString();

        RequestParams requestParams = new RequestParams();
        requestParams.add("Transporter", Select_tranporter);
        requestParams.add("_From", From);
        requestParams.add("_To", To);
        requestParams.add("Consignor_name", Consigner_name);
        requestParams.add("Consignor_address", Consigner_add);
        requestParams.add("GST_no", Consigner_gst);
        requestParams.add("Tanker_no", Tanker_no);
        requestParams.add("Consignee_name", Consignee_name);
        requestParams.add("Consignee_address", Consignee_add);
        requestParams.add("GST_no2", Consignee_gst);
        requestParams.add("Product", Product);
        requestParams.add("Gross_loaded", Gross_loaded);
        requestParams.add("Gross_unloaded", Gross_unloaded);
        requestParams.add("Tare_loaded", Tare_loaded);
        requestParams.add("Tare_unloaded", Tare_unloaded);
        requestParams.add("Nett_loaded", Nett_loaded);
        requestParams.add("Nett_unloaded", Nett_unloaded);
        requestParams.add("Strength_loaded", Strength_loaded);
        requestParams.add("Strength_unloaded", Strength_unloaded);
        requestParams.add("Wt_loaded", Wt_loaded);
        requestParams.add("Wt_unloaded", Wt_unloaded);
        requestParams.add("Freight_amount", Freight_amount);
        requestParams.add("GPI_no", Gpi_no);
        requestParams.add("Challan_no", Challan_no);
        requestParams.add("_Date", Remark_date);
        requestParams.add("Sign", lorry_Sign);
        requestParams.add("date", lorry_Date);
        requestParams.add("Transporter_Address_id", String.valueOf(trans_id+1));
        requestParams.add("tbname", "lorry_receipt");

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(Routes.insert2, requestParams, new AsyncHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onStart() {
                dialog = ProgressDialog.show(LorryActivity.this, "Working",
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
                                new DialogBox(LorryActivity.this, jo.get("message").toString()).asyncDialogBox();
                                Toast.makeText(context, "Data inserted " + jo.getString("status"), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, "Data not inserted " + jo.getString("status"), Toast.LENGTH_SHORT).show();
                                new DialogBox(LorryActivity.this, jo.get("message").toString()).asyncDialogBox();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        L.L(statusCode + str);
                        new DialogBox(LorryActivity.this, str).asyncDialogBox();
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
