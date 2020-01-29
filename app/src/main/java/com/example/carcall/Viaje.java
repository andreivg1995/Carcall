package com.example.carcall;

public class Viaje {

    String id, cSalida, cLlegada, hSalida, hLlegada, uid;

    public Viaje(String id, String cSalida, String cLlegada, String hSalida, String hLlegada, String uid) {
        this.id = id;
        this.cSalida = cSalida;
        this.cLlegada = cLlegada;
        this.hSalida = hSalida;
        this.hLlegada = hLlegada;
        this.uid = uid;
    }

    public Viaje() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcSalida() {
        return cSalida;
    }

    public void setcSalida(String cSalida) {
        this.cSalida = cSalida;
    }

    public String getcLlegada() {
        return cLlegada;
    }

    public void setcLlegada(String cLlegada) {
        this.cLlegada = cLlegada;
    }

    public String gethSalida() {
        return hSalida;
    }

    public void sethSalida(String hSalida) {
        this.hSalida = hSalida;
    }

    public String gethLlegada() {
        return hLlegada;
    }

    public void sethLlegada(String hLlegada) {
        this.hLlegada = hLlegada;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
