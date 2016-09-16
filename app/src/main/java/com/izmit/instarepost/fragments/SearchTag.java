package com.izmit.instarepost.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.izmit.instarepost.Home;
import com.izmit.instarepost.R;
import com.izmit.instarepost.adapters.TagGridAdapter;
import com.izmit.instarepost.model.Instagram;
import com.izmit.instarepost.utils.InstagramApi;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchTag extends Fragment {


    private InputMethodManager inputManager;
    private Bitmap cop;
    private ImageView search;

    public SearchTag() {
        // Required empty public constructor
    }


    private EditText editText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search_tag, container, false);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        final List<Instagram> instagramList=new ArrayList<>();
        search=(ImageView)view.findViewById(R.id.search);
        editText=(EditText)view.findViewById(R.id.editTag);
        editText.setOnTouchListener(new android.view.View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent motionevent) {
                int i = editText.getWidth()
                        - (cop.getWidth() + editText.getPaddingRight());
                int j = editText.getWidth() - editText.getPaddingRight();
                int k = (int) motionevent.getX();
                if (k >= i && k <= j) {
                    editText.setText("");
                }
                return false;
            }
        });

        cop = BitmapFactory.decodeResource(getResources(),
                R.drawable.abs__ic_clear_search_api_holo_light);




        RecyclerView tagGrid=(RecyclerView)view.findViewById(R.id.tagGrid);
        tagGrid.setLayoutManager(new GridLayoutManager(getContext(), 3));


        final TagGridAdapter adapter = new TagGridAdapter()
                .setInstagramList(instagramList)
                .setActivity(getActivity());
        tagGrid.setAdapter(adapter);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    List<Instagram> feed = InstagramApi.getFeed("https://www.instagram.com/explore/tags/"+arg0+"/", Home.instagramCookies);
                    instagramList.clear();
                    instagramList.addAll(feed);
                    adapter.refreshData(feed);
                    if(getActivity().getCurrentFocus()!=null){
                        inputManager.hideSoftInputFromWindow( getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
                }
                return false;
            }
        });

        search.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText.getText().length()>0){
                    List<Instagram> feed = InstagramApi.searchTag("https://www.instagram.com/explore/tags/"+editText.getText()+"/", Home.instagramCookies);
                    instagramList.clear();
                    instagramList.addAll(feed);
                    adapter.refreshData(feed);
                }

                if(getActivity().getCurrentFocus()!=null){
                    inputManager.hideSoftInputFromWindow( getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });


        return view;
    }




}
