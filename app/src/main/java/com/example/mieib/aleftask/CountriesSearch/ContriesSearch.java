package com.example.mieib.aleftask.CountriesSearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


import com.example.mieib.aleftask.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContriesSearch extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<>();

    @BindView(R.id.my_toolbarsub)Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contries_search);

        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.CountriesName));
        setSupportActionBar(toolbar);

        get_json();


    }



    public void get_json(){
        String json;
        try{
            InputStream inputStream = getAssets().open("countries.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);

                    arrayList.add(object.getString("name"));

            }


        }catch (Exception e){

        }


       for(int i=0;i<arrayList.size();i++){
            Log.e("Country",arrayList.get(i));
       }


        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.search);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.auto_complete_layout,R.id.countryName,arrayList);
        autoCompleteTextView.setAdapter(adapter);
    }

}
