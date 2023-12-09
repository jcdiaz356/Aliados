package com.devsystem.aliados.views;

public interface ClientProgramView {


 void getDataSuccess(boolean success);
 void getDataError(String message);
 void serverError(Throwable error);
 void showProgressBar();
 void hideProgresbar();


}
