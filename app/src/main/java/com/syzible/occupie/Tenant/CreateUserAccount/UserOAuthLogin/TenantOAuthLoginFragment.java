package com.syzible.occupie.Tenant.CreateUserAccount.UserOAuthLogin;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.syzible.occupie.MainActivity;
import com.syzible.occupie.R;

import java.util.Arrays;

public class TenantOAuthLoginFragment extends Fragment implements TenantOAuthLoginView {

    private CallbackManager callbackManager;
    private TenantOAuthLoginPresenter presenter;

    public TenantOAuthLoginFragment() {

    }

    public static TenantOAuthLoginFragment getInstance() {
        return new TenantOAuthLoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tenant_oauth_login, container, false);

        TextView continueTextView = view.findViewById(R.id.tenant_continue_without_account);
        continueTextView.setOnClickListener((v) -> onContinueClick());

        LoginButton facebookLoginButton = view.findViewById(R.id.tenant_facebook_login_button);
        facebookLoginButton.setOnClickListener(v -> LoginManager.getInstance().logInWithReadPermissions(
                TenantOAuthLoginFragment.this,
                Arrays.asList("public_profile", "email")
                // TODO request birthday
        ));

        if (presenter == null)
            presenter = new TenantOAuthLoginPresenterImpl();

        presenter.attach(this);
        presenter.onFacebookCallback(callbackManager);

        return view;
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onContinueClick() {
        getActivity().finish();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void onTosClick() {

    }
}
