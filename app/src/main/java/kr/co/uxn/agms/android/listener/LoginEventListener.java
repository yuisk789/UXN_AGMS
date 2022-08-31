package kr.co.uxn.agms.android.listener;

import kr.co.uxn.agms.android.data.Result;
import kr.co.uxn.agms.android.data.model.LoggedInUser;

public interface LoginEventListener {
    void addListener(LoginStateListener l);
    void removeListener(LoginStateListener l);
    void notifyListeners(Result<LoggedInUser> result);
}
