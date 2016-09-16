package com.izmit.instarepost.utils;

import android.os.AsyncTask;

import com.izmit.instarepost.model.Instagram;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class InstagramApi {


	// Gets the instagram data json
	public static String getJson(String url,String cookie){

		try {
			return new AsyncJson(cookie).execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}


	private static class AsyncJson extends AsyncTask<String, String, String>{

		Map<String ,String> cookieMap;

		public AsyncJson(String cookies) {
			if (cookies!=null){
				String[] temp=cookies.split(";");
				cookieMap=new HashMap<>();
				for (String ar1 : temp ){
					String[] temp1=ar1.split("=");
					if (temp1.length>1)
						cookieMap.put(temp1[0],temp1[1]);
				}
			}


		}




		// Parses isntagram.com html with user login cookies and get instagram data json.
		@Override
		protected String doInBackground(String... params) {

			String s="";

			try {
				Connection connection = Jsoup.connect(params[0])
						.userAgent("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");
				if (cookieMap!=null){
					connection.cookies(cookieMap);
				}
				Connection.Response res = connection.execute();
				Document doc = res.parse();

				Elements img = doc.getElementsByTag("script ");

				for (Element e : img) {
					if (e.html().contains("window._sharedData")) {
						String substring = e.html().substring(e.html().indexOf("{"));
						s=substring.substring(0, substring.length() - 1);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return s;
		}


	}


	
	public static List<Instagram> getFeed(String url,String cookie){
		String json = getJson(url,cookie);
		return parseJson(json);
	}


	//Parses instagram data jason
	private  static List<Instagram> parseJson(String json){
		List<Instagram> list = new ArrayList<>();
		JSONArray datas=null;
		try{
			JSONObject jsonObject=new JSONObject(json);
			datas=jsonObject.getJSONObject("entry_data").getJSONArray("FeedPage").getJSONObject(0) .getJSONObject("feed").getJSONObject("media").getJSONArray("nodes");


		}catch(Exception e){

			e.printStackTrace();
		}
		if(datas !=null){
			for(int i=0;i<datas.length();i++){
				try{

					Instagram instagram=new Instagram();
					String urls="";
					String username=datas.getJSONObject(i).getJSONObject("owner").getString("username");
					String type = "";
					boolean is_video = datas.getJSONObject(i).getBoolean("is_video");
					if (is_video){
						type="video";
					}else{
						type="image";
					}

					urls=datas.getJSONObject(i).getString("display_src");
					String userThumb = datas.getJSONObject(i).getJSONObject("owner").getString("profile_pic_url");
					instagram.setUserThumb(userThumb);
					instagram.setFormat(type);
					instagram.setUsername(username);
					instagram.setUrl(urls);
					list.add(instagram);
				}catch(Exception e){
					e.printStackTrace();
				}

			}
		}
		return list;

	}

	public static List<Instagram> searchTag(String url,String cookie){
		String json = getJson(url, cookie);
		return parseJsonTag(json);
	}

	private  static List<Instagram> parseJsonTag(String json){
		List<Instagram> list = new ArrayList<>();
		JSONArray datas=null;
		try{
			JSONObject jsonObject=new JSONObject(json);
			datas=jsonObject.getJSONObject("entry_data").getJSONArray("TagPage").getJSONObject(0) .getJSONObject("tag").getJSONObject("media").getJSONArray("nodes");


		}catch(Exception e){

			e.printStackTrace();
		}
		if(datas !=null){
			for(int i=0;i<datas.length();i++){
				try{

					Instagram instagram=new Instagram();
					String urls="";
					String id=datas.getJSONObject(i).getString("code");
					String type = "";
					boolean is_video = datas.getJSONObject(i).getBoolean("is_video");
					if (is_video){
						type="video";
					}else{
						type="image";
					}


					urls=datas.getJSONObject(i).getString("display_src");

					instagram.setId(id);
					instagram.setFormat(type);
					instagram.setUsername("");
					instagram.setUrl(urls);
					list.add(instagram);
				}catch(Exception e){
					e.printStackTrace();
				}

			}
		}
		return list;

	}

}
