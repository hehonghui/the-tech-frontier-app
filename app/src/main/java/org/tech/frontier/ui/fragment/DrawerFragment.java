package org.tech.frontier.ui.fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tech.frontier.R;
import org.tech.frontier.ui.activity.ContainActivity;
import org.tech.frontier.ui.activity.MainActivity;
import org.tech.frontier.ui.adapter.NavRecycleViewAdapter;


public class DrawerFragment extends Fragment implements NavRecycleViewAdapter.OnItemClickListener {
    private static final String TAG = "org.tech.frontier.ui.fragment.DrawerFragment";

    MainActivity mainActivity;
    NavRecycleViewAdapter navAdapter;


    RecyclerView navList = null;

    TextView username= null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, null);

        mainActivity = (MainActivity) getActivity();

        navAdapter = new NavRecycleViewAdapter();

        navList = (RecyclerView) view.findViewById(R.id.nav_list);
        username = (TextView) view.findViewById(R.id.username);

        init();
        return view;
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        //设置item click响应事件
        navAdapter.setOnItemClickListener(this);
        navList.setLayoutManager(linearLayoutManager);
        navList.setAdapter(navAdapter);

        username.setText("name");
    }

    @Override
    public void onItemClick(View view, final int position) {
        //改变选中状态颜色
        navAdapter.setSelectedPosition(position);
        if (position > 0) {
            startContainer(position);
        } else {
            DrawerFragment.this.mainActivity.showContent();
        }
    }

    private void startContainer(int position) {
        Intent intent = new Intent(getActivity(), ContainActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    public void setSelectedPosition(int position) {
        navAdapter.setSelectedPosition(position);
    }

/*
    @OnClick(R.id.action_feedback)
    public void onFeedBackClick() {

    }

    @OnClick(R.id.action_setting)
    public void onSettingClick() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
*/


}
