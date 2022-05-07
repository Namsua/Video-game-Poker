package com.example.pokkergraafiline;

import java.util.ArrayList;

public class Mangija {
    private ArrayList<Kaart> kaardid; //See list kirjutatakse iga kaartide genereerimisega üle
    private Pakk pakk = new Pakk();
    private double hetkeBalanss; //Kuvame kasutajale, palju tal hetkel raha on. Kui 0 siis mäng läbi
    private String nimi;

    public Mangija(double hetkeBalanss, String nimi) {
        this.hetkeBalanss = hetkeBalanss;
        jagaKaardid(); //Jagame kaardid siin, et vältida päringuid initialiseerimata muutujast
        this.nimi = nimi;
    }

    public ArrayList<Kaart> getKaardid() {
        return kaardid;
    }

    public double getHetkeBalanss() {
        return hetkeBalanss;
    }

    public void setHetkeBalanss(double s) {this.hetkeBalanss = s;}

    /**
     * Genereerib 5 uut kaarti ja seob need mängijaga
     */
    public void jagaKaardid() {
        this.pakk = new Pakk(); //Kui hakkame kaarte uuesti jagama, on vaja uut pakki 52 kaardiga
        this.pakk.sega();
        ArrayList<Kaart> mangijaKaardid = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            mangijaKaardid.add(this.pakk.võtaKaart());
        }

        this.kaardid = mangijaKaardid;
    }

    /**
     * Asetab antud indeksitele uued kaardid, kusjuures välja vahetatud kaart ei saa vahetusega uuesti pakist tulla
     * @param indeksid
     */
    public void vahetaKaardid(int[] indeksid) { //Indeksid, millel kaart vahetada
        for (int i : indeksid) {
            this.kaardid.remove(i);
            this.kaardid.add(i, this.pakk.võtaKaart());
        }
    }

    /**
     * Kuvab mängija hetke kaardid konsooli
     */
    public void kuvaKaardid() {
        for (Kaart kaart : kaardid) {
            System.out.print(kaart + " ");
        }

        System.out.println();
    }
}
