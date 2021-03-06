package com.syzible.occupie.Tenant.FindProperty.Results.RentalResults;

import com.syzible.occupie.Common.Mvp;
import com.syzible.occupie.Common.Objects.Rental;
import com.syzible.occupie.Tenant.FindProperty.Common.PropertyType;

import java.util.List;

public interface FindRentalView extends Mvp.IView {
    void showProperties(List<Rental> propertyList);

    void setProgressBarLoading(boolean isLoading);

    PropertyType getPropertyType();
}
