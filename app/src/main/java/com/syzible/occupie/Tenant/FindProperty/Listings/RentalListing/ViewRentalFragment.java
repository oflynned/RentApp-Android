package com.syzible.occupie.Tenant.FindProperty.Listings.RentalListing;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.syzible.occupie.Common.Authentication.CreateAccountActivity;
import com.syzible.occupie.Common.Helpers.StringHelper;
import com.syzible.occupie.Common.Objects.Rental;
import com.syzible.occupie.Common.Persistence.LocalPrefs;
import com.syzible.occupie.Common.Persistence.Target;
import com.syzible.occupie.R;
import com.syzible.occupie.Tenant.FindProperty.Common.ImageAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import mehdi.sakout.fancybuttons.FancyButton;

public class ViewRentalFragment extends Fragment implements ViewRentalView {

    private ViewRentalPresenter presenter;
    private FloatingActionButton favouriteFab;
    private boolean isFavourite = false;

    private Rental property;
    private TextView propertyType, bedroomCount;
    private TextView address, description, deposit, rent;
    private FancyButton enquire;

    public ViewRentalFragment() {

    }

    public static ViewRentalFragment getInstance(Rental property) {
        return new ViewRentalFragment().setProperty(property);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_rental, container, false);

        favouriteFab = view.findViewById(R.id.view_listing_favourite_fab);
        favouriteFab.setOnClickListener((v) -> onFavouriteClick());

        propertyType = view.findViewById(R.id.property_type);
        bedroomCount = view.findViewById(R.id.property_number_of_bedrooms);

        address = view.findViewById(R.id.address);
        description = view.findViewById(R.id.property_listing_description);

        rent = view.findViewById(R.id.rent_status_bar);
        deposit = view.findViewById(R.id.deposit_status_bar);

        enquire = view.findViewById(R.id.enquire_to_listing);
        enquire.setOnClickListener((v) -> onEnquireClick());

        ImageAdapter adapter = new ImageAdapter(getContext(), property.getImages());
        ViewPager viewPager = view.findViewById(R.id.property_image);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        CirclePageIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        return view;
    }

    @Override
    public void onStart() {
        if (presenter == null)
            presenter = new ViewRentalPresenterImpl();

        presenter.attach(this);
        presenter.displayListingDetails();
        super.onStart();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void onFavouriteClick() {
        if (LocalPrefs.isUserLoggedIn(getContext())) {
            isFavourite = !isFavourite;
            int icon = isFavourite ? R.drawable.heart_filled : R.drawable.heart_outline;
            favouriteFab.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
        } else {
            redirectAccountCreation();
        }
    }

    @Override
    public void onEnquireClick() {
        if (LocalPrefs.isUserLoggedIn(getContext())) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Apply to Listing")
                    .setMessage("Are you sure you want to enquire about becoming a tenant at this property? The landlord should accept or reject your application soon, and you can get in contact once accepted.")
                    .setPositiveButton("Enquire", (dialog, which) -> Toast.makeText(getContext(), property.getAddress().getTileAddress(), Toast.LENGTH_SHORT).show())
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            redirectAccountCreation();
        }

    }

    @Override
    public void redirectAccountCreation() {
        Intent intent = new Intent(getContext(), CreateAccountActivity.class);
        intent.putExtra("target", Target.user.name());
        startActivity(intent);
    }

    @Override
    public void setListingDetails() {
        String dwellingType = StringHelper.capitalise(property.getDetails().getDwelling());
        propertyType.setText(dwellingType);

        int bedroomCountSize = property.getBedrooms().size();
        String bedroomFormat = String.format("%s %s", bedroomCountSize,
                bedroomCountSize == 1 ? "bedroom" : "bedrooms");
        bedroomCount.setText(bedroomFormat);

        address.setText(property.getAddress().getTileAddress());
        description.setText(property.getDetails().getDescription());
        rent.setText(String.format("€%s", property.getRent()));
        deposit.setText(String.format("€%s", property.getDeposit()));
    }

    public ViewRentalFragment setProperty(Rental property) {
        this.property = property;
        return this;
    }
}
