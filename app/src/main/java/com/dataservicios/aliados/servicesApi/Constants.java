package com.dataservicios.aliados.servicesApi;

public class Constants {
    //https://backoffice.ttaudit.com/api/v1/ganamas/getUserForCode
    public static final String VERSION = "v1";
    public static final String PROJECT_PREFIX = "/ganamas/";
//    public static final String ROOT_URL = "http://jsonserviceslaravel.test" + VERSION;
    public static final String SERVICE = "api/" + VERSION + PROJECT_PREFIX;
 //  public static final String ROOT_URL = "http://38f1acd6bad1.ngrok.io/" + VERSION + SERVICE;
    public static final String ROOT_URL = "https://backoffice.ttaudit.com/" +  SERVICE;
    public static final String URL_GET_USER = "users/2";
    public static final String URL_GET_LOGIN_USER = "/api/sanctum/token";
    public static final String BEARER = "2e889186461b1ff8cad15fe0f9525394c74ed4deff7c49087c40c9865bbe4f815";
}
