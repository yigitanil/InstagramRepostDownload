package com.izmit.instarepost.utils;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.izmit.instarepost.R;

public class Download {
	
	private Activity context;

	private String saveDir;
	private Long did;
	 private DownloadManager downloadManager=null;
    public Download(Activity activity) {
		this.context=activity;
		downloadManager=(DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
		context.registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		context.registerReceiver(onNotificationClick,
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
         saveDir=Environment.getDataDirectory().getParent()+"/"+
				 context.getResources().getString(R.string.app_name)+"/";
		
	
    }
 

	public void download(String url,String filename){
		Uri uri=Uri.parse(url);
		

	did=	downloadManager.enqueue(new DownloadManager.Request(uri)
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle(filename)
                .setDestinationInExternalPublicDir(saveDir, filename)
              );
	if(did!=null){
    	Toast.makeText(context, context.getResources().getString(R.string.downloadStarted), Toast.LENGTH_SHORT).show();

	}


	}
	
	  BroadcastReceiver onComplete=new BroadcastReceiver() {
		    public void onReceive(Context ctxt, Intent intent) {
		    	
		    	Toast.makeText(ctxt, context.getResources().getString(R.string.downloadCompleted), Toast.LENGTH_SHORT).show();

		    	context.unregisterReceiver(onComplete);
		    }
		  };
		  
		  BroadcastReceiver onNotificationClick=new BroadcastReceiver() {
		    public void onReceive(Context ctxt, Intent intent) {
		    	downloadManager.remove(did);
		    	Toast.makeText(ctxt, context.getResources().getString(R.string.downloadCancelled), Toast.LENGTH_LONG).show();
		    	context.unregisterReceiver(onNotificationClick);
		    }
		  };
}