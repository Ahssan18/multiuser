package com.food.multiuser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.food.multiuser.Helper;
import com.food.multiuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView firebasenameview;
    private CardView Staff, Supplier, Products, Settings, Reports, OrdersCard;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebasenameview = findViewById(R.id.textView5);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String result = finaluser.substring(0, finaluser.indexOf("@"));
        String resultemail = result.replace(".", "");
        firebasenameview.setText("Welcome, " + resultemail);

        Staff = (CardView) findViewById(R.id.Staff);
        Supplier = (CardView) findViewById(R.id.Suppliers);
        Products = (CardView) findViewById(R.id.Products);
        Settings = (CardView) findViewById(R.id.Settings);
        Reports = (CardView) findViewById(R.id.Reports);
        OrdersCard = (CardView) findViewById(R.id.OrdersCard);

        Staff.setOnClickListener(this);
        Supplier.setOnClickListener(this);
        Products.setOnClickListener(this);
        Settings.setOnClickListener(this);
        Reports.setOnClickListener(this);
        OrdersCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.Staff:
                i = new Intent(this, StaffActivity.class);
                startActivity(i);
                break;
            case R.id.OrdersCard:
                i = new Intent(this, OrderActivity.class);
                startActivity(i);
                break;
            case R.id.Suppliers:
                i = new Intent(this, SupplierActivity.class);
                startActivity(i);
                break;
            case R.id.Products:
                i = new Intent(this, ProductList.class);
                startActivity(i);
                break;
            case R.id.Settings:
                i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;

            case R.id.Reports:
                i = new Intent(this, ReportsActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    private void Logout() {
        Helper helper = new Helper(this);
        helper.logout();
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        Toast.makeText(HomeActivity.this, "LOGOUT SUCCESSFUL", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu: {
                Logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}