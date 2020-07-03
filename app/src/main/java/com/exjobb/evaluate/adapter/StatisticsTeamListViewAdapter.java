package com.exjobb.evaluate.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.data.ListItemTeamModel;

import java.util.ArrayList;

public class StatisticsTeamListViewAdapter extends BaseAdapter {
    private static final String TAG = "StatisticsListViewAdapter";
    private Activity activity;
    private ArrayList<ListItemTeamModel> list;

    public StatisticsTeamListViewAdapter(Activity activity, ArrayList<ListItemTeamModel> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private TextView question, questionCellMon, questionCellTue, questionCellWed, questionCellThu, questionCellFri, questionCellSat, questionCellSun;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_listview_item, null);
            holder = new ViewHolder();
            holder.question = convertView.findViewById(R.id.item_1);
            holder.questionCellMon = convertView.findViewById(R.id.item_2);
            holder.questionCellTue = convertView.findViewById(R.id.item_3);
            holder.questionCellWed = convertView.findViewById(R.id.item_4);
            holder.questionCellThu = convertView.findViewById(R.id.item_5);
            holder.questionCellFri = convertView.findViewById(R.id.item_6);
            holder.questionCellSat = convertView.findViewById(R.id.item_7);
            holder.questionCellSun = convertView.findViewById(R.id.item_8);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ListItemTeamModel itemModel = list.get(position);
        holder.question.setText(itemModel.getQuestion());
        holder.questionCellMon.setText(itemModel.getQuestionCellMon());
        holder.questionCellTue.setText(itemModel.getQuestionCellTue());
        holder.questionCellWed.setText(itemModel.getQuestionCellWed());
        holder.questionCellThu.setText(itemModel.getQuestionCellThu());
        holder.questionCellFri.setText(itemModel.getQuestionCellFri());
        holder.questionCellSat.setText(itemModel.getQuestionCellSat());
        holder.questionCellSun.setText(itemModel.getQuestionCellSun());

        return convertView;
    }
}
