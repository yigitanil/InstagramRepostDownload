package com.izmit.instarepost.adapters;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.izmit.instarepost.R;
import com.izmit.instarepost.model.Instagram;
import com.izmit.instarepost.utils.Download;
import com.izmit.instarepost.utils.ImageOperation;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by t41203 on 14.02.2016.
 */
public class InstagramGridAdapter extends RecyclerView.Adapter<InstagramGridAdapter.MyViewHolder>  {

    private List<Instagram> instagramList;

    private Activity activity;

    public InstagramGridAdapter setInstagramList(List<Instagram> instagramList) {
        this.instagramList = instagramList;
        return this;
    }
   public InstagramGridAdapter setActivity(Activity activity) {
       this.activity=activity;
        return this;
    }

    public void refreshData(List<Instagram> list){
        this.instagramList.addAll(list);
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
        Instagram instagram = instagramList.get(position);
        holder.instagram=instagram;
        Ion.with(holder.feedImage)
                .load(instagram.getUrl());
    }

    @Override
    public int getItemCount() {
        return instagramList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView feedImage;
       private Instagram instagram;
       private Bitmap bitmapForRepost=null;

       private ImageView image;
       private ImageView rt0;
       private ImageView rt1;
       private ImageView rt2;
       private ImageView rt3;
       private ImageView rt4;
       private Button repost;
       private LinearLayout linearLayout;
       private ImageOperation imageOperation;


        public MyViewHolder(View view, final ViewGroup parent) {
            super(view);
            feedImage = (ImageView) view.findViewById(com.izmit.instarepost.R.id.feedImage);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                    LayoutInflater layoutInflater=activity.getLayoutInflater();
                    View view = layoutInflater.inflate(R.layout.item_dialog, parent, false);


                    image = (ImageView) view.findViewById(R.id.image);
                    rt0 = (ImageView) view.findViewById(R.id.rt0);
                    rt1 = (ImageView) view.findViewById(R.id.rt1);
                    rt2 = (ImageView) view.findViewById(R.id.rt2);
                    rt3 = (ImageView) view.findViewById(R.id.rt3);
                    rt4 = (ImageView) view.findViewById(R.id.rt4);

                    repost = (Button) view.findViewById(R.id.repost);
                    linearLayout = (LinearLayout) view.findViewById(R.id.rtLayout);
                    Button downloadButton = (Button) view.findViewById(R.id.download);


                    downloadButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Download download = new Download(activity);
                            Random random = new Random();
                            if (instagram.getFormat().equals("image")) {
                                download.download(instagram.getUrl(), instagram.getUsername() + "_" + random.nextInt(10000) + ".jpg");
                            } else {
                                download.download(instagram.getUrl(), instagram.getUsername() + "_" + random.nextInt(10000) + ".mp4");
                            }


                        }
                    });


                    if (!instagram.getFormat().equals("image")) {
                        repost.setVisibility(View.INVISIBLE);
                        repost.setClickable(false);
                        linearLayout.setVisibility(View.INVISIBLE);
                        rt0.setClickable(false);
                        rt1.setClickable(false);
                        rt2.setClickable(false);
                        rt3.setClickable(false);
                        rt4.setClickable(false);
                    }


                    final Bitmap userThumb;
                    final Bitmap photo;
                    imageOperation = new ImageOperation().setActivity(activity);


                    try {
                        userThumb = Ion.with(activity).load(instagram.getUserThumb()).asBitmap().get();
                        if (instagram.getFormat().equals("image")) {
                            photo = Ion.with(activity).load(instagram.getUrl()).asBitmap().get();
                        } else {
                            photo = Ion.with(activity).load(instagram.getThumb()).asBitmap().get();
                        }

                        bitmapForRepost = photo.copy(Bitmap.Config.RGB_565, true);

                        rt0.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bitmapForRepost = photo.copy(Bitmap.Config.RGB_565, true);
                                image.setImageBitmap(bitmapForRepost);
                            }
                        });
                        rt1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bitmapForRepost = imageOperation.createBitmapForRepost(photo, instagram.getUsername(), userThumb, 1);
                                image.setImageBitmap(bitmapForRepost);
                            }
                        });
                        rt2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bitmapForRepost = imageOperation.createBitmapForRepost(photo, instagram.getUsername(), userThumb, 2);
                                image.setImageBitmap(bitmapForRepost);
                            }
                        });
                        rt3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bitmapForRepost = imageOperation.createBitmapForRepost(photo, instagram.getUsername(), userThumb, 3);
                                image.setImageBitmap(bitmapForRepost);
                            }
                        });
                        rt4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bitmapForRepost = imageOperation.createBitmapForRepost(photo, instagram.getUsername(), userThumb, 4);
                                image.setImageBitmap(bitmapForRepost);
                            }
                        });

                        image.setImageBitmap(photo);


                        repost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String res = repost(bitmapForRepost, "image");
                                if (res.equals("noInstagramApp")) {
                                    Toast.makeText(activity, activity.getResources().getString(R.string.noInstagramAppWarning), Toast.LENGTH_LONG).show();
                                }


                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


                    builder.setView(view);

                    builder.show();
                }
            });
        }


        private String repost(Bitmap image,String type){

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), image, "Title", null);
            Uri uri= Uri.parse(path);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType(type+"/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "@InstaRepost #repost @");
            PackageManager pm = activity.getPackageManager();

            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);

            int instagramAppCheck=0;
            for (final ResolveInfo app : activityList)
            {
                if ("com.instagram.android.activity.ShareHandlerActivity".equals(app.activityInfo.name))
                { instagramAppCheck=1;
                    final ActivityInfo activityInfo = app.activityInfo;
                    ComponentName name;
                    if(type.equals("image")){
                        name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                    }else{
                        name = new ComponentName("com.instagram.android", "com.instagram.android.activity.ShareHandlerActivity");
                    }
                    shareIntent.setComponent(name);

                    activity.startActivity(shareIntent);

                    break;
                }
            }

            if(instagramAppCheck==0){
                return "noInstagramApp";
            }else{
                return "";
            }
        }
    }
}
