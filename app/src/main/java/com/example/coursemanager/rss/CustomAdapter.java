package com.example.coursemanager.rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coursemanager.R;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    public static final int DODAI = 100;
    Context context;
    public static List<RssItem> rssItemList;

    public CustomAdapter(Context context, List<RssItem> rssItemList) {
        this.context = context;
        this.rssItemList = rssItemList;
    }

    @Override
    public int getCount() {
        return rssItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return rssItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.rss_oneitem, viewGroup, false);
        }
        TextView titleTxt = view.findViewById(R.id.titleTxt);
        TextView descTxt = view.findViewById(R.id.descTxt);
        TextView dateTxt = view.findViewById(R.id.dateTxt);
        ImageView img = view.findViewById(R.id.articleImage);
        RssItem rssItem = (RssItem) this.getItem(i);
        final String tittle = rssItem.getTitle();
        String desc = rssItem.getDescription();
        desc = removeTags(desc);
        String date = rssItem.getDate();
        String imageUrl = rssItem.getImageUrl();
        titleTxt.setText(tittle);
        descTxt.setText(desc.substring(0, Math.min(desc.length(), DODAI)));
        dateTxt.setText(date);
        PicassoClient.downloadImage(context, imageUrl, img);
        return view;
    }

    public static String removeTags(String str) {
        str = str.replaceAll("<.*?>", " ");
        str = str.replaceAll("\\s+", " ");
        return str;
    }
}
