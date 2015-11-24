package com.heaven7.databinding.demo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.heaven7.databinding.demo.samples.BaseBehaviourActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    private List<ActivityInfo> activitiesInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitiesInfo = getActivityInfos();
        String[] titles = getActivityTitles();
        setListAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, android.R.id.text1, titles));
    }

    private List<ActivityInfo> getActivityInfos() {
        ArrayList<ActivityInfo> array = new ArrayList<ActivityInfo>();
        array.add(new ActivityInfo(BaseBehaviourActivity.class, getString(R.string.ac_base_behaviour)));
        //TODO the more demos
        return array;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Class<? extends Activity> clazz = activitiesInfo.get(position).activityClass;
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    private String[] getActivityTitles() {
        String[] result = new String[activitiesInfo.size()];
        int i = 0;
        for (ActivityInfo info : activitiesInfo) {
            if(info.title != null)
                result[i++] = info.title;
            else
                result[i++] = getString(info.titleResourceId);
        }
        return result;
    }


    static class ActivityInfo{

        public final Class<? extends Activity> activityClass;
        public int titleResourceId = -1;
        public String title;

        public ActivityInfo(Class<? extends Activity> activityClass,
                            int titleResourceId) {
            super();
            this.activityClass = activityClass;
            this.titleResourceId = titleResourceId;
        }

        public ActivityInfo(Class<? extends Activity> activityClass,String title){
            this.activityClass = activityClass;
            this.title = title;
        }

    }

}
