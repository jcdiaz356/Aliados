package com.devsystem.aliados.repo;

import android.app.Activity;

public interface ClientProgramRepo {

    /**
     * Obtiene Datos del periodo selecionado
     * @param userId
     * @param programId
     * @param activity
     */
    void getClientProgram(int clientId, int programId, Activity activity);
}
