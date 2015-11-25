package com.fedor.pavel.exchangerates.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.fedor.pavel.exchangerates.R;
import com.fedor.pavel.exchangerates.models.Model;

import java.util.ArrayList;


public class SpAdapter <T extends Model> extends BaseAdapter {


    private ArrayList <T> models = new ArrayList<>();
    private LayoutInflater inflater;


    public SpAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SpAdapter(Context context,ArrayList <T> models){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.models.addAll(models);
    }

    public void clear(){
        models.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList <T> models){
        this.models.addAll(models);
        notifyDataSetChanged();
    }

    public int getItemPositionByName(String name){

        for (int i =0; i<models.size();i++){
            if (models.get(i).getName().equals(name)){
                return i;
            }
        }

        return 0;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public T getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;

        if(convertView!=null){
            v=convertView;
        }else {
            v = inflater.inflate(R.layout.item_sp_adapter,null);
        }

        TextView tv = (TextView) v.findViewById(R.id.item_sp_adapter_tv_text);
        tv.setText(models.get(position).getName());

        return v;
    }



}
