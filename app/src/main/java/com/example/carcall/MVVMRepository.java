package com.example.carcall;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MVVMRepository {

    //Singleton
    private static MVVMRepository srepository;

    private static Context context;

    private MVVMRepository(Context context) {
        this.context = context;
    }
    public static MVVMRepository get(Context context){
        if (srepository == null){
            srepository = new MVVMRepository(context);
        }
        return srepository;
    }

    /******** DATE METHODS ********/

    /**
     * Devuelve la fecha en formato dd/MM/yyyy HH:mm:ss
     * @return
     */
    public static String fechaActualDiaHora(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        Date date = new Date();

        return dateFormat.format(date);
    }

    /**
     * Devuelve la fecha actual dd/MM/yyyy antes del espacio (sin la hora)
     * @param fechaEnteraActual
     * @return
     */
    public static String getDiaActual(String fechaEnteraActual){
        String[] fechaActual = fechaEnteraActual.split(" ");
        String dia = fechaActual[0];

        return dia;
    }

    /**
     * Devuelve la hora actual HH:mm:ss después del espacio (sin el día)
     * @param fechaEnteraActual
     * @return
     */
    public static String getHoraActual(String fechaEnteraActual){
        String[] fechaActual = fechaEnteraActual.split(" ");
        String hora = fechaActual[1];

        return hora;
    }

    /******** FIN DATE METHODS ********/
}
