package com.dataservicios.aliados.views;

import com.dataservicios.aliados.model.Client;

public interface ClientProgramView {


 void getDataSuccess(boolean success);
 void getDataError(String message);
 void serverError(Throwable error);
 void showProgressBar();
 void hideProgresbar();


}
