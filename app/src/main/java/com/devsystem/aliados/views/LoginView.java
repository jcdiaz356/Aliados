package com.devsystem.aliados.views;

import com.devsystem.aliados.model.Client;

public interface LoginView {



    void  loginSuccess(Client client);
    void loginError(String message);
    void serverError(Throwable error);
    void showProgressBar();
    void hideProgresbar();


}
