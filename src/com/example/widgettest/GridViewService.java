package com.example.widgettest;


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
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class GridViewService extends RemoteViewsService {

	static ArrayList<HashMap<String, Object>> array;
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
		public String IMAGE_ITEM = "image_item";
		public String TEXT_ITEM = "text_item";
		ArrayList<HashMap<String, Object>> data;

		
		private final String[] arrText = new String[] {
				"http://www.baidu.com/img/bdlogo.png",
				"http://www.baidu.com/img/bdlogo.png",
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
			map = (HashMap<String, Object>) data.get(position);

			rv = new RemoteViews(context.getPackageName(),
					R.layout.item_appwidget);
			rv.setTextViewText(R.id.tv_appw, (String) map.get(TEXT_ITEM));
			
			// GridView单元素的单击
			Intent intent = new Intent();
			intent.putExtra(WidgetShow.OnButtonClick, position);
			rv.setOnClickFillInIntent(R.id.item_layout, intent);

			return rv;
		}

		

		public void initData() {
			data = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < arrText.length; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(IMAGE_ITEM, arrImages[i]);
				map.put(TEXT_ITEM, arrText[i]);
				data.add(map);
			}

		}

		@Override
		public void onCreate() {
			// TODO Auto-generated method stub
			initData();
			Intent intent = new Intent();
			intent.setAction(WidgetShow.Refresh);
			sendBroadcast(intent);
		}

		@Override
		public void onDataSetChanged() {
			// TODO Auto-generated method stub
            
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			data.clear();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
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
			int count = params.length;
			boolean result;
			HttpClient httpClient = new DefaultHttpClient();
			/* String uri = arrText[0]; */
			HttpGet get = new HttpGet(params[0]);
			HttpResponse response;
			try {
				HashMap<String, Object> map;
				response = httpClient.execute(get);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

					InputStream is = response.getEntity().getContent();
					bitmap = BitmapFactory.decodeStream(is);
				}
				map = new HashMap<String,Object>();
				map.put(params[0], bitmap);
				array = new ArrayList<HashMap<String, Object>>();
                array.add(map);
				Intent intent = new Intent();
			    intent.setAction(WidgetShow.Refresh);
			    sendBroadcast(intent);
			    
			    AppWidgetManager am;
			    am.notifyAppWidgetViewDataChanged(appWidgetId, viewId);
			} catch (ClientProtocolException e) {
				e.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block e.printStackTrace(); }
				// return
				// 1l;

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


}
