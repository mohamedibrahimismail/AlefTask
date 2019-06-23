package com.example.mieib.aleftask.PhoneContacs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mieib.aleftask.Authentication.MainActivity;
import com.example.mieib.aleftask.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneContacts extends AppCompatActivity {

    ListView listView ;
    ArrayList<String> StoreContacts ;
    ArrayAdapter<String> arrayAdapter ;
    Cursor cursor ;
    String name, phonenumber ;
    public  static final int RequestPermissionCode  = 1 ;
    Button button;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone_contacts);

        listView = (ListView)findViewById(R.id.listview1);

        toolbar = (Toolbar)findViewById(R.id.my_toolbarsub);
        toolbar.setTitle(getString(R.string.PhoneContacts));

        setSupportActionBar(toolbar);

        StoreContacts = new ArrayList<>();

        EnableRuntimePermission();





    }

    public void GetContactsIntoArrayList(){

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            StoreContacts.add(name + " "  + ":" + " " + phonenumber);
            Log.e("PhoneContacts",name + " "  + ":" + " " + phonenumber);
        }

        cursor.close();

        initRecyclerView();

    }

    private void initRecyclerView() {


        arrayAdapter = new ArrayAdapter<String>(
                PhoneContacts.this,
                R.layout.phonecontact_item,
                R.id.phonecontact_item, StoreContacts
        );



        listView.setAdapter(arrayAdapter);

    }

    public void EnableRuntimePermission(){

        if(ActivityCompat.checkSelfPermission(PhoneContacts.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    PhoneContacts.this,
                    Manifest.permission.READ_CONTACTS))
            {

                Toast.makeText(PhoneContacts.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(PhoneContacts.this,new String[]{
                        Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
            }


            ActivityCompat.requestPermissions(PhoneContacts.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);


        }else{

              GetContactsIntoArrayList();

            }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(PhoneContacts.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();
                    GetContactsIntoArrayList();
                } else {

                    Toast.makeText(PhoneContacts.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}

