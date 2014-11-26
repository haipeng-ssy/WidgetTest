package com.example.widgettest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;





public class LruCacheImagerDownloader {
	/**
	 * ����Image���࣬���洢Image�Ĵ�С����LruCache�趨��ֵ��ϵͳ�Զ��ͷ��ڴ� if Image's cache >
	 * LruCache's size , system will remove cache auto;
	 */
	private LruCache<String, Bitmap> mMemoryCache;
 	public LruCacheImagerDownloader(Context context) {
		// ��ȡϵͳ�����ÿ��Ӧ�ó��������ڴ棬ÿ��Ӧ��ϵͳ����32M
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int mCacheSize = maxMemory / 4;
		int m;
		// ��LruCache����1/8 4M
		mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {

			// ������д�˷�����������Bitmap�Ĵ�С
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
