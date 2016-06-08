package com.example.djlewis.monetflash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.fancybuttons.FancyButton;
import utility.Utility;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    FancyButton buttonMomo, buttonMasterCard;
    String businessname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        buttonMomo = (FancyButton) findViewById(R.id.buttonmomo);
        buttonMasterCard = (FancyButton) findViewById(R.id.buttonmastercard);

        buttonMomo.setOnClickListener(this);
        buttonMasterCard.setOnClickListener(this);
        businessname  = getIntent().getStringExtra(Utility.APP_USER);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.buttonmomo:{
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
        }
    }
}
