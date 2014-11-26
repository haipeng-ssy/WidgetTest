package com.example.widgettest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class WidgetShow extends AppWidgetProvider {

	public static final String Refresh = "com.widget.refresh";
	public static final String singleAction = "com.widget.single.action";
	public static final String OnButtonClick = "OnButtonClick";

	static ArrayList<HashMap<String, Object>> array;
	
	AppWidgetManager appManager;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName componentName = new ComponentName(context,WidgetShow.class);
		if (intent.getAction().equals(singleAction)) {
			int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			int viewIndex   = intent.getIntExtra(OnButtonClick, 0);
			Toast.makeText(context, viewIndex+"", Toast.LENGTH_LONG).show(); 
		}
		if(intent.getAction().equals(Refresh))
		{
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.gv_appw);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		for (int appWidgetId : appWidgetIds) {
			RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.appwidget);
            
			// setIntentAdapter
			Intent intent = new Intent(context,GridViewService.class);
			rv.setRemoteAdapter(R.id.gv_appw, intent);
			
			
			
			//通过intent 启动 Broadcast,GridView中单个元素的点击，启动broadcast,
			Intent intentSigleAction = new Intent();
            intentSigleAction.setAction(singleAction);
            intentSigleAction.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentSigleAction, PendingIntent.FLAG_UPDATE_CURRENT);
      
            rv.setPendingIntentTemplate(R.id.gv_appw, pendingIntent);
            appManager = appWidgetManager;
            appWidgetManager.updateAppWidget(appWidgetId, rv);
			
			
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	
}
