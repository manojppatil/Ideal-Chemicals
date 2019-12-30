package com.bitsinfotec.idealchemicals;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bitsinfotec.idealchemicals.Helper.DialogBox;
import com.bitsinfotec.idealchemicals.Helper.FileUploader;
import com.bitsinfotec.idealchemicals.Helper.L;
import com.bitsinfotec.idealchemicals.Helper.Routes;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Trip_detail extends AppCompatActivity {

    EditText trip_subject, trip_amount;
    Spinner trip_no;
    Button select_photo, submit;
    ImageView imageView;
    Uri imageuri;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    RequestParams requestParams = new RequestParams();
    private int inserted_id;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        getTripNo();
        setTitle("Trip Expenses");
        trip_amount = findViewById(R.id.trip_amount);
        trip_no = findViewById(R.id.select_trip);
        trip_subject = findViewById(R.id.trip_subject);
        select_photo = findViewById(R.id.trip_photo);
        submit = findViewById(R.id.trip_submit);
        imageView = findViewById(R.id.trip_photoimage);
    }

    public void savetrip(View view) {
        String subject = trip_subject.getText().toString();
        String amount = trip_amount.getText().toString();
        String tripno = trip_no.getSelectedItem().toString();
        requestParams.add("Other_expenses_sub", subject);
        requestParams.add("Other_expenses_amount", amount);
        requestParams.add("id", tripno);
        requestParams.add("tbname", "Trip_expenses");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(Routes.update, requestParams, new AsyncHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onStart() {
                dialog = ProgressDialog.show(Trip_detail.this, "Working",
                        "Saving data...", true);
            }

            @Override
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
                                new DialogBox(Trip_detail.this, jo.get("message").toString()).asyncDialogBox();
                                Toast.makeText(context, "Data inserted " + jo.getString("status"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Data not inserted " + jo.getString("status"), Toast.LENGTH_SHORT).show();
                                new DialogBox(Trip_detail.this, jo.get("message").toString()).asyncDialogBox();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        L.L(statusCode + str);
                        new DialogBox(Trip_detail.this, str).asyncDialogBox();
                    }
                } catch (Exception ex) {
                    L.L(ex.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                new DialogBox(Trip_detail.this, responseBody.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                dialog = ProgressDialog.show(Trip_detail.this, "none to say",
                        "Saving.Please wait...", true);
            }
        });

    }

    public void trip_photo(View view) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Trip_detail.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_IMAGE_CAMERA);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, PICK_IMAGE_GALLERY);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_CAMERA) {
            if (data != null) {
                try {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(Button.VISIBLE);
                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    L.L(finalFile + "");
                    requestParams.put("image", finalFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            if (data != null) {
                try {
                    imageuri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                    imageView.setImageBitmap(bitmap);
                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    L.L(finalFile + "");
                    requestParams.put("image", finalFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public void getTripNo() {
        RequestParams requestParams = new RequestParams();
        requestParams.add("tbname", "Trip_expenses");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(Routes.selectAll, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                L.L(string);
                JSONArray jr = null;
                JSONObject jo = null;
                ArrayList arrayList = new ArrayList();
                if (statusCode == 200) {
                    try {
                        jr = new JSONArray(string);
                        for (int i = 0; i < jr.length(); i++) {
                            jo = jr.getJSONObject(i);
                            arrayList.add(jo.getInt("id"));
                        }
                        ArrayAdapter<String> arrayAdapterid = new ArrayAdapter<String>(Trip_detail.this, android.R.layout.simple_spinner_item, arrayList);
                        arrayAdapterid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        trip_no.setAdapter(arrayAdapterid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
