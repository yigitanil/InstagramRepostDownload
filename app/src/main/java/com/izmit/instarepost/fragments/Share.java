package com.izmit.instarepost.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.izmit.instarepost.R;
import com.izmit.instarepost.model.Instagram;
import com.izmit.instarepost.utils.Download;
import com.izmit.instarepost.utils.ImageOperation;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Share extends Fragment {


    public Share() {
        // Required empty public constructor
    }
    

    private Bitmap bitmapForRepost=null;
    private Activity activity;
    private ImageView image;
    private ImageView rt0;
    private ImageView rt1;
    private ImageView rt2;
    private ImageView rt3;
    private ImageView rt4;
    private Button repost;
    private LinearLayout linearLayout;
    private ImageOperation imageOperation;
    private EditText ylink;
    private ImageView isearch;
    private ImageView paste;
    private Bitmap cop;
    private InputMethodManager inputManager;
    private ProgressDialog pd;
    private LinearLayout mediaLayout;
    private  View view;

    private String url=null;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



         view = inflater.inflate(R.layout.activity_main, container, false);
        activity=getActivity();
        pd = new ProgressDialog(activity);

        ylink = (EditText) view.findViewById(R.id.ylink);
        isearch=(ImageView)view.findViewById(R.id.isearch);
        paste=(ImageView)view.findViewById(R.id.ipaste);

        mediaLayout=(LinearLayout)view.findViewById(R.id.mediaLayout);
        mediaLayout.setVisibility(View.INVISIBLE);
        inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        cop = BitmapFactory.decodeResource(getResources(),
                R.drawable.abs__ic_clear_search_api_holo_light);
        final ClipboardManager cm=(ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);

        if (cm.hasPrimaryClip()) {
            android.content.ClipDescription description = cm.getPrimaryClipDescription();
            android.content.ClipData data = cm.getPrimaryClip();
            if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                CharSequence text = data.getItemAt(0).getText();
                if (text.toString().startsWith("http") && text.toString().contains("instagram") && text.toString().length()>39){

                    AsyncTask<String, Instagram, Instagram> getInstagramData = new GetInstragramData();

                    getInstagramData.execute(text.toString().substring(0,40));
                }
            }
        }


        paste.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cm.hasPrimaryClip()) {
                    android.content.ClipDescription description = cm.getPrimaryClipDescription();
                    android.content.ClipData data = cm.getPrimaryClip();
                    if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                        ylink.setText(String.valueOf(data.getItemAt(0).getText()));
                }else{
                    Toast.makeText(activity, "No value copied", Toast.LENGTH_LONG).show();
                }

            }
        });
        ylink.setOnTouchListener(new android.view.View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent motionevent) {
                int i = ylink.getWidth()
                        - (cop.getWidth() + ylink.getPaddingRight());
                int j = ylink.getWidth() - ylink.getPaddingRight();
                int k = (int) motionevent.getX();
                if (k >= i && k <= j) {
                    ylink.setText("");
                }
                return false;
            }
        });
        ylink.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    if (haveNetworkConnection()) {
                        String s = ylink.getText().toString();
                        if (s.isEmpty()) {
                            Toast.makeText(activity, "Enter a query!", Toast.LENGTH_LONG).show();
                        } else if (!(s.startsWith("http") && s.contains("instagram"))){
                            Toast.makeText(activity, "Not a valid url!", Toast.LENGTH_LONG).show();
                        } else {
                            AsyncTask<String, Instagram, Instagram> getInstagramData = new GetInstragramData();

                            getInstagramData.execute(s);

                        }
                    } else {
                        Toast.makeText(activity, "Check your internet connection", Toast.LENGTH_LONG).show();
                    }
                    if(activity.getCurrentFocus()!=null){
                        inputManager.hideSoftInputFromWindow( activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
                }
                return false;
            }
        });

        isearch.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (haveNetworkConnection()) {
                    String s = ylink.getText().toString();
                    if (s.isEmpty()) {
                        Toast.makeText(activity, "Enter a query!", Toast.LENGTH_LONG).show();
                    } else if (!(s.startsWith("http") && s.contains("instagram"))){
                        Toast.makeText(activity, "Not a valid url!", Toast.LENGTH_LONG).show();
                    } else {
                        AsyncTask<String, Instagram, Instagram> getInstagramData = new GetInstragramData();

                        getInstagramData.execute(s);


                    }
                } else {
                    Toast.makeText(activity, "Check your internet connection", Toast.LENGTH_LONG).show();
                }
                if(activity.getCurrentFocus()!=null){
                    inputManager.hideSoftInputFromWindow( activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        if(activity.getCurrentFocus()!=null){
            inputManager.hideSoftInputFromWindow( activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }

        if (url!=null ){
            AsyncTask<String, Instagram, Instagram> getInstagramData = new GetInstragramData();

            getInstagramData.execute(url);
        }

        return view;


    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    class GetInstragramData extends AsyncTask<String, Instagram, Instagram> {
        @Override
        protected Instagram doInBackground(String... params) {
            Connection.Response res = null;
            try {
                res = Jsoup.connect(params[0]).userAgent("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")
                        .method(Connection.Method.GET).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Document doc = null;
            try {
                doc = res.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Instagram instagram = new Instagram();
            Elements img = doc.getElementsByTag("script ");

            for (Element e : img) {
                if (e.html().contains("window._sharedData")) {
                    String substring = e.html().substring(e.html().indexOf("{"));
                    String json = substring.substring(0, substring.length() - 1);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject media = jsonObject.getJSONObject("entry_data").getJSONArray("PostPage").getJSONObject(0).getJSONObject("media");
                        if (media.getBoolean("is_video")) {
                            instagram.setFormat("video");
                            instagram.setUrl(media.getString("video_url"));

                        } else {
                            instagram.setFormat("image");
                            instagram.setUrl(media.getString("display_src"));

                        }
                        instagram.setThumb(media.getString("display_src"));
                        instagram.setUserThumb(media.getJSONObject("owner").getString("profile_pic_url"));
                        instagram.setUsername(media.getJSONObject("owner").getString("username"));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    break;
                }
            }

            return instagram;
        }


        @Override
        protected void onPreExecute() {

            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();

        }

        @Override
        protected void onPostExecute(final Instagram instagram) {

            if (pd != null) {
                pd.dismiss();
            }


            if (instagram != null) {


            mediaLayout.setVisibility(View.VISIBLE);
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


        }
        }
    }

    private String repost(Bitmap image,String type){

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), image, "Title", null);
        Uri uri= Uri.parse(path);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(type+"/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
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
