package sod.applc.modelo;

import android.provider.BaseColumns;

/**
 * Created by Ricard on 07/12/2017.
 */

public class Logs  {

    public static abstract  class Log implements BaseColumns{

        public static final String TABLE_NAME ="logs";

        public static final String ID = "id";
        public static final String ACCION = "accion";
        public static final String FECHA = "fecha";
        public static final String RESUL = "resultado";

    }
}
