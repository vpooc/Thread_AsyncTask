package com.vpooc.thread_asynctask;

import android.app.ActivityManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URI;

/**为主线程减轻负的多线程方案有哪些呢？这些方案分别适合在什么场景下使用？
 * Android系统为我们提供了若干组工具类来帮助解决这个问题。
 *AsyncTask:     为UI线程与工作线程之间进行快速的切换提供一种简单便捷的机制。适用于当下立即需要启动，
 *               但是异步执行的生命周期短暂的使用场景。
 *HandlerThread: 为某些回调方法或者等待某些任务的执行设置一个专属的线程，并提供线程任务的调度机制。
 *ThreadPool:    把任务分解成不同的单元，分发到各个不同的线程上，进行同时并发处理。
 *IntentService: 适合于执行由UI触发的后台Service任务，并可以把后台任务执行的情况通过一定的机制反馈给UI。
 *
 * ******************************************************************
 * 此为AsyncTask异步加载图片，及优化内存。
 * 1、图片以解码方式改变内存在占用 option.inSampleSize = 2;
 * 2、图片缩图改变内存在占用 Bitmap.Config.RGB_565
 *
 * 3、非静态内部类的静态实例容易造成内存泄漏
 * */
public class MainActivity extends AppCompatActivity {
    private String Tag = "MainActivity";
    private ImageView imgView;
    private ImageView imgView1;
    String imagePath1 = "/mnt/sdcard/DCIM/Camera/IMG_20160730_222344.jpg";
    String imagePath2 = "/mnt/sdcard/DCIM/Camera/IMG_20160730_222335.jpg";
    boolean isSleep = true;
//    static LoadImageAsyctask asyctask;
//    static LoadImageAsyctask asyctask1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView) findViewById(R.id.img);
        imgView1 = (ImageView) findViewById(R.id.img1);

        LoadImageAsyctask asyctask = new LoadImageAsyctask();
//        asyctask = new LoadImageAsyctask();
        asyctask.setImageView(imgView);
        asyctask.execute(imagePath1);

        isSleep = false;
//        LoadImageAsyctask asyctask1 = new LoadImageAsyctask();
//        asyctask1 = new LoadImageAsyctask();
//        asyctask1.setImageView(imgView1);
//        asyctask1.execute(imagePath2);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("mainactivity", "onDestroy");
    }

    private static class LoadImageAsyctask extends AsyncTask<String, Integer, Bitmap> {
        private ImageView iv;
        protected void setImageView(ImageView iv) {
            this.iv = iv;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.i("doInBackground", "");
            Bitmap bm = null;
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inSampleSize = 2;
            option.inPreferredConfig = Bitmap.Config.RGB_565;
            for (int i = 0; i < params.length; i++) {
                bm = BitmapFactory.decodeFile(params[i], option);
                Log.i("doInBackground", params[i]);
                return bm;
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.i("Mainactivity", "进度值：" + values.toString());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Log.i("Mainactivity", "设置ImageView");
            iv.setImageBitmap(bitmap);
        }
    }


    private class ImageLUR extends LruCache{

        public ImageLUR(int maxSize) {
            super(maxSize);
        }

        @Override
        public void resize(int maxSize) {
            super.resize(maxSize);
        }

        @Override
        protected Object create(Object key) {
            return super.create(key);
        }

        @Override
        protected void entryRemoved(boolean evicted, Object key, Object oldValue, Object newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
        }

        @Override
        public void trimToSize(int maxSize) {
            super.trimToSize(maxSize);
        }
    }
}
