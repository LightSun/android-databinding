package com.heaven7.databinding.demo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.heaven7.databinding.demo.samples.BaseBehaviourActivity;
import com.heaven7.databinding.demo.samples.DoubleListViewTest;
import com.heaven7.databinding.demo.samples.ListViewBindAdapterTest;
import com.heaven7.databinding.demo.samples.MultiItemAdapterTest;
import com.heaven7.databinding.demo.samples.RecycleViewBindAdapterTest;
import com.heaven7.databinding.demo.samples.RoundImageBindTest;
import com.heaven7.databinding.demo.samples.SelectModeTest;
import com.heaven7.databinding.demo.samples.SelfAttributeTest;
import com.heaven7.databinding.demo.samples.SelfEventTest;
import com.heaven7.databinding.demo.samples.TextChangeTest;

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
        array.add(new ActivityInfo(ListViewBindAdapterTest.class, getString(R.string.ac_simple_listview)));
        array.add(new ActivityInfo(RecycleViewBindAdapterTest.class, getString(R.string.ac_simple_recycle_view)));
        array.add(new ActivityInfo(TextChangeTest.class, getString(R.string.ac_text_change_listener)));
        array.add(new ActivityInfo(MultiItemAdapterTest.class, getString(R.string.ac_text_multi_item_listview)));
        array.add(new ActivityInfo(RoundImageBindTest.class, getString(R.string.ac_bind_round_image)));
        array.add(new ActivityInfo(DoubleListViewTest.class, getString(R.string.ac_double_listview)));
        array.add(new ActivityInfo(SelectModeTest.class, getString(R.string.ac_select_mode)));
        array.add(new ActivityInfo(SelfAttributeTest.class, getString(R.string.ac_self_attr)));
        array.add(new ActivityInfo(SelfEventTest.class, getString(R.string.ac_self_event)));
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
