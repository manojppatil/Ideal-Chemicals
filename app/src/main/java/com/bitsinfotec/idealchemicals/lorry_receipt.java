package com.bitsinfotec.idealchemicals;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URLEncoder;

public class lorry_receipt extends AppCompatActivity {

    private WebView webadmin;
    private int inserted_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_receipt);
        Intent intent = getIntent();
        inserted_id = intent.getIntExtra("inserted_id", 1);
        Toast.makeText(this, ""+inserted_id, Toast.LENGTH_SHORT).show();
        webadmin = findViewById(R.id.webadmin);
        webadmin.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings mWebSettings = webadmin.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setAllowContentAccess(true);
        webadmin.getSettings().setBuiltInZoomControls(true);
        webadmin.setInitialScale(1);
        webadmin.getSettings().setLoadWithOverviewMode(true);
        webadmin.getSettings().setUseWideViewPort(true);
        webadmin.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        StringBuffer buffer = new StringBuffer("http://wesoftech.com/IdealChemical/receipts/lorry_receipt.php");
        buffer.append("?id=" + URLEncoder.encode(""+inserted_id));
        webadmin.loadUrl(buffer.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createWebPrintJob(WebView webadmin) {

        //create object of print manager in your device
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        //create object of print adapter
        PrintDocumentAdapter printAdapter = webadmin.createPrintDocumentAdapter();

        //provide name to your newly generated pdf file
        String jobName = getString(R.string.app_name) + " Print Test" ;

        //open print dialog
        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }

    //perform click pdf creation operation on click of print button click
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void printPDF(View view) {
        createWebPrintJob(webadmin);
    }
}
