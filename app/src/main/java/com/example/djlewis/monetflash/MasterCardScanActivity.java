package com.example.djlewis.monetflash;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask;
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;

public class MasterCardScanActivity extends AppCompatActivity implements CardNfcAsyncTask.CardNfcInterface, View.OnClickListener {

    private NfcAdapter mNfcAdapter;
    private CardNfcUtils mCardNfcUtils;
    private boolean mIntentFromCreate;
    private CardNfcAsyncTask mCardNfcAsyncTask;
    public static final String LOG_NFC = MasterCardScanActivity.class.getCanonicalName();
    private FancyButton scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_card_scan);
        scanButton = (FancyButton) findViewById(R.id.scan_button);
        scanButton.setOnClickListener(this);
        Log.d(LOG_NFC, "Oncreate");
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null){
            //do something if there are no nfc module on device
            Log.d(LOG_NFC, "No NFC on Device");
            Toast.makeText(this, getString(R.string.nfcnotavailable), Toast.LENGTH_LONG).show();
        } else {
            //do something if there are nfc module on device

            mCardNfcUtils = new CardNfcUtils(this);
            //next few lines here needed in case you will scan credit card when app is closed
            mIntentFromCreate = true;
            onNewIntent(getIntent());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntentFromCreate = false;
        if (mNfcAdapter != null && !mNfcAdapter.isEnabled()){
            //show some turn on nfc dialog here. take a look in the sample
            Log.d(LOG_NFC, "NFC Adapter off");
        } else if (mNfcAdapter != null){
            mCardNfcUtils.enableDispatch();
            Log.d(LOG_NFC,"Adapter set ON");
            scan();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_NFC, "OnPasue");
        if (mNfcAdapter != null) {
            mCardNfcUtils.disableDispatch();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(LOG_NFC,"OnNew Intent");
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            mCardNfcAsyncTask = new CardNfcAsyncTask.Builder(this, intent, mIntentFromCreate)
                    .build();
        }
    }

    @Override
    public void startNfcReadCard() {
        Log.d(LOG_NFC, "NFC Card Read Started");
    }

    @Override
    public void cardIsReadyToRead() {
        String card = mCardNfcAsyncTask.getCardNumber();
        String expiredDate = mCardNfcAsyncTask.getCardExpireDate();
        String cardType = mCardNfcAsyncTask.getCardType();

        Log.d(LOG_NFC, "Card Ready. Details: "+card+"\nExpiry: "+expiredDate+"\nType: "+cardType);
    }

    @Override
    public void doNotMoveCardSoFast() {
        Log.d(LOG_NFC, "Warning, do not move card so fast");
    }

    @Override
    public void unknownEmvCard() {
        Log.d(LOG_NFC, "Unknown EmvCard");
    }

    @Override
    public void cardWithLockedNfc() {
        Log.d(LOG_NFC, "card With Locked NFC");
    }

    @Override
    public void finishNfcReadCard() {
        Log.d(LOG_NFC, "Finish NFC Card Read");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scan_button){
            scan();
        }
    }

    private void scan() {
        //start progress dialog here
        SweetAlertDialog progressdialog  = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressdialog.setTitleText(getString(R.string.scanning));
        progressdialog.setContentText(getString(R.string.scan_info));
        progressdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                sweetAlertDialog.cancel();
            }
        });
        progressdialog.setCancelable(true);
        progressdialog.setCanceledOnTouchOutside(true);
        progressdialog.show();
    }
}
