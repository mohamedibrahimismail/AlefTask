package com.example.mieib.aleftask;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mieib.aleftask.Authentication.AuthenticationPresenter;
import com.example.mieib.aleftask.Authentication.MainActivity;
import com.example.mieib.aleftask.Authentication.UserData;
import com.example.mieib.aleftask.CountriesSearch.ContriesSearch;
import com.example.mieib.aleftask.PhoneContacs.PhoneContacts;

import com.example.mieib.aleftask.SharePhotoToFacebook.SharePhotoToFacebook;
import com.example.mieib.aleftask.TodoList.Todo;
import com.example.mieib.aleftask.TodoList.TodoDAO;
import com.example.mieib.aleftask.TodoList.TodoListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends AppCompatActivity implements AuthenticationPresenter.Logout, NavigationView.OnNavigationItemSelectedListener{


      @BindView(R.id.my_toolbar)Toolbar toolbar;
      @BindView(R.id.drawer_Layout)DrawerLayout drawerLayout;
      @BindView(R.id.design_navigation_view)NavigationView navigationView;
      @BindView(R.id.fab)FloatingActionButton floatingActionButton;
      @BindView(R.id.AddingTodo)View Adding_TODO_view;
      @BindView(R.id.todoitemList)RecyclerView todoitemList;
      ImageView Menu_Item;

      TextView nav_user_name;
      ImageView  nav_image;
      private int mSelectedId;
      UserData userData;

      private TodoDAO dao;

      final String[] date = new String[1];
      List x;

    TodoListAdapter categoryAdapter;
    EditText Todo_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
          userData = (UserData)getApplicationContext();
          Log.e("Home",userData.getFirst_name()+userData.getLast_name()+"------------------------------");

          initViews();

          initDAO();
          initToDoList();

    }

    private void initDAO() {
        dao = new TodoDAO(this);
        x = dao.getTodos();
        for(int i=0 ;i<x.size();i++) {
            Log.e("TODO", (((Todo) x.get(i)).getId()+" "+((Todo) x.get(i)).getText() + " " +((Todo) x.get(i)).getDate()) );

        }
    }


    public void initToDoList(){

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        todoitemList.setLayoutManager(mLayoutManager);
        categoryAdapter = new TodoListAdapter(this,x);
        todoitemList.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();


    }

    @OnClick(R.id.fab)
    public void handle_FloatingButton(){
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog[] datePickerDialog = new DatePickerDialog[1];


        Adding_TODO_view.setVisibility(View.VISIBLE);
        Todo_Text = (EditText)Adding_TODO_view.findViewById(R.id.TodoText);
        Button laststep=(Button) Adding_TODO_view.findViewById(R.id.laststep);
        laststep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adding_TODO_view.setVisibility(View.GONE);
            }
        });

        Button AddTodo =  (Button)Adding_TODO_view.findViewById(R.id.createTodo);
        AddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(Todo_Text.getText().toString().trim().matches("")){
                     showToast(getApplicationContext().getString(R.string.Please_Enter_TODO));
                }else{

                     if(date[0]!=null){

                         dao.createTodo(Todo_Text.getText().toString().trim(),date[0]);
                         showToast(getApplicationContext().getString(R.string.Successfully_saved));
                         initDAO();
                         categoryAdapter.todolist = x;
                         categoryAdapter.notifyDataSetChanged();
                         Todo_Text.setText("");
                         date[0]=null;
                         Adding_TODO_view.setVisibility(View.GONE);

                     }else{
                         showToast(getApplicationContext().getString(R.string.Please_Select_Date));
                     }


                 }


            }
        });

        ImageButton calender = (ImageButton)Adding_TODO_view.findViewById(R.id.calender);
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    datePickerDialog[0] = new DatePickerDialog(Home.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            date[0] = day +"/"+(month+1)+"/"+year;
                            showToast(date[0]);
                        }
                    },day,month,year);

                 datePickerDialog[0].show();
                }

            }
        });



    }

    public void initViews(){
        setSupportActionBar(toolbar);
        ButterKnife.bind(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        //to get username and image in left menu
        View hView = navigationView.getHeaderView(0);
        nav_user_name = hView.findViewById(R.id.userNameNav);
        nav_user_name.setText(userData.getFirst_name()+" "+userData.getLast_name());
        nav_image = hView.findViewById(R.id.image);
        Picasso.get().load("https://graph.facebook.com/"+userData.getId()+"/picture?type=large&width=720&height=720").into(nav_image);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @OnClick(R.id.menu_item)
    public void open_Nav(){
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void logout() {
        AuthenticationPresenter authenticationPresenter = new AuthenticationPresenter(this,this);
        authenticationPresenter.delete_User_Data();
        authenticationPresenter.SignOut();
        startActivity(new Intent(this,MainActivity.class));
        finish();
        Log.e("logout","-------------");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(false);
        mSelectedId = menuItem.getItemId();
        itemSelection(mSelectedId);
        return true;
    }



    @SuppressLint("WrongConstant")
    private void itemSelection(int mSelectedId) {

        //IntentDetail data;
        switch (mSelectedId) {


            case R.id.CountriesName:
                drawerLayout.closeDrawer(GravityCompat.START);
                showToast("CountriesName");
                startActivity(new Intent(Home.this, ContriesSearch.class));
                break;

            case R.id.PhoneContacts:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(Home.this, PhoneContacts.class));
                showToast("PhoneContacts");
                break;


            case R.id.SharePhotoToFacebook:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(Home.this, SharePhotoToFacebook.class));
                showToast("SharePhotoToFacebook");
                break;

            case R.id.logout_lm:
                drawerLayout.closeDrawer(GravityCompat.START);
                logout();
                break;

        }

    }

    public void showToast(String info){
        Toast.makeText(this,info,Toast.LENGTH_LONG).show();
    }


}
