package com.example.ts.news.Utils;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ts on 18-8-21.
 */

/**
 * Application维护所有activity
 * 目的:退出功能的实现
 */
public class ApplicationUtil extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private static ApplicationUtil instance;

    private ApplicationUtil() {
    }

    public synchronized static ApplicationUtil getInstance() {
        if (instance == null) {
            instance = new ApplicationUtil();
        }
        return instance;
    }

    // 添加Activity到列表中维持
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
