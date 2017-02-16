package tourist.android.com.newproject.sql;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tourist.android.com.newproject.R;
import tourist.android.com.newproject.Utils;

public class PlaceAdapter extends ArrayAdapter<Place> {

    private Activity context;
    private List<Place> places;

    static class ViewHolder {
        public ImageView img;
        public TextView txtTitle;
        public TextView txtDescription;
    }

    public PlaceAdapter(Activity context, List<Place> resource) {
        super(context, R.layout.list_item_place, resource);
        this.context = context;
        this.places = resource;
    }

    @Override
    public Place getItem(int position) {
        if(places != null && position <= places.size()) {
            return places.get(position);
        }
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return places == null ? 0 : places.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_item_place, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.txtTitle);
            viewHolder.txtDescription = (TextView) rowView.findViewById(R.id.txtDescription);
            viewHolder.img = (ImageView) rowView.findViewById(R.id.img);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        String name = places.get(position).getPlaceName();
        holder.txtTitle.setText(name);
        String type = places.get(position).getPlaceType();
        holder.txtDescription.setText(type);
        String images = places.get(position).getImages();
        if(images != null && !images.isEmpty()) {
            String image = images.split(";")[0];
            Picasso.with(this.context)
                    .load(image)
                    .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                    .error(R.drawable.common_google_signin_btn_icon_dark_focused)
                    .resize(Utils.convertDipToPixcel(getContext().getResources(), 100), Utils.convertDipToPixcel(getContext().getResources(), 100))
                    .into(holder.img);
        }

        return rowView;
    }

    public List<Place> getPlaces() {
        if(places == null) {
            places = new ArrayList<Place>();
        }
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}