package ru.runa.taskin;

import android.support.v4.app.Fragment;

public class SignInActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SignInFragment();
    }
}
