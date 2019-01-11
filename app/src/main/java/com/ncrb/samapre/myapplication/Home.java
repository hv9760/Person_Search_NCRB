package com.ncrb.samapre.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncrb.samapre.myapplication.AppPreferences;
import com.ncrb.samapre.myapplication.Constants;
import com.ncrb.samapre.myapplication.PersonSearch;
import com.ncrb.samapre.myapplication.PropertySearch;
import com.ncrb.samapre.myapplication.Utils;

import com.ncrb.samapre.myapplication.R;
import com.ncrb.samapre.myapplication.Singleton;


public class Home extends AppCompatActivity {

    private AppPreferences objAppPreferences;
    private Singleton singleton;
    private TextView person_count;
    private TextView property_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        singleton = Singleton.getInstance();

        // core objects
        objAppPreferences = new AppPreferences(Home.this);


        person_count = (TextView) findViewById(R.id.person_count);
        property_count = (TextView) findViewById(R.id.property_count);






        ImageView img_person_search;
        ImageView img_property_search;
        img_person_search=(ImageView)findViewById(R.id.img_view_person_search);
        img_property_search=(ImageView)findViewById(R.id.img_view_property_search);

        img_person_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,PersonSearch.class);
                startActivity(intent);
            }
        });

        img_property_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,PropertySearch.class);
                startActivity(intent);
            }
        });
    }

    // get count from preference
    @Override
    public void onResume(){
        super.onResume();

        try {
            person_count.setText("Total Count: " + getCount(Home.this, Constants.SearchTypePerson));

        }catch (Exception e){
            e.printStackTrace();

            person_count.setText("Total Count: 0");

        }

        try {

            property_count.setText("Total Count: " + getCount(Home.this, Constants.SearchTypeProperty));

        }catch (Exception e){
            e.printStackTrace();


            property_count.setText("Total Count: 0");
        }


    }//end onResume


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    // get count from preference
    public String getCount(Context context, String searchType) {


        String checkUser = "0";
        String date = Utils.getDateTime("dd-MM-yyyy");
        String uniqueKey = searchType+" "+date+" "+this.singleton.username;


        try {


            // core objects
            objAppPreferences = new AppPreferences(context);

            checkUser = objAppPreferences.getUserDefaults(uniqueKey);
            Utils.printv("Home preference: "+checkUser+"key "+uniqueKey);

            return checkUser;

        }catch (Exception e){
            e.printStackTrace();
            return checkUser;
        }

    }// get person count


}
