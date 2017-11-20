package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trung.onepiecewallpagers.R;
import com.example.trung.onepiecewallpagers.ViewImage;

import java.util.ArrayList;
import java.util.List;

import model.Figure;

/**
 * Created by Admin on 11/17/2017.
 */

public class AdapterLvFigures extends ArrayAdapter<Figure> {

    private ArrayList<Figure> arrFi;
    private LayoutInflater inflater;

    public AdapterLvFigures(@NonNull Context context, int resource, @NonNull ArrayList<Figure> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        arrFi = objects;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (view == null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_lv_figures,parent,false);
            holder.tvName = view.findViewById(R.id.tvNameOfFigure);

            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        Figure figure = arrFi.get(position);
        holder.tvName.setText(figure.getName());
        return view;
    }

    private class ViewHolder{
        TextView tvName;
    }
}
