package com.syzible.occupie.Authentication.CreateUserAccount.UserOAuthLogin;

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
import com.syzible.occupie.Authentication.CreateAccountActivity;
import com.syzible.occupie.MainActivity;
import com.syzible.occupie.R;

import java.util.Arrays;

public class OAuthLoginFragment extends Fragment implements OAuthLoginView {

    private CallbackManager callbackManager;
    private OAuthLoginPresenter presenter;

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
                OAuthLoginFragment.this,
                Arrays.asList("public_profile", "email")
                // TODO request birthday
        ));

        if (presenter == null)
            presenter = new OAuthLoginPresenterImpl();

        presenter.attach(this);
        presenter.onFacebookCallback(callbackManager);

        return view;
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
