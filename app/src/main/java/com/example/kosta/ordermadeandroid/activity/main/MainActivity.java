package com.example.kosta.ordermadeandroid.activity.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.deal.DealConsumerActivity;
import com.example.kosta.ordermadeandroid.activity.member.MemberLoginActivity;
import com.example.kosta.ordermadeandroid.activity.member.MemberMyPageActivity;
import com.example.kosta.ordermadeandroid.activity.request.RequestJoinActivity;
import com.example.kosta.ordermadeandroid.activity.request.RequestMyListActivity;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public RelativeLayout relativeLayout;
    public Toolbar mToolbar;
    public NavigationView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_for_frame);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        //Initializing NavigationView
        view = (NavigationView)findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MainTempFragment mainTempFragment = new MainTempFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout_for_frame, mainTempFragment)
                .commit();

    }

    /* We can override onBackPressed method to toggle navigation drawer*/
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }

        mDrawerLayout.closeDrawers();

        switch(item.getItemId()){
            case R.id.nav_Home:
                MainTempFragment mainTempFragment = new MainTempFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.relativeLayout_for_frame, mainTempFragment).commit();
                return true;
            case R.id.nav_member_login:
                startActivity(new Intent(this, MemberLoginActivity.class));
                return true;
//            case R.id.nav_myPage_Consumer:
//                //Toast.makeText(getApplicationContext(), "nav_account Selected", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(this, MemberMyPageActivity.class));
//                return true;
//            case R.id.nav_requestMyList:
//                startActivity(new Intent(this, RequestMyListActivity.class));
//                return true;
//            case R.id.nav_requestJoin:
//                startActivity(new Intent(this, RequestJoinActivity.class));
//                return true;
//            case R.id.nav_dealConsumer:
//                startActivity(new Intent(this, DealConsumerActivity.class));
//                return true;

            default:
                Toast.makeText(getApplicationContext(), "Error!! Error!!!", Toast.LENGTH_SHORT).show();
                return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
