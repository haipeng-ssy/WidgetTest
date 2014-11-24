package com.example.widgettest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.GridView;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class WidgetShow extends AppWidgetProvider {

	public final String Refresh = "com.widget.refresh";
	public final String singleAction = "com.widget.single.action";
	public static final String OnButtonClick = "OnButtonClick";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		if (intent.getAction().equals(singleAction)) {
			int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			int viewIndex   = intent.getIntExtra(OnButtonClick, 0);
			Toast.makeText(context, viewIndex+"", Toast.LENGTH_LONG).show();

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
            
            appWidgetManager.updateAppWidget(appWidgetId, rv);
			
			
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}
