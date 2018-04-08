package com.syzible.occupie.Tenant.CreateUserAccount.UserDetailsConfirmation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.syzible.occupie.Common.Authentication.CreateAccountActivity;
import com.syzible.occupie.R;
import com.syzible.occupie.Tenant.CreateUserAccount.UserProfileBuilder.UserProfileBuilderFragment;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class UserDetailsConfirmationFragment extends Fragment implements UserDetailsConfirmationView {

    private EditText forename, surname;
    private Spinner sex, profession;
    private EditText dob;

    private UserDetailsConfirmationPresenter presenter;
    private JSONObject oauth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tenant_confirm_oauth_details, container, false);

        forename = view.findViewById(R.id.details_confirmation_forename);
        surname = view.findViewById(R.id.details_confirmation_surname);
        sex = view.findViewById(R.id.details_confirmation_sex);
        profession = view.findViewById(R.id.details_confirmation_profession);
        dob = view.findViewById(R.id.details_confirmation_dob);

        FloatingActionButton next = view.findViewById(R.id.signup_next_screen);
        next.setOnClickListener(v -> {
            try {
                presenter.updateAccount();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        if (presenter == null)
            presenter = new UserDetailsConfirmationPresenterImpl();

        presenter.attach(this);
        presenter.parsePayload(oauth);
        super.onStart();
    }

    @Override
    public void onStop() {
        presenter.detach();
        super.onStop();
    }

    public static UserDetailsConfirmationFragment getInstance(JSONObject oauth) {
        UserDetailsConfirmationFragment fragment = new UserDetailsConfirmationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("oauth", oauth.toString());
        fragment.setArguments(bundle);
        fragment.setOauth(oauth);
        return fragment;
    }

    @Override
    public void setForename(String forename) {
        this.forename.setText(forename);
    }

    @Override
    public void setSurname(String surname) {
        this.surname.setText(surname);
    }

    @Override
    public void setDob(String dob) {
        this.dob.setText(dob);
    }

    @Override
    public void setSex(String sex) {
        switch (sex) {
            case "male":
                this.sex.setSelection(0);
                break;
            case "female":
                this.sex.setSelection(1);
                break;
            default:
                this.sex.setSelection(2);
                break;
        }
    }

    @Override
    public void setProfession(String profession) {
        switch (profession) {
            case "student":
                this.profession.setSelection(0);
                break;
            case "professional":
                this.profession.setSelection(1);
                break;
            default:
                this.profession.setSelection(2);
                break;
        }
    }

    public void setOauth(JSONObject oauth) {
        this.oauth = oauth;
    }

    @Override
    public String getForename() {
        return forename.getText().toString();
    }

    @Override
    public String getSurname() {
        return surname.getText().toString();
    }

    @Override
    public String getSex() {
        return String.valueOf(sex.getSelectedItemPosition());
    }

    @Override
    public String getDob() {
        return dob.getText().toString();
    }

    @Override
    public String getProfession() {
        return String.valueOf(profession.getSelectedItemPosition());
    }

    @Override
    public boolean isSectionCompleted() {
        return !getForename().isEmpty() && !getSurname().isEmpty() &&
                !getDob().isEmpty() && !getSex().isEmpty() && !getProfession().isEmpty();
    }

    @Override
    public void setNextFragment(JSONObject profile) {
        CreateAccountActivity.setFragmentBackstack(getFragmentManager(), UserProfileBuilderFragment.getInstance(profile));
    }
}
