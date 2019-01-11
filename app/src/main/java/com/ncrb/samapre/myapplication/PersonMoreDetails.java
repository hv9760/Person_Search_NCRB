package com.ncrb.samapre.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.github.silvestrpredko.dotprogressbar.DotProgressBarBuilder;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class PersonMoreDetails extends AppCompatActivity {
    ImageButton btn_md_back;
    MCoCoRy mCoCoRy = new  MCoCoRy();





    private TextView loading;
    private DotProgressBar dot_progress_bar;
    private ScrollView scrollView;

    private  String FIR_REG_NUM,ACCUSED_SRNO;
    private  String ARREST_SURRENDER_DT,STATE_ENG_Arr,DISTRICT_Arr,PS_Arr,occupation_md,ARREST_ACTION_md;
    private  String IS_CHRGSHEETED_md,CHARGESHEET_NUM_md,CHARGESHEET_DT_md, CHARGESHEET_ACTSEC_md;
    private  String ArrestedResult, SURREND_MAGISTRATE, SURRENDERED_COURT;
    private  String IsConvicted, PunishmentType, JudgementDt, PunishmentPeriod;
    private String isProclaimedOffender, ProclaimedOffenderCourtName, ProclaimedOffenderCourtType, ProclaimedOffenderCourtLocation, ProclaimedOffenderOrderNum, ProclaimedOffenderOrderDt, isHabitualOffender;
    private  String section;


    private    TextView textView_person_md_ArrSurrDt,textView_person_md_StateArr, textView_person_md_DistrictArr,textView_person_md_PSArr,textView_person_md_SectionCdArr;
    private  TextView textView_person_md_OccArr, textView_person_md_ArrAction, textView_person_md_IsChrg, textView_person_md_ChrgNum, textView_person_md_ChrgDt;
    private  TextView textView_person_md_ChrgActSec;
    private  TextView textView_person_md_ArrestedResult, textView_person_md_SURREND_MAGISTRATE, textView_person_md_SURRENDERED_COURT;
    private  TextView textView_person_md_IsConvicted, textView_person_md_PunishmentType, textView_person_md_JudgementDt, textView_person_md_PunishmentPeriod;
    private   TextView textView_person_md_isProclaimedOffender,textView_person_md_ProclaimedOffenderCourtName,textView_person_md_ProclaimedOffenderCourtType, textView_person_md_ProclaimedOffenderCourtLocation;
    private  TextView textView_person_md_ProclaimedOffenderOrderNum, textView_person_md_ProclaimedOffenderOrderDt, textView_person_md_isHabitualOffender;
    Button btn_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_more_details_activity);
        setToolbar();

        btn_pdf=(Button)findViewById(R.id.btn_pdf);

        textView_person_md_ArrSurrDt=(TextView)findViewById(R.id.txt_person_md_ArrSurrDt);
        textView_person_md_StateArr=(TextView)findViewById(R.id.txt_person_md_StateArr);
        textView_person_md_DistrictArr=(TextView)findViewById(R.id.txt_person_md_DistrictArr);
        textView_person_md_PSArr=(TextView)findViewById(R.id.txt_person_md_PSArr);
        textView_person_md_SectionCdArr=(TextView)findViewById(R.id.txt_person_md_SectionCdArr);
        textView_person_md_OccArr=(TextView)findViewById(R.id.txt_person_md_OccArr);
        textView_person_md_ArrAction=(TextView)findViewById(R.id.txt_person_md_ArrAction);
        textView_person_md_ArrestedResult=(TextView)findViewById(R.id.txt_person_md_ArrestedResult);
        textView_person_md_SURREND_MAGISTRATE=(TextView)findViewById(R.id.txt_person_md_SURREND_MAGISTRATE);
        textView_person_md_SURRENDERED_COURT=(TextView)findViewById(R.id.txt_person_md_SURRENDERED_COURT);
        textView_person_md_IsChrg=(TextView)findViewById(R.id.txt_person_md_IsChrg);
        textView_person_md_ChrgNum=(TextView)findViewById(R.id.txt_person_md_ChrgNum);
        textView_person_md_ChrgDt=(TextView)findViewById(R.id.txt_person_md_ChrgDt);
        textView_person_md_ChrgActSec=(TextView)findViewById(R.id.txt_person_md_ChrgActSec);

        scrollView=findViewById(R.id.scroll); loading=findViewById(R.id.loading);
        textView_person_md_IsConvicted=(TextView)findViewById(R.id.txt_person_md_IsConvicted);
        textView_person_md_PunishmentType=(TextView)findViewById(R.id.txt_person_md_PunishmentType);
        textView_person_md_JudgementDt=(TextView)findViewById(R.id.txt_person_md_JudgementDt);
        textView_person_md_PunishmentPeriod=(TextView)findViewById(R.id.txt_person_md_PunishmentPeriod);

        textView_person_md_isProclaimedOffender=(TextView)findViewById(R.id.txt_person_md_isProclaimedOffender);
        textView_person_md_ProclaimedOffenderCourtName=(TextView)findViewById(R.id.txt_person_md_ProclaimedOffenderCourtName);
        textView_person_md_ProclaimedOffenderCourtType=(TextView)findViewById(R.id.txt_person_md_ProclaimedOffenderCourtType);
        textView_person_md_ProclaimedOffenderCourtLocation=(TextView)findViewById(R.id.txt_person_md_ProclaimedOffenderCourtLocation);
        textView_person_md_ProclaimedOffenderOrderNum=(TextView)findViewById(R.id.txt_person_md_ProclaimedOffenderOrderNum);
        textView_person_md_ProclaimedOffenderOrderDt=(TextView)findViewById(R.id.txt_person_md_ProclaimedOffenderOrderDt);
        textView_person_md_isHabitualOffender=(TextView)findViewById(R.id.txt_person_md_isHabitualOffender);
        dot_progress_bar=findViewById(R.id.dot_progress_bar);
        new DotProgressBarBuilder(getApplicationContext())


                .setDotAmount(3)
                .setAnimationDirection(DotProgressBar.RIGHT_DIRECTION)
                .build();


        Bundle data= getIntent().getExtras();

        FIR_REG_NUM=data.getString("REGISTRATION_NUM");
        ACCUSED_SRNO=data.getString("AccusedSrno");

        try {
            GetArrestDetailWebService();
            GetChargesheetDetailWebService();
            GetConvictDetailWebService();
            GetOffenderDetailWebService();

        } catch (Exception e) {
            e.printStackTrace();
        }



        btn_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printPDF(view);
            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void printPDF(View view) {
        try {
            PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
            printManager.print("print_any_view_job_name", new ViewPrintAdapter(this, findViewById(R.id.pdfLayout)), null);
        }
        catch (Exception e) {
            e.printStackTrace();
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
                    }}).setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    private void setToolbar(){


        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.person_more_details));



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    public void GetArrestDetailWebService() throws Exception {

        //  this.mProgressDialog.show();


        dot_progress_bar.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);

        String coco_seed = ""; String coco_seed_encd = "";
        try {
            Map postParams = new HashMap();
        postParams.put("FIR_REG_NUM",FIR_REG_NUM.toString());
        postParams.put("ACCUSED_SRNO",ACCUSED_SRNO.toString());
        Gson gsonObj = new Gson();
        coco_seed = gsonObj.toJson(postParams);
        coco_seed_encd  = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), coco_seed, "ENCODE");
    } catch (Exception e) {
        e.printStackTrace();
    }
    Map postParams = new HashMap();

        postParams.put("seed", coco_seed_encd);

        JSONPostParams jsonPostParams = new JSONPostParams("mPersonMoreDetails", postParams);

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);
        apiCaller.mPersonMoreDetails(jsonPostParams, new Callback<WSPLoginConnect>() {

                    @Override
                    public void failure(RetrofitError arg0) {

                        dot_progress_bar.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.INVISIBLE);

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

                            dot_progress_bar.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);

                            TypedInput body = response.getBody();

                            try {
                                JSONObject reader = new JSONObject(jsonString);
                                JSONArray states = reader.getJSONArray("PersonMoreDetailsList");

                                for (int i = 0; i < states.length(); i++) {
                                    JSONObject jsonObj2 = states.getJSONObject(i);
                                     ARREST_SURRENDER_DT=states.getJSONObject(0).getString("ARREST_SURRENDER_DT");
                                    STATE_ENG_Arr=states.getJSONObject(0).getString("STATE_ENG");
                                    DISTRICT_Arr=states.getJSONObject(0).getString("DISTRICT");
                                    PS_Arr=states.getJSONObject(0).getString("PS");

                                    occupation_md=states.getJSONObject(0).getString("occupation");
                                    ARREST_ACTION_md=states.getJSONObject(0).getString("ARREST_ACTION");

                                    ArrestedResult=states.getJSONObject(0).getString("ArrestedResult");
                                    SURREND_MAGISTRATE=states.getJSONObject(0).getString("SURREND_MAGISTRATE");
                                    SURRENDERED_COURT=states.getJSONObject(0).getString("SURRENDERED_COURT");
                                    section=states.getJSONObject(0).getString("section");

                                } //end forloop


                                if(ARREST_SURRENDER_DT!=null&&!TextUtils.isEmpty(ARREST_SURRENDER_DT)){
                                    textView_person_md_ArrSurrDt.setText(ARREST_SURRENDER_DT);
                                }else{
                                    textView_person_md_ArrSurrDt.setText(getResources().getString(R.string.info_not_available));
                                }


                                if (STATE_ENG_Arr!=null&&!TextUtils.isEmpty(STATE_ENG_Arr)){
                                    textView_person_md_StateArr.setText(STATE_ENG_Arr);
                                }else{
                                    textView_person_md_StateArr.setText(getResources().getString(R.string.info_not_available));
                                }


                                if (DISTRICT_Arr!=null&&!TextUtils.isEmpty(DISTRICT_Arr)){
                                    textView_person_md_DistrictArr.setText(DISTRICT_Arr);
                                }else{
                                    textView_person_md_DistrictArr.setText(getResources().getString(R.string.info_not_available));
                                }

                                if (PS_Arr!=null&&!TextUtils.isEmpty(PS_Arr)){
                                    textView_person_md_PSArr.setText(PS_Arr);
                                }else{
                                    textView_person_md_PSArr.setText(getResources().getString(R.string.info_not_available));
                                }

                               if (section!=null&&!TextUtils.isEmpty(section)){
                                   textView_person_md_SectionCdArr.setText(section);
                               }else{
                                   textView_person_md_SectionCdArr.setText(getResources().getString(R.string.info_not_available));
                               }

                               if (occupation_md!=null&&!TextUtils.isEmpty(occupation_md)){
                                   textView_person_md_OccArr.setText(occupation_md);
                               }else{
                                   textView_person_md_OccArr.setText(getResources().getString(R.string.info_not_available));
                               }


                               if (ARREST_ACTION_md!=null&&!TextUtils.isEmpty(ARREST_ACTION_md)){
                                   textView_person_md_ArrAction.setText(ARREST_ACTION_md);
                               }else{
                                   textView_person_md_ArrAction.setText(getResources().getString(R.string.info_not_available));
                               }


                               if (ArrestedResult!=null&&!TextUtils.isEmpty(ArrestedResult)){
                                   textView_person_md_ArrestedResult.setText(ArrestedResult);
                               }else{
                                   textView_person_md_ArrestedResult.setText(getResources().getString(R.string.info_not_available));
                               }

                               if (SURREND_MAGISTRATE!=null&&!TextUtils.isEmpty(SURREND_MAGISTRATE)){
                                   textView_person_md_SURREND_MAGISTRATE.setText(SURREND_MAGISTRATE);
                               }else{
                                   textView_person_md_SURREND_MAGISTRATE.setText(getResources().getString(R.string.info_not_available));
                               }

                                if (SURRENDERED_COURT!=null&&!TextUtils.isEmpty(SURRENDERED_COURT)){
                                    textView_person_md_SURRENDERED_COURT.setText(SURRENDERED_COURT);
                                }else{
                                    textView_person_md_SURRENDERED_COURT.setText(getResources().getString(R.string.info_not_available));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            dot_progress_bar.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.VISIBLE);
                            scrollView.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

                        }
                    }

                }

        );

    }
    public void GetChargesheetDetailWebService() throws Exception {
        //  this.mProgressDialog.show();
        String coco_seed = ""; String coco_seed_encd = "";


        try {

            Map postParams = new HashMap();

        postParams.put("FIR_REG_NUM_CS",FIR_REG_NUM.toString());
        postParams.put("ACCUSED_SRNO_CS",ACCUSED_SRNO.toString());

            Gson gsonObj = new Gson();
            coco_seed = gsonObj.toJson(postParams);




            coco_seed_encd  = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), coco_seed, "ENCODE");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map postParams = new HashMap();

        postParams.put("seed", coco_seed_encd);

        JSONPostParams jsonPostParams = new JSONPostParams("mPersonMoreDetailsChargesheet", postParams);

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);

        int cnt;
        apiCaller.mPersonMoreDetailsChargesheet(jsonPostParams, new Callback<WSPLoginConnect>() {

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
                        if (result.STATUS_CODE.toString().equals("200")) {

                            try {
                                JSONObject reader = new JSONObject(jsonString);
                                JSONArray states = reader.getJSONArray("PersonMoreDetailsListChargesheet");

                                for (int i = 0; i < states.length(); i++) {
                                    JSONObject jsonObj2 = states.getJSONObject(i);



                                    IS_CHRGSHEETED_md=states.getJSONObject(0).getString("IS_CHRGSHEETED");
                                    CHARGESHEET_NUM_md=states.getJSONObject(0).getString("CHARGESHEET_NUM");
                                    CHARGESHEET_DT_md=states.getJSONObject(0).getString("CHARGESHEET_DT");
                                    CHARGESHEET_ACTSEC_md=states.getJSONObject(0).getString("Section");


                                } //end forloop


                                if (IS_CHRGSHEETED_md!=null&&!TextUtils.isEmpty(IS_CHRGSHEETED_md)){
                                    textView_person_md_IsChrg.setText(IS_CHRGSHEETED_md);
                                }else{
                                    textView_person_md_IsChrg.setText(getResources().getString(R.string.info_not_available));
                                }

                                if (CHARGESHEET_NUM_md!=null&&!TextUtils.isEmpty(CHARGESHEET_NUM_md)){
                                    textView_person_md_ChrgNum.setText(CHARGESHEET_NUM_md);
                                }else{
                                    textView_person_md_ChrgNum.setText(getResources().getString(R.string.info_not_available));
                                }

                                if (CHARGESHEET_DT_md!=null&&!TextUtils.isEmpty(CHARGESHEET_DT_md)){
                                    textView_person_md_ChrgDt.setText(CHARGESHEET_DT_md);
                                }else{
                                    textView_person_md_ChrgDt.setText(getResources().getString(R.string.info_not_available));
                                }

                                if (CHARGESHEET_ACTSEC_md!=null&&!TextUtils.isEmpty( CHARGESHEET_ACTSEC_md)){
                                    textView_person_md_ChrgActSec.setText(CHARGESHEET_ACTSEC_md);
                                }else{
                                    textView_person_md_ChrgActSec.setText(getResources().getString(R.string.info_not_available));
                                }


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

    public void GetConvictDetailWebService() throws Exception {

        String coco_seed = ""; String coco_seed_encd = "";


        try {

            Map postParams = new HashMap();

        postParams.put("FIR_REG_NUM_con",FIR_REG_NUM.toString());
        postParams.put("ACCUSED_SRNO_con",ACCUSED_SRNO.toString());
        Gson gsonObj = new Gson();
        coco_seed = gsonObj.toJson(postParams);
        coco_seed_encd  = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), coco_seed, "ENCODE");

    } catch (Exception e) {
        e.printStackTrace();
    }

    Map postParams = new HashMap();

        postParams.put("seed", coco_seed_encd);


        JSONPostParams jsonPostParams = new JSONPostParams("mPersonMoreDetailsConvict", postParams);


        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);

        int cnt;
        apiCaller.mPersonMoreDetailsConvict(jsonPostParams, new Callback<WSPLoginConnect>() {

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
                        if (result.STATUS_CODE.toString().equals("200")) {

                            try {
                                JSONObject reader = new JSONObject(jsonString);
                                JSONArray states = reader.getJSONArray("PersonMoreDetailsConvictList");

                                for (int i = 0; i < states.length(); i++) {
                                    JSONObject jsonObj2 = states.getJSONObject(i);

                                    System.out.println("IsConvicted:"+IsConvicted);


                                    IsConvicted=states.getJSONObject(0).getString("IsConvicted");
                                    PunishmentType=states.getJSONObject(0).getString("PUNISHMENT_TYPE");
                                    JudgementDt=states.getJSONObject(0).getString("judgement_date");
                                    PunishmentPeriod = states.getJSONObject(0).getString("PUNISH_YRS") + " Yrs " + states.getJSONObject(0).getString("PUNISH_MNTH") + " Months " + states.getJSONObject(0).getString("PUNISH_DAYS") + " Days";

                                } //end forloop


                                if(IsConvicted!=null&&!TextUtils.isEmpty(IsConvicted)){
                                    textView_person_md_IsConvicted.setText(IsConvicted);
                                }else{
                                    textView_person_md_IsConvicted.setText(getResources().getString(R.string.info_not_available));
                                }

                                if (PunishmentType != null && !TextUtils.isEmpty(PunishmentType)) {
                                    textView_person_md_PunishmentType.setText(PunishmentType);

                                }else{
                                    textView_person_md_PunishmentType.setText(getResources().getString(R.string.info_not_available));
                                }

                                if (JudgementDt!=null&&!TextUtils.isEmpty(JudgementDt)){
                                    textView_person_md_JudgementDt.setText(JudgementDt);
                                }else{
                                    textView_person_md_JudgementDt.setText(getResources().getString(R.string.info_not_available));
                                }


                                if (PunishmentPeriod!=null&&!TextUtils.isEmpty(PunishmentPeriod)){
                                    textView_person_md_PunishmentPeriod.setText(PunishmentPeriod);

                                }else{
                                    textView_person_md_PunishmentPeriod.setText(getResources().getString(R.string.info_not_available));

                                }





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

    public void GetOffenderDetailWebService() throws Exception {

        String coco_seed = ""; String coco_seed_encd = "";

        try {

            Map postParams = new HashMap();

        postParams.put("FIR_REG_NUM_off",FIR_REG_NUM.toString());
        postParams.put("ACCUSED_SRNO_off",ACCUSED_SRNO.toString());

            Gson gsonObj = new Gson();
            coco_seed = gsonObj.toJson(postParams);

            coco_seed_encd  = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), coco_seed, "ENCODE");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map postParams = new HashMap();

        postParams.put("seed", coco_seed_encd);

        JSONPostParams jsonPostParams = new JSONPostParams("mPersonMoreDetailsPHOffender", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);

        int cnt;
        apiCaller.mPersonMoreDetailsPHOffender(jsonPostParams, new Callback<WSPLoginConnect>() {

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
                        if (result.STATUS_CODE.toString().equals("200")) {

                            try {
                                JSONObject reader = new JSONObject(jsonString);
                                JSONArray states = reader.getJSONArray("PersonMoreDetailsPHOffenderList");

                                for (int i = 0; i < states.length(); i++) {
                                    JSONObject jsonObj2 = states.getJSONObject(i);

                                    isProclaimedOffender=states.getJSONObject(0).getString("is_proclaimed_offender");
                                    ProclaimedOffenderCourtName=states.getJSONObject(0).getString("PROCL_OFFENDER_COURT_NAME");
                                    ProclaimedOffenderCourtType=states.getJSONObject(0).getString("COURT_TYPE");
                                    ProclaimedOffenderCourtLocation=states.getJSONObject(0).getString("PROCL_OFFENDER_COURT_LOCATION");
                                    ProclaimedOffenderOrderNum=states.getJSONObject(0).getString("PROCL_OFFENDER_ORDER_NUM");
                                    ProclaimedOffenderOrderDt=states.getJSONObject(0).getString("PROCL_OFFENDER_ORDER_DT");
                                    isHabitualOffender=states.getJSONObject(0).getString("HabitualOffender");

                                } //end forloop

                                if (isProclaimedOffender!=null&&!TextUtils.isEmpty(isProclaimedOffender)){
                                    textView_person_md_isProclaimedOffender.setText(isProclaimedOffender);
                                }else{
                                    textView_person_md_isProclaimedOffender.setText(getResources().getString(R.string.info_not_available));
                                }


                                if (ProclaimedOffenderCourtName!=null&&!TextUtils.isEmpty(ProclaimedOffenderCourtName)){
                                    textView_person_md_ProclaimedOffenderCourtName.setText(ProclaimedOffenderCourtName);
                                }else{
                                    textView_person_md_ProclaimedOffenderCourtName.setText(getResources().getString(R.string.info_not_available));
                                }

                                if (ProclaimedOffenderCourtType!=null&&!TextUtils.isEmpty(ProclaimedOffenderCourtType)){
                                    textView_person_md_ProclaimedOffenderCourtType.setText(ProclaimedOffenderCourtType);
                                }else{
                                    textView_person_md_ProclaimedOffenderCourtType.setText(getResources().getString(R.string.info_not_available));
                                }

                                if (ProclaimedOffenderCourtLocation!=null&&!TextUtils.isEmpty(ProclaimedOffenderCourtLocation)){
                                    textView_person_md_ProclaimedOffenderCourtLocation.setText(ProclaimedOffenderCourtLocation);
                                }else{
                                    textView_person_md_ProclaimedOffenderCourtLocation.setText(getResources().getString(R.string.info_not_available));
                                }


                                if (ProclaimedOffenderOrderNum!=null&&!TextUtils.isEmpty(ProclaimedOffenderOrderNum)){
                                    textView_person_md_ProclaimedOffenderOrderNum.setText(ProclaimedOffenderOrderNum);
                                }else{
                                    textView_person_md_ProclaimedOffenderOrderNum.setText(getResources().getString(R.string.info_not_available));
                                }


                                if (ProclaimedOffenderOrderDt!=null&&!TextUtils.isEmpty(ProclaimedOffenderOrderDt)){
                                    textView_person_md_ProclaimedOffenderOrderDt.setText(ProclaimedOffenderOrderDt);
                                }else{
                                    textView_person_md_ProclaimedOffenderOrderDt.setText(getResources().getString(R.string.info_not_available));
                                }

                                if (isHabitualOffender!=null&&!TextUtils.isEmpty(isHabitualOffender)){
                                    textView_person_md_isHabitualOffender.setText(isHabitualOffender);
                                }else{
                                    textView_person_md_isHabitualOffender.setText(getResources().getString(R.string.info_not_available));
                                }


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
