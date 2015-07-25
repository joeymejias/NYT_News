package edu.nyu.scps.top;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;


public class ArticleAdapter extends BaseAdapter {
    private Context context;
    String[] title;
    String[] abs;

    public ArticleAdapter(Context context, String[] title, String[] abs) {
        this.context = context;
        this.title = title;
        this.abs = abs;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TwoLineListItem twoLineListItem;
        if (convertView != null) {
            twoLineListItem = (TwoLineListItem)convertView;
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            twoLineListItem = (TwoLineListItem)inflater.inflate(android.R.layout.simple_list_item_2, null);
        }
        TextView text1 = (TextView)twoLineListItem.findViewById(android.R.id.text1);
        text1.setText(title[position]);
        TextView text2 = (TextView)twoLineListItem.findViewById(android.R.id.text2);
        text2.setText(abs[position]);
        return twoLineListItem;
    }
}