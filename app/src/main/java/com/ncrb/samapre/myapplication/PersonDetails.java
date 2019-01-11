package com.ncrb.samapre.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;

import com.ncrb.samapre.myapplication.ApiCaller;
import com.ncrb.samapre.myapplication.AppPreferences;
import com.ncrb.samapre.myapplication.Constants;
import com.ncrb.samapre.myapplication.FirInputs;
import com.ncrb.samapre.myapplication.JSONPostParams;
import com.ncrb.samapre.myapplication.MCoCoRy;

import com.ncrb.samapre.myapplication.Singleton;
import com.ncrb.samapre.myapplication.Utils;
import com.ncrb.samapre.myapplication.ViewPrintAdapter;
import com.ncrb.samapre.myapplication.WSPLoginConnect;
import com.ncrb.samapre.myapplication.fragments.PersonDetailsFragment;
import com.ncrb.samapre.myapplication.responses.PersonDetail;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class PersonDetails extends AppCompatActivity {



    private MCoCoRy mCoCoRy = new  MCoCoRy();
    private AppPreferences objAppPreferences;
    private Singleton singleton;



    private String FullRegnum,AccusedSrno;
    private String FIR_REG_NUM_detail, ACCUSED_SRNO_detail;

    private String  section, DISTRICT_CD,PS_CD,FIR_NUMBER_ONLY,FIR_YEAR_ONLY;






    private String full_name, alias, relation_type, relative_name;

    public SendValues sendValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);


        setToolbar();
        singleton = Singleton.getInstance();

        // core objects
        objAppPreferences = new AppPreferences(PersonDetails.this);

       // data = getIntent().getExtras();



        // update count value
        updateUserCount(PersonDetails.this, Constants.SearchTypePerson);


        AccusedSrno = ACCUSED_SRNO_detail=getIntent().getStringExtra("AccusedSrno");
        FullRegnum = FIR_REG_NUM_detail=getIntent().getStringExtra("REGISTRATION_NUM");
        full_name=getIntent().getStringExtra("FULL_NAME");
        alias=getIntent().getStringExtra("ALIAS");
        relation_type=getIntent().getStringExtra("RELATION_TYPE");
        relative_name=getIntent().getStringExtra("RELATIVE_NAME");

        try {
            GetPersonDetailDisplayWebService();

        } catch (Exception e) {
            e.printStackTrace();
        }


        FragmentManager myfragmentManager = getSupportFragmentManager();
        FragmentTransaction myfragmentTransaction = myfragmentManager.beginTransaction ();

        myfragmentTransaction.add(R.id.activity_person_details, PersonDetailsFragment.newInstance(full_name, alias, relation_type, relative_name, ACCUSED_SRNO_detail,FIR_REG_NUM_detail)).commit();

    }// end oncreate


    public interface SendValues{


        void values(PersonDetail response);
    }







    private void check() {
        if(FullRegnum==null ||FullRegnum==""|| AccusedSrno==null||AccusedSrno=="")
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof SendValues) {
            sendValues = (SendValues) fragment;
        }
    }


    public void printPDF() {
        try {
            PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
            printManager.print("print_any_view_job_name", new ViewPrintAdapter(this, findViewById(R.id.pdfLayout)), null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create and update count dynamically
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




    private void setToolbar(){


        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.person_search_details));
        toolbar.inflateMenu(R.menu.dots);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.view_fir:
                        check();
                        return true;
                    case R.id.save_pdf:
                        printPDF();
                        return true;
                }
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    public void GetPersonDetailDisplayWebService() throws Exception {

        String coco_seed = ""; String coco_seed_encd = "";


        try {

            Map postParams = new HashMap();

            postParams.put("FIR_REG_NUM_detail",FIR_REG_NUM_detail.toString());
            postParams.put("ACCUSED_SRNO_detail",ACCUSED_SRNO_detail.toString());

            Gson gsonObj = new Gson();
            coco_seed = gsonObj.toJson(postParams);



            coco_seed_encd  = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), coco_seed, "ENCODE");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map postParams = new HashMap();

        postParams.put("seed", coco_seed_encd);

        JSONPostParams jsonPostParams = new JSONPostParams("mPersonDetailDisplay", postParams);


        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);

        int cnt;
        apiCaller.mPersonDetailDisplay(jsonPostParams, new Callback<WSPLoginConnect>() {

                    @Override
                    public void failure(RetrofitError arg0) {

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
                        Log.d("results", result.toString());
                        if (result.STATUS_CODE.toString().equals("200")) {
                            // if (mProgressDialog != null && mProgressDialog.isShowing())
                            //  mProgressDialog.dismiss();

                            try {
                                JSONObject reader = new JSONObject(jsonString);
                                JSONArray states = reader.getJSONArray("PersonDetailDisplayList");
                                for (int i = 0; i < states.length(); i++) {
                                    JSONObject jsonObj2 = states.getJSONObject(i);

                                    PersonDetail personDetail=new PersonDetail();
                                    personDetail.setGENDER(states.getJSONObject(0).getString("GENDER"));
                                    personDetail.setAGE(states.getJSONObject(0).getString("AGE"));

                                    personDetail.setRELIGION(states.getJSONObject(0).getString("RELIGION"));
                                    personDetail.setHEIGHT_FROM_CM(states.getJSONObject(0).getString("HEIGHT_FROM_CM"));
                                    personDetail.setHEIGHT_TO_CM(states.getJSONObject(0).getString("HEIGHT_TO_CM"));
                                    personDetail.setUID_NUM(states.getJSONObject(0).getString("UID_NUM"));

                                    personDetail.setAll_accused_names(states.getJSONObject(0).getString("all_accused_names"));
                                    personDetail.setSTATE_ENG(states.getJSONObject(0).getString("STATE_ENG"));
                                    personDetail.setDISTRICT(states.getJSONObject(0).getString("DISTRICT"));

                                    personDetail.setPS(states.getJSONObject(0).getString("PS"));
                                    personDetail.setREG_NUM(states.getJSONObject(0).getString("REG_NUM"));
                                    personDetail.setREG_DT(states.getJSONObject(0).getString("REG_DT"));
                                    personDetail.setPERMANENT(states.getJSONObject(0).getString("PERMANENT"));
                                    personDetail.setPRESENT(states.getJSONObject(0).getString("PRESENT"));
                                    personDetail.setUploadedFile(states.getJSONObject(0).getString("UploadedFile"));
                                    personDetail.setSection(states.getJSONObject(0).getString("section"));
                                    personDetail.setDISTRICT_CD(states.getJSONObject(0).getString("DISTRICT_CD"));

                                    personDetail.setPS_CD(states.getJSONObject(0).getString("PS_CD"));
                                    personDetail.setFIR_YEAR_ONLY(states.getJSONObject(0).getString("FIR_YEAR_ONLY"));
                                    personDetail.setFIR_NUMBER_ONLY(states.getJSONObject(0).getString("FIR_NUMBER_ONLY"));


                                    sendValues.values(personDetail);


                                } //end forloop


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                        }
                    }// end success

                }

        );

    }
}
