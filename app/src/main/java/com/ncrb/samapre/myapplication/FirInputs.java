package com.ncrb.samapre.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class FirInputs extends AppCompatActivity {



    AlertDialog alertDialog;




    ArrayList<String> YearValue=new ArrayList<>();
    int subDistrictCD,subPsCD,thisYear;
    private String TAG = "TEST";
    //SoapPrimitive s;
    Object response;
    File filePath;
    String nameapp=null;
    boolean ispdfexist;
    String district=null,ps=null,year,firNO=null,result=null;
    String msg= "";

    public ProgressDialog mProgressDialog;
    Singleton singleton = Singleton.getInstance();

    private Spinner listOfDistrict;
    //private ArrayAdapter<DistrictKV> listOfDistrictAdaptor;


    private Spinner listOfPoliceStation;
    //private ArrayAdapter<PoliceStationKV> listOfPoliceStationAdaptor;

    final String txt_select = "-- select --";

    String district_code = "";
    String police_station_code = "";
    MCoCoRy mCoCoRy = new  MCoCoRy();

    TextView txtview_folder_locs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fir_inputs);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");

        txtview_folder_locs=(TextView)findViewById(R.id.txtview_folder_locs);
        thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2016; i <= thisYear; i++) {
            YearValue.add(Integer.toString(i));
        }



        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {


            String appendstring=null;

            try {

                if(singleton.firregnum.length()==4) {
                    nameapp = singleton.firregnum + singleton.district_cd + singleton.ps_cd;
                }else if(singleton.firregnum.length()==3){
                    appendstring="0"+singleton.firregnum;
                    nameapp = appendstring + singleton.district_cd + singleton.ps_cd;
                }else if(singleton.firregnum.length()==2){
                    appendstring="0"+"0"+singleton.firregnum;
                    nameapp = appendstring + singleton.district_cd + singleton.ps_cd;
                }else  if(singleton.firregnum.length()==1){
                    appendstring="0"+"0"+"0"+singleton.firregnum;
                    nameapp = appendstring + singleton.district_cd + singleton.ps_cd;
                }

                Utils.printv("PDF Name "+ nameapp + " FIR num "+singleton.firregnum);


                GetFIRStatusDetail();


            } catch (Exception e) {
                Utils.printv("Error while parsing data.!! Please try later.");
            }


        } else {

            Utils.showAlert(FirInputs.this, "Network Connection Failed..");

        }



    }// end oncreate


    /**
     * @ get auth service
     */
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
    public void GetFIRStatusDetail() throws Exception {

        this.mProgressDialog.show();


        String coco_seed = ""; String coco_seed_encd = "";

        try {
        Map postParamsFIR = new HashMap();
        postParamsFIR.put("firregnum", singleton.firregnum);
        postParamsFIR.put("district_cd", singleton.district_cd);
        postParamsFIR.put("ps_cd", singleton.ps_cd);
        postParamsFIR.put("year", singleton.year);
        postParamsFIR.put("m_service", "replaceforjava");

            Gson gsonObj = new Gson();
            coco_seed = gsonObj.toJson(postParamsFIR);




            coco_seed_encd  = mCoCoRy.ThreadToSecureDetail(getApplicationContext(), coco_seed, "ENCODE");

        } catch (Exception e) {
            e.printStackTrace();
        }


        // -----------------------------------------------------------------

        // create a new hash which you want to send on server
        Map postParamsFIR = new HashMap();

        postParamsFIR.put("seed", coco_seed_encd);


        JSONPostParams jsonPostParams = new JSONPostParams("mFIRReportConnect", postParamsFIR);
            RestAdapter restAdapter =providesRestAdapter();

        ApiCaller apiCaller = restAdapter.create(ApiCaller.class);
        apiCaller.mFIRReportConnect(jsonPostParams,
                new Callback<WSPGetFIR>() {
                    @Override
                    public void failure(RetrofitError arg0) {

                        Utils.showToastMsg(FirInputs.this, "Can not connect to server.", Toast.LENGTH_SHORT);
                        Utils.printv("failure " + arg0.toString());

                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();

                    }// end failure

                    @Override
                    public void success(WSPGetFIR result2, Response response) {
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


                        WSPGetFIR result = gson.fromJson(jsonString, WSPGetFIR.class);
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();



                        if (result.getSTATUS_CODE().toString().equals("200")) {



                            // don't store username and password in shared preference for security reason

                            try {



                                showFIRtoUser(result.BASE64_BINARY);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Utils.showToastMsg(FirInputs.this, result.MESSAGE, Toast.LENGTH_SHORT);
                        }
                    }// end success
                });


    }// end auth web service

    /**
     * @ get district and ps list
     */

    public void showFIRtoUser(String result) throws Exception {

        if(result==null){

            Utils.showAlert(FirInputs.this,"Error while trying to fetch your FIR request." );

        }

        else {

            //EditFirNo.setText("");

            Log.i(TAG, "onPostExecute");

            try {
                if (result.equals("ZWZk")) {

                    txtview_folder_locs.setText("FIR Not Found Or FIR is restricted to view as per supreme court order."+filePath);
                    Utils.showAlert(FirInputs.this, "FIR Not Found Or FIR is restricted to view as per supreme court order.");


                } else if (result.equals("YWJjZA==")) {

                    txtview_folder_locs.setText("FIR Not Found Or FIR is restricted to view as per supreme court order."+filePath);
                    Utils.showAlert(FirInputs.this, "FIR Not Found Or FIR is restricted to view as per supreme court order.");


                } else if (ispdfexist == true) {


                    txtview_folder_locs.setText("FIR file already exist. Please check your download folder."+filePath);
                    Utils.showAlert(FirInputs.this, "FIR file already exist. Please check your download folder.");

                } else {

                    {

                        byte[] pdfAsBytes = Base64.decode(result.toString(), 0);

                        Log.i(TAG, "sendEnvelope");

                        File direct = new File(Environment.getExternalStorageDirectory()+"/FIR Folder");

                        if(!direct.exists()){

                            direct.mkdir();

                            filePath = new File(direct, File.separator + "FIR_no_"+nameapp+".pdf");

                            System.out.println("filepath "+filePath);

                            if(filePath.isFile()==true){

                                txtview_folder_locs.setText("File is already exist"+filePath);
                                Log.v("MSG", "File is already exist");
                                ispdfexist=filePath.isFile();

                            } else {

                                try {

                                    filePath.createNewFile();

                                    FileOutputStream fo = new FileOutputStream(filePath.getPath());

                                    fo.write(pdfAsBytes);

                                    fo.flush();

                                    fo.close();

                                } catch (Exception exc) {
                                    exc.printStackTrace();
                                }
                            }

                        } else{
                            filePath = new File(direct, File.separator + "FIR_no_"+nameapp + ".pdf");
                            System.out.println("filepath "+filePath);
                            if(filePath.isFile()==true){
                                txtview_folder_locs.setText("File is already exist"+filePath);
                                Log.v("MSG", "File is already exist");
                                ispdfexist=filePath.isFile();
                            }else{
                                try {
                                    filePath.createNewFile();
                                    FileOutputStream fo = new FileOutputStream(filePath.getPath());
                                    fo.write(pdfAsBytes);
                                    fo.flush();
                                    fo.close();
                                } catch (Exception exc) {
                                    exc.printStackTrace();
                                }
                            }
                        }
                    }


                    txtview_folder_locs.setText("FIR in PDF has been downloaded in Device storage  \n Location: "+filePath);

                    alertDialog = new AlertDialog.Builder(FirInputs.this).create();
                    alertDialog.setTitle("");
                    alertDialog.setMessage("FIR in PDF has been downloaded in Device storage FIR Folder");
                    alertDialog.setIcon(R.drawable.ncrb_logo_trans_55);
                    alertDialog.setButton("OK..", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (filePath.exists()) {
                                Uri filepath = Uri.fromFile(filePath);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(filepath, "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                try {
                                    startActivity(intent);
                                } catch (Exception e) {
                                    Log.e("error", "" + e);
                                }

                            } else {
                                txtview_folder_locs.setText("There is some problem"+filePath);

                                Log.e("else", "There is some problem");
                            }
                        }
                    });
                    alertDialog.show();
                }
            } catch (Exception e) {

                e.printStackTrace();

                alertDialog = new AlertDialog.Builder(FirInputs.this).create();
                alertDialog.setTitle("Attention...");
                alertDialog.setMessage("Get Error while fetching PDF...");
                alertDialog.setIcon(R.drawable.ncrb_logo_trans_55);
                alertDialog.setButton("OK..", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }
                });
                alertDialog.show();
            }

        }// end else

    }// end show fir to user




}// end main class

