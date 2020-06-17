package com.example.ts.news.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ts.news.Bean.User;
import com.example.ts.news.R;
import com.example.ts.news.Utils.ApplicationUtil;
import com.example.ts.news.Utils.MyDatabaseHelper;
import com.example.ts.news.Utils.SharedPreUtil;

public class EditMineActivity extends AppCompatActivity {

    private EditText update_username, update_password, update_repassword;

    private TextView update_user;

    private MyDatabaseHelper dbHelper;

    //User user = User.getInstance();
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mine);

        initView();

        dbHelper = new MyDatabaseHelper(this, "UserDB.db", null, 1);

        username = (String) SharedPreUtil.getParam(EditMineActivity.this, SharedPreUtil.LOGIN_DATA, "");
        update_username.setText(username);


        update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
                //finish();
            }
        });
        ApplicationUtil.getInstance().addActivity(this);

    }

    private void initView() {
        update_user =(TextView) findViewById(R.id.update_user);
        update_username = (EditText) findViewById(R.id.update_username);
        update_password = (EditText) findViewById(R.id.update_password);
        update_repassword = (EditText) findViewById(R.id.update_repassword);

    }

    private void updateUser() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String update_username_str = update_username.getText().toString();
        String update_password_str = update_password.getText().toString();
        String update_repassword_str = update_repassword.getText().toString();

        //设置密码
        if (update_password_str.equals(update_repassword_str)) {
            db.execSQL("update User set name = ?,password = ? where name = ?",
                    new String[]{update_username_str, update_password_str, username});
            Toast.makeText(EditMineActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(EditMineActivity.this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
        }

    }

}
