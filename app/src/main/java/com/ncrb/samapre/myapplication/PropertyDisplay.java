package com.ncrb.samapre.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.spec.PSSParameterSpec;
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
import android.app.ProgressDialog;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

public class PropertyDisplay extends AppCompatActivity {

    ImageButton btn_Back,btn_Next;
    Integer VTCODE,DistCode,PSCode;
    Integer Manufacturer;
    Integer Model,Color_Code;
    String RegistrationNumber;
    String ChasisNo;
    String EngineNo;
    String ChkUAP;
    String ChkPS;
    String ChkFIR;

    String STATE_CD=Constants.STATE_CD;
    static Integer CURRENT_PAGE_INDEX_AUTO=1;
    TextView title;

    public ProgressDialog mProgressDialog;
    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 10;
    private int noOfBtns;
    private Button[] btns;
    MCoCoRy mCoCoRy = new  MCoCoRy();
    private PropertySearchInfo propertySearchInfo;
    private ArrayList<PropertySearchInfo> arrayList_property_search=new ArrayList<PropertySearchInfo>();
    private ArrayList<PropertySearchInfo> sort=new ArrayList<PropertySearchInfo>();
    private PropertySearchAdapter   propertySearchAdapter;
    private ListView listview_property_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_display);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        listview_property_search=(ListView)findViewById(R.id.list_search_result) ;
        btn_Back = (ImageButton) findViewById(R.id.btn_Back);
        btn_Next = (ImageButton) findViewById(R.id.btn_Next);
        title = (TextView)findViewById(R.id.title);

        Bundle bundle = getIntent().getExtras();
        VTCODE=bundle.getInt("VTCODE");
        Color_Code=bundle.getInt("C_Code");
        Manufacturer=bundle.getInt("Manufacturer");
        Model=bundle.getInt("Model");
        RegistrationNumber=bundle.getString("RegistrationNumber");
        DistCode=bundle.getInt("Di_Code");
        PSCode=bundle.getInt("P_Code");

        if (RegistrationNumber.isEmpty())
        {
            RegistrationNumber="0";
        }

        ChasisNo=bundle.getString("ChasisNo");
        if (ChasisNo.isEmpty())
        {
            ChasisNo="0";
        }
        EngineNo=bundle.getString("EngineNo");
        if (EngineNo.isEmpty())
        {
            EngineNo="0";
        }
        Boolean UAP=bundle.getBoolean("UAP");
        Boolean PS=bundle.getBoolean("PS");
        Boolean REGISTRATION=bundle.getBoolean("REGISTRATION");
        if (UAP) {
            ChkUAP="true";
        }
        else
        {
            ChkUAP="false";
        }
        if (PS) {
            ChkPS="true";
        }
        else
        {
            ChkPS ="false";
        }
        if (REGISTRATION) {
            ChkFIR="true";
        }
        else
        {
            ChkFIR ="false";
        }


        try {
            GetAuthDetailWebService();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Btnfooter();
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                CURRENT_PAGE_INDEX_AUTO=CURRENT_PAGE_INDEX_AUTO+1;
                try {
                    GetAuthDetailWebService();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                View b=findViewById(R.id.btn_Next);
                b.setVisibility(View.INVISIBLE);
            }
        });
        listview_property_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(PropertyDisplay.this,PropertyDetails.class);



                intent.putExtra("PROP_REG_NUM",propertySearchAdapter.getItem(position).getPROP_REG_NUM());
                intent.putExtra("Vehicle_Type",propertySearchAdapter.getItem(position).getMV_Type());

                intent.putExtra("Registration_Number",propertySearchAdapter.getItem(position).getREGISTRATION_NO());
                intent.putExtra("CHASSIS_NO",propertySearchAdapter.getItem(position).getCHASSIS_NO());
                intent.putExtra("ENGINE_NO",propertySearchAdapter.getItem(position).getENGINE_NO());
                intent.putExtra("Full_FIR_NUMBER",propertySearchAdapter.getItem(position).getFull_FIR_NUMBER());
                intent.putExtra("mvModel",propertySearchAdapter.getItem(position).getMvModel());

                startActivity(intent);
            }
        });
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


    public void GetAuthDetailWebService() throws Exception {
        this.mProgressDialog.show();
        String coco_seed = ""; String coco_seed_encd = "";

        try {
        Map postParams = new HashMap();

        postParams.put("MATCH_CRITERIA_UAP",ChkUAP.toString());
        postParams.put("MATCH_CRITERIA_SEIZED",ChkPS.toString());
        postParams.put("MATCH_CRITERIA_REGISTERED",ChkFIR.toString());
        postParams.put("MV_TYPECD",VTCODE);
        postParams.put("MV_MAKECD",Manufacturer);
        postParams.put("MV_MODELCD",Model);
        postParams.put("REGISTRATION_NO",RegistrationNumber.toString());
        postParams.put("CHASSIS_NO",ChasisNo.toString());
        postParams.put("ENGINE_NO",EngineNo.toString());
        //postParams.put("ENGINE_NO",EngineNo.toString());
        postParams.put("MVCOLOR_CD",Color_Code);
        postParams.put("STATE_CD",STATE_CD);
        postParams.put("DISTRICT_CD",DistCode);
        postParams.put("PS_CD",PSCode);
        postParams.put("CURRENT_PAGE_INDEX_AUTO",CURRENT_PAGE_INDEX_AUTO);

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

        JSONPostParams jsonPostParams = new JSONPostParams("mPropertySearch", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);

        int cnt;
        apiCaller.mPropertySearch(jsonPostParams, new Callback<WSPLoginConnect>() {

                    @Override
                    public void failure(RetrofitError arg0) {
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Can not connect to server.", Toast.LENGTH_LONG).show();

                    }// end failure

                    @Override
                    public void success(WSPLoginConnect result2, Response response) {
                        // 1. convert seed into string
                        // 2 .convert string into json

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
                                JSONArray states = reader.getJSONArray("propertySearchCheck");

                                for (int i = 0; i < states.length(); i++) {
                                    JSONObject jsonObj2 = states.getJSONObject(i);

                                    Gson gson1 = new Gson();
                                    PropertySearchInfo objPropertySearchInfo = gson1.fromJson(jsonObj2.toString(), PropertySearchInfo.class);

                                    arrayList_property_search.add(objPropertySearchInfo);

                                } //end forloop

                                Btnfooter((int)states.length(),CURRENT_PAGE_INDEX_AUTO);
                                loadList(CURRENT_PAGE_INDEX_AUTO-1);
                                CheckBtnBackGroud(0,CURRENT_PAGE_INDEX_AUTO);

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

    @Override
    public void onBackPressed() {
        try {
            LinearLayout ll = (LinearLayout) findViewById(R.id.btnLay);
            for (int i = 0; i < noOfBtns; i++) {
                Button btn;
                btn = btns[i];
                ll.removeView(btn);
            }
            CURRENT_PAGE_INDEX_AUTO =1;
            super.onBackPressed();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void Btnfooter(int count,int CURRENT_PAGE_INDEX)
    {
try {
    TOTAL_LIST_ITEMS = count;
    int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
    val = val == 0 ? 0 : 1;
    noOfBtns = TOTAL_LIST_ITEMS / NUM_ITEMS_PAGE + val;

    LinearLayout ll = (LinearLayout) findViewById(R.id.btnLay);

    int k = (CURRENT_PAGE_INDEX * 5) - 4;
    if (CURRENT_PAGE_INDEX == 1)
        btns = new Button[noOfBtns];
    for (int i = 0; i < noOfBtns; i++) {
        if (CURRENT_PAGE_INDEX == 1)
            btns[i] = new Button(this);
        btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));

        btns[i].setText("" + k);
        k = k + 1;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (CURRENT_PAGE_INDEX == 1)
            ll.addView(btns[i], lp);

        final int j = i;
        final int cpi = CURRENT_PAGE_INDEX;

        btns[j].setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                loadList(j);
                CheckBtnBackGroud(j, cpi);

                check(j);
            }

        });
    }
}
catch (Exception e) {
    e.printStackTrace();
}
    }


    private void loadList(int number)
    {
        try {
            sort.clear();

            int start = number * NUM_ITEMS_PAGE;
            for (int i = start; i < (start) + NUM_ITEMS_PAGE; i++) {
                if (i < arrayList_property_search.size()) {
                    sort.add(arrayList_property_search.get(i));
                } else {
                    break;
                }
            }
            propertySearchAdapter = new PropertySearchAdapter(getApplicationContext(), sort);
            listview_property_search.setAdapter(propertySearchAdapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void check(int j) {
        try {
            if ((j != 0) && ((j + 1) % 5 == 0)) {

                View b = findViewById(R.id.btn_Next);
                b.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void CheckBtnBackGroud(int index,int cpi) {
        try {
            int temp = (cpi * 5) - 5;
            temp = temp + index + 1;
            title.setText("Page " + temp);
            for (int i = 0; i < noOfBtns; i++) {
                if (i == index) {

                    btns[i].setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btns[i].setTextColor(getResources().getColor(android.R.color.black));
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}