package adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.trung.onepiecewallpagers.R;

import java.util.ArrayList;

import model.ItemGridView;

/**
 * Created by Admin on 11/15/2017.
 */

public class AdapterGridView extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<ItemGridView> arrItem;

    public AdapterGridView(Context context, int layout, ArrayList<ItemGridView> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }


    private class ViewHolder{
        ImageView imView;
    }

    @Override
    public int getCount() {
        return arrItem.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewRow = view;
        if(viewRow == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewRow = inflater.inflate(layout,viewGroup,false);

            ViewHolder holder = new ViewHolder();
            holder.imView = viewRow.findViewById(R.id.itemGridView);

            viewRow.setTag(holder);
        }
        ItemGridView itemGridView = arrItem.get(i);
        ViewHolder holder = (ViewHolder) viewRow.getTag();
        Glide.with(context).load(itemGridView.getSmallImg())
                .placeholder(R.drawable.loading)
                .into(holder.imView);
        return viewRow;
    }
}
