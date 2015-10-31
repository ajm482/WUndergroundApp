package com.example.alexmarion.wundergroundapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alex Marion on 8/12/2015.
 */
public class CustomListAdapter extends ArrayAdapter<String[]> {

    public CustomListAdapter(Context context, int resource, String[][] elementsArray) {
        super(context, resource, elementsArray);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout, parent, false);
        }

        // Order for array:
        // Icon Image > Date > Time > Temp > Condition > Humidity
        // 0 = Icon Image
        // 1 = Date
        // 2 = Time
        // 3 = Temp
        // 4 = Condition
        // 5 = Humidity

        ImageView icon = (ImageView) convertView.findViewById(R.id.icon_image);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView temp = (TextView) convertView.findViewById(R.id.temp);
        TextView condition = (TextView) convertView.findViewById(R.id.condition);
        TextView humidity = (TextView) convertView.findViewById(R.id.humidity);

        DownloadIconTask iconTask = new DownloadIconTask(icon);
        iconTask.execute(this.getItem(position)[0]);

        date.setText(this.getItem(position)[1]);
        if(date.getText().equals("")) {
            date.setVisibility(View.GONE);
        } else {
            date.setVisibility(View.VISIBLE);
        }

        time.setText(this.getItem(position)[2]);
        temp.setText(this.getItem(position)[3]);
        condition.setText(this.getItem(position)[4]);
        humidity.setText(this.getItem(position)[5]);


        return convertView;
    }
}
