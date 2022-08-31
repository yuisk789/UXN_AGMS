package kr.co.uxn.agms.android.data;

import java.io.IOException;
import java.util.ArrayList;

import kr.co.uxn.agms.android.data.model.LoggedInUser;
import kr.co.uxn.agms.android.listener.LoginEventListener;
import kr.co.uxn.agms.android.listener.LoginStateListener;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource implements LoginEventListener {

    private ArrayList<LoginStateListener> listeners;

    public LoginDataSource() {
        this.listeners = new ArrayList<>();
    }

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser = new LoggedInUser(java.util.UUID.randomUUID().toString(), "UXN");
            notifyListeners(new Result.Success<>(fakeUser));
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    @Override
    public void addListener(LoginStateListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListener(LoginStateListener l) {
        int idx = listeners.indexOf(l);
        listeners.remove(idx);
    }

    @Override
    public void notifyListeners(Result<LoggedInUser> result) {
        for(LoginStateListener l : listeners) {
            l.onStateChanged(result);
        }
    }
}