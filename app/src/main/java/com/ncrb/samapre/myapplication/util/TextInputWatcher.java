package com.ncrb.samapre.myapplication.util;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

public class TextInputWatcher implements TextWatcher {

    TextInputLayout textInputLayout;
    public TextInputWatcher(TextInputLayout textInputLayout){
        this.textInputLayout=textInputLayout;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
