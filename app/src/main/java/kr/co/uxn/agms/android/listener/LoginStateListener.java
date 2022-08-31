package kr.co.uxn.agms.android.listener;

import kr.co.uxn.agms.android.data.Result;
import kr.co.uxn.agms.android.data.model.LoggedInUser;

public interface LoginStateListener {
    void onStateChanged(Result<LoggedInUser> result);
}
