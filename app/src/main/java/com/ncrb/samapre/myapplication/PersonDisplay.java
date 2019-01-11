package com.ncrb.samapre.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.github.silvestrpredko.dotprogressbar.DotProgressBarBuilder;
import com.google.gson.Gson;
import com.ncrb.samapre.myapplication.ApiCaller;
import com.ncrb.samapre.myapplication.Constants;
import com.ncrb.samapre.myapplication.JSONPostParams;
import com.ncrb.samapre.myapplication.MCoCoRy;
import com.ncrb.samapre.myapplication.PersonSearchAdapter;
import com.ncrb.samapre.myapplication.PersonSearchInfo;
import com.ncrb.samapre.myapplication.WSPLoginConnect;

import com.ncrb.samapre.myapplication.R;

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


public class PersonDisplay extends AppCompatActivity {
    String P_FName,P_MName,P_LName,P_Alias,P_RelativeName;
    Integer P_GCODE,P_RELGNCODE,P_RELTYPECODE,P_AgeFrom,P_AgeTo,DistCode,PSCode;
    Double P_HEIGHTFROM,P_HEIGHTTO;
    String ChkArr,ChkCon,ChkPO,ChkCS,ChkHO,ChkNA;

    String STATE_CD=Constants.STATE_CD;
    private int CURRENT_PAGE_INDEX=1;

    MCoCoRy mCoCoRy = new  MCoCoRy();

    private ListView listview_person_search;
    private TextView title;
    private ImageButton btn_Back;
    private TextView btn_Next;


    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 10;
    private int noOfBtns;
    private Button[] btns;
    private ArrayList<PersonSearchInfo> arrayList_person_search=new ArrayList<PersonSearchInfo>();
    private ArrayList<PersonSearchInfo> sort=new ArrayList<PersonSearchInfo>();
    private PersonSearchAdapter personSearchAdapter;

    private DotProgressBar dot_progress_bar;
    private TextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_person_display);
        setContentView(R.layout.person_search_results);
        setToolbar();
        listview_person_search=(ListView)findViewById(R.id.list_person_search_result) ;
        btn_Back = (ImageButton) findViewById(R.id.btn_Back);

        btn_Next = (TextView) findViewById(R.id.btn_Next);
        title = (TextView)findViewById(R.id.title);

        loading = (TextView)findViewById(R.id.loading);

        dot_progress_bar=findViewById(R.id.dot_progress_bar);
        new DotProgressBarBuilder(getApplicationContext())


                .setDotAmount(3)
                .setAnimationDirection(DotProgressBar.RIGHT_DIRECTION)
                .build();

        Bundle bundle = getIntent().getExtras();

        P_FName=bundle.getString("P_FName");
        if (P_FName.isEmpty())
        {
            P_FName="0";
        }
        P_MName=bundle.getString("P_MName");
        if (P_MName.isEmpty())
        {
            P_MName="0";
        }
        P_LName=bundle.getString("P_LName");
        if (P_LName.isEmpty())
        {
            P_LName="0";
        }
        P_Alias=bundle.getString("P_Alias");
        if (P_Alias.isEmpty())
        {
            P_Alias="0";
        }
        P_GCODE=bundle.getInt("P_GCODE");
        P_RELGNCODE=bundle.getInt("P_RELGNCODE");
        P_RELTYPECODE=bundle.getInt("P_RELTYPECODE");
        P_RelativeName=bundle.getString("P_RelativeName");
        if (P_RelativeName.isEmpty())
        {
            P_RelativeName="0";
        }
        P_AgeFrom=bundle.getInt("P_AgeFrom");
        P_AgeTo=bundle.getInt("P_AgeTo");
        P_HEIGHTFROM=bundle.getDouble("P_HEIGHTFROM");
        P_HEIGHTTO=bundle.getDouble("P_HEIGHTTO");
        STATE_CD=bundle.getString("St_Code");
        PSCode=bundle.getInt("PS_Code");
        DistCode=bundle.getInt("Dist_Code");
        Boolean Arr=bundle.getBoolean("Arr");
        if (Arr) {
            ChkArr="true";
        }
        else
        {
            ChkArr="false";
        }

        Boolean Con=bundle.getBoolean("Con");
        if (Con) {
            ChkCon="true";
        }
        else
        {
            ChkCon="false";
        }

        Boolean PO=bundle.getBoolean("PO");
        if (PO) {
            ChkPO="true";
        }
        else
        {
            ChkPO="false";
        }

        Boolean CS=bundle.getBoolean("CS");
        if (CS) {
            ChkCS="true";
        }
        else
        {
            ChkCS="false";
        }
        Boolean HO=bundle.getBoolean("HO");
        if (HO) {
            ChkHO="true";
        }
        else
        {
            ChkHO="false";
        }
        Boolean NA=bundle.getBoolean("NA");
        if (NA) {
            ChkNA="true";
        }
        else
        {
            ChkNA="false";
        }
        new AsyncLoading().execute();
        //  paging();





        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                CURRENT_PAGE_INDEX=CURRENT_PAGE_INDEX+1;
                new AsyncLoading().execute();

                //View b=findViewById(R.id.btn_Next);
                btn_Next.setVisibility(View.GONE);
            }
        });


        listview_person_search.setOnItemClickListener(onItemClickListener);
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
            CURRENT_PAGE_INDEX=1;
            super.onBackPressed();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private AbsListView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent=new Intent(PersonDisplay.this,PersonDetails.class);


            intent.putExtra("FULL_NAME",arrayList_person_search.get(position).getFULL_NAME());

            intent.putExtra("ALIAS",arrayList_person_search.get(position).getALIAS1());

            intent.putExtra("RELATION_TYPE",arrayList_person_search.get(position).getRELATION_TYPE());
            intent.putExtra("RELATIVE_NAME",arrayList_person_search.get(position).getRELATIVE_NAME());

            intent.putExtra("REGISTRATION_NUM",arrayList_person_search.get(position).getREGISTRATION_NUM());

            intent.putExtra("AccusedSrno",arrayList_person_search.get(position).getAccused_srno());



            startActivity(intent);



        }
    };


    private void setToolbar(){


        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        android.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.person_search_result));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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








    public void  GetAuthDetailWebService() throws Exception {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.VISIBLE);
                dot_progress_bar.setVisibility(View.VISIBLE);
                listview_person_search.setVisibility(View.INVISIBLE);
            }
        });

        String coco_seed = ""; String coco_seed_encd = "";


        try {
            Map postParams = new HashMap();
            postParams.put("MATCH_CRITERIA_ARR",ChkArr);
            postParams.put("MATCH_CRITERIA_CON",ChkCon);
            postParams.put("MATCH_CRITERIA_PO",ChkPO);
            postParams.put("MATCH_CRITERIA_CS",ChkCS);
            postParams.put("MATCH_CRITERIA_HO",ChkHO);
            postParams.put("MATCH_CRITERIA_NOTARR",ChkNA);
            postParams.put("P_FName",P_FName);
            postParams.put("P_MName",P_MName);
            postParams.put("P_LName",P_LName);
            postParams.put("P_Alias",P_Alias);
            postParams.put("P_GCODE",P_GCODE);
            postParams.put("P_RELGNCODE",P_RELGNCODE);
            postParams.put("P_RELTYPECODE",P_RELTYPECODE);
            postParams.put("P_RelativeName",P_RelativeName);
            postParams.put("P_AgeFrom",P_AgeFrom);
            postParams.put("P_AgeTo",P_AgeTo);
            postParams.put("P_HEIGHTFROM",P_HEIGHTFROM);
            postParams.put("P_HEIGHTTO",P_HEIGHTTO);
            postParams.put("STATE_CD",STATE_CD);
            postParams.put("DISTRICT_CD",DistCode);
            postParams.put("PS_CD",PSCode);
            postParams.put("CURRENT_PAGE_INDEX",CURRENT_PAGE_INDEX);

            Gson gsonObj = new Gson();
            coco_seed = gsonObj.toJson(postParams);
            coco_seed_encd  = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), coco_seed, "ENCODE");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map postParams = new HashMap();

        postParams.put("seed", coco_seed_encd);

        JSONPostParams jsonPostParams = new JSONPostParams("mPersonSearch", postParams);


        // -----------------------------------------------------------------

        RestAdapter restAdapter =providesRestAdapter();


        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);

        int cnt;
        apiCaller.mPersonSearch(jsonPostParams, new Callback<WSPLoginConnect>() {

                    @Override
                    public void failure(RetrofitError arg0) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.GONE);
                                dot_progress_bar.setVisibility(View.GONE);
                                listview_person_search.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "Can not connect to server.", Toast.LENGTH_LONG).show();
                            }
                        });




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

                            loading.setVisibility(View.GONE);
                            dot_progress_bar.setVisibility(View.GONE);
                            listview_person_search.setVisibility(View.VISIBLE);
                            try {

                                JSONObject reader = new JSONObject(jsonString);
                                JSONArray states = reader.getJSONArray("personSearchCheck");

                                // arrayList_person_search.clear();
                                for (int i = 0; i < states.length(); i++) {

                                    JSONObject jsonObj2 = states.getJSONObject(i);

                                    Gson gson1 = new Gson();
                                    PersonSearchInfo objPersonSearchInfo = gson1.fromJson(jsonObj2.toString(), PersonSearchInfo.class);

                                    arrayList_person_search.add(objPersonSearchInfo);

                                } //end forloop



                                Btnfooter((int)states.length(),CURRENT_PAGE_INDEX);

                                loadList(CURRENT_PAGE_INDEX-1);

                                CheckBtnBackGroud(0,CURRENT_PAGE_INDEX);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loading.setVisibility(View.GONE);
                                    dot_progress_bar.setVisibility(View.GONE);
                                    listview_person_search.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                                }
                            });




                        }
                    }// end success

                }

        );

    }

    private void Btnfooter(int count,int CURRENT_PAGE_INDEX) {
        try {
            TOTAL_LIST_ITEMS = count;
            int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
            val = val == 0 ? 0 : 1;
            noOfBtns = TOTAL_LIST_ITEMS / NUM_ITEMS_PAGE + val;

            LinearLayout ll = (LinearLayout) findViewById(R.id.btnLay);


            int k = (CURRENT_PAGE_INDEX * 3) - 2;
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
                        //Log.d("personslist", ""+arrayList_person_search.size());
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

    private void check(int j) {
        try {
            //  View b = findViewById(R.id.btn_Next);


            if ((j != 0) && ((j + 1) % 3 == 0)) {
                //  View b = findViewById(R.id.btn_Next);
                btn_Next.setVisibility(View.VISIBLE);
            }else{
                btn_Next.setVisibility(View.GONE);
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

            for (int i = start; i < (start )+ NUM_ITEMS_PAGE; i++) {

                if (i < arrayList_person_search.size()) {

                    sort.add(arrayList_person_search.get(i));

                } else {
                    break;
                }
            }

            personSearchAdapter = new PersonSearchAdapter(getApplicationContext(), sort);
            listview_person_search.setAdapter(personSearchAdapter);

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void CheckBtnBackGroud(int index , int cpi) {
        try {
            int temp = (cpi * 3) - 3;
            temp = temp + index + 1;
            title.setText("Page " + temp);
            for (int i = 0; i < noOfBtns; i++) {
                if (i == index) {
                    btns[i].setTextColor(getResources().getColor(android.R.color.white));
                    btns[i].setBackground(getResources().getDrawable(R.drawable.buttn_click_bcgrnd));
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

    private class AsyncLoading extends AsyncTask<String, String, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            try {
                GetAuthDetailWebService();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
