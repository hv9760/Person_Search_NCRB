package com.ncrb.samapre.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;

import retrofit.mime.TypedInput;
import android.app.ProgressDialog;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;


public class PropertySearch extends AppCompatActivity {


    Button btn_Prop_Auto_Search;
    ImageButton btn_Back;



    EditText edt_Reg_Num;
    EditText edt_Chasis_Num;
    EditText edt_Engine_Num;
    Spinner sp1;
    Spinner sp2,sp3,sp4,Sp_PS,Sp_Dist;
    public ProgressDialog mProgressDialog;

    CheckBox chkFIR;
    CheckBox chkUAP;
    CheckBox chkPS;
    Object VCode,MCode,CCode,MoCode,Dist_Code,PS_Code;
    Integer Vehicle_Code=0;

    Integer C_Code,Dis_Code,PSIn_Code=0;
    MCoCoRy mCoCoRy = new  MCoCoRy();
    Integer manu=0;
    Integer MV_Model=0;


    String search = "vehicle";
    String Color="Default";
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> vehicleCode = new ArrayList<>();
    ArrayList<String> ManuData = new ArrayList<>();
    ArrayList<String> ManuCode = new ArrayList<>();
    ArrayList<String> ModelData = new ArrayList<>();
    ArrayList<String> ModelCode = new ArrayList<>();
    ArrayList<String> ColorData = new ArrayList<>();
    ArrayList<String> ColorCode = new ArrayList<>();
    ArrayList<String> DistrictData = new ArrayList<>();
    ArrayList<String> DistrictCode = new ArrayList<>();
    ArrayList<String> PSData = new ArrayList<>();
    ArrayList<String> PSCode = new ArrayList<>();

    StringBuilder out,out1,out2,out3,outDist,outPS;
String State=Constants.STATE_CD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_search);

        btn_Prop_Auto_Search = (Button) findViewById(R.id.btn_Prop_Auto_Search);

        edt_Reg_Num = (EditText) findViewById(R.id.edt_Reg_Num);
        edt_Chasis_Num = (EditText) findViewById(R.id.edt_Chasis_Num);
        edt_Engine_Num = (EditText) findViewById(R.id.edt_Engine_Num);
        chkUAP=(CheckBox) findViewById(R.id.chkUAP);
        chkPS=(CheckBox) findViewById(R.id.chkPS);
        chkFIR=(CheckBox) findViewById(R.id.chkFIR);
        btn_Back = (ImageButton) findViewById(R.id.btn_Back);
        sp1 = (Spinner) findViewById(R.id.sp_Type_Vehicle);
        sp2 = (Spinner) findViewById(R.id.sp_Manufacturer);
        sp3 = (Spinner) findViewById(R.id.sp_color);
        sp4 = (Spinner) findViewById(R.id.sp_model);
        Sp_PS = (Spinner) findViewById(R.id.PS);
        Sp_Dist = (Spinner) findViewById(R.id.District);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");

        try {
            GetAuthDetailWebService(search);
        } catch (Exception e) {
            e.printStackTrace();
        }

            try {
                GetColorDetailWebService(Color);
            } catch (Exception e) {
                e.printStackTrace();
            }

        try {
            GetDistrictWebService(State);
        } catch (Exception e) {
            e.printStackTrace();
        }
      btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }});
       sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {


                VCode=vehicleCode.get(position);

Integer VCODE1;
VCODE1=Integer.parseInt(VCode.toString());

                if (VCODE1!=0) {
                    try {
                        ManuData.clear();
                        ManuCode.clear();
                        sp2.setAdapter(null);
                        ModelData.clear();
                        ModelCode.clear();
                        sp4.setAdapter(null);
                        GetManuDetailWebService(VCODE1);
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
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                MCode=ManuCode.get(position);

Integer Mcode1;
                Mcode1=Integer.parseInt(MCode.toString());
                if (Mcode1!=0) {
                    try {
                        ModelData.clear();
                        ModelCode.clear();
                        sp4.setAdapter(null);

                        GetModelDetailWebService(Mcode1);
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
        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                MoCode=ModelCode.get(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);

                CCode=ColorCode.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
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
        btn_Prop_Auto_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkFIR.isChecked() || chkPS.isChecked() || chkUAP.isChecked()) {

                    Intent intent = new Intent(getApplicationContext(), PropertyDisplay.class);
                    Bundle bundle = new Bundle();

                        if (MCode!=null)
                        manu = Integer.parseInt(MCode.toString());
                        else
                        manu = 0;


                      if(VCode!=null)
                        Vehicle_Code = Integer.parseInt(VCode.toString());

                        else
                        Vehicle_Code = 0;

                    if(MoCode!=null)
                        MV_Model  = Integer.parseInt(MoCode.toString());

                    else
                        MV_Model = 0;

                    if (CCode!=null)
                        C_Code = Integer.parseInt(CCode.toString());
                    else
                        C_Code = 0;


                    if (Dist_Code!=null)
                        Dis_Code = Integer.parseInt(Dist_Code.toString());
                    else
                        Dis_Code = 0;
                    if (PS_Code!=null)
                        PSIn_Code = Integer.parseInt(PS_Code.toString());
                    else
                        PSIn_Code = 0;


                    bundle.putInt("VTCODE", Vehicle_Code);
                    bundle.putInt("C_Code", C_Code);
                    bundle.putInt("Di_Code", Dis_Code);

                    bundle.putInt("P_Code", PSIn_Code);
                    bundle.putInt("Manufacturer", manu);
                    bundle.putInt("Model", MV_Model);
                    bundle.putString("RegistrationNumber", edt_Reg_Num.getText().toString());
                    bundle.putString("ChasisNo", edt_Chasis_Num.getText().toString());
                    bundle.putString("EngineNo", edt_Engine_Num.getText().toString());
                    bundle.putBoolean("UAP", chkUAP.isChecked());
                    bundle.putBoolean("PS", chkPS.isChecked());
                    bundle.putBoolean("REGISTRATION", chkFIR.isChecked());
                    intent.putExtras(bundle);
                    startActivity(intent);


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please select any of the search criteria", Toast.LENGTH_LONG).show();
                }

            }

        }
        );

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


    public void GetAuthDetailWebService(String username1) throws Exception {
        this.mProgressDialog.show();
        String coco_seed = ""; String coco_seed_encd = "";


        try {
        Map postParams = new HashMap();
        postParams.put("hello", username1.toString());
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

        JSONPostParams jsonPostParams = new JSONPostParams("mVehicleTypeConnect", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);

        apiCaller.mVehicleTypeConnect(jsonPostParams, new Callback<WSPLoginConnect>() {

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
                    JSONArray states = reader.getJSONArray("vehicleTypeCheck");

                    for (int i = 0; i < states.length(); i++) {
                        JSONObject jsonObj2 = states.getJSONObject(i);
                        data.add(new String(jsonObj2.getString("VehicleType")));
                        vehicleCode.add(new String(jsonObj2.getString("VehicleTypecd")));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,
                        data); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);

                sp1.setAdapter(spinnerArrayAdapter);


                } else {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

                }
            }// end success
        });

    }
    public void GetManuDetailWebService(Integer VCode1) throws Exception {
        this.mProgressDialog.show();

        String coco_seed = ""; String coco_seed_encd = "";


        try {

        Map postParams = new HashMap();
        postParams.put("VType_Cd", VCode1);

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

        JSONPostParams jsonPostParams1 = new JSONPostParams("mManufacturerConnect", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);


        apiCaller.mManufacturerConnect(jsonPostParams1, new Callback<WSPLoginConnect>() {

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
                    JSONArray states = reader.getJSONArray("ManufacturerList");

                    for (int i = 0; i < states.length(); i++) {
                        JSONObject jsonObj2 = states.getJSONObject(i);
                        ManuData.add(new String(jsonObj2.getString("ManuName")));
                        ManuCode.add(new String(jsonObj2.getString("ManuCd")));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }




                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,
                        ManuData); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                sp2.setAdapter(spinnerArrayAdapter);

                } else {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

                }
            }// end success
        });

    }
    public void GetModelDetailWebService(Integer MCode1) throws Exception {
        this.mProgressDialog.show();
        String coco_seed = ""; String coco_seed_encd = "";

        try {

        Map postParams = new HashMap();
        postParams.put("MVMake", MCode1);
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

        JSONPostParams jsonPostParams1 = new JSONPostParams("mMVModelConnect", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);


        apiCaller.mMVModelConnect(jsonPostParams1, new Callback<WSPLoginConnect>() {

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
                    JSONArray states = reader.getJSONArray("MVModelList");

                    for (int i = 0; i < states.length(); i++) {
                        JSONObject jsonObj2 = states.getJSONObject(i);
                        ModelData.add(new String(jsonObj2.getString("MVModelName")));
                        ModelCode.add(new String(jsonObj2.getString("MVModelCd")));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,
                        ModelData); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                sp4.setAdapter(spinnerArrayAdapter);

                } else {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

                }
            }// end success
        });

    }
    public void GetColorDetailWebService(String Color) throws Exception {

        this.mProgressDialog.show();
        String coco_seed = ""; String coco_seed_encd = "";


        try {
        Map postParams = new HashMap();
        postParams.put("MVColor", Color);
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

        JSONPostParams jsonPostParams1 = new JSONPostParams("mMVColorConnect", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);


        apiCaller.mMVColorConnect(jsonPostParams1, new Callback<WSPLoginConnect>() {

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
                    JSONArray states = reader.getJSONArray("MVColorList");

                    for (int i = 0; i < states.length(); i++) {
                        JSONObject jsonObj2 = states.getJSONObject(i);
                        ColorData.add(new String(jsonObj2.getString("MVColorName")));
                        ColorCode.add(new String(jsonObj2.getString("MVColorCd")));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,
                        ColorData); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                sp3.setAdapter(spinnerArrayAdapter);

                } else {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();


                }
            }// end success
        });

    }
    public void GetDistrictWebService(String State) throws Exception {

        this.mProgressDialog.show();
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

        // -----------------------------------------------------------------

        // create a new hash which you want to send on server
        Map postParams = new HashMap();

        postParams.put("seed", coco_seed_encd);

        JSONPostParams jsonPostParams1 = new JSONPostParams("mDistrictConnect", postParams);

        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();
        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);

        apiCaller.mDistrictConnect(jsonPostParams1, new Callback<WSPLoginConnect>() {

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
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_spinner_dropdown_item);
                    Sp_Dist.setAdapter(spinnerArrayAdapter);

                } else {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

                }
            }// end success
        });

    }
    public void GetPSWebService(Integer DCode) throws Exception {
        this.mProgressDialog.show();
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
                            PSData); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_spinner_dropdown_item);
                    Sp_PS.setAdapter(spinnerArrayAdapter);

                } else {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

                }
            }// end success
        });

    }
}


