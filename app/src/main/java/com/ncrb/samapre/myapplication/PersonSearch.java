package com.ncrb.samapre.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class PersonSearch extends AppCompatActivity {
    private Spinner sp_Gender, sp_Relation_Type,sp_Religion,Sp_PS,Sp_Dist, Sp_St;
    private CheckBox chkArr;
    private CheckBox chkCon;
    private CheckBox chkPO;
    private CheckBox chkCS;
    private CheckBox chkHO;
    private CheckBox chkNA;
    private TextInputLayout edt_person_first_name,edt_person_middle_name,edt_person_last_name, edt_person_alias, edt_Relative_Name;

    private TextInputLayout edt_person_age_From, edt_person_age_To;
    private TextInputLayout edt_person_height_From_Ft, edt_person_height_From_Inch,edt_person_height_To_Ft,edt_person_height_To_Inch;
    private String relation="relationtype";
    private String religion="religiontype";
    private MCoCoRy mCoCoRy = new  MCoCoRy();
    private ArrayList<String> RelTypeData = new ArrayList<>();
    private ArrayList<String> RelTypeCode = new ArrayList<>();
    private ArrayList<String> ReligionData = new ArrayList<>();
    private ArrayList<String> ReligionCode = new ArrayList<>();
    private ArrayList<String> DistrictData = new ArrayList<>();
    private ArrayList<String> DistrictCode = new ArrayList<>();
    private ArrayList<String> PSData = new ArrayList<>();
    private ArrayList<String> PSCode = new ArrayList<>();
    private ArrayList<String> StateData = new ArrayList<>();
    private ArrayList<String> StateCode = new ArrayList<>();
    int GCode, GenCode, RelgnCode,RelTypCode;
    String GenderSelected;
    String State=Constants.STATE_CD;
    Object RCode,RTypeCode,Dist_Code,PS_Code,AllSt_Code;
    Button button_person_search;
    // ImageButton btn_Back;
    private    String St_Code="";
    private Integer Dis_Code,PSIn_Code=0;
    private ScrollView scroll;

    public ProgressDialog mProgressDialog;
    private TextView loader;
    private DotProgressBar dot_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");

        setContentView(R.layout.person_search_activity);
        setToolbar();

        chkArr=(CheckBox) findViewById(R.id.chkArrested);
        chkCon=(CheckBox) findViewById(R.id.chkConvicted);
        chkPO=(CheckBox) findViewById(R.id.chkPO);
        chkCS=(CheckBox) findViewById(R.id.chkCS);
        chkHO=(CheckBox) findViewById(R.id.chkHO);
        chkNA=(CheckBox) findViewById(R.id.chkNA);
        loader=findViewById(R.id.loading);
        Sp_St = (Spinner) findViewById(R.id.AllState);
        scroll=findViewById(R.id.scroll);
        // btn_Back = (ImageButton) findViewById(R.id.btn_Back);
        edt_person_first_name=(TextInputLayout)findViewById(R.id.edt_FName);
        edt_person_middle_name=(TextInputLayout)findViewById(R.id.edt_MName);
        edt_person_last_name=(TextInputLayout)findViewById(R.id.edt_LName);
        edt_person_alias=(TextInputLayout)findViewById(R.id.edt_Alias);

        edt_person_age_From=(TextInputLayout)findViewById(R.id.edt_age_From);
        edt_person_age_To=(TextInputLayout)findViewById(R.id.edt_age_To);
        edt_person_height_From_Ft=(TextInputLayout)findViewById(R.id.edt_height_From_Ft);
        edt_person_height_From_Inch=(TextInputLayout)findViewById(R.id.edt_height_From_Inch);
        edt_person_height_To_Ft=(TextInputLayout)findViewById(R.id.edt_height_To_Ft);
        edt_person_height_To_Inch=(TextInputLayout)findViewById(R.id.edt_height_To_Inch);

        button_person_search=(Button)findViewById(R.id.btn_Person_Search);

        sp_Relation_Type=(Spinner) findViewById(R.id.sp_Relation_Type);
        sp_Religion=(Spinner) findViewById(R.id.sp_Religion);
        edt_Relative_Name=(TextInputLayout)findViewById(R.id.edt_Rel_Name);

        dot_progress_bar=findViewById(R.id.dot_progress_bar);
        new DotProgressBarBuilder(getApplicationContext())
                .setStartColor(Color.BLACK)
                .setDotAmount(3)
                .setAnimationDirection(DotProgressBar.LEFT_DIRECTION)
                .build();
        Sp_PS = (Spinner) findViewById(R.id.PS);
        Sp_Dist = (Spinner) findViewById(R.id.District);

        sp_Gender=(Spinner) findViewById(R.id.sp_Gender);
        ArrayList<String> genderList = new ArrayList<String>();

        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Transgender");
        genderList.add("Unknown");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genderList);
        //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.insert("Select Gender", 0);
        adapter.notifyDataSetChanged();
        sp_Gender.setAdapter(adapter);

        try {
            GetRelationTypeWebService(relation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //GetDistrictWebService(State);ionono
        } catch (Exception e) {
            e.printStackTrace();
        }




        try {
            GetAllStateWebService(St_Code);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Sp_Dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                Dist_Code=DistrictCode.get(position);

                Integer DCode;
                DCode=Integer.parseInt(Dist_Code.toString());

                if (DCode!=0) {
                    try {
                        PSCode.clear();
                        PSData.clear();
                        Sp_PS.setAdapter(null);

                        GetPSWebService(DCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });


        Sp_PS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                PS_Code=PSCode.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        sp_Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                GenderSelected=((Spinner)findViewById(R.id.sp_Gender)).getSelectedItem().toString();
                if(GenderSelected=="Male")
                {
                    GCode=3;
                }
                else if(GenderSelected=="Female")
                {
                    GCode=2;
                }
                else if(GenderSelected=="Transgender")
                {
                    GCode=1;
                }
                else if(GenderSelected=="Unknown")
                {
                    GCode=4;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        sp_Religion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                RCode=ReligionCode.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        sp_Relation_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                RTypeCode=RelTypeCode.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });


        Sp_St.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                AllSt_Code=StateCode.get(position);

                Integer SCode;
                SCode=Integer.parseInt(AllSt_Code.toString());

                if (SCode!=0) {
                    try {
                        DistrictCode.clear();
                        DistrictData.clear();
                        Sp_Dist.setAdapter(null);

                        GetDistrictWebService(SCode.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });


        button_person_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (chkArr.isChecked() || chkCon.isChecked() || chkPO.isChecked() || chkCS.isChecked() || chkHO.isChecked() || chkNA.isChecked()) {
                        Intent intent = new Intent(getApplicationContext(), PersonDisplay.class);
                        Bundle bundle = new Bundle();

                        bundle.putString("P_FName", edt_person_first_name.getEditText().getText().toString());
                        bundle.putString("P_MName", edt_person_middle_name.getEditText().getText().toString());
                        bundle.putString("P_LName", edt_person_last_name.getEditText().getText().toString());
                        bundle.putString("P_Alias", edt_person_alias.getEditText().getText().toString());
                        if (GenderSelected != null)
                            GenCode = GCode;
                        else
                            GenCode = 0;
                        bundle.putInt("P_GCODE", (int) GenCode);
                        if (RCode != null)
                            RelgnCode = Integer.parseInt(RCode.toString());
                        else
                            RelgnCode = 0;
                        bundle.putInt("P_RELGNCODE", RelgnCode);
                        if (RTypeCode != null)
                            RelTypCode = Integer.parseInt(RTypeCode.toString());
                        else
                            RelTypCode = 0;
                        bundle.putInt("P_RELTYPECODE", RelTypCode);
                        bundle.putString("P_RelativeName", edt_Relative_Name.getEditText().getText().toString());
                        if (edt_person_age_From.getEditText().getText().length() == 0)

                            edt_person_age_From.getEditText().setText("0");
                        bundle.putInt("P_AgeFrom", Integer.parseInt(edt_person_age_From.getEditText().getText().toString()));
                        if (edt_person_age_To.getEditText().getText().length() == 0)
                            edt_person_age_To.getEditText().setText("0");
                        bundle.putInt("P_AgeTo", Integer.parseInt(edt_person_age_To.getEditText().getText().toString()));

                        if (edt_person_height_From_Ft.getEditText().getText().length() == 0)
                            edt_person_height_From_Ft.getEditText().setText("0");
                        if (edt_person_height_From_Inch.getEditText().getText().length() == 0)
                            edt_person_height_From_Inch.getEditText().setText("0");
                        int Height_From_Ft = Integer.parseInt(edt_person_height_From_Ft.getEditText().getText().toString());
                        int Height_From_Inch = Integer.parseInt(edt_person_height_From_Inch.getEditText().getText().toString());
                        double Height_From_cm = 2.54 * ((Height_From_Ft * 12) + Height_From_Inch);
                        bundle.putDouble("P_HEIGHTFROM", Height_From_cm);
                        if (edt_person_height_To_Ft.getEditText().getText().length() == 0)
                            edt_person_height_To_Ft.getEditText().setText("0");
                        if (edt_person_height_To_Inch.getEditText().getText().length() == 0)
                            edt_person_height_To_Inch.getEditText().setText("0");
                        int Height_To_Ft = Integer.parseInt(edt_person_height_To_Ft.getEditText().getText().toString());
                        int Height_To_Inch = Integer.parseInt(edt_person_height_To_Inch.getEditText().getText().toString());
                        double Height_To_cm = 2.54 * ((Height_To_Ft * 12) + Height_To_Inch);
                        bundle.putDouble("P_HEIGHTTO", Height_To_cm);


                        if (AllSt_Code != null)
                            St_Code = AllSt_Code.toString();
                        else
                            St_Code = "0";


                        if (Dist_Code != null)
                            Dis_Code = Integer.parseInt(Dist_Code.toString());
                        else
                            Dis_Code = 0;
                        if (PS_Code != null)
                            PSIn_Code = Integer.parseInt(PS_Code.toString());
                        else
                            PSIn_Code = 0;

                        bundle.putString("St_Code", St_Code);
                        bundle.putInt("Dist_Code", Dis_Code);
                        bundle.putInt("PS_Code", PSIn_Code);
                        bundle.putBoolean("Arr", chkArr.isChecked());
                        bundle.putBoolean("Con", chkCon.isChecked());
                        bundle.putBoolean("PO", chkPO.isChecked());
                        bundle.putBoolean("CS", chkCS.isChecked());
                        bundle.putBoolean("HO", chkHO.isChecked());
                        bundle.putBoolean("NA", chkNA.isChecked());

                        intent.putExtras(bundle);



                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select any of the search criteria", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
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
    public void GetRelationTypeWebService(String relation) throws Exception {

        //  this.mProgressDialog.show();

        scroll.setVisibility(View.INVISIBLE);
        loader.setVisibility(View.VISIBLE);
        dot_progress_bar.setVisibility(View.VISIBLE);
        button_person_search.setVisibility(View.INVISIBLE);

        String coco_seed = ""; String coco_seed_encd = "";
        try {

            Map postParams = new HashMap();
            postParams.put("relation", relation);
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

        JSONPostParams jsonPostParams1 = new JSONPostParams("mRelationTypeConnect", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);


        apiCaller.mRelationTypeConnect(jsonPostParams1, new Callback<WSPLoginConnect>() {

            @Override
            public void failure(RetrofitError arg0) {

                scroll.setVisibility(View.INVISIBLE);
                button_person_search.setVisibility(View.INVISIBLE);
                loader.setVisibility(View.GONE);
                dot_progress_bar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Can not connect to server for relation type.", Toast.LENGTH_LONG).show();


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

                    scroll.setVisibility(View.VISIBLE);
                    button_person_search.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    dot_progress_bar.setVisibility(View.GONE);
                    try {
                        JSONObject reader = new JSONObject(jsonString.toString());
                        JSONArray states = reader.getJSONArray("RelationTypeList");

                        for (int i = 0; i < states.length(); i++) {
                            JSONObject jsonObj2 = states.getJSONObject(i);
                            RelTypeData.add(new String(jsonObj2.getString("RelationType")));
                            RelTypeCode.add(new String(jsonObj2.getString("RelationTypeCd")));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,
                            RelTypeData); //selected item will look like a spinner set from XML
                   /* spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_spinner_dropdown_item);*/
                    sp_Relation_Type.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.insert("Select Relation Type", 0);
                    spinnerArrayAdapter.notifyDataSetChanged();
                    try {
                        PersonSearch.this.GetReligionTypeWebService(religion);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {

                    scroll.setVisibility(View.VISIBLE);
                    button_person_search.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    dot_progress_bar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

                }
            }// end success
        });

    }

    private void setToolbar(){


        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.person_search));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    public void GetReligionTypeWebService(String religion) throws Exception {
        //  this.mProgressDialog.show();
        String coco_seed = ""; String coco_seed_encd = "";
        try {

            Map postParams = new HashMap();
            postParams.put("religion", religion);
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

        JSONPostParams jsonPostParams1 = new JSONPostParams("mReligionConnect", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);

        apiCaller.mReligionConnect(jsonPostParams1, new Callback<WSPReligionConnect>() {

            @Override
            public void failure(RetrofitError arg0) {
                // if (mProgressDialog != null && mProgressDialog.isShowing())
                //   mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Can not connect to server.", Toast.LENGTH_LONG).show();


            }// end failure

            @Override
            public void success(WSPReligionConnect result2, Response response) {

                String jsonString = "";

                if(!result2.seed.equals("")) {

                    jsonString = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), result2.seed, "DECODE");

                    if(jsonString.equals("")) {
                        Toast.makeText(getApplicationContext(), "System error, please contact administrator.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                Gson gson = new Gson();

                WSPReligionConnect result = gson.fromJson(jsonString, WSPReligionConnect.class);

                if (result.STATUS_CODE.toString().equals("200")) {

                    //  if (mProgressDialog != null && mProgressDialog.isShowing())
                    //    mProgressDialog.dismiss();

                    try {

                        JSONObject reader = new JSONObject(jsonString.toString());
                        JSONArray states = reader.getJSONArray("ReligionList");

                        for (int i = 0; i < states.length(); i++) {
                            JSONObject jsonObj2 = states.getJSONObject(i);
                            ReligionData.add(new String(jsonObj2.getString("ReligionName")));
                            ReligionCode.add(new String(jsonObj2.getString("ReligionCd")));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,
                            ReligionData); //selected item will look like a spinner set from XML
                   /* spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_dropdown_item_1line);*/
                    spinnerArrayAdapter.insert("Select Religion",0);
                    spinnerArrayAdapter.notifyDataSetChanged();
                    sp_Religion.setAdapter(spinnerArrayAdapter);


                } else {

                    //  if (mProgressDialog != null && mProgressDialog.isShowing())
                    //    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

                }
            }// end success
        });



    }

    public void GetAllStateWebService(String allstate) throws Exception {

        //    this.mProgressDialog.show();
        String coco_seed = ""; String coco_seed_encd = "";
        try {

            Map postParams = new HashMap();
            postParams.put("allstate", allstate);
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

        JSONPostParams jsonPostParams1 = new JSONPostParams("mAllStateConnect", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);


        apiCaller.mAllStateConnect(jsonPostParams1, new Callback<WSPLoginConnect>() {

            @Override
            public void failure(RetrofitError arg0) {

               /* if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
*/
                Toast.makeText(getApplicationContext(), "Can not connect to server for relation type.", Toast.LENGTH_LONG).show();


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
                        JSONObject reader = new JSONObject(jsonString.toString());
                        JSONArray states = reader.getJSONArray("AllStateList");

                        for (int i = 0; i < states.length(); i++) {
                            JSONObject jsonObj2 = states.getJSONObject(i);
                            StateData.add(new String(jsonObj2.getString("StateName")));
                            StateCode.add(new String(jsonObj2.getString("StateCd")));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,
                            StateData); //selected item will look like a spinner set from XML

                    spinnerArrayAdapter.insert("Select State", 0);
                    spinnerArrayAdapter.notifyDataSetChanged();
                    Sp_St.setAdapter(spinnerArrayAdapter);


                }
                else {
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                }
            }// end success
        });

    }

    public void GetDistrictWebService(String State) throws Exception {
        //  this.mProgressDialog.show();
        String coco_seed = ""; String coco_seed_encd = "";

        try {

            Map postParams = new HashMap();
            postParams.put("FxdState_Cd", State);
            Gson gsonObj = new Gson();
            coco_seed = gsonObj.toJson(postParams);

            coco_seed_encd  = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), coco_seed, "ENCODE");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map postParams = new HashMap();

        postParams.put("seed", coco_seed_encd);

        JSONPostParams jsonPostParams1 = new JSONPostParams("mDistrictConnect", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);


        apiCaller.mDistrictConnect(jsonPostParams1, new Callback<WSPLoginConnect>() {

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
                        JSONArray states = reader.getJSONArray("DistrictList");

                        for (int i = 0; i < states.length(); i++) {
                            JSONObject jsonObj2 = states.getJSONObject(i);
                            DistrictData.add(new String(jsonObj2.getString("DistrictName")));
                            DistrictCode.add(new String(jsonObj2.getString("DistrictCd")));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,
                            DistrictData); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.insert("Select District", 0);
                    spinnerArrayAdapter.notifyDataSetChanged();

                    Sp_Dist.setAdapter(spinnerArrayAdapter);


                } else {

                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();


                }
            }// end success
        });

    }

    public void GetPSWebService(Integer DCode) throws Exception {

        String coco_seed = ""; String coco_seed_encd = "";


        try {

            Map postParams = new HashMap();
            postParams.put("District_Cd", DCode);
            Gson gsonObj = new Gson();
            coco_seed = gsonObj.toJson(postParams);




            coco_seed_encd  = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), coco_seed, "ENCODE");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map postParams = new HashMap();

        postParams.put("seed", coco_seed_encd);

        JSONPostParams jsonPostParams1 = new JSONPostParams("mPoliceStationConnect", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);


        apiCaller.mPoliceStationConnect(jsonPostParams1, new Callback<WSPLoginConnect>() {

            @Override
            public void failure(RetrofitError arg0) {

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
                    // if (mProgressDialog != null && mProgressDialog.isShowing())
                    //   mProgressDialog.dismiss();
                    try {
                        JSONObject reader = new JSONObject(jsonString.toString());
                        JSONArray states = reader.getJSONArray("PoliceStationList");
                        for (int i = 0; i < states.length(); i++) {
                            JSONObject jsonObj2 = states.getJSONObject(i);
                            PSData.add(new String(jsonObj2.getString("PoliceStationName")));
                            PSCode.add(new String(jsonObj2.getString("PoliceStationCd")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,
                            PSData);
                    spinnerArrayAdapter.insert("Select Police Station", 0);
                    spinnerArrayAdapter.notifyDataSetChanged();
                   /* spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_spinner_dropdown_item);*/
                    Sp_PS.setAdapter(spinnerArrayAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();


                }
            }// end success
        });

    }

}
