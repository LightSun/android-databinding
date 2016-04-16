package com.heaven7.databinding.demo;

import android.app.Application;

import com.android.volley.data.RequestManager;
import com.heaven7.databinding.core.DataBindingFactory;

/**
 * Created by heaven7 on 2015/12/1.
 */
public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RequestManager.init(this);
        DataBindingFactory.setImagePropertyApplier(new VolleyImageApplier());
    }
}
