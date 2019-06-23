package com.example.mieib.aleftask.Authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mieib.aleftask.Home;
import com.example.mieib.aleftask.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements AuthenticationPresenter.View{

    AuthenticationPresenter authenticationPresenter;
    @BindView(R.id.login_button) LoginButton loginButton;
    boolean remberme_checked;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        authenticationPresenter = new AuthenticationPresenter(this,this);
        authenticationPresenter.CheckLogging();
try {
            authenticationPresenter.FaceBookAuthenticaton(loginButton);
        }catch (NullPointerException e){}



      /*  insertItem();
        saveData();
*/




    }



        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        authenticationPresenter.onActivityResult( requestCode, resultCode,  data);
        super.onActivityResult(requestCode, resultCode, data);
    }




    public void Signout(View view) {


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

       Log.e("onDestroy","=----------------------");

    }

    @OnClick(R.id.rememberme)

    public void onCheckboxClicked(View v){

        remberme_checked = ((CheckBox) v).isChecked();
        authenticationPresenter.remmemberme = remberme_checked;
        Log.e("CheckBox","remberme_checked"+remberme_checked);



    }


    @Override
    public void ShowToast(String info) {
        Toast.makeText(this,info,Toast.LENGTH_LONG).show();
    }

    @Override
    public void gotoHome() {
        startActivity(new Intent(MainActivity.this,Home.class));
        finish();
    }
}
