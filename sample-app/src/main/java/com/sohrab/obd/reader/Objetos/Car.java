package com.sohrab.obd.reader.Objetos;

public class Car {

    public static final String PATH_CAR = "car";
    private  String Model;
    private String Plaque;
    private String macArduino;
    private String date;

    public Car() {

    }

    public Car(String model, String plaque, String macArduino, String date) {
        Model = model;
        Plaque = plaque;
        this.macArduino = macArduino;
        this.date = date;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getPlaque() {
        return Plaque;
    }

    public void setPlaque(String plaque) {
        Plaque = plaque;
    }

    public String getMacArduino() {
        return macArduino;
    }

    public void setMacArduino(String macArduino) {
        this.macArduino = macArduino;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Car{" +
                "Model='" + Model + '\'' +
                ", Plaque='" + Plaque + '\'' +
                ", macArduino='" + macArduino + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
