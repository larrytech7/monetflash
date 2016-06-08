package com.example.djlewis.monetflash;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import mehdi.sakout.fancybuttons.FancyButton;
import utility.Utility;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    private FancyButton registerButton;
    private EditText businessName, businessPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        businessName = (EditText) findViewById(R.id.businessNameeditText);
        businessPhone = (EditText) findViewById(R.id.businessPhoneeditText);
        registerButton = (FancyButton) findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonRegister){
            //save business name and setup preferences
            String name = businessName.getText().toString();
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(Utility.APP_USER, name)
                    .putBoolean(Utility.APP_AUTH, true)
                    .putLong(Utility.APP_NUMBER, Long.parseLong(businessPhone.getText().toString()))
                    .commit();
            Intent homeintent = new Intent(this, HomeActivity.class);
            homeintent.putExtra(Utility.APP_USER, name);
            startActivity(homeintent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if this is returning user, launch next activity at once
        boolean issetup = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Utility.APP_AUTH, false);
        String name = PreferenceManager.getDefaultSharedPreferences(this).getString(Utility.APP_USER, "Unknown");
        if(issetup) {
            Intent homeintent = new Intent(this, HomeActivity.class);
            homeintent.putExtra(Utility.APP_USER, name);
            startActivity(homeintent);
        }
    }
}
