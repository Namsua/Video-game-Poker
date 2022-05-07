package com.example.pokkergraafiline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Kontroller {

    public static final Map<String, ? extends Number> KÄSITOKORDAJA = Map.of( //Võimaldab arvutada võidusummat
            "Kuninglik mastirida", 250.0,
            "Mastirida", 50.0,
            "Nelik", 25.0,
            "Maja", 9.0,
            "Mast", 6.0,
            "Rida", 4.0,
            "Kolmik", 3.0,
            "2 paari", 2.0,
            "1 paar", 1.0,
            "Mitte midagi", 0.0
    );

    public String kontrolliKaarte(ArrayList<Kaart> kaardidAlgne) {
        ArrayList<Kaart> kaardid = new ArrayList<>(kaardidAlgne); //Peame tegema koopia, et mitte orginaalset järjestust rikkuda
        //Hakkame kontrollima kõige paremast käest alates, ehk kuninglikust mastireast
        Collections.sort(kaardid); //Sorteerime kaardid kasvavasse järjekorda (seda läheb peaaegu kõikides kontrollides vaja)

        if (onKuninglikMastirida(kaardid)) return "Kuninglik mastirida";
        else if (onRida(kaardid) && onMast(kaardid)) return "Mastirida";
        else if (onNelik(kaardid)) return "Nelik"; //Nelik
        else if (onMaja(kaardid)) return "Maja"; //Maja (1 paar ja 1 kolmik)
        else if (onMast(kaardid)) return "Mast"; //Kõik sama masti
        else if (onRida(kaardid)) return "Rida"; //Tavaline rida
        else if (onKolmik(kaardid)) return "Kolmik"; //Tavaline kolmik
        else if (onKaksPaari(kaardid)) return "2 paari"; //Tavalised kaks paari
        else if (onPaarPoisse(kaardid)) return "1 paar"; //1 poiste või parem paar
        else return "Mitte midagi"; //Mitte midagi
    }

    private boolean onKuninglikMastirida(ArrayList<Kaart> kaardid) {
        //Vaatame, kas tegu on mastireaga
        if (onRida(kaardid) && onMast(kaardid)) {
            //Kontrollime, et sisalduksid nii äss kui ka 10 (kaardid on juba varasemalt sorteeritud)
            return kaardid.get(0).getTugevus().equals("10") && kaardid.get(4).getTugevus().equals("A");
        }

        return false;
    }

    private boolean onRida(ArrayList<Kaart> kaardid) {
        int eelmiseTugevus = tugevusToInt(kaardid.get(0).getTugevus());

        if (!kaardid.get(0).getTugevus().equals("2") || !kaardid.get(4).getTugevus().equals("A")) { //Seda olukorda tuleb vaadata eraldi
            for (int i = 1; i < 5; i++) {
                if (eelmiseTugevus + 1 != tugevusToInt(kaardid.get(i).getTugevus())) {
                    return false;
                } else {
                    eelmiseTugevus = tugevusToInt(kaardid.get(i).getTugevus());
                }
            }
        } else {
            //Kuna selliseid võimalusi on täpselt 4, kontrollime neid manuaalselt

            //Rida hakkab posist ja lõpeb 2ga
            if (kaardid.get(1).getTugevus().equals("J") && kaardid.get(0).getTugevus().equals("2")) return true;
            //Hakkab emandast ja lõpeb kolmega
            else if (kaardid.get(2).getTugevus().equals("Q") && kaardid.get(1).getTugevus().equals("3")) return true;
            //Hakkab kuningast ja lõpeb neljaga
            else if (kaardid.get(3).getTugevus().equals("K") && kaardid.get(2).getTugevus().equals("4")) return true;
            //Hakkab ässast ja lõpeb viiega
            else if (kaardid.get(4).getTugevus().equals("A") && kaardid.get(3).getTugevus().equals("5")) return true;
            else return false;

        }


        return true;
    }

    private boolean onMast(ArrayList<Kaart> kaardid) {
        Kaart eelmine = kaardid.get(0);

        for (int i = 1; i < 4; i++) {
            if (eelmine.getMast() != kaardid.get(i).getMast()) return false;
        }

        return true;
    }

    private boolean onNelik(ArrayList<Kaart> kaardid) {
       //Neliku puhul on kas 1. ja 4. või 2. ja 5. sama tugevusega;

        return kaardid.get(0).onVõrdne(kaardid.get(3)) || kaardid.get(1).onVõrdne(kaardid.get(4));
    }

    private boolean onMaja(ArrayList<Kaart> kaardid) {
        //Maja puhul on 1. ja 3. ning 4. ja 5. või 1. ja 2. ning 3. ja 5. sama tugevusega
        if (kaardid.get(0).onVõrdne(kaardid.get(2)) && kaardid.get(3).onVõrdne(kaardid.get(4))) return true;
        else return kaardid.get(0).onVõrdne(kaardid.get(1)) && kaardid.get(2).onVõrdne(kaardid.get(4));
    }

    private boolean onKolmik(ArrayList<Kaart> kaardid) {
        //3 eri võimalust
        for (int i = 0; i < 3; i++) {
            if (kaardid.get(i).onVõrdne(kaardid.get(i + 2))) return true;
        }

        return false;
    }


    private boolean onKaksPaari(ArrayList<Kaart> kaardid) {
        int paare = 0;

        Kaart eelmine = kaardid.get(0);

        for (int i = 1; i < 5; i++) {
            if (eelmine.onVõrdne(kaardid.get(i))) {
                paare++;
            } else {
                eelmine = kaardid.get(i);
            }
        }

        return paare == 2;
    }

    /**
     * Kontrollib kas kaartide seas on JJ, QQ, KK või AA
     * @param kaardid
     * @return
     */
    private boolean onPaarPoisse(ArrayList<Kaart> kaardid) {
        Kaart eelmine = kaardid.get(0);

        for (int i = 1; i < 5; i++) {
            if (eelmine.getTugevus().equals("J") || eelmine.getTugevus().equals("Q") || eelmine.getTugevus().equals("K") || eelmine.getTugevus().equals("A")) {
                if (eelmine.onVõrdne(kaardid.get(i))) return true;
                else eelmine = kaardid.get(i);
            }

            eelmine = kaardid.get(i);
        }

        return false;
    }

    /**
     * Teisendab kaardi tugevuse intiks
     * @param tugevus Stringina
     * @return
     */
    private int tugevusToInt(String tugevus) {
        return switch (tugevus) {
            case "J" -> 11;
            case "Q" -> 12;
            case "K" -> 13;
            case "A" -> 14;
            default -> Integer.parseInt(tugevus);
        };
    }
}
