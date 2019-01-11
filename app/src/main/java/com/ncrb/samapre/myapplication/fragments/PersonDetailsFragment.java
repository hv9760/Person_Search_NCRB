package com.ncrb.samapre.myapplication.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.github.silvestrpredko.dotprogressbar.DotProgressBarBuilder;
import com.ncrb.samapre.myapplication.PersonDetails;
import com.ncrb.samapre.myapplication.PersonMoreDetails;
import com.ncrb.samapre.myapplication.R;

import com.ncrb.samapre.myapplication.responses.PersonDetail;

public class PersonDetailsFragment extends Fragment implements PersonDetails.SendValues {


    private ScrollView scrollV;
    private TextView textView_person_full_name, textView_person_alias, textView_person_relation_type;
    private TextView textView_person_gender, textView_person_age, textView_person_religion, txt_person_Act_Section, txt_person_All_Accused;
    //textView_person_nationality;
    private TextView textView_person_height_from, textView_person_height_to, textView_person_uid;
    private TextView textView_person_State, textView_person_district, textView_person_ps;
    private TextView textView_person_FIR_reg_num, textView_person_FIR_date;
    private TextView textView_person_relative_name, textView_person_permanent, textView_person_present;

    private ImageView accusedimage;
    private TextView loading;
    private TextView btn_more_details;
    private DotProgressBar dot_progress_bar;


    private String full_name, alias, relation_type, relative_name, FullRegnum, AccusedSrno;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            full_name = getArguments().getString("person_full_name");
            alias = getArguments().getString("person_alias");
            relation_type = getArguments().getString("person_relation_type");

            relative_name = getArguments().getString("person_relative_name");
            AccusedSrno = getArguments().getString("AccusedSrno");
            FullRegnum = getArguments().getString("FullRegnum");


        }

    }

    public static PersonDetailsFragment newInstance(String person_full_name, String person_alias, String person_relation_type, String person_relative_name, String AccusedSrno, String FullRegnum) {


        PersonDetailsFragment fragment = new PersonDetailsFragment();
        Bundle args = new Bundle();
        args.putString("person_full_name", person_full_name);
        args.putString("person_alias", person_alias);
        args.putString("person_relation_type", person_relation_type);

        args.putString("person_relative_name", person_relative_name);
        args.putString("AccusedSrno", AccusedSrno);
        args.putString("FullRegnum", FullRegnum);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_person_details, container, false);
        textView_person_full_name = (TextView) view.findViewById(R.id.txt_person_detail_Full_Name);
        textView_person_alias = (TextView) view.findViewById(R.id.txt_person_detail_alias);
        textView_person_relation_type = (TextView) view.findViewById(R.id.txt_person_detail_Relation_Type);
        textView_person_relative_name = (TextView) view.findViewById(R.id.txt_person_detail_Relative_Name);
        textView_person_gender = (TextView) view.findViewById(R.id.txt_person_detail_Gender);
        textView_person_age = (TextView) view.findViewById(R.id.txt_person_detail_age);
        textView_person_religion = (TextView) view.findViewById(R.id.txt_person_detail_religion);
        textView_person_height_from = (TextView) view.findViewById(R.id.txt_person_detail_Height_From);
        textView_person_height_to = (TextView) view.findViewById(R.id.txt_person_detail_Height_To);
        textView_person_uid = (TextView) view.findViewById(R.id.txt_person_detail_UID);
        textView_person_State = (TextView) view.findViewById(R.id.txt_person_detail_State);
        textView_person_district = (TextView) view.findViewById(R.id.txt_person_detail_district);
        textView_person_ps = (TextView) view.findViewById(R.id.txt_person_detail_ps);
        textView_person_FIR_reg_num = (TextView) view.findViewById(R.id.txt_person_detail_fir_no);
        textView_person_FIR_date = (TextView) view.findViewById(R.id.txt_person_detail_FIR_Date);
        textView_person_permanent = (TextView) view.findViewById(R.id.txt_person_Permanent_Address);
        textView_person_present = (TextView) view.findViewById(R.id.txt_person_Present_Address);
        txt_person_Act_Section = (TextView) view.findViewById(R.id.txt_person_Act_Section);
        txt_person_All_Accused = (TextView) view.findViewById(R.id.txt_person_All_Accused);
        accusedimage = (ImageView) view.findViewById(R.id.accusedimage);
        btn_more_details = view.findViewById(R.id.btn_more_details);
        btn_more_details.setOnClickListener(btn_more_details_listener);
        loading=view.findViewById(R.id.loading);
        scrollV = view.findViewById(R.id.scrollV);
        dot_progress_bar=view.findViewById(R.id.dot_progress_bar);
        new DotProgressBarBuilder(getActivity())
                .setStartColor(Color.BLACK)
                .setAnimationDirection(DotProgressBar.RIGHT_DIRECTION)
                .build();
        checkButton();


        return view;
    }



    private void checkButton(){
        if (FullRegnum == null || TextUtils.isEmpty(FullRegnum) || AccusedSrno == null || TextUtils.isEmpty(AccusedSrno)) {
            btn_more_details.setVisibility(View.GONE);
        }else{
            btn_more_details.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void values(PersonDetail response) {

        scrollV.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        dot_progress_bar.setVisibility(View.GONE);

        if (response.getGENDER() != null || !TextUtils.isEmpty(response.getGENDER())) {
            textView_person_gender.setText(response.getGENDER());
        } else {
            textView_person_gender.setText(getResources().getString(R.string.info_not_available));
        }

        if (response.getAGE() != null && !TextUtils.isEmpty(response.getAGE())) {
            textView_person_age.setText(response.getAGE());
        } else {
            textView_person_age.setText(getResources().getString(R.string.info_not_available));
        }

        if (response.getRELIGION() != null && !TextUtils.isEmpty(response.getRELIGION())) {
            textView_person_religion.setText(response.getRELIGION());
        } else {
            textView_person_religion.setText(getResources().getString(R.string.info_not_available));
        }

        if (response.getHEIGHT_TO_CM() != null && !TextUtils.isEmpty(response.getHEIGHT_TO_CM())) {
            textView_person_height_to.setText(response.getHEIGHT_TO_CM());
        } else {
            textView_person_height_to.setText(getResources().getString(R.string.info_not_available));
        }
        if (response.getHEIGHT_FROM_CM() != null && !TextUtils.isEmpty(response.getHEIGHT_FROM_CM())) {
            textView_person_height_from.setText(response.getHEIGHT_FROM_CM());
        } else {
            textView_person_height_from.setText(getResources().getString(R.string.info_not_available));
        }


        if (response.getUID_NUM() != null && !TextUtils.isEmpty(response.getUID_NUM())) {
            textView_person_uid.setText(response.getUID_NUM());
        } else {
            textView_person_uid.setText(getResources().getString(R.string.info_not_available));
        }


        if (response.getSTATE_ENG() != null && !TextUtils.isEmpty(response.getSTATE_ENG())) {
            textView_person_State.setText(response.getSTATE_ENG());
        } else {
            textView_person_State.setText(getResources().getString(R.string.info_not_available));
        }


        if (response.getDISTRICT() != null && !TextUtils.isEmpty(response.getDISTRICT())) {
            textView_person_district.setText(response.getDISTRICT());
        } else {
            textView_person_district.setText(getResources().getString(R.string.info_not_available));
        }

        if (response.getPS() != null && !TextUtils.isEmpty(response.getPS())) {
            textView_person_ps.setText(response.getPS());
        } else {
            textView_person_ps.setText(getResources().getString(R.string.info_not_available));
        }

        if (response.getPS() != null && !TextUtils.isEmpty(response.getREG_NUM())) {
            textView_person_FIR_reg_num.setText(response.getREG_NUM());
        } else {
            textView_person_FIR_reg_num.setText(getResources().getString(R.string.info_not_available));
        }


        if (response.getREG_DT() != null && !TextUtils.isEmpty(response.getREG_DT())) {
            textView_person_FIR_date.setText(response.getREG_DT());
        } else {
            textView_person_FIR_date.setText(getResources().getString(R.string.info_not_available));
        }


        if (response.getPERMANENT() != null && !TextUtils.isEmpty(response.getPERMANENT())) {
            textView_person_permanent.setText(response.getPERMANENT());
        } else {
            textView_person_permanent.setText(getResources().getString(R.string.info_not_available));
        }

        if (response.getPRESENT() != null && !TextUtils.isEmpty(response.getPRESENT())) {
            textView_person_present.setText(response.getPRESENT());
        } else {
            textView_person_present.setText(getResources().getString(R.string.info_not_available));
        }


        if (response.getSection() != null && !TextUtils.isEmpty(response.getSection())) {
            txt_person_Act_Section.setText(response.getSection());
        } else {
            txt_person_Act_Section.setText(getResources().getString(R.string.info_not_available));
        }


        if (response.getAll_accused_names() != null && !TextUtils.isEmpty(response.getAll_accused_names())) {
            txt_person_All_Accused.setText(response.getAll_accused_names());
        } else {
            txt_person_All_Accused.setText(getResources().getString(R.string.info_not_available));
        }

        if (alias != null && !TextUtils.isEmpty(alias)) {
            textView_person_alias.setText(alias);
        } else {
            textView_person_alias.setText(getResources().getString(R.string.info_not_available));
        }

        textView_person_full_name.setText(full_name);

        if (relation_type != null && !TextUtils.isEmpty(relation_type)) {
            textView_person_relation_type.setText(relation_type);
        } else {
            textView_person_relation_type.setText(getResources().getString(R.string.info_not_available));
        }


        if (response.getUploadedFile() != null && !TextUtils.isEmpty(response.getUploadedFile())) {

            byte[] decodedString = Base64.decode(response.getUploadedFile(), Base64.DEFAULT);

            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            accusedimage.setImageBitmap(decodedByte);

        }

        if (relative_name != null && !TextUtils.isEmpty(relative_name)) {
            textView_person_relative_name.setText(relative_name);

        } else {
            textView_person_relative_name.setText("Not Known");

        }


    }

    private View.OnClickListener btn_more_details_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

                Intent intentmore = new Intent(getContext(), PersonMoreDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("REGISTRATION_NUM", FullRegnum);
                bundle.putString("AccusedSrno", AccusedSrno);
                intentmore.putExtras(bundle);

                intentmore.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentmore);
            }

    };

}
