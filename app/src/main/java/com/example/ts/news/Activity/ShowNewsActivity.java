package com.example.ts.news.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ts.news.R;
import com.example.ts.news.Utils.MyDatabaseHelper;

import java.util.UUID;

public class ShowNewsActivity extends AppCompatActivity {

    private WebView show_news;
    private ImageView collect_news;

    // 添加用户等待显示控件
    private ProgressDialog mDialog;

    private MyDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        initView();
        helper = new MyDatabaseHelper(this, "UserDB.db", null, 1);

        mDialog = new ProgressDialog(ShowNewsActivity.this);
        mDialog.setMessage("玩命加载ing");
        show_news.setWebViewClient(new WebViewClient() {
            //网页加载时的回调
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
            }

            //网页停止加载时的回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 如果没有显示，则显示
                if (mDialog.isShowing())
                    mDialog.dismiss();
            }
        });
        show_news.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        final String news_url = intent.getStringExtra("url");
        final String news_title = intent.getStringExtra("title");
        final String news_date = intent.getStringExtra("date");
        final String news_author = intent.getStringExtra("author");
        final String news_picurl = intent.getStringExtra("pic_url");
        show_news.loadUrl(news_url);

        collect_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                collect_news.setImageResource(R.drawable.favorite_selected);

                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues values = new ContentValues();
                //组装数据
                values.put("news_url", news_url);
                values.put("news_title", news_title);
                values.put("news_date", news_date);
                values.put("news_author", news_author);
                values.put("news_picurl", news_picurl);

                db.insert("Collection_News", null, values);

                db.close();


               /* SharedPreferences sp = getSharedPreferences("collection", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(news_title, news_url);
                //提交
                editor.apply();*/

                Toast.makeText(ShowNewsActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        show_news =(WebView) findViewById(R.id.show_news);
        collect_news =(ImageView) findViewById(R.id.collect_news);
    }

}
