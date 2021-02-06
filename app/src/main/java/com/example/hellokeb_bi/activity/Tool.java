package com.example.hellokeb_bi.activity;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tool  extends AppCompatActivity {
    public String getToken(){
        SharedPreferences shared = getSharedPreferences("token", MODE_PRIVATE);
        return shared.getString("token", "");
    }

}
