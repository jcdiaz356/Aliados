package com.dataservicios.aliados.views;

import com.dataservicios.aliados.model.Client;

import java.time.Period;

public interface LoginView {



    void  loginSuccess(Client client);
    void loginError(String message);
    void serverError(Throwable error);
    void showProgressBar();
    void hideProgresbar();


}
