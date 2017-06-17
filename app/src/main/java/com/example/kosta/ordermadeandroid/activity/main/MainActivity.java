package com.example.kosta.ordermadeandroid.activity.main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.deal.DealConsumerActivity;
import com.example.kosta.ordermadeandroid.activity.deal.DealMakerFragment;
import com.example.kosta.ordermadeandroid.activity.member.MemberLoginActivity;
import com.example.kosta.ordermadeandroid.activity.member.MemberMyPageActivity;
import com.example.kosta.ordermadeandroid.activity.member.MemberMyPageFragment;
import com.example.kosta.ordermadeandroid.activity.member.MemberMyPageMakerFragment;
import com.example.kosta.ordermadeandroid.activity.member.MemberRegisterActivity;
import com.example.kosta.ordermadeandroid.activity.portfolio.PortfolioDetailActivity;
import com.example.kosta.ordermadeandroid.activity.product.ProductListActivity;
import com.example.kosta.ordermadeandroid.activity.product.ProductMyListActivity;
import com.example.kosta.ordermadeandroid.activity.product.ProductMyListFragment;
import com.example.kosta.ordermadeandroid.activity.request.RequestJoinActivity;
import com.example.kosta.ordermadeandroid.activity.request.RequestMyListActivity;
import com.example.kosta.ordermadeandroid.activity.request.RequestReceivedFragment;
import com.example.kosta.ordermadeandroid.activity.request.RequestSearchActivity;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SharedPreferences prefs;
    private String loginId;
    private String memberType;


    public DrawerLayout mDrawerLayout;
    public RelativeLayout relativeLayout;
    public Toolbar mToolbar;
    public NavigationView view;
    public ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_for_frame);
        mToolbar = (Toolbar)findViewById(R.id.nav_action);
        view = (NavigationView)findViewById(R.id.navigation_view);

        prefs = getSharedPreferences("login_info",MODE_PRIVATE);
        loginId = prefs.getString("loginId","");//SharedPreferences에서 꺼낸다
        memberType = prefs.getString("memberType","");//
        Toast.makeText(getApplication(),loginId+"-------"+memberType, Toast.LENGTH_SHORT).show();

        if(memberType.equals("C")) {
            view.getMenu().clear();//메뉴삭제
            view.inflateMenu(R.menu.navigation_menu_consumer);//메뉴추가
            //getMenuInflater().inflate(R.menu.navigation_menu, view.getMenu());//메뉴추가.
        }else if(memberType.equals("M")){
            view.getMenu().clear();
            view.inflateMenu(R.menu.navigation_menu_maker);
        }
//        else{
//            view.getMenu().clear();
//            view.getMenu().add(1, 1, 1, "menu_1");//동적으로 하나씩 추가
//            view.inflateMenu(R.menu.navigation_menu);
//        }


//        View headerView = view.getHeaderView(0);//headview클릭
//        headerView.findViewById();


        mToolbar.setNavigationIcon(R.mipmap.ic_launcher);//Logo = menu button icon
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        view.setNavigationItemSelectedListener(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_list_black_24dp);



        getSupportFragmentManager()
                .beginTransaction()
                //.replace(R.id.relativeLayout_for_frame, new MainTempFragment())
                .replace(R.id.relativeLayout_for_frame,  new MainFragment())
                .commit();

    }


    //메뉴 클릭했을때
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean result;

        item.setChecked(true);
        mDrawerLayout.closeDrawers();

        if(memberType.equals("C")){
            result = doNavigationMenuConsumer(item);
        }else if(memberType.equals("M")){
            result = doNavigationMenuMaker(item);
        }else{
            result = doNavigationMenu(item);
        }
        return result;

    }
    //주메뉴를 호출하는 버튼용
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    //로기인 하기전 상태의 메뉴용
    private boolean doNavigationMenu(MenuItem item){
        switch(item.getItemId()){
            case R.id.nav_Home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
			case R.id.nav_member_login:
				startActivity(new Intent(this, MemberLoginActivity.class));
				return true;
            case R.id.nav_member_register:
                startActivity(new Intent(this, MemberRegisterActivity.class));
                return true;

            default:
                Toast.makeText(getApplicationContext(), "Error!! Error!!!", Toast.LENGTH_SHORT).show();
                return true;
        }

    }

    //로그인 타입이 소비자 일때 실행
    private boolean doNavigationMenuConsumer(MenuItem item){
        switch(item.getItemId()){
            // 메인 메뉴아이템
            case R.id.nav_Home:
                // fragment만 사용
                /*setTitle("메인입니다");
                MainFragment mainFragment = new MainFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.relativeLayout_for_frame, mainFragment)
                        .commit();*/
                startActivity(new Intent(this, MainActivity.class));
                return true;
			/*case R.id.nav_member_login:
				startActivity(new Intent(this, MemberLoginActivity.class));
				return true;*/
			// 나의 프로필 메뉴아이템
            case R.id.nav_myPage_Consumer:
                // fragment만 사용
                setTitle("나의 프로필임");
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativeLayout_for_frame, new MemberMyPageFragment()).commit();
                // activity에서 fragment 호출해서 사용
                //startActivity(new Intent(this, MemberMyPageActivity.class));
				return true;
            // 나의 의뢰서 메뉴아이템
            case R.id.nav_requestMyList:
                startActivity(new Intent(this, RequestMyListActivity.class));
                return true;
            // 참가요청내역 메뉴아이템
            case R.id.nav_requestJoin:
                startActivity(new Intent(this, RequestJoinActivity.class));
                return true;
            // 구매이력 메뉴아이템
            case R.id.nav_dealConsumer:
                startActivity(new Intent(this, DealConsumerActivity.class));
                return true;

            default:
                Toast.makeText(getApplicationContext(), "Error!! Error!!!", Toast.LENGTH_SHORT).show();
                return true;
        }

    }

    //로그인 타입이 제작자 일때 실행
    private boolean doNavigationMenuMaker(MenuItem item){
        switch(item.getItemId()){
            case R.id.nav_Home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.nav_myPage_Maker:
                setTitle("나의 프로필");
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativeLayout_for_frame, new MemberMyPageMakerFragment()).commit();
                return true;
            case R.id.nav_request_Search:
                setTitle("의뢰서 검색");
                getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, new RequestSearchFragment()).commit();
                return true;
            case R.id.nav_rquest_receivedList:
                setTitle("받은 의뢰서");
                getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, new RequestReceivedFragment()).commit();
                return true;
            case R.id.nav_product_myList:
                setTitle("상품 관리");
                getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, new ProductMyListFragment()).commit();
                return true;
//            case R.id.nav_portfolio_myList:
//                setTitle("의뢰서 관리");
//                startActivity(new Intent(this, PortfolioMyListActivity.class));
//                return true;
            case R.id.nav_requestList:
                //의뢰서 요청내역
                startActivity(new Intent(this, RequestMyListActivity.class));
                return true;
            case R.id.nav_dealMaker:
                setTitle("거래 이력");
                getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, new DealMakerFragment()).commit();
                return true;

            default:
                Toast.makeText(getApplicationContext(), "Error!! Error!!!", Toast.LENGTH_SHORT).show();
                return true;
        }

    }




}
