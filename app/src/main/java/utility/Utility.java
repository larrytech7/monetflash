package utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.djlewis.monetflash.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by root on 5/14/16.
 */
public class Utility {

    public static final String PAYMENT_URL = "http://monetflash.iceteck.com/index.php/requestpayment";
    public static final String FIREBASE_TRANSACTION_URL =  "https://monetflash.firebaseio.com/";
    public static final String PHONE_KEY = "phonenumber";
    public static final String AMOUNT_KEY =  "amount";
    public final static  String APP_USER = "monetflash.app.preference.user";
    public final static String APP_AUTH = "monetflash.app.auth";
    public final static String APP_NUMBER = "monetflash.app.number";

    private Context context;

    private Utility(Context c){
        context = c;
    }

    public static Utility getInstance(Context c){
        return new Utility(c);
    }

    //start payment progress dialog
    public SweetAlertDialog startPaymentDialog(String message){
       SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setTitleText(context.getString(R.string.startpayment));
            sweetAlertDialog.setContentText(message);

        return sweetAlertDialog;
    }
    public void stopPaymentDialog(SweetAlertDialog sw, String message, final boolean success){
        if (sw != null){
            sw.setTitleText(context.getString(R.string.finished));
            sw.setContentText(message);
            sw.changeAlertType(success? SweetAlertDialog.SUCCESS_TYPE:SweetAlertDialog.ERROR_TYPE);
            sw.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            sw.show();
        }
    }

    public static void showMessage(View v, String message){
        Snackbar.make(v,message, Snackbar.LENGTH_LONG).show();
    }

    public static boolean isNetworkAvailable(Context c){
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo ==null? false:networkInfo.isConnected();
    }
}
