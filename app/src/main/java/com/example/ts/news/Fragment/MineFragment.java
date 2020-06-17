package com.example.ts.news.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ts.news.Activity.CollectionActivity;
import com.example.ts.news.Activity.EditMineActivity;
import com.example.ts.news.Activity.LoginOrRegisterActivity;
import com.example.ts.news.R;
import com.example.ts.news.Utils.AlbumUtil;
import com.example.ts.news.Utils.MD5Encoder;
import com.example.ts.news.Utils.MyDatabaseHelper;
import com.example.ts.news.Utils.SharedPreUtil;
import com.example.ts.news.Utils.TimeCount;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by ts on 18-8-20.
 */

public class MineFragment extends Fragment implements View.OnClickListener {
    private View view;

    private TextView edit_mine, exit_login, collection, mine_user_name, request_authentication, jinriyuedu;

    private ImageView my_head;

    private static final int CHOSSE_PHOTO = 1;

    private MyDatabaseHelper helper;


    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //获取旧时间
                long time_old = TimeCount.getInstance().getTime();
                //得到新时间
                long time_new = System.currentTimeMillis();
                TimeCount.getInstance().setTime(time_new);

                int time = (int) (time_new - time_old) / 1000 / 60;

                jinriyuedu.setText("今日阅读" + time + "分钟");

            }
        }
    };
    final Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                try {
                    Thread.sleep(60000);
                    //每分钟更新一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        thread.start();

        helper = new MyDatabaseHelper(getContext(), "UserDB.db", null, 1);
        String username = (String) SharedPreUtil.getParam(getContext(), SharedPreUtil.LOGIN_DATA, "");
        mine_user_name.setText(username);

        edit_mine.setOnClickListener(this);

        exit_login.setOnClickListener(this);

        collection.setOnClickListener(this);

        my_head.setOnClickListener(this);

        request_authentication.setOnClickListener(this);

        try {
            String path = getContext().getCacheDir().getPath();
            String fileName = "user_head";
            File file = new File(path, fileName);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                Bitmap round = AlbumUtil.toRoundBitmap(bitmap);
                my_head.setImageBitmap(round);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getContext(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initView() {
        mine_user_name = view.findViewById(R.id.mine_user_name);
        edit_mine = view.findViewById(R.id.edit_mine);
        exit_login = view.findViewById(R.id.exit_login);
        collection = view.findViewById(R.id.collection);
        my_head = view.findViewById(R.id.my_head);
        request_authentication = view.findViewById(R.id.request_authentication);
        jinriyuedu = view.findViewById(R.id.jinriyuedu);
    }


    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOSSE_PHOTO);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOSSE_PHOTO:
                if (resultCode == -1) {
                    String imgPath = AlbumUtil.handleImageOnKitKat(getContext(), data);
                    setHead(imgPath);
                }
                break;
            default:
                break;
        }
    }


    private void setHead(String imgPath) {
        if (imgPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            Bitmap round = AlbumUtil.toRoundBitmap(bitmap);
            try {
                String path = getContext().getCacheDir().getPath();
                File file = new File(path, "user_head");
                round.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            my_head.setImageBitmap(round);
        } else {
            Toast.makeText(getContext(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.request_authentication:
                Toast.makeText(getContext(), "该功能暂未开启,敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_mine: {
                Intent intent = new Intent(getContext(), EditMineActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.exit_login: {
                SharedPreUtil.setParam(getContext(), SharedPreUtil.IS_LOGIN, false);
                SharedPreUtil.removeParam(getContext(), SharedPreUtil.LOGIN_DATA);

                //重新跳转到登录页面
                Intent intent = new Intent(getContext(), LoginOrRegisterActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            break;
            case R.id.collection: {
                Intent intent = new Intent(getContext(), CollectionActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.my_head: {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
            break;
            default:
                break;

        }
    }
}
