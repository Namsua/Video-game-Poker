package com.example.pokkergraafiline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Pakk {
    private final List<Kaart> kaardid = new ArrayList<>();

    public Pakk() {
        //Genereerime täispaki, selleks kasutame Kaart klassi konstante

        for (char c : Kaart.getSOBIVADMASTID()) {
            for (String s : Kaart.getSOBIVADTUGEVUSED()) {
                kaardid.add(new Kaart(s, c));
            }
        }
    }

    public void sega() {
        Collections.shuffle(kaardid);
    }

    public Kaart võtaKaart() {
        if (kaardid.size() == 0) {
            throw new RuntimeException("Pakk on tühi!");
        }

        Kaart tagastus = kaardid.get(0);
        kaardid.remove(0);

        //Kuna me kaarti ennast kuskil ei muuda, tagastame viite
        return tagastus;
    }

}
