package com.izmit.instarepost.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.izmit.instarepost.Home;
import com.izmit.instarepost.R;
import com.izmit.instarepost.adapters.InstagramGridAdapter;
import com.izmit.instarepost.model.Instagram;
import com.izmit.instarepost.utils.InstagramApi;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Feed extends Fragment {


    public Feed() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        RecyclerView feedGrid=(RecyclerView)view.findViewById(R.id.feedGrid);

        final List<Instagram> instagramList= InstagramApi.getFeed("https://www.instagram.com/", Home.instagramCookies);


        feedGrid.setLayoutManager(new GridLayoutManager(getContext(), 3));


        final InstagramGridAdapter adapter = new InstagramGridAdapter()
                        .setInstagramList(instagramList)
                        .setActivity(getActivity());
        feedGrid.setAdapter(adapter);



        return view;


    }

}
