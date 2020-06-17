package com.example.ts.news.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ts.news.Bean.User;
import com.example.ts.news.R;
import com.example.ts.news.Utils.AlbumUtil;
import com.example.ts.news.Utils.ApplicationUtil;
import com.example.ts.news.Utils.MyDatabaseHelper;
import com.example.ts.news.Utils.SharedPreUtil;
import com.example.ts.news.Utils.TimeCount;

import java.io.File;
import java.io.FileInputStream;

public class LoginActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private Button check_user;
    private EditText username, userpassword;
    private ImageView login_head;

    //加载User实例
    //User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        dbHelper = new MyDatabaseHelper(this, "UserDB.db", null, 1);

        check_user = (Button) findViewById(R.id.check_user);

        username = (EditText) findViewById(R.id.login_username);
        userpassword = (EditText) findViewById(R.id.login_password);

        login_head = (ImageView) findViewById(R.id.login_head);

        try {
            String path = getCacheDir().getPath();
            String fileName = "user_head";
            File file = new File(path, fileName);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                Bitmap round = AlbumUtil.toRoundBitmap(bitmap);
                login_head.setImageBitmap(round);
            } else {
                login_head.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.head));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        check_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String username_str = username.getText().toString();
                String userpassword_str = userpassword.getText().toString();

                Cursor cursor = db.rawQuery("select * from User where name=?", new String[]{username_str});
                if (cursor.getCount() == 0) {
                    Toast.makeText(LoginActivity.this, "用户名不存在！", Toast.LENGTH_SHORT).show();
                } else {
                    if (cursor.moveToFirst()) {
                        String userpassword_db = cursor.getString(cursor.getColumnIndex("password"));
                        if (userpassword_str.equals(userpassword_db)) {
                            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.IS_LOGIN, true);
                            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.LOGIN_DATA, username_str);
                            //user.setUsername(username_str);
                            //user.setPassword(userpassword_str);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            TimeCount.getInstance().setTime(System.currentTimeMillis());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "密码错误，请重新登录", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                cursor.close();
                db.close();
            }
        });
        ApplicationUtil.getInstance().addActivity(this);
    }
}
