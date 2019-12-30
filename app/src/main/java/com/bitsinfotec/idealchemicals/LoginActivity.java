package com.bitsinfotec.idealchemicals;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bitsinfotec.idealchemicals.Helper.DialogBox;
import com.bitsinfotec.idealchemicals.Helper.L;
import com.bitsinfotec.idealchemicals.Helper.NetworkConnection;
import com.bitsinfotec.idealchemicals.Helper.Routes;
import com.bitsinfotec.idealchemicals.Helper.SharedPreferencesWork;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.bitsinfotec.idealchemicals.Helper.Routes.user;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    RelativeLayout relay1;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relay1.setVisibility(View.VISIBLE);
        }
    };
    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SharedPreferencesWork(this).checkForLogin();

        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        relay1 = findViewById(R.id.relay1);
        handler.postDelayed(runnable, 2000);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void signin(View view) {
        user = username.getText().toString();
        pass = password.getText().toString();
        if (NetworkConnection.checkNetworkConnection(this)) {
            ArrayList validationMessage = new ArrayList();
            if (username.getText().toString().length() > 0) {
            } else
                validationMessage.add("Name field is not valid");

            if (password.getText().toString().length() > 0) {
            } else
                validationMessage.add("password field id empty");

            if (validationMessage.size() > 0) {
                String allmessage = "";
                for (int i = 0; i < validationMessage.size(); i++) {
                    allmessage += "\n" + validationMessage.get(i);
                    new DialogBox(this, allmessage).asyncDialogBox();
                    validationMessage.clear();
                }
            } else {
                RequestParams params = new RequestParams();
                params.add("userid", user);
                params.add("password", pass);
                params.add("tbname", "user");
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(Routes.LoginByApp, params, new AsyncHttpResponseHandler() {
                    ProgressDialog dialog;

                    @Override
                    public void onStart() {
                        dialog = ProgressDialog.show(LoginActivity.this, "Welcome...",
                                "Validating...", true);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        L.L(responseBody + "");
                        dialog.dismiss();
                        String str = new String(responseBody);
                        L.L(str);
                        JSONArray jr = null;
                        JSONObject jo = null;
                        if (statusCode == 200) {
                            try {
                                jo = new JSONObject(str);
                                if (jo.getString("status").equals("success")) {
                                    HashMap hm = new HashMap();
                                    hm.put("apikey", jo.getString("api_key"));
                                    hm.put("userid", user);
                                    hm.put("password", pass);
                                    hm.put("id", jo.getString("userid"));
                                    hm.put("role", jo.getString("role"));

                                    new SharedPreferencesWork(LoginActivity.this).insertOrReplace(hm, Routes.sharedPrefForLogin);

                                    if (jo.getString("role").equals("admin")) {
                                        Intent admin = new Intent(LoginActivity.this, AdminActivity.class);
                                        startActivity(admin);
                                        Toast.makeText(LoginActivity.this, "Admin Logged in", Toast.LENGTH_SHORT).show();
                                    }

                                    if (jo.getString("role").equals("supervisor")) {
                                        Intent supervisor = new Intent(LoginActivity.this, SupervisorActivity.class);
                                        startActivity(supervisor);
                                        Toast.makeText(LoginActivity.this, "Supervisor Logged in", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    new DialogBox(LoginActivity.this, jo.get("status_message").toString()).asyncDialogBox();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            L.L(statusCode + str);
                            new DialogBox(LoginActivity.this, str).asyncDialogBox();

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        new DialogBox(LoginActivity.this, responseBody.toString());

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        dialog = ProgressDialog.show(LoginActivity.this, "none to say",
                                "Saving.Please wait...", true);
                    }
                });
            }
        } else {
            new DialogBox(this, "Check your network connnection").asyncDialogBox();
        }
    }

}


