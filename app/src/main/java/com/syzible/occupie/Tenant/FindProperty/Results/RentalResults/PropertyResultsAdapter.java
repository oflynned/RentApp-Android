package com.syzible.occupie.Tenant.FindProperty.Results.RentalResults;

import android.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.syzible.occupie.Common.Objects.Rental;
import com.syzible.occupie.Tenant.FindProperty.Common.ImageAdapter;
import com.syzible.occupie.Tenant.FindProperty.Listings.RentalListing.ViewRentalFragment;
import com.syzible.occupie.MainActivity;
import com.syzible.occupie.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ed on 30/10/2016
 */
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {
    private List<Rental> rentals = new ArrayList<>();
    private FragmentManager manager;

    public PropertyAdapter(List<Rental> rentals, FragmentManager manager) {
        this.rentals = rentals;
        this.manager = manager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_tile, parent, false);
        return new ViewHolder(view);
    }

    private void formatCard(final ViewHolder holder, Rental rental) {
        holder.bedroomCount.setText(String.valueOf(rental.getBedrooms().size()));
        holder.bathroomCount.setText(String.valueOf(rental.getBathrooms().size()));
        holder.address.setText(rental.getAddress().getFullAddress());
        holder.rent.setText(String.format("€%s pm", rental.getRent()));

        ImageAdapter adapter = new ImageAdapter(holder.itemView.getContext(), rental.getImages());
        holder.viewPager.setAdapter(adapter);
        holder.viewPager.setOffscreenPageLimit(3);

        CirclePageIndicator indicator = holder.itemView.findViewById(R.id.indicator);
        indicator.setViewPager(holder.viewPager);

        holder.itemView.setOnClickListener(v -> {
            ViewRentalFragment fragment = ViewRentalFragment.getInstance(rental);
            MainActivity.setFragmentBackstack(manager, fragment);
        });
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Rental property = rentals.get(position);
        formatCard(holder, property);
    }

    @Override
    public int getItemCount() {
        return rentals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewPager viewPager;
        TextView bedroomCount, bathroomCount, address, rent;

        ViewHolder(View itemView) {
            super(itemView);

            viewPager = itemView.findViewById(R.id.property_image);
            rent = itemView.findViewById(R.id.property_tile_rent);
            bedroomCount = itemView.findViewById(R.id.property_tile_bedroom_count);
            bathroomCount = itemView.findViewById(R.id.property_tile_bathroom_count);
            address = itemView.findViewById(R.id.property_tile_address);
        }
    }
}
