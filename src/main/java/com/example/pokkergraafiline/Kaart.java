package com.example.pokkergraafiline;

import java.util.HashMap;
import java.util.Objects;

public class Kaart implements Comparable<Kaart>{
    private final String tugevus;
    private final char mast;
    private final static char[] SOBIVADMASTID = {'♣', '♦', '♠', '♥'};
    private final static String[] SOBIVADTUGEVUSED = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
    private final HashMap<String, Integer> tugevusToNum = new HashMap<>(); //Et saaksime võrrelda nn. pildikaarte

    public Kaart(String tugevus, char mast) {
        if (!kasSobib(tugevus, mast)) {
            throw new RuntimeException("Sellise tugevuse või mastiga kaarti ei saa teha");
        }
        this.tugevus = tugevus;
        this.mast = mast;

        //Initialiseerime ka hashmapi
        for (int i = 2; i <= 10; i++) {
            tugevusToNum.put(i + "", i);
        }

        tugevusToNum.put("J", 11);
        tugevusToNum.put("Q", 12);
        tugevusToNum.put("K", 13);
        tugevusToNum.put("A", 14);
    }

    /**
     * Kontrollib, kas antud tugevuse ja mastiga on lubatud kaarti moodustada
     * @param tugevus
     * @param mast
     * @return
     */
    private boolean kasSobib(String tugevus, char mast) {

        for (char c : SOBIVADMASTID) {
            if (c == mast) {
                for (String s : SOBIVADTUGEVUSED) {
                    if (s.equals(tugevus)) return true;
                }
            }
        }

        return false;
    }

    public String getTugevus() {
        return tugevus;
    }

    public char getMast() {
        return mast;
    }

    @Override
    public String toString() {
        return tugevus + mast;
    }

    //Kuna me neid kahte ei muuda, võime siin viite tagastada
    public static char[] getSOBIVADMASTID() {
        return SOBIVADMASTID;
    }

    public static String[] getSOBIVADTUGEVUSED() {
        return SOBIVADTUGEVUSED;
    }

    //Need kaks meetodit genereeris IntelliJ ise
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kaart kaart = (Kaart) o;
        return mast == kaart.mast && tugevus.equals(kaart.tugevus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tugevus, mast);
    }

    @Override
    public int compareTo(Kaart o) {
        return tugevusToNum.get(this.tugevus) - tugevusToNum.get(o.getTugevus());
    }

    public boolean onVõrdne(Kaart kaart) {
        return this.tugevus.equals(kaart.getTugevus());
    }
}
