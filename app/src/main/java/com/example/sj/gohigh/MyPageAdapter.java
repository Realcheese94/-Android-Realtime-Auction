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

public class MyPageAdapter extends BaseAdapter {

    private ArrayList<MyPageItem> mItems = new ArrayList<>();
    public int getCount() {
        return mItems.size();
    }
    public MyPageItem getItem(int position){
        return mItems.get(position);
    }
    public long getItemId(int position){
        return 0;
    }
    public View getView(int position, View convertView , ViewGroup parent){
        Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mylistview_custom,parent,false);
        }

        ImageView iv_img = (ImageView)convertView.findViewById(R.id.myiv_img);
        TextView tv_name = (TextView)convertView.findViewById(R.id.mytv_name);
        TextView tv_contents = (TextView)convertView.findViewById(R.id.mytv_contents);
        MyPageItem myItem = getItem(position);
        iv_img.setImageDrawable(myItem.getIcon());
        tv_name.setText(myItem.getName());
        tv_contents.setText(myItem.getContents());
        return convertView;


    }
    public void addItem(Drawable img,String name,String contents){
        MyPageItem mitem = new MyPageItem();
        mitem.setIcon(img);
        mitem.setName(name);
        mitem.setContents(contents);
        mItems.add(mitem);
    }
}
