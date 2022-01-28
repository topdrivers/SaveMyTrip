package com.openclassrooms.savemytrip.base;

import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;

//import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Philippe on 26/02/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    // --------------------
    // LIFE CYCLE
    // --------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutContentViewID());
        ButterKnife.bind(this); //Configure Butterknife
    }

    public abstract int getLayoutContentViewID();

    // --------------------
    // UI
    // --------------------

    protected void configureToolbar(){
        ActionBar ab = getSupportActionBar();
        ((ActionBar) ab).setDisplayHomeAsUpEnabled(true);
    }
}

