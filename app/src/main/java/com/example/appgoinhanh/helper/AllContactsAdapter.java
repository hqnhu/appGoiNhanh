package com.example.appgoinhanh.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appgoinhanh.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class AllContactsAdapter extends BaseAdapter {

    private ArrayList<ContactVO> listData;
    private LayoutInflater layoutInflater;
    ArrayList<ContactVO> arrayList;

    public AllContactsAdapter( Context context, ArrayList<ContactVO> listData){
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);

        this.arrayList = new ArrayList<ContactVO>();
        this.arrayList.addAll(listData);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {

        TextView Name;
        ImageView HinhAnh;
        TextView Phone;
        TextView Id;
    }

    //filter
    public void filter_name (String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        listData.clear();
        if (charText.length()==0){
            listData.addAll(arrayList);
        }
        else {
            for (ContactVO model : arrayList){
                if (model.getContactName().toLowerCase(Locale.getDefault()).contains(charText)){
                    listData.add(model);
                }
            }
        }
        //sắp xếp
        Collections.sort(arrayList, new Comparator<ContactVO>() {
            @Override public int compare(ContactVO p1, ContactVO p2) {
                return p1.getContactName().toLowerCase().compareTo(p2.getContactName().toLowerCase()); // Ascending
            }

        });
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null)
        {

            convertView = layoutInflater.inflate(R.layout.single_contact_view, null);

            holder = new ViewHolder();

            holder.Id =  (TextView) convertView.findViewById(R.id.tv_Id);
            holder.HinhAnh = (ImageView) convertView.findViewById(R.id.ivContactImage);

            holder.Name = (TextView) convertView.findViewById(R.id.tvContactName);
            holder.Phone = (TextView) convertView.findViewById(R.id.tvPhoneNumber);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(listData.size()>0){

            ContactVO list_ds_danhba = listData.get(position);

            if (list_ds_danhba != null) {

                    holder.Id.setText(list_ds_danhba.getContactId());

                    Bitmap bm = null ;
                    bm = (Bitmap)list_ds_danhba.getContactImage();
                    //Bitmap ResizedBitmap = null;
                    //ResizedBitmap = Url_config.getResizedBitmap(bm,100);

                if(list_ds_danhba.getContactImage() != null){
                        holder.HinhAnh.setImageBitmap(bm);
                    }else {
                        holder.HinhAnh.setImageResource(R.drawable.icon_danhba);
                    }
                    holder.Name.setText(list_ds_danhba.getContactName());
                    holder.Phone.setText(list_ds_danhba.getContactNumber());
            }
        }

        return convertView;
    }
}
