package ru.runa.taskin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rruskih on 22.11.2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private TextView _StatusTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        _StatusTextView = view.findViewById(R.id.fragment_login_status);

        view.findViewById(R.id.fragment_login_sign_in_button).setOnClickListener(this);
        view.findViewById(R.id.fragment_login_sign_out_button).setOnClickListener(this);
        view.findViewById(R.id.fragment_login_disconnect_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
