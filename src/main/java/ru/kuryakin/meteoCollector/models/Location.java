package ru.kuryakin.meteoCollector.models;

/***
 * Местоположение.
 */
public enum Location {
    STREET("street"),
    BALCONY("balcony"),
    ROOM("room");

    private final String name;

    Location(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId(){
        for (int i = 0; i < values().length; i++) {
            if(values()[i].getName().equals(name)) return i;
        }
        return -1;
    }
}