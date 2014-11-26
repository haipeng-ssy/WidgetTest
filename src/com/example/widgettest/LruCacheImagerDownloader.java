package com.example.widgettest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;





public class LruCacheImagerDownloader {
	/**
	 * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存 if Image's cache >
	 * LruCache's size , system will remove cache auto;
	 */
	private LruCache<String, Bitmap> mMemoryCache;
 	public LruCacheImagerDownloader(Context context) {
		// 获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int mCacheSize = maxMemory / 4;
		int m;
		// 给LruCache分配1/8 4M
		mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {

			// 必须重写此方法，来测量Bitmap的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}

		};

	}
	
	public void addBitmapToCache(String key,Bitmap bitmap){
		if(getBitmap(key)==null&&bitmap!=null)
		{
			mMemoryCache.put(key, bitmap);
		}
	}
	
	public Bitmap getBitmap(String key){
	
		return mMemoryCache.get(key);
	
	}

}
