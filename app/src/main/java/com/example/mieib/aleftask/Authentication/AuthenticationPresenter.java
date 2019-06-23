package com.example.mieib.aleftask.Authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

public class AuthenticationPresenter {

    View view;
    Context context;
    CallbackManager callbackManager;
    UserData userData;
    ArrayList<UserData> userDataList;
    boolean remmemberme = false;
    Logout logout;

    public AuthenticationPresenter (Context context,View view){
        this.context = context;
        this.view = view;
    }

    public AuthenticationPresenter (Context context,Logout logout){
        this.context = context;
        this.logout=logout;
    }

    public void FaceBookAuthenticaton(LoginButton loginButton){

        if(isNetworkAvailable()) {



            this.remmemberme =remmemberme;
            FacebookSdk.sdkInitialize(context.getApplicationContext());
            AppEventsLogger.activateApp(context);
            callbackManager = CallbackManager.Factory.create();
            loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

        }else {
            view.ShowToast(context.getString(R.string.checknetworkmessage));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }catch (NullPointerException e){
            view.ShowToast(context.getString(R.string.Errorinloading));
        }

    }

    public void loadUserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {


                    userData = new UserData();
                    userDataList = new ArrayList<>();
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");


                    userData.setFirst_name(first_name);
                    userData.setLast_name(last_name);
                    userData.setEmail(email);
                    userData.setId(id);
                    userDataList.add(userData);

                    Log.e("first_name",userData.getFirst_name());
                    Log.e("last_name",userData.getLast_name());
                    Log.e("email",userData.getEmail());
                    Log.e("id",userData.getId());
                    Log.e("remmemberme",remmemberme+"");
                    if(remmemberme) {
                        saveData();

                    }else{
                        Adding_data_toGlobal(userData);
                       try {
                           view.gotoHome();
                       }catch (Exception e){

                            }
                    }

                   // startActivity(new Intent(MainActivity.this,Home.class));

                }catch (JSONException e){

                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }


    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken!=null){
                loadUserProfile(currentAccessToken);
            }else{
                //mAuth.signOut();
            }
        }
    } ;


    private void handleFacebookAccessToken(AccessToken token) {
        Log.e("handilg", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

    }




    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void SignOut(){

        LoginManager.getInstance().logOut();
    }



    private void saveData() {
        Log.e("saveData","----------------------------------");
        SharedPreferences sharedPreferences =context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userDataList);
        editor.putString("task list", json);
        editor.apply();
        CheckLogging();

    }

    private void loadData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<UserData>>() {
        }.getType();
        userDataList = gson.fromJson(json, type);

    }

    public void CheckLogging() {

        loadData();
        if (userDataList == null) {

           SignOut();

        } else {
            Adding_data_toGlobal(userDataList.get(0));
            view.ShowToast(context.getString(R.string.welcome)+" "+userDataList.get(0).getFirst_name());
            view.gotoHome();
        }
    }


    public void delete_User_Data(){

        SharedPreferences sharedPreferences =context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("task list");
        editor.apply();
       // this.logout.logout();

    }

    public void Adding_data_toGlobal(UserData userData){

        UserData global = (UserData)context.getApplicationContext();
        global.setId(userData.getId());
        global.setFirst_name(userData.getFirst_name());
        global.setLast_name(userData.getLast_name());
        global.setEmail(userData.getEmail());

    }




    public interface View{

        void ShowToast(String info);
        void gotoHome();


    }


    public interface Logout{
        void logout();
    }





}
