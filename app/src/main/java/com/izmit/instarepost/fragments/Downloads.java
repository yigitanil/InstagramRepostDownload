package com.izmit.instarepost.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.izmit.instarepost.R;
import com.izmit.instarepost.adapters.DownloadsGridAdapter;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class Downloads extends Fragment {


    public Downloads() {
        // Required empty public constructor
    }

    private DownloadsGridAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.downloads, container, false);


        RecyclerView tagGrid = (RecyclerView) view.findViewById(R.id.downloadGrid);
        tagGrid.setLayoutManager(new GridLayoutManager(getContext(), 3));


        File file = new File(Environment.getDataDirectory().getParent() + "/" +
                getActivity().getResources().getString(R.string.app_name) + "/");

        File[] files = file.listFiles();
        if (files == null) {
            files = new File[1];
            files[0] = new File("     ");
        }
        Arrays.sort(files, new Comparator<File>() {

            @SuppressLint("NewApi")
            public int compare(File f1, File f2) {
                return Long.compare(f2.lastModified(), f1.lastModified());

            }
        });
        adapter = new DownloadsGridAdapter()
                .setFiles(files)
                .setActivity(getActivity());
        tagGrid.setAdapter(adapter);


        return view;
    }

    public void refresh() {
        if (adapter != null) {
            adapter.refresh();
        }
    }


}
