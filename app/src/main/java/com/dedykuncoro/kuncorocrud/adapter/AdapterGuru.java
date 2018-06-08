package com.dedykuncoro.kuncorocrud.adapter;

/**
 * Created by Elad Oktarizo on 23/04/2018.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dedykuncoro.kuncorocrud.R;
import com.dedykuncoro.kuncorocrud.data.DataGuru;

import java.util.List;

/**
 * Created by Kuncoro on 26/03/2016.
 */
public class AdapterGuru extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataGuru> items;

    public AdapterGuru(Activity activity, List<DataGuru> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_rowguru, null);

        TextView id_guru = (TextView) convertView.findViewById(R.id.id_guru);
        TextView namalengkap = (TextView) convertView.findViewById(R.id.namalengkap);
        TextView alamat = (TextView) convertView.findViewById(R.id.alamat);
        //TextView nohp = (TextView) convertView.findViewById(R.id.nohp);

        DataGuru data = items.get(position);

        id_guru.setText(data.getId_guru());
        namalengkap.setText(data.getNamalengkap());
        alamat.setText(data.getAlamat());

        return convertView;
    }

}
