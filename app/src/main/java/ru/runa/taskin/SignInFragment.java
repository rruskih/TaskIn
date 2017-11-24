package ru.runa.taskin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by rruskih on 22.11.2017.
 */

public class SignInFragment extends Fragment implements OnClickListener {

    private static final String TAG = "SignInFragment";
    private static final int RC_SIGN_IN = 9001;

    private TextView _StatusTextView;

    private SignInButton _SignInButton;
    private LinearLayout _SignOutLinearLayout;

    private GoogleSignInClient _GoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_in, container, false);

        _StatusTextView = view.findViewById(R.id.fragment_login_status);

        view.findViewById(R.id.fragment_sign_in_sign_in_button).setOnClickListener(this);
        view.findViewById(R.id.fragment_login_sign_out_button).setOnClickListener(this);
        view.findViewById(R.id.fragment_login_disconnect_button).setOnClickListener(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        _GoogleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);

        _SignInButton = view.findViewById(R.id.fragment_sign_in_sign_in_button);
        _SignOutLinearLayout = view.findViewById(R.id.fragment_login_sign_out_and_disconnect);

        _SignInButton.setSize(SignInButton.SIZE_STANDARD);
        _SignInButton.setColorScheme(SignInButton.COLOR_LIGHT);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        updateUI(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            updateUI(account);
        }
        catch (ApiException e) {
            Log.w(TAG, "signInResult: failed code = " + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            _StatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));

            _SignInButton.setVisibility(View.GONE);
            _SignOutLinearLayout.setVisibility(View.VISIBLE);
        }
        else {
            _StatusTextView.setText(R.string.signed_out);

            _SignInButton.setVisibility(View.VISIBLE);
            _SignOutLinearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_sign_in_sign_in_button:
                signIn();
                break;

            case R.id.fragment_login_sign_out_button :
                signOut();
                break;

            case R.id.fragment_login_disconnect_button :
                revokeAccess();
                break;
        }
    }

    private void revokeAccess() {
        _GoogleSignInClient.revokeAccess()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void signOut() {
        _GoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void signIn() {
        Intent signIntent = _GoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }
}
