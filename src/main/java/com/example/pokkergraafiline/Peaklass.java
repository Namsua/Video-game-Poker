package com.example.pokkergraafiline;

import java.util.Scanner;

public class Peaklass {
    public static void main(String[] args) {
        Scanner enter = new Scanner(System.in);
        System.out.println("Tere tulemast mängima video pokkerit! Maja poolt anname teile tervelt 10 karvapalli millega panuseid teha!");
        System.out.println("Mängu alustamiseks vajutage Enter klahvi.");
        enter.nextLine();

        Mangija mängija = new Mangija(10);
        Kontroller kontrollija = new Kontroller();

        String vahetus, tulemus;
        double hetkePanus;

        while (true) {
            do {
                System.out.print("Sisestage soovitud panus (max " + Math.round(mängija.getHetkeBalanss() * 100.0) / 100.0 + "): ");
                hetkePanus = enter.nextDouble();
                enter.nextLine(); //Flushime tolknema jäänud newlinei
            } while (hetkePanus <= 0.0 || hetkePanus > Math.round(mängija.getHetkeBalanss() * 100.0) / 100.0); //Küsime panust seni, kuni kasutaja sisestab valiidse panuse

            mängija.setHetkeBalanss(mängija.getHetkeBalanss() - Math.round(hetkePanus * 100.0) / 100.0); //Võtame panuse mängija karvapallidest maha
            mängija.kuvaKaardid();

            System.out.println("Kaartide vahetamiseks sisestage komaga eraldatult kaartide järjekorranumbrid vasakult paremale [1 - 5].");
            System.out.print("Kui kaarte vahetada ei soovi, vajutage lihtsalt Enter: ");

            vahetus = enter.nextLine();

            if (!vahetus.equals("")) { //Kui mängija soovib mingeid kaarte vahetada
                String[] jupid = vahetus.split(",");
                int[] indeksid = new int[jupid.length];

                for (int i = 0; i < indeksid.length; i++) {
                    int indeks = Integer.parseInt(jupid[i].strip()) - 1;

                    indeksid[i] = indeks;
                }

                mängija.vahetaKaardid(indeksid);
            }
            System.out.println(); //Paremaks vormindamiseks
            mängija.kuvaKaardid();

            tulemus = kontrollija.kontrolliKaarte(mängija.getKaardid()); //Kontrollime, kas kaartide seas leidub mingi võidu kombinatsioon

            if (!tulemus.equals("Mitte midagi")) {
                //Kontroller.KÄSITOKORDAJA võimaldab meil kombinatsiooni teades leida vastav kordaja (rohkem leiab Kontroller.java)
                double võidusumma = Math.round((double) (Kontroller.KÄSITOKORDAJA.get(tulemus)) * hetkePanus * 100.0) / 100.0;
                System.out.println(tulemus + "! " + "Palju õnne, võitsite " + võidusumma + " karvapalli!");
                System.out.println();

                mängija.setHetkeBalanss(mängija.getHetkeBalanss() + võidusumma);
            } else {
                System.out.println("Kahjuks seekord ei vedanud. Kaotasite " + hetkePanus + " karvapalli.");
                System.out.println();
                if (Math.round(mängija.getHetkeBalanss() * 100.0) / 100.0 <= 0.0) {
                    System.out.println("Kahjuks said karvapallid otsa, mäng läbi!");
                    break;
                }
            }

            mängija.jagaKaardid(); //Jagame uue raundi jaoks uued kaardid (kaardid jagatakse uuest 52 kaardilisest täispakist)
        }
    }
}
