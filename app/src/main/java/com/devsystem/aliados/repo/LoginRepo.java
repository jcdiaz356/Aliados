package com.devsystem.aliados.repo;

import android.app.Activity;

public interface LoginRepo {

    /**
     * Method para autenticación de inicio de sesión
     * @param code
     * @param password
     * @param sesionSave
     * @param activity
     */
    void signIn(String code, String password, boolean sesionSave, Activity activity);

}
