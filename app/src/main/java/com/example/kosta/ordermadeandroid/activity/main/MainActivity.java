package com.example.kosta.ordermadeandroid.activity.main;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

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

                    case R.id.nav_myPage_Consumer:
                        Toast.makeText(getApplicationContext(), "nav_account Selected", Toast.LENGTH_SHORT).show();
                        MemberMyPageFragment myPageFragment = new MemberMyPageFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, myPageFragment).commit();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
