package com.example.djlewis.monetflash;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask;
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils;

public class MasterCardScanActivity extends AppCompatActivity implements CardNfcAsyncTask.CardNfcInterface {

    private NfcAdapter mNfcAdapter;
    private CardNfcUtils mCardNfcUtils;
    private boolean mIntentFromCreate;
    private CardNfcAsyncTask mCardNfcAsyncTask;
    public static final String LOG_NFC = MasterCardScanActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_card_scan);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null){
            //do something if there are no nfc module on device
            Log.d(LOG_NFC, "No NFC on Device");
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
            //show some turn on nfc dialog here. take a look in the samle ;-)
        } else if (mNfcAdapter != null){
            mCardNfcUtils.enableDispatch();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mCardNfcUtils.disableDispatch();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
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
}
