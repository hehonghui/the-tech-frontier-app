package org.tech.frontier.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.tech.frontier.R;
import org.tech.frontier.module.NavItemInfo;
import org.tech.frontier.utils.AppTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 15/3/14-下午11:06.
 */
public class NavRecycleViewAdapter
        extends RecyclerView.Adapter<NavRecycleViewAdapter.NavItemHolder> {
    private List<NavItemInfo> mList;
    private View selectedView = null;
    private OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    // 选中位置
    private static int selectedPosition = 0;

    public NavRecycleViewAdapter(List<NavItemInfo> mList) {
        this.mList = mList;
    }

    public NavRecycleViewAdapter() {
        List<NavItemInfo> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(new NavItemInfo("name" + i, R.drawable.ic_launcher));
        }

        this.mList = list;
    }

    public void setData(@NonNull List<NavItemInfo> list) {
        this.mList = list;
    }

    @Override
    public NavItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_nav, parent, false);
        return new NavItemHolder(view);
    }

    @Override
    public void onBindViewHolder(NavItemHolder holder, int position) {
        NavItemInfo item = mList.get(position);
        holder.img.setImageResource(item.getIconId());
        holder.text.setText(item.getName());

        if (position == selectedPosition) {
            holder.text.setTextColor(AppTools.getColor(R.color.theme_green));
        } else {
            holder.text.setTextColor(AppTools.getColor(R.color.drawer_text_color));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //View Holder
    public final class NavItemHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView text;

        public NavItemHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            text = (TextView) itemView.findViewById(R.id.text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getPosition());
                    }
                }
            });
        }

    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }


}
