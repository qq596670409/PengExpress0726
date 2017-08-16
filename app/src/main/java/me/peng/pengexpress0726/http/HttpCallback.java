package me.peng.pengexpress0726.http;

import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2017/7/28.
 */

public interface HttpCallback<T> {
    void onResponse(T t);

    void onError(VolleyError volleyError);
}
