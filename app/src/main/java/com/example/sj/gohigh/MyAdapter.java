package com.example.sj.gohigh;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sj on 2017-10-07.
 */

public class MyAdapter  extends BaseAdapter {

    private ArrayList<MyItem> mItems = new ArrayList<>();
    public int getCount() {
        return mItems.size();

    }
    public MyItem getItem(int position){
        return mItems.get(position);
    }
    public long getItemId(int position){
        return 0;
    }
    public View getView(int position, View convertView , ViewGroup parent){
        Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_custom,parent,false);
        }

        ImageView iv_img = (ImageView)convertView.findViewById(R.id.iv_img);
        TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
        TextView tv_contents = (TextView)convertView.findViewById(R.id.tv_contents);
        MyItem myItem = getItem(position);
        iv_img.setImageDrawable(myItem.getIcon());
        tv_name.setText(myItem.getName());
        tv_contents.setText(myItem.getContents());
        return convertView;


    }
    public void addItem(Drawable img,String name,String contents){
        MyItem mitem = new MyItem();
        mitem.setIcon(img);
        mitem.setName(name);
        mitem.setContents(contents);
        mItems.add(mitem);
    }
}
