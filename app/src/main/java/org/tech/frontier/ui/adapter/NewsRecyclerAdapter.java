package org.tech.frontier.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tech.frontier.R;
import org.tech.frontier.module.NewsInfo;

import java.util.ArrayList;

/**
 * Created by kang on 15/5/14-下午9:19.
 */
public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {
    ArrayList<NewsInfo> list = null;

    public NewsRecyclerAdapter() {
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            NewsInfo news = new NewsInfo();
            news.setAuthor("author" + i);
            news.setDate("date" + i);
            news.setId("id" + i);
            news.setTitle("title" + i);
            list.add(news);
        }
    }

    public NewsRecyclerAdapter(@NonNull ArrayList<NewsInfo> list) {
        this.list = list;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.id.setText(list.get(position).getId());
        holder.date.setText(list.get(position).getDate());
        holder.title.setText(list.get(position).getTitle());
        holder.author.setText(list.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView date;
        public TextView title;
        public TextView author;


        public NewsViewHolder(View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.id);
            date = (TextView) itemView.findViewById(R.id.date);
            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
        }
    }
}
