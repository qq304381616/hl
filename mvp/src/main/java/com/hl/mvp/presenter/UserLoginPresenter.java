package com.hl.mvp.presenter;

import com.hl.mvp.view.IUserLoginView;

/**
 * Created by HL on 2016/12/30.
 */

public class UserLoginPresenter {

    private IUserLoginView userLoginView;

    public UserLoginPresenter(IUserLoginView userLoginView)
    {
        this.userLoginView = userLoginView;
    }

    public void login(){

    }

    public void clear(){

    }
}
