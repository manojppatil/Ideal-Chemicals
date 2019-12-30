package com.bitsinfotec.idealchemicals;

import android.content.Intent;
import android.os.Bundle;

import com.bitsinfotec.idealchemicals.Helper.Routes;
import com.bitsinfotec.idealchemicals.Helper.SharedPreferencesWork;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void DO(View view) {
        Intent intent = new Intent(AdminActivity.this, Show_do.class);
        startActivity(intent);
    }

    public void lorry(View view) {
        Intent intent = new Intent(AdminActivity.this, ShowLR.class);
        startActivity(intent);
    }

    public void challan(View view) {
        Intent intent = new Intent(AdminActivity.this, Show_challan.class);
        startActivity(intent);
    }

    public void trip(View view) {
        Intent intent = new Intent(AdminActivity.this, Trip_detail.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(AdminActivity.this,AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lorry) {
            Intent intent = new Intent(AdminActivity.this, LorryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_challan) {
            Intent intent = new Intent(AdminActivity.this, Delivery_challan.class);
            startActivity(intent);
        } else if (id == R.id.nav_trip_expenses) {
            Intent intent = new Intent(AdminActivity.this, Trip_detail.class);
            startActivity(intent);
        } else if (id == R.id.nav_do) {
            Intent intent = new Intent(AdminActivity.this, DeliveryOrder.class);
            startActivity(intent);

        } else if (id == R.id.nav_message) {
            Intent intent = new Intent(AdminActivity.this, MessagesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {

            if (new SharedPreferencesWork(AdminActivity.this).eraseData(Routes.sharedPrefForLogin)) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
