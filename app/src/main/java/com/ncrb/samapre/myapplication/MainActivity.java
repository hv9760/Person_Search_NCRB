package com.ncrb.samapre.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ncrb.samapre.myapplication.MCoCoRy;
import com.ncrb.samapre.myapplication.Utils;

import com.ncrb.samapre.myapplication.util.TextInputWatcher;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private Button btn_login;
    private Button btn_cancel;

    private TextInputLayout edt_userid;
    private String edt_userid1;

    private TextInputLayout edt_password;
    private String edt_password2;
    private Singleton singleton;


    private AppPreferences objAppPreferences;

   private SharedPreferences.Editor prefsEditor;
    private MCoCoRy mCoCoRy = new  MCoCoRy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            if(getSupportActionBar()!=null){
                getSupportActionBar().hide();

            }
            singleton = Singleton.getInstance();
            objAppPreferences = new AppPreferences(this);
            setContentView(R.layout.activity_login);
            btn_login = (Button) findViewById(R.id.button_login);
            btn_cancel = (Button) findViewById(R.id.btn_cancel);
            edt_userid = (TextInputLayout) findViewById(R.id.edt_userid);
            edt_password = (TextInputLayout) findViewById(R.id.edt_password);

            if (edt_userid.getEditText()!=null){
                edt_userid.getEditText().addTextChangedListener(new TextInputWatcher(edt_userid));
            }

            if ( edt_password .getEditText()!=null){
                edt_password .getEditText().addTextChangedListener(new TextInputWatcher(edt_password));
            }


            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    edt_userid.setEnabled(true);
                    edt_password.setEnabled(true);
                    if (edt_userid.getEditText()!=null&&edt_userid.getEditText().getText().length()>=2){

                        if (edt_password.getEditText()!=null&&edt_password.getEditText().getText().length()>=2){

                            login();
                        }else{
                            edt_password.setError("Please Input Password");

                            edt_password.requestFocus();
                        }

                    }else{
                        edt_userid.setError("Please Input UserName");
                        edt_password.requestFocus();
                    }

                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login() {
        if(edt_userid.getEditText()!=null){
            edt_userid1=edt_userid.getEditText().getText().toString();
        }
        if (edt_password.getEditText()!=null){
            edt_password2=edt_password.getEditText().getText().toString();
        }
        try {
            GetAuthDetailWebService(edt_userid1,edt_password2);
            //GetSeditionWebService();
        } catch (Exception ex) {
            Utils.printv("Exception " + ex.getMessage());
        }
    }

    public RestAdapter providesRestAdapter() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(480, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(480, TimeUnit.SECONDS);
        return new RestAdapter.Builder()
                .setEndpoint(Constants.API_BASE_URL)
                .setClient(new OkClient(okHttpClient))
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String msg) {
                        Log.i("Res Complaint -", msg);
                    }}).setLogLevel(RestAdapter.LogLevel.FULL).build();
    }


    @Override
    protected void onResume() {
        super.onResume();
        btn_login .setText("LOG IN");
        btn_login.setBackgroundColor(getResources().getColor(R.color.gray));
        btn_login.setTextColor(getResources().getColor(R.color.white));
        edt_userid.setEnabled(true);
        edt_password.setEnabled(true);




    }

    public void GetAuthDetailWebService(String username1, String password1) throws Exception {

        //  this.mProgressDialog.show();
        btn_login .setText("LOGGING YOU IN...PLEASE WAIT");
        btn_login.setBackgroundColor(getResources().getColor(R.color.blue));
        btn_login.setTextColor(getResources().getColor(R.color.white));
        edt_userid.setEnabled(false);
        edt_password.setEnabled(false);
        /**
         *
         * @logic : NCRB report on secure audit, hide the detail of user
         *
         * */

        String coco_seed = ""; String coco_seed_encd = "";


        try {

            Map postParams = new HashMap();
            postParams.put("username", username1.toString());
            postParams.put("password", password1.toString());
            postParams.put("sedition", "");



            // keep the username for next use
            this.singleton.username = username1.toString();
            // posting json on server with request params
            Gson gsonObj = new Gson();
            coco_seed = gsonObj.toJson(postParams);

            coco_seed_encd  = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), coco_seed, "ENCODE");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // -----------------------------------------------------------------

        // create a new hash which you want to send on server
        Map postParams = new HashMap();

        postParams.put("seed", coco_seed_encd);


        JSONPostParams jsonPostParams = new JSONPostParams("mLoginVerify", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);

        // -----------------------------------------------------------------

        apiCaller.mLoginVerify(jsonPostParams, new Callback<WSPLoginConnect>() {

            @Override
            public void failure(RetrofitError arg0) {

                btn_login .setText("LOG IN");
                btn_login.setBackgroundColor(getResources().getColor(R.color.gray));
                btn_login.setTextColor(getResources().getColor(R.color.white));
                Toast.makeText(getApplicationContext(), "Can not connect to server", Toast.LENGTH_LONG).show();
                edt_userid.setEnabled(true);
                edt_password.setEnabled(true);
            }// end failure

            @Override
            public void success(WSPLoginConnect result2, Response response) {
                // 1. convert seed into string
                // 2 .convert string into json
                try {
                    String jsonString = "";

                    if (result2.seed !=null&&!result2.seed.equals("")) {

                        jsonString = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), result2.seed, "DECODE");

                        if (jsonString.equals("")) {
                            Toast.makeText(getApplicationContext(), "System error, please contact administrator", Toast.LENGTH_LONG).show();
                            return;
                        }

                    }

                    Gson gson = new Gson();


                    WSPLoginConnect result = gson.fromJson(jsonString, WSPLoginConnect.class);

                    if (result.STATUS_CODE.equals("200")) {
                        if ( edt_userid.getEditText()!=null&&edt_password.getEditText()!=null){
                            objAppPreferences.saveUserLogin(edt_userid.getEditText().getText().toString(),edt_password.getEditText().getText().toString() );
                        }

                        btn_login .setText("SUCCESS...!");
                        btn_login.setBackgroundColor(getResources().getColor(R.color.green_A700));

                        btn_login.setTextColor(getResources().getColor(R.color.white));
                       final int interval = 2000; // 1 Second
                        final Handler handler = new Handler();
                        final Runnable runnable = new Runnable(){
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, Home.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        };

                        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
                        handler.postDelayed(runnable, interval);

                    } else {
                        btn_login.setText("LOG IN");
                        btn_login.setBackgroundColor(getResources().getColor(R.color.gray));
                        edt_userid.setEnabled(true);
                        edt_password.setEnabled(true);
                        btn_login.setTextColor(getResources().getColor(R.color.white));
                        Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

                    }
                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }// end success
        });
    }
    /**
     * @ very important logic : call the server to get token.
     *  token attached with login and validated at server end
     *  token logic code and decode written on server side only
     * @
     */

    public void GetSeditionWebService() throws Exception {
        //   this.mProgressDialog.show();
        /**
         *
         * @logic : NCRB report on secure audit, hide the detail of user
         *
         * */

        String coco_seed = ""; String coco_seed_encd = "";

        try {
            Map postParams = new HashMap();
            postParams.put("m_service", Constants.mReqSedition);
            // posting json on server with request params
            Gson gsonObj = new Gson();
            coco_seed = gsonObj.toJson(postParams);
            coco_seed_encd  = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), coco_seed, "ENCODE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RestAdapter restAdapter =providesRestAdapter();
        // create a new hash which you want to send on server
        Map postParams = new HashMap();
        postParams.put("seed", coco_seed_encd);
        JSONPostParams jsonPostParams = new JSONPostParams("mReqSedition", postParams);
        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);
        apiCaller.mReqSedition(jsonPostParams,
                new Callback<WSPReqSedition>() {
                    @Override
                    public void failure(RetrofitError arg0) {

                        Utils.showToastMsg(getApplicationContext(), "Can not connect to server.", Toast.LENGTH_SHORT);
                        Utils.printv("failure " + arg0.toString());
                        //  if (mProgressDialog != null && mProgressDialog.isShowing())
                        //    mProgressDialog.dismiss();
                    }// end failure

                    @Override
                    public void success(WSPReqSedition result, Response response) {
                        seditionResponse(result, response);
                    }// end success
                });
    }// end auth web service

    /*
     *
     *
     *
     * */

    public void seditionResponse(WSPReqSedition result, Response response) {

        //  if (mProgressDialog != null && mProgressDialog.isShowing())
        //    mProgressDialog.dismiss();
        if (result.getSTATUS_CODE().toString().equals("200")) {
            // don't store username and password in shared preference for security reason
            try {
                singleton.sedition_token = result.SEDITION;
                try {
                    GetAuthDetailWebService(edt_userid1,edt_password2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utils.showToastMsg(getApplicationContext(), result.MESSAGE, Toast.LENGTH_SHORT);
            }
        } else {
            Utils.showToastMsg(getApplicationContext(), result.MESSAGE, Toast.LENGTH_SHORT);
        }
    }// end seqdition response


}