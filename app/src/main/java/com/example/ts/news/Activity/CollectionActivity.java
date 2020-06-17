package com.example.ts.news.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ts.news.Adapter.NewsAdapter;
import com.example.ts.news.Bean.News;
import com.example.ts.news.R;
import com.example.ts.news.Utils.HttpUtils;
import com.example.ts.news.Utils.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CollectionActivity extends AppCompatActivity implements NewsAdapter.CallBack {

    private List<News> newsList = new ArrayList<>();

    private ListView collection;

    private NewsAdapter adapter;

    private MyDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        helper = new MyDatabaseHelper(this, "UserDB.db", null, 1);
        initView();
        initNews();

        collection.setAdapter(adapter);

        collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = newsList.get(i).getNews_url();
                Intent intent = new Intent(CollectionActivity.this, ShowNewsActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }


    private void initNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from Collection_News", null);
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            //遍历Cursor对象，取出数据并打印
                            String news_url = cursor.getString(cursor.getColumnIndex("news_url"));
                            String news_title = cursor.getString(cursor.getColumnIndex("news_title"));
                            String news_date = cursor.getString(cursor.getColumnIndex("news_date"));
                            String news_author = cursor.getString(cursor.getColumnIndex("news_author"));
                            String news_picurl = cursor.getString(cursor.getColumnIndex("news_picurl"));
                            Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(news_picurl);
                            News news = new News(bitmap, news_title, news_url, news_picurl, news_date, news_author);
                            newsList.add(news);

                        } while (cursor.moveToNext());
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "收藏夹为空！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

                cursor.close();
                db.close();
            }
        }).start();




       /* SharedPreferences sp = getSharedPreferences("collection", MODE_PRIVATE);
        HashMap<String, String> map = (HashMap<String, String>) sp.getAll();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, String> data = iter.next();
            //取出标题
            String title = data.getKey();
            //取出网址
            String url = data.getValue();
            //往数据源中填充数据
            News news = new News(BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_launcher), title, url);
            newsList.add(news);
        }*/
    }

    private void initView() {
        collection = (ListView) findViewById(R.id.listview_collection);
        adapter = new NewsAdapter(this, R.layout.news_item, newsList, this);
    }

    @Override
    public void click(View view) {
        int position = Integer.parseInt(view.getTag().toString());
        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL("delete from Collection_News where news_title=?",
                new String[]{newsList.get(position).getNews_title()});
        db.close();
        newsList.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "该新闻已被移除收藏夹！", Toast.LENGTH_SHORT).show();

        /*
        int position = Integer.parseInt(view.getTag().toString());
        SharedPreferences sp = getSharedPreferences("collection", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(newsList.get(position).getNews_title());
        editor.apply();
        newsList.remove(position);
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "该新闻已被移除收藏夹！", Toast.LENGTH_SHORT).show();
        */
    }
}
