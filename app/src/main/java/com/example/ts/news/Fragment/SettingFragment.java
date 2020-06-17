package com.example.ts.news.Fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ts.news.Activity.CoverActivity;
import com.example.ts.news.R;
import com.example.ts.news.Utils.ApplicationUtil;
import com.example.ts.news.Utils.FileCacheUtils;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by ts on 18-8-20.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;

    private TextView exit_app, about_app, check_version, welcome_app, clear_cache, app_notice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        initView();

        app_notice.setOnClickListener(this);

        welcome_app.setOnClickListener(this);

        check_version.setOnClickListener(this);

        about_app.setOnClickListener(this);

        clear_cache.setOnClickListener(this);

        exit_app.setOnClickListener(this);

        exit_app.setOnClickListener(this);


        return view;
    }

    private void clearCache() {
        try {
            String cacheSize = FileCacheUtils.getCacheSize(context.getCacheDir());
            FileCacheUtils.cleanInternalCache(context);
            Toast.makeText(context, "本次清理" + cacheSize + "缓存", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initView() {
        welcome_app = view.findViewById(R.id.welcome_app);
        check_version = view.findViewById(R.id.check_version);
        about_app = view.findViewById(R.id.about_app);
        clear_cache = view.findViewById(R.id.clear_cache);
        exit_app = view.findViewById(R.id.exit_app);
        app_notice = view.findViewById(R.id.app_notice);
        context = getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_notice: {
                Intent intent = new Intent(getContext(), getActivity().getClass());
                /**
                 * 参数1：Content
                 * 参数2：一般不用，传入0
                 * 参数3：Intent
                 * 参数4：确定PendingIntent的行为，一般传入0
                 */
                PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, 0);
                NotificationManager manager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
                NotificationChannel channel = new NotificationChannel("channel_1", "channel_name_1",
                        NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                Notification notification = new Notification.Builder(getContext(), "channel_1")
                        .setContentTitle("您收到一条新通知")
                        .setContentText("提示：您已开启应用通知")
                        .setWhen(System.currentTimeMillis())//指定通知创建的时间（单位：毫秒）
                        .setSmallIcon(R.mipmap.app_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon))
                        .setContentIntent(pi)//设置通知的意图（点击事件）
                        .setAutoCancel(true)//设置通知点击后自动取消
                        .build();
                manager.notify(1, notification);
            }
            break;
            case R.id.welcome_app: {
                Intent intent = new Intent(getContext(), CoverActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.check_version: {
                Toast.makeText(getContext(), "当前已为最新版1.0", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.about_app: {
                Toast.makeText(getContext(), "@News news.com version 1.0", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.clear_cache: {
                clearCache();
            }
            break;
            case R.id.exit_app: {
                ApplicationUtil.getInstance().exit();
            }
            break;
            default:
                break;

        }
    }
}
