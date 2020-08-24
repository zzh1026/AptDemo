package com.zzh.api;

import android.app.Activity;
import android.util.Log;
import android.view.View;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2020/8/21.
 */
public class ButterKnife {

    private static final String TAG = "ButterKnife";
    public static final String PROXY = "_ViewBinding";

    /**
     * activity 调用
     *
     * @param activity
     */
    public static void bind(Activity activity) {
        findProxyActivity(activity).inject(activity, activity);
    }

    /**
     * fragment. adapter调用
     *
     * @param object
     * @param view
     */
    public static void bind(Object object, View view) {
        findProxyActivity(object).inject(object, view);
    }

    /**
     * 根据使用注解的类和约定的命名规则，通过反射找到动态生成的代理类（处理注解逻辑）
     *
     * @param object
     * @return
     */
    private static IViewInjector findProxyActivity(Object object) {

        String proxyClassName = object.getClass().getName() + PROXY;
        Log.e(TAG, "findProxyActivity: " + proxyClassName);
        Class<?> proxyClass = null;
        try {
            proxyClass = Class.forName(proxyClassName);
            return (IViewInjector) proxyClass.newInstance();
        } catch (Exception e) {
            Log.e(TAG, "proxyClass error: " + e.getMessage());
        }
        return null;
    }
}
