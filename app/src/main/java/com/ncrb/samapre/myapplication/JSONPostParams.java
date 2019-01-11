package com.ncrb.samapre.myapplication;

/**
 * Created by Lenovo on 16-02-2018.
 */

import android.support.annotation.Keep;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;


/**
 * Class to represent a category in app.
 * @author
 */
public class JSONPostParams {


    String seed;

    ///Map Message = new HashMap();	Message.put("title", "Test message");Message.get("title")
    public JSONPostParams(String method_name, Map mapParams) {


        // @logic may in future we need to add more parameters
        if (method_name == "mLoginVerify") {


            this.seed = mapParams.get("seed").toString();

        }
        else if (method_name == "mVehicleTypeConnect") {

            this.seed = mapParams.get("seed").toString();

        }
        else if (method_name == "mPropertySearch") {

            this.seed = mapParams.get("seed").toString();

        }
        else if (method_name == "mPropertyDetailDisplay") {

            this.seed = mapParams.get("seed").toString();

        }



        else if (method_name == "mManufacturerConnect") {

            this.seed = mapParams.get("seed").toString();

        }

        else if (method_name == "mAllStateConnect") {

            this.seed = mapParams.get("seed").toString();
        }

        else if (method_name == "mRelationTypeConnect") {

            this.seed = mapParams.get("seed").toString();
        }

        else if (method_name == "mReligionConnect") {

            this.seed = mapParams.get("seed").toString();

        }
        else if (method_name == "mMVColorConnect") {

            this.seed = mapParams.get("seed").toString();

        }
        else if (method_name == "mMVModelConnect") {

            this.seed = mapParams.get("seed").toString();

        }

        else if (method_name == "mDistrictConnect") {

            this.seed = mapParams.get("seed").toString();

        }
        else if (method_name == "mPoliceStationConnect") {

            this.seed = mapParams.get("seed").toString();

        }

        else if (method_name == "mPersonSearch") {

            this.seed = mapParams.get("seed").toString();

        }

        else if (method_name == "mPersonDetailDisplay") {


            this.seed = mapParams.get("seed").toString();
        }

        else if (method_name == "mPersonMoreDetails") {

            this.seed = mapParams.get("seed").toString();

        }

        else if (method_name == "mPersonMoreDetailsConvict") {

            this.seed = mapParams.get("seed").toString();

        }
        else if (method_name == "mPersonMoreDetailsChargesheet") {

            this.seed = mapParams.get("seed").toString();

        }

        else if (method_name == "mPersonMoreDetailsPHOffender") {

            this.seed = mapParams.get("seed").toString();

        }

        else if (method_name == "mFIRReportConnect") {

            this.seed = mapParams.get("seed").toString();




        }
        else if (method_name == "mReqSedition") {

            this.seed = mapParams.get("seed").toString();




        }

    }// end json post params



}// end json post params