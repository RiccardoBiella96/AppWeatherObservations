package supsi.ch.airportweatherobservations.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import supsi.ch.airportweatherobservations.R;
import supsi.ch.airportweatherobservations.models.Airport;

public class AirportAdapter extends RecyclerView.Adapter<AirportAdapter.ViewHolder> {

    private final List<Airport> mAirports;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public AirportAdapter(Context context, List<Airport> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mAirports = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.airport_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.label.setText(mAirports.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mAirports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView label;

        ViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.tv_airport_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Airport getItem(int id) {
        return mAirports.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}