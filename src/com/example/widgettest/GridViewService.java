package com.example.widgettest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class GridViewService extends RemoteViewsService {

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

		private String[] arrText = new String[] {
				"http://c.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3ce0150fa279045d688d43f2015.jpg",
				"http://imgt7.bdstatic.com/it/u=2,724296170&fm=25&gp=0.jpg",
				"http://h.hiphotos.baidu.com/image/pic/item/d31b0ef41bd5ad6e2afd8cf183cb39dbb6fd3ca1.jpg", };
		private int[] arrImages = new int[] { R.drawable.p1, R.drawable.p2,
				R.drawable.p3 };

		public GridRemoteViewsFactory(Context context, Intent intent) {

			// TODO Auto-generated constructor stub
			this.context = context;
			appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);

		}

		@Override
		public RemoteViews getViewAt(int position) {
			// TODO Auto-generated method stub
			HashMap<String, Object> map;
			map = (HashMap<String, Object>) data.get(position);

			RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.item_appwidget);
			rv.setTextViewText(R.id.tv_appw, (String) map.get(TEXT_ITEM));
//			rv.setImageViewResource(R.id.img_appw, (int) map.get(IMAGE_ITEM));
			returnBitmap((String) map.get(TEXT_ITEM), rv);

		
			// GridView单元素的单击
			Intent intent = new Intent();
			intent.putExtra(WidgetShow.OnButtonClick, position);
			rv.setOnClickFillInIntent(R.id.item_layout, intent);
			return rv;
		}
		
		public void returnBitmap(final String uri,final RemoteViews rv){
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						HttpClient httpClient = new DefaultHttpClient();
						/*String uri = arrText[0];*/
						HttpGet get = new HttpGet(uri);
						HttpResponse response = httpClient.execute(get);
					    if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
					    {
					    	InputStream is = (InputStream) response.getEntity();
					    	byte[] bytes = new byte[1024];
					    	int count;
					    	ByteArrayOutputStream bOutputStream = new ByteArrayOutputStream();
					    	if((count = is.read(bytes))!=-1)
					    	{
					    		bOutputStream.write(bytes);
					    	}
					    	byte[] byteArray = bOutputStream.toByteArray();
					    	Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
					    	
					    	
					        rv.setImageViewBitmap(R.id.img_appw, bitmap);
					        
					    }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
				}
			});
	
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

}
