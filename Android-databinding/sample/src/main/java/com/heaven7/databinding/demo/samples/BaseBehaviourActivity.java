package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.heaven7.databinding.core.DataBinder;
import com.heaven7.databinding.core.IDataBinder;
import com.heaven7.databinding.core.PropertyNames;
import com.heaven7.databinding.demo.R;
import com.heaven7.databinding.demo.bean.User;
import com.heaven7.databinding.demo.callback.MainEventHandler;
import com.heaven7.databinding.demo.util.Util;
import com.heaven7.databinding.util.StringUtil2;

/**
 * this sample show the base behaviour of data-binding framework.
 */
public class BaseBehaviourActivity extends AppCompatActivity {

    private IDataBinder mDataBinder;

    User mUser;

    private final View.OnClickListener mOnclickChangeDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Util.changeUserName(mUser,"traditional_onClick");
            mDataBinder.notifyDataSetChanged(R.id.bt);
        }
    };
    private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Util.changeUserName(mUser, "notifyDataSetChanged_by_propertyname");
            mDataBinder.notifyDataSetChanged(R.id.bt, PropertyNames.TEXT);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //traditional event
        final View v = findViewById(R.id.bt100);
        v.setOnClickListener(mOnclickChangeDataListener);
        v.setOnLongClickListener(mOnLongClickListener);

        doBind2();
    }

    /**  old call */
    private void doBind() {
        mDataBinder = DataBinder.getDataBinder(this, R.raw.db_main,false);
        //bind a User and cache it for latter call notify.
        mDataBinder.bind(R.id.bt, true, mUser = new User("heaven7", false));

        //bind onClick event and onLongClick event and not cache any data
        mDataBinder.bind(R.id.bt0, false, mUser, new MainEventHandler(mDataBinder));

        //bind a data to multi views. but not cache
        mDataBinder.bind(new User("joker", true, "xxx_joker"));
    }

    private void doBind2() {
        (mDataBinder = DataBinder.getDataBinder(this, R.raw.db_main, false))
                .bind(R.id.bt, true, mUser = new User("heaven7", false))
                .bind(R.id.bt0, false, mUser,new MainEventHandler(mDataBinder))
                .bind(new User("joker", true, "xxx_joker"));

        test();
    }

    private void test() {
        //test
        System.out.println("read android R res: " + StringUtil2.getResId("android.R.color.holo_red_light", this));
        int color  = getResources().getColor(StringUtil2.getResId("android.R.color.holo_red_light", this));
        // mDataBinder.getViewHelper().setTextColor(R.id.bt3, color);
        // mDataBinder.getViewHelper().setTextColor(R.id.bt3, (Integer) ResourceResolver.getResValue(this,"{@android:color/holo_red_light}"));
    }

    @Override
    protected void onDestroy() {
        mDataBinder.onDestroy();
        super.onDestroy();
    }
}
