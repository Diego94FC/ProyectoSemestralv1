package com.tareas.diego.proyectosemestralv1;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Item> items;

    public CustomAdapter(
            Context context,
            int layout,
            ArrayList<Item> users){
        this.context = context;
        this.layout = layout;
        this.items = users;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(
                this.context);
        v = layoutInflater.inflate(
                R.layout.item_layout, null);

        TextView tv = (TextView) v.findViewById(R.id.name);
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        tv.setTypeface(boldTypeface);
        tv.setText((items.get(position)).name);

        TextView tv2 = (TextView) v.findViewById(R.id.price);
        tv2.setTypeface(boldTypeface);

        tv2.setText((items.get(position)).price);

        ImageView iv = (ImageView) v.findViewById(R.id.imageView);
        Picasso.get().load(items.get(position).imageurl) .resize(200, 200).into(iv);




        return v;
    }
}

