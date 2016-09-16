package com.izmit.instarepost.fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.izmit.instarepost.Home;
import com.izmit.instarepost.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private Activity activity;

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        activity = getActivity();
        sharedPreferences= activity.getSharedPreferences("instagramCookies", 0);
        editor=sharedPreferences.edit();


        // Login to instagram with a webview and get cookies

        WebView webView=(WebView)view.findViewById(R.id.web);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl("https://www.instagram.com/accounts/login");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, final String url) {
                if (url.equals("https://www.instagram.com/")){
                    String cookies = CookieManager.getInstance().getCookie(url);

                    editor.putString("instagramCookies", cookies);
                    editor.commit();
                    Intent i=new Intent();
                    i.setClass(activity, Home.class);
                    activity.startActivity(i);


                }
            }
        });


        return view;
    }

}
