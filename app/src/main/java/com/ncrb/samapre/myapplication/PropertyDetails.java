package com.ncrb.samapre.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class PropertyDetails extends AppCompatActivity {
    ImageButton btn_Back;
    AppPreferences objAppPreferences;
    Singleton singleton;

    Bundle data;
    String PROP_REG_NUM, FIR_REG_NUM_Prop;
    public ProgressDialog mProgressDialog;
    TextView textView_property_fir_no;
    TextView textView_property_vehicle_type ;
    TextView textView_property_owner_name;
    TextView textView_REGISTRATION_NO;
    TextView textView_CHASSIS_NO;
    TextView textView_ENGINE_NO;

    TextView textView_State;
    TextView textView_district;
    TextView textView_ps;
    TextView textView_propertyNature;
    TextView textView_model;
    TextView textView_color;
    TextView textView_manufacturer;
    TextView textView_GD_No, textView_property_fir_regdt,textView_property_fir_actsec;
    MCoCoRy mCoCoRy = new  MCoCoRy();
    String Pr_FIR_NO, Pr_FIR_DT, Pr_OWNER_NAME,DISTRICT_CD,PS_CD,FIR_NUMBER_ONLY,FIR_YEAR_ONLY, Pr_State, Pr_district, Pr_PS, Pr_propertyNature, Pr_mvMake, Pr_mvColor, Pr_GD_No, Pr_section;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        Button btn_pdf, btn_view_fir;
        singleton = Singleton.getInstance();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");

        // core objects
        objAppPreferences = new AppPreferences(PropertyDetails.this);
        // update count value
        updateUserCount(PropertyDetails.this, Constants.SearchTypeProperty);
        btn_pdf=(Button)findViewById(R.id.btn_pdf);
        btn_view_fir=(Button)findViewById(R.id.btn_view_fir);
        textView_property_fir_no=(TextView)findViewById(R.id.txt_property_fir_no);
        textView_property_vehicle_type=(TextView)findViewById(R.id.txt_vehicle_type);
        textView_property_owner_name=(TextView)findViewById(R.id.txt_property_owner_name);

        textView_REGISTRATION_NO=(TextView)findViewById(R.id.txt_REGISTRATION_NO);
        textView_CHASSIS_NO=(TextView)findViewById(R.id.txt_CHASSIS_NO);
        textView_ENGINE_NO=(TextView)findViewById(R.id.txt_ENGINE_NO);

        textView_State=(TextView)findViewById(R.id.txt_State);
        textView_district=(TextView)findViewById(R.id.txt_district);
        textView_ps=(TextView)findViewById(R.id.txt_ps);
        textView_propertyNature=(TextView)findViewById(R.id.txt_propertyNature);
        textView_manufacturer=(TextView)findViewById(R.id.txt_vehicle_Manu);
        textView_model=(TextView)findViewById(R.id.txt_vehicle_Model);
        textView_color=(TextView)findViewById(R.id.txt_vehicle_color);
        textView_GD_No=(TextView)findViewById(R.id.txt_property_GD_No);
        textView_property_fir_regdt=(TextView)findViewById(R.id.txt_property_fir_regdt);
        textView_property_fir_actsec=(TextView)findViewById(R.id.txt_property_fir_actsec);

        btn_Back=(ImageButton)findViewById(R.id.btn_Back);

        data= getIntent().getExtras();

        PROP_REG_NUM=data.getString("PROP_REG_NUM");

        FIR_REG_NUM_Prop=data.getString("Full_FIR_NUMBER");

        try {
            GetPropertyDetailDisplayWebService();

        } catch (Exception e) {
            e.printStackTrace();
        }


        textView_property_vehicle_type.setText(data.getString("Vehicle_Type"));
        textView_REGISTRATION_NO.setText(data.getString("Registration_Number"));
        textView_CHASSIS_NO.setText(data.getString("CHASSIS_NO"));
        textView_ENGINE_NO.setText(data.getString("ENGINE_NO"));
        textView_model.setText(data.getString("mvModel"));




        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printPDF(view);
            }
        });

        btn_view_fir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(FIR_REG_NUM_Prop==null||FIR_REG_NUM_Prop==""||TextUtils.isEmpty(FIR_REG_NUM_Prop))
                {
                    Toast.makeText(getApplicationContext(), "No Data Available...", Toast.LENGTH_LONG).show();
                }
                else {

                    singleton.firregnum = FIR_NUMBER_ONLY;// fir number 4 digit only
                    singleton.district_cd = DISTRICT_CD;
                    singleton.ps_cd = PS_CD;
                    singleton.year = FIR_YEAR_ONLY;// year of fir

                    try {

                        Intent intentmore = new Intent(getApplicationContext(), FirInputs.class);
                        startActivity(intentmore);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        });

    }
    public void printPDF(View view) {
        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
        printManager.print("print_any_view_job_name", new ViewPrintAdapter(this,findViewById(R.id.pdfLayout)), null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }


    public void updateUserCount(Context context, String searchType) {

        String date = Utils.getDateTime("dd-MM-yyyy");

        String uniqueKey = searchType+" "+date+" "+this.singleton.username;

        try {

            // core objects
            objAppPreferences = new AppPreferences(context);

            String checkUser = objAppPreferences.getUserDefaults(uniqueKey);

            if(!checkUser.equals("")) {
                int count = Integer.parseInt(checkUser);
                count++;
                objAppPreferences.setUserDefaults(uniqueKey, ""+count);
                Utils.printv("Set Preference "+uniqueKey+"count: "+count);
            } else {
                objAppPreferences.setUserDefaults(uniqueKey, "1");
                Utils.printv("Set Preference "+uniqueKey+"count: 1");
            }

        }catch (Exception e){
            e.printStackTrace();
            objAppPreferences.setUserDefaults(uniqueKey, "1");
            Utils.printv("Set Preference "+uniqueKey+"count: 1");
        }

    }// end update count

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
                    }}).setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }
    public void GetPropertyDetailDisplayWebService() throws Exception {
        this.mProgressDialog.show();
        String coco_seed = ""; String coco_seed_encd = "";

        try {
        Map postParams = new HashMap();

        postParams.put("PROP_REG_NUM",PROP_REG_NUM.toString());

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

        JSONPostParams jsonPostParams = new JSONPostParams("mPropertyDetailDisplay", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);

        int cnt;
        apiCaller.mPropertyDetailDisplay(jsonPostParams, new Callback<WSPLoginConnect>() {

                    @Override
                    public void failure(RetrofitError arg0) {
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Can not connect to server.", Toast.LENGTH_LONG).show();

                    }// end failure

                    @Override
                    public void success(WSPLoginConnect result2, Response response) {

                        String jsonString = "";

                        if(!result2.seed.equals("")) {

                            jsonString = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), result2.seed, "DECODE");

                            if(jsonString.equals("")) {
                                Toast.makeText(getApplicationContext(), "System error, please contact administrator.", Toast.LENGTH_LONG).show();
                                return;
                            }

                        }

                        Gson gson = new Gson();


                        WSPLoginConnect result = gson.fromJson(jsonString, WSPLoginConnect.class);
                        if (result.STATUS_CODE.toString().equals("200")) {
                            if (mProgressDialog != null && mProgressDialog.isShowing())
                                mProgressDialog.dismiss();

                            try {
                                JSONObject reader = new JSONObject(jsonString);
                                JSONArray states = reader.getJSONArray("PropertyDetailDisplayList");

                                for (int i = 0; i < states.length(); i++) {
                                    JSONObject jsonObj2 = states.getJSONObject(i);

                                    Pr_FIR_NO=states.getJSONObject(0).getString("FIR_REGN_NUM_Prop");
                                    Pr_FIR_DT=states.getJSONObject(0).getString("FIR_REGISTRATION_DT");
                                    Pr_OWNER_NAME=states.getJSONObject(0).getString("REG_OWNER");
                                    Pr_State=states.getJSONObject(0).getString("State");
                                    Pr_district =states.getJSONObject(0).getString("district");
                                    Pr_PS=states.getJSONObject(0).getString("ps");
                                    Pr_propertyNature=states.getJSONObject(0).getString("propertyNature");
                                    Pr_mvMake=states.getJSONObject(0).getString("mvMake");
                                    Pr_mvColor=states.getJSONObject(0).getString("mvColor");
                                    Pr_GD_No=states.getJSONObject(0).getString("GD_No");
                                    Pr_section=states.getJSONObject(0).getString("section");
                                    DISTRICT_CD=states.getJSONObject(0).getString("DISTRICT_CD");
                                    PS_CD=states.getJSONObject(0).getString("PS_CD");
                                    FIR_NUMBER_ONLY=states.getJSONObject(0).getString("FIR_NUMBER_ONLY");
                                    FIR_YEAR_ONLY=states.getJSONObject(0).getString("FIR_YEAR_ONLY");

                                } //end forloop

                                textView_property_fir_no.setText(Pr_FIR_NO);
                                textView_property_fir_regdt.setText(Pr_FIR_DT);
                                textView_property_owner_name.setText(Pr_OWNER_NAME);
                                textView_State.setText(Pr_State);
                                textView_district.setText(Pr_district);
                                textView_ps.setText(Pr_PS);
                                textView_propertyNature.setText(Pr_propertyNature);
                                textView_manufacturer.setText(Pr_mvMake);
                                textView_color.setText(Pr_mvColor);
                                textView_GD_No.setText(Pr_GD_No);
                                textView_property_fir_actsec.setText(Pr_section);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            if (mProgressDialog != null && mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();


                        }
                    }// end success

                }

        );




    }
}
