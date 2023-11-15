package com.dataservicios.aliados.model;

import com.j256.ormlite.field.DatabaseField;

public class AwardDetail {
    @DatabaseField(id = true)
    private int id;

    /*"program_id": 1,
            "client_id": 1,
            "category_id": 4,
            "plan": 198909,
            "plan_total": 1104598,
            "realv": 64182,
            "real_total": 765109,
            "avance": 32,
            "avance_total": 69,
            "points": 428,
            "point_total": 5101,
            "keyv_total": 0,
            "point_real": 0,
            "point_real_total": 0,
            "fecha": "2023-11-10 00:00:00",*/
    private int program_id;

}
