package com.izmit.instarepost.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.izmit.instarepost.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by t41203 on 14.02.2016.
 */
public class DownloadsGridAdapter extends RecyclerView.Adapter<DownloadsGridAdapter.MyViewHolder>  {


    private Activity activity;
    private String saveDir;
    private File [] files;


   public DownloadsGridAdapter setActivity(Activity activity) {
       this.activity=activity;
        return this;
    }

    public DownloadsGridAdapter setFiles(File [] files) {
       this.files=files;
        return this;
    }



    private void getFiles(){
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+
                activity.getResources().getString(R.string.app_name)+"/");

        files=file.listFiles();
        if(files==null){
            files=new File[1];
            files[0]=new File("     ");
        }
        Arrays.sort(files, new Comparator<File>(){

            @SuppressLint("NewApi")
            public int compare(File f1, File f2) {
                return Long.compare(f2.lastModified(), f1.lastModified());

            }
        });
    }

    public void refresh(){

        getFiles();
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(com.izmit.instarepost.R.layout.grid_element, parent, false);


        return new MyViewHolder(itemView,parent);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        File f=files[position];
        Bitmap thumbnail=getThumbnail(f);
        if(thumbnail!=null){
            holder.feedImage.setImageBitmap(thumbnail);
        }else{
            thumbnail=getVideoThumbnail(R.drawable.vidicon);
            holder.feedImage.setImageBitmap(thumbnail);
        }

        holder.feedText.setText(f.getName());
        String ss=f.getAbsolutePath();
        final String  s=ss;

        holder.feedImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Uri vidUri;
                String	v_url=s;
                File f=new File(v_url);
                vidUri = Uri.fromFile(f);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                if(f.getName().substring(f.getName().length()-3,f.getName().length()).equals("jpg")){
                    intent.setDataAndType(vidUri, "image/*");
                }
                if(f.getName().substring(f.getName().length()-3,f.getName().length()).equals("mp4")){
                    intent.setDataAndType(vidUri, "video/*");
                }
                activity.startActivity(intent);

            }
        });
    }

    public Bitmap getThumbnail(File file) {
        byte[] imageData = null;
        Bitmap imageBitmap = null;
        try
        {



            FileInputStream fis = new FileInputStream(file); //file is the path to the image-to-be-thumbnailed.
            imageBitmap = BitmapFactory.decodeStream(fis);




        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return imageBitmap;
    }
    public Bitmap getVideoThumbnail(int id){
        byte[] imageData = null;
        Bitmap imageBitmap = null;
        try
        {


            imageBitmap = BitmapFactory.decodeResource(activity.getResources(), id);




        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return imageBitmap;
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView feedImage;
        public TextView feedText;


        public MyViewHolder(View view, final ViewGroup parent) {
            super(view);
            feedImage = (ImageView) view.findViewById(com.izmit.instarepost.R.id.feedImage);
            feedText = (TextView) view.findViewById(com.izmit.instarepost.R.id.feedText);

        }



    }
}
