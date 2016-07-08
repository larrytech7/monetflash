package com.example.djlewis.monetflash;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import utility.Utility;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView buttonMomo, buttonOrangeMomo;
    ImageView buttonMasterCard;
    String businessname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar actionBar = getSupportActionBar();
        buttonMomo = (ImageView) findViewById(R.id.buttonmtn_momo);
        buttonMasterCard = (ImageView) findViewById(R.id.buttonmastercard);
        buttonOrangeMomo = (ImageView) findViewById(R.id.buttonorange_momo);

        buttonMomo.setOnClickListener(this);
        buttonOrangeMomo.setOnClickListener(this);
        buttonMasterCard.setOnClickListener(this);
        businessname  = getIntent().getStringExtra(Utility.APP_USER);

        actionBar.setTitle(getString(R.string.paymentconsole, businessname));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.buttonmtn_momo:{
                //start Momo Activity
                Intent momointent = new Intent(this, MainActivity.class);
                momointent.putExtra(Utility.APP_USER, businessname);
                startActivity(momointent);
            }
                break;
            case R.id.buttonmastercard:{
                //start Card Scan activity
                Intent masterc_intent = new Intent(this, MasterCardScanActivity.class);
                masterc_intent.putExtra(Utility.APP_USER, businessname);
                startActivity(masterc_intent);
            }
                break;
            case R.id.buttonorange_momo:{
                Snackbar.make(buttonMasterCard, R.string.comingsoon, Snackbar.LENGTH_LONG).show();
            }
            break;
        }
    }
}
