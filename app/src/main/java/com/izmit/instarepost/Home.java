package com.izmit.instarepost;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.izmit.instarepost.fragments.*;


import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    public static String instagramCookies;
    private  Toolbar toolbar;
    private TabLayout mTabs;
    private ViewPager mPager;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        mTabs=(TabLayout)findViewById(R.id.tabs);
        mPager=(ViewPager)findViewById(R.id.pager);


        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        Intent intent = getIntent();
        String s = intent.getAction();
        String s1 = intent.getType();
        Share share = new Share();
        int shareInt=0;
        if ("android.intent.action.SEND".equals(s) && s1 != null && s1.startsWith("text/"))
        {
            String text=intent.getStringExtra("android.intent.extra.TEXT");
            for (String t:text.split(" ")){
                if (t.contains("http")){
                    share.setUrl( t.substring(0,40));
                    shareInt=1;
                }
            }

        }

        SharedPreferences preferencesCookie = getSharedPreferences("instagramCookies", 0);

        String instagramCookies = preferencesCookie.getString("instagramCookies","");

        //  Checks if users login.
        if (instagramCookies!=null && instagramCookies.length()>10){
            Home.instagramCookies=instagramCookies;
            adapter.addFragment(new Feed(),"");
        }else{
            adapter.addFragment(new Login(),"");
        }

        adapter.addFragment(share,"");
        adapter.addFragment(new SearchTag(),"");
        final Downloads downloads = new Downloads();
        adapter.addFragment(downloads,"");




        mPager.setAdapter(adapter);
        if (shareInt==1){
            mPager.setCurrentItem(1);
        }
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==3){
                    downloads.refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mTabs.setupWithViewPager(mPager);

        mTabs.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.home));
        mTabs.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.retweet));
        mTabs.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.search_icon));
        mTabs.getTabAt(3).setIcon(getResources().getDrawable(R.drawable.download));




        // Permissions for Andoid 6+
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

            }
        }

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_home, menu);
        if (Home.instagramCookies==null){
            menu.getItem(0).setTitle("Login");
        }else{
            menu.getItem(0).setTitle("Logout");
        }
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final Context context=this;


           if (Home.instagramCookies==null){
               Intent i=new Intent();
               i.setClass(context, Home.class);
               context.startActivity(i);
           }else{
               WebView web=new WebView(context);
               web.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");
               web.loadUrl("https://www.instagram.com/accounts/logout/");



               web.setWebViewClient(new WebViewClient() {

                   @Override
                   public void onPageFinished(WebView view, String url) {
                       Home.instagramCookies=null;
                       sharedPreferences= context.getSharedPreferences("instagramCookies", 0);
                       SharedPreferences.Editor editor = sharedPreferences.edit();
                       editor.remove("instagramCookies");
                       editor.commit();
                       Intent i=new Intent();
                       i.setClass(context, Home.class);
                       context.startActivity(i);
                   }

               });
           }






        return super.onOptionsItemSelected(item);
    }



}
