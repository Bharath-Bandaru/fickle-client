package info.fickle.fickleclient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by Bharath on 04/11/16.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText fullname,email,phno;
    public static final String PREFS = "MY_PREF";
    public static final String Stat = "Status";
    public static final String FullName = "FULLNAME";
    public static final String EMAIL = "email";
    public static final String PHNO = "phno";
    public static final int MY_PERMISSIONS_REQUEST = 1;
    private TextInputLayout inp_email,inp_full,inp_phno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = this.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(sharedPref.getBoolean(Stat,false)){
            Intent i= new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.CHANGE_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.CHANGE_WIFI_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.CHANGE_WIFI_STATE},
                        MY_PERMISSIONS_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        fullname = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.emailid);
        phno = (EditText) findViewById(R.id.phno);
        inp_email =(TextInputLayout) findViewById(R.id.input_email);
        inp_phno =(TextInputLayout) findViewById(R.id.input_phno);
        inp_full =(TextInputLayout) findViewById(R.id.input_fullname);
        editor.putBoolean(Stat,true);
        editor.commit();

    }
    public void onClickLogin(View v){

        validate();
    }

    public void validate(){
        String emaili = email.getText().toString();
        String fullnamei = fullname.getText().toString();
        String phnoi = phno.getText().toString();

        if(fullnamei.isEmpty()){
            inp_full.setError("Enter your Name!");
            return;
        }else{
            inp_full.setErrorEnabled(false);
        }
        if(phnoi.isEmpty()){
            inp_phno.setError("Enter your Phone No!");
            return;
        }else{
            inp_phno.setErrorEnabled(false);
        }
        if(emaili.isEmpty() || !isValidEmail(emaili)){
            inp_email.setError("Invalid Email");
            return;
        }else{
            inp_email.setErrorEnabled(false);

        }
        SharedPreferences sharedPref = this.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(FullName, fullnamei);
        editor.putString(PHNO, phnoi);
        editor.putString(EMAIL, emaili);
        editor.commit();
        Intent i= new Intent(this,MainActivity.class);
        startActivity(i);
        finish();

    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
