package com.example.kosta.ordermadeandroid.activity.main;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.deal.DealConsumerFragment;
import com.example.kosta.ordermadeandroid.activity.member.MemberMyPageFragment;
import com.example.kosta.ordermadeandroid.activity.portfolio.PortfolioDetailActivity;
import com.example.kosta.ordermadeandroid.activity.portfolio.PortfolioRegister2Activity;
import com.example.kosta.ordermadeandroid.activity.portfolio.PortfolioRegisterActivity;
import com.example.kosta.ordermadeandroid.activity.product.ProductDetailActivity;
import com.example.kosta.ordermadeandroid.activity.product.ProductRegisterActivity;
import com.example.kosta.ordermadeandroid.activity.request.RequestJoinFragment;
import com.example.kosta.ordermadeandroid.activity.request.RequestMyListFragment;
import com.example.kosta.ordermadeandroid.activity.request.RequestRegisterActivity;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private static final int REQUEST_CODE = 1;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        mToolbar = (Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        //Initializing NavigationView
        NavigationView view = (NavigationView)findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                mDrawerLayout.closeDrawers();

                switch(item.getItemId()){
                    case R.id.nav_Home:
                        MainTempFragment mainTempFragment = new MainTempFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.relativeLayout_for_frame, mainTempFragment).commit();
                        return true;
                    case R.id.nav_myPage_Consumer:
                        //Toast.makeText(getApplicationContext(), "nav_account Selected", Toast.LENGTH_SHORT).show();
                        MemberMyPageFragment myPageFragment = new MemberMyPageFragment();
                        FragmentManager manager = getSupportFragmentManager();
                        manager.beginTransaction()
                                .replace(R.id.relativeLayout_for_frame, myPageFragment).commit();
                        return true;
                    case R.id.nav_requestMyList:
                        Intent intent = new Intent(MainActivity.this, RequestRegisterActivity.class);
                        startActivity(intent);
                        /*RequestMyListFragment requestMyList = new RequestMyListFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.relativeLayout_for_frame, requestMyList).commit();*/
                        return true;
                    case R.id.nav_requestJoin:
                        RequestJoinFragment joinFragment = new RequestJoinFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.relativeLayout_for_frame, joinFragment).commit();
                        return true;
                    case R.id.nav_dealConsumer:
                        DealConsumerFragment dealConsumer = new DealConsumerFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.relativeLayout_for_frame, dealConsumer).commit();
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Error!! Error!!!", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.addDrawerListener(mToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MainTempFragment mainTempFragment = new MainTempFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout_for_frame, mainTempFragment)
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
