package com.example.widgettest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

public class GridViewService extends RemoteViewsService {

	static ArrayList<HashMap<String, Object>> array;
	LruCacheImagerDownloader idl;
	FileOutputStream fileOutputStream;

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		// TODO Auto-generated method stub

		return new GridRemoteViewsFactory(this, intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onBind(intent);
	}

	public class GridRemoteViewsFactory implements RemoteViewsFactory {

		public Context context;
		public int appWidgetId;
		public final static String IMAGE_ITEM = "image_item";
		public final static String TEXT_ITEM = "text_item";

		private final String[] arrText = new String[] {
				"http://www.baidu.com/img/bdlogo.png",
				"http://www.sogou.com/images/logo/new/sogou.png",
				"http://www.baidu.com/img/bdlogo.png",
				"http://www.baidu.com/img/bdlogo.png" };
		private int[] arrImages = new int[] { R.drawable.p1, R.drawable.p2,
				R.drawable.p3, R.drawable.p4 };

		public GridRemoteViewsFactory(Context context, Intent intent) {

			// TODO Auto-generated constructor stub
			this.context = context;
			appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		RemoteViews rv;

		@Override
		public RemoteViews getViewAt(int position) {
			// TODO Auto-generated method stub
			HashMap<String, Object> map;
			map = (HashMap<String, Object>) array.get(position);

			rv = new RemoteViews(context.getPackageName(),
					R.layout.item_appwidget);
			rv.setTextViewText(R.id.tv_appw, (String) map.get(TEXT_ITEM));
			rv.setImageViewBitmap(R.id.img_appw, (Bitmap) (map.get(IMAGE_ITEM)));
			// GridView单元素的单击
			Intent intent = new Intent();
			intent.putExtra(WidgetShow.OnButtonClick, position);
			rv.setOnClickFillInIntent(R.id.item_layout, intent);

			return rv;
		}

		public void initData() {
			array = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < arrText.length; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						arrImages[i]);
				map.put(IMAGE_ITEM, bitmap);
				map.put(TEXT_ITEM, arrText[i]);
				array.add(map);
			}

		}

		@Override
		public void onCreate() {
			// TODO Auto-generated method stub
			initData();
			// Toast.makeText(context, ""+array.size(),
			// Toast.LENGTH_SHORT).show();
			new LoaderImage().execute(arrText);
			idl = new LruCacheImagerDownloader(context);

		}

		@Override
		public void onDataSetChanged() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			array.clear();

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return array.size();
		}

		@Override
		public RemoteViews getLoadingView() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

	}

	public class LoaderImage extends AsyncTask<String, Integer, Bitmap> {

		protected Bitmap doInBackground(String... params) { // TODO
			Bitmap bitmap = null;
			HashMap<String, Object> map;
			for (int i = 0; i < params.length; i++) {
				try {

					if ((idl.getBitmap(params[i])) != null) {
						bitmap = idl.getBitmap(params[i]);
					} else {
						int count = params.length;
						HttpClient httpClient = new DefaultHttpClient();
						HttpGet get = new HttpGet(params[i]);
						HttpResponse response;

						response = httpClient.execute(get);

						if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							InputStream is = response.getEntity().getContent();
							
							bitmap = BitmapFactory.decodeStream(is);
							
							fileOutputStream = openFileOutput(getIndexlast(params[i]),
									Context.MODE_PRIVATE);
							
							bitmap.compress(CompressFormat.PNG, 50, fileOutputStream);
							fileOutputStream.flush();
							fileOutputStream.close();
//							int ch = 0;
//							fileOutputStream = openFileOutput(params[i],
//									Context.MODE_PRIVATE);
//							if((ch=is.read())!=-1){
//							   	fileOutputStream.write(ch);
//							};
//							
							final InputStream ist = is;
							final String strS = params[i];
//						new Thread(new Runnable() {
//							
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								int ch = 0;
//								
//								try {
//									fileOutputStream = openFileOutput(strS,
//											Context.MODE_PRIVATE);
//									if((ch=ist.read())!=-1){
//									   	fileOutputStream.write(ch);
//									}
//									fileOutputStream.close();
//								} catch (Exception e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								};
//							}
//						}).start();
						}
						// lruCache.put(params[i], bitmap);
						idl.addBitmapToCache(params[i], bitmap);
						idl.getBitmap(params[i]);
						
						// fileOutputStream.write(bitmap);
						map = new HashMap<String, Object>();
						map.put(GridRemoteViewsFactory.IMAGE_ITEM, bitmap);
						map.put(GridRemoteViewsFactory.TEXT_ITEM, params[i]);
						// array.clear();
						array.add(map);
                        
						Intent intent = new Intent();
						intent.setAction(WidgetShow.Refresh);
						sendBroadcast(intent);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			return bitmap;

		}

		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}

		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
	}
	
	public String getIndexlast(String str){
		String[] s=str.split("/");
		String   needStr = null;
		for(int i=0;i<s.length;i++)
		{
			if(i==s.length-1)
			{
				needStr = s[s.length-1];
			}
		}
		return needStr;
	}

}
