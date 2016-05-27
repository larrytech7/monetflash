package com.example.djlewis.monetflash;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;
import utility.Utility;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private Button buttonCancel; //cancels a digit from current filed when clicked
    private Button buttonBack; //move to next field when clicked
    private FancyButton buttonPay;//sends payment request when pressed
    private EditText phoneNumber;
    private EditText paymentAmount;
    private Button bOne, bTwo, bThree, bFour, bFive, bSix, bSeven, bEight, bNine, bZero; //data entry buttons

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //buttons
        buttonBack = (Button) rootView.findViewById(R.id.buttonNext);
        buttonCancel = (Button) rootView.findViewById(R.id.buttonCancel);
        buttonPay = (FancyButton) rootView.findViewById(R.id.buttonpay);
        bOne = (Button) rootView.findViewById(R.id.buttonOne); bOne.setOnClickListener(this);
        bTwo = (Button) rootView.findViewById(R.id.buttonTwo); bTwo.setOnClickListener(this);
        bThree = (Button) rootView.findViewById(R.id.buttonThree); bThree.setOnClickListener(this);
        bFour = (Button) rootView.findViewById(R.id.buttonFour); bFour.setOnClickListener(this);
        bFive = (Button) rootView.findViewById(R.id.buttonFive); bFive.setOnClickListener(this);
        bSix = (Button) rootView.findViewById(R.id.buttonSix); bSix.setOnClickListener(this);
        bSeven = (Button) rootView.findViewById(R.id.buttonSeven); bSeven.setOnClickListener(this);
        bEight = (Button) rootView.findViewById(R.id.buttonEight); bEight.setOnClickListener(this);
        bNine = (Button) rootView.findViewById(R.id.buttonNine); bNine.setOnClickListener(this);
        bZero = (Button) rootView.findViewById(R.id.buttonZero); bZero.setOnClickListener(this);
        //editTexts
        phoneNumber = (EditText) rootView.findViewById(R.id.phoneEditText);
        paymentAmount = (EditText) rootView.findViewById(R.id.amountEditText);
        phoneNumber.setFocusable(true);
        paymentAmount.setFocusable(true);

        buttonCancel.setOnClickListener(this);
        buttonPay.setOnClickListener(this);
        buttonBack.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState != null){
            outState.putString(Utility.PHONE_KEY, phoneNumber.getText().toString());
            outState.putString(Utility.AMOUNT_KEY, paymentAmount.getText().toString());
        }
        super.onSaveInstanceState(outState);
        //save any required persistent data
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //restore persistent data to the fragment
        if (savedInstanceState != null){
            phoneNumber.setText(savedInstanceState.getString(Utility.PHONE_KEY));
            paymentAmount.setText(savedInstanceState.getString(Utility.AMOUNT_KEY));
        }
    }

    @Override
    public void onClick(View v) {
        //wire up buttons with appropriate actions
        int id = v.getId();
        EditText edited = focusedTextField();
        //cancel button action
        switch (id) {
            case R.id.buttonCancel: {
                String currentText = edited.getText().toString();
                try {
                    String updatedText = currentText.substring(0, currentText.length() - 1);
                    edited.setText(updatedText);
                } catch (IndexOutOfBoundsException iob) {
                    iob.printStackTrace();
                }
            }
            break;
            //back button action
            case R.id.buttonNext: {
                if (isFocused(phoneNumber)) {
                    paymentAmount.setEnabled(true);
                    phoneNumber.setEnabled(false);
                    paymentAmount.requestFocus();
                } else {
                    phoneNumber.setEnabled(true);
                    paymentAmount.setEnabled(false);
                    phoneNumber.requestFocus();
                }
                System.err.println("Back Pressed");
            }
            break;
            //payButton action
            case R.id.buttonpay: {
                //get subscriber's phone number
               // TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

                String clientphone = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Utility.APP_NUMBER, "237");
                clientphone = new StringBuilder(clientphone).insert(0,"237").toString();
                final String cphone = clientphone;
                if (!clientphone.isEmpty() && clientphone.length()>3){
                    final String clientname = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Utility.APP_USER,"Unknown Business");
                    final SweetAlertDialog sw = Utility.getInstance(getActivity())
                            .startPaymentDialog(getActivity().getString(R.string.paymentmessage));
                    sw.show();
                    final String amount = paymentAmount.getText().toString().equals("")?"0":paymentAmount.getText().toString();
                    String customer = phoneNumber.getText().toString().equals("")?"0":phoneNumber.getText().toString();

                    customer = new StringBuilder(customer).insert(0,"237").toString();
                    final String cust = customer;

                    final Firebase firebaseHandler = new Firebase(Utility.FIREBASE_TRANSACTION_URL);
                    //start async request to request for payment
                    Ion.with(this)
                            .load(Utility.PAYMENT_URL)
                            .setBodyParameter("client_name", clientname)
                            .setBodyParameter("client", clientphone)
                            .setBodyParameter("amount", String.valueOf(Integer.parseInt(amount)))
                            .setBodyParameter("customer", String.valueOf(Long.parseLong(customer)))
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (e == null) {
                                        //show message
                                        String message ="";
                                        int status = result.get("statuscode").getAsInt();
                                        String msg = result.get("message").getAsString();
                                        final Map<String ,String> mtransaction = new HashMap<>();
                                        mtransaction.put("client_name",clientname);
                                        mtransaction.put("client_number", cphone);
                                        mtransaction.put("customer_number", cust);
                                        mtransaction.put("amount", amount);
                                        mtransaction.put("date", SimpleDateFormat.getDateTimeInstance().format(new Date()));

                                        switch (status){
                                            case 200:
                                                message = getContext().getString(R.string.paymentsuccess);
                                                firebaseHandler.authWithPassword("admin7@monetflash.com", "admin7@monetflash",
                                                        new Firebase.AuthResultHandler() {
                                                            @Override
                                                            public void onAuthenticated(AuthData authData) {
                                                                firebaseHandler.child("transactions/success").push().setValue(mtransaction);
                                                            }
                                                            @Override
                                                            public void onAuthenticationError(FirebaseError firebaseError) {
                                                                //authentication failure
                                                                Log.d("Firebase Auth", "Error -> "+firebaseError.getDetails());
                                                            }
                                                        });
                                                //don't manage to listen for completion callbacks for now
                                                break;
                                            case 403:
                                            case 404:
                                                firebaseHandler.authWithPassword("admin7@monetflash.com", "admin7@monetflash",
                                                        new Firebase.AuthResultHandler() {
                                                    @Override
                                                    public void onAuthenticated(AuthData authData) {
                                                        firebaseHandler.child("transactions/error").push().setValue(mtransaction);
                                                    }
                                                    @Override
                                                    public void onAuthenticationError(FirebaseError firebaseError) {
                                                        Log.d("Firebase Auth", "Error -> "+firebaseError.getDetails());
                                                    }
                                                });

                                                message = getContext().getString(R.string.paymentfailed, msg);
                                                break;
                                            default: //failure
                                                message = "General Failure. Unknown server response";
                                                firebaseHandler.authWithPassword("admin7@monetflash.com", "admin7@monetflash",
                                                        new Firebase.AuthResultHandler() {
                                                    @Override
                                                    public void onAuthenticated(AuthData authData) {
                                                        firebaseHandler.child("transactions/error").push().setValue(mtransaction);
                                                    }
                                                    @Override
                                                    public void onAuthenticationError(FirebaseError firebaseError) {
                                                        Log.d("Firebase Auth", "Error -> "+firebaseError.getDetails());
                                                    }
                                                });
                                                break;
                                        }
                                        Log.i("STatus CODE:","code = "+status);
                                        boolean requestOk = false; if (status == 200) requestOk = true;
                                        Utility.getInstance(getActivity()).stopPaymentDialog(sw, message,requestOk);
                                        clearFields();
                                    }else{
                                        //stop the sweetdialog and clear fields
                                        String message = getContext().getString(R.string.paymentfailed, e.getLocalizedMessage());
                                        Utility.getInstance(getActivity()).stopPaymentDialog(sw, message, false);
                                    }
                                }
                            });
            }
                else{
                    Utility.getInstance(getActivity()).showMessage(rootView, getActivity().getString(R.string.numbernotavailable));
                }
            }
            break;
            case R.id.buttonOne:{
                edited.append("1");
            }break;
            case R.id.buttonTwo:{
                edited.append("2");
            }break;
            case R.id.buttonThree:{
                edited.append("3");
            }break;
            case R.id.buttonFour:{
                edited.append("4");
            }break;
            case R.id.buttonFive:{
                edited.append("5");
            }break;
            case R.id.buttonSix:{
                edited.append("6");
            }break;
            case R.id.buttonSeven:{
                edited.append("7");
            }break;
            case R.id.buttonEight:{
                edited.append("8");
            }break;
            case R.id.buttonNine:{
                edited.append("9");
            }break;
            case R.id.buttonZero:{
                edited.append("0");
            }
            break;

        }
    }

    private void clearFields() {
        paymentAmount.setText("");
        phoneNumber.setText("");
    }

    private boolean isFocused(EditText ed){
        return ed.isFocused() && ed.hasFocus();
    }

    private EditText focusedTextField(){
        return isFocused(phoneNumber)? phoneNumber:paymentAmount;
    }
}
