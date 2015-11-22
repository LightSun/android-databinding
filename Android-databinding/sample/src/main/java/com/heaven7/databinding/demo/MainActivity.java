package com.heaven7.databinding.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.heaven7.databinding.core.DataBinder;
import com.heaven7.databinding.core.PropertyNames;
import com.heaven7.databinding.demo.bean.User;
import com.heaven7.databinding.demo.callback.MainEventHandler;
import com.heaven7.databinding.demo.util.Util;

public class MainActivity extends AppCompatActivity {

    private DataBinder mDataBinder;

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
        setContentView(R.layout.activity_main);
        final View v = findViewById(R.id.bt100);
        v.setOnClickListener(mOnclickChangeDataListener);
        v.setOnLongClickListener(mOnLongClickListener);

        doBind();
    }

    private void doBind() {
        //init DataBinder
        mDataBinder = new DataBinder(this, R.raw.databinding_main);

        //bind a User and cache it for latter call notify.
        mDataBinder.bind(R.id.bt, true, mUser = new User("heaven7", false));

        //bind onClick event and onLongClick event and not cache any data
        mDataBinder.bind(R.id.bt0, false, mUser,new MainEventHandler(mDataBinder));

        //bind a data to multi views. but not cache
        mDataBinder.bind(new User("joker", true,"xxx_joker"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
