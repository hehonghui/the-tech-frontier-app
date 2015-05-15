package org.tech.frontier.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tech.frontier.R;
import org.tech.frontier.ui.adapter.NewsRecyclerAdapter;

/**
 * Created by kang on 15/5/14-下午9:13.
 */
public class NewsFragment extends Fragment {
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_news, null);


        recyclerView.setAdapter(new NewsRecyclerAdapter());
        recyclerView.setLayoutManager(layoutManager);
        return recyclerView;
    }
}
