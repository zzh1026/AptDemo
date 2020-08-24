package com.zzh.api;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2020/8/21.
 */
public interface IViewInjector<T> {

    /**
     * 通过source.findViewById()
     *
     * @param target 泛型参数，调用类 activity、fragment等
     * @param source Activity、View
     */
    void inject(T target, Object source);
}
