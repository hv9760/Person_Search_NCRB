package com.ncrb.samapre.myapplication;

import android.support.annotation.Keep;

/**
 * Created by sez1 on 24/11/15.
 */
@Keep
public class WSPGetFIR {

    public String STATUS_CODE;
    public String STATUS;
    public String MESSAGE;
    public String BASE64_BINARY;
    public String seed;


    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getSTATUS_CODE() {
        return STATUS_CODE;
    }

    public void setSTATUS_CODE(String STATUS_CODE) {
        this.STATUS_CODE = STATUS_CODE;
    }

}// end main class
