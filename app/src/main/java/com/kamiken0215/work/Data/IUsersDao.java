package com.kamiken0215.work.Data;

import com.kamiken0215.work.Domain.Entities.User;

public interface IUsersDao {

    interface OnFindFinishedListener {
        void onFinished(User user);
        void onFailure(Throwable t);
    }

    void findNoticeUser(OnFindFinishedListener onFinishedListener, String email, String password);

}
