package com.example.pokkergraafiline;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("VideoPoker");

        BorderPane avamenüü = new BorderPane(); //Esimene tseen, mis kasutajale kuvatakse
        VBox avamenüüValikud = new VBox(15);
        avamenüüValikud.setAlignment(Pos.BASELINE_CENTER);
        VBox tabloo = new VBox(10); //Kuvab kasutajale tema parimad tulemused
        tabloo.setAlignment(Pos.TOP_LEFT);
        VBox logo = new VBox(10); //VBox avalehe logo jaoks

        //Avalehe pilt
        Image avalehepilt = new Image(Paths.get("build/resources/main/Videopoker.png").toUri().toString());
        ImageView avalehepildikuva = new ImageView(avalehepilt);
        avalehepildikuva.fitWidthProperty().bind(stage.widthProperty());
        avalehepildikuva.setPreserveRatio(true);
        logo.getChildren().add(avalehepildikuva);

        //Parimate mängijate tabel
        Label parimad = new Label("Parimad mängijad:               ");
        Label tulemused = new Label(kuvaTulemusFailist("parimadtulemused.txt"));  //Kuvab parimad mängijad tekstina
        parimad.setFont(Font.font(20));
        tulemused.setFont(Font.font(15));
        tabloo.getChildren().addAll(parimad,tulemused);

        avamenüü.setCenter(avamenüüValikud);
        avamenüü.setRight(tabloo);
        avamenüü.setTop(logo);

        Button alustaMänguNupp = new Button("Alusta mängu"); //Nupu vajutusel vahetub stseen mängu tseeniga
        Button reeglid = new Button("Reeglid"); //Kirjeldab mängu põhimõtte ja erinevad võidukombinatsioonid
        alustaMänguNupp.setFont(Font.font(18));
        reeglid.setFont(Font.font(18));

        avamenüüValikud.getChildren().addAll(alustaMänguNupp, reeglid);

        Scene avamenüüStseen = new Scene(avamenüü, 500, 700);

        //Layoutina kasutame gridpane-i, kus esimene rida on kaardi pildid ja teine rida vastavad nupud
        GridPane kaardidKoosNuppudega = new GridPane();
        kaardidKoosNuppudega.setHgap(10);
        kaardidKoosNuppudega.setVgap(20);

        Label info = new Label("Sisesta soovitud panus"); //Seda kasutame kasutajaga suhtlemiseks
        info.setFont(Font.font(20));

        Label hetkeBilanss = new Label("Bilanss: 100");
        hetkeBilanss.setFont(Font.font(30));
        hetkeBilanss.setAlignment(Pos.CENTER_RIGHT);

        //Lubab kasutajal oma panust sisestada
        TextField hetkePanus = new TextField();
        hetkePanus.setPromptText("Panus");
        hetkePanus.setFont(Font.font(25));

        //5 kaarti koos vastavate nuppudega, mille seast kasutaja peab valiku tegema
        //Igasse kaarti tuleb lisada pilt vastavast kaardist ja nupp
        //Lisame kaartidele vastavad pildid

        //Nime tuleb mängijalt eraldi küsida, seal saaks ka erindite püüdmise sisse tuua
        Mangija mangija = new Mangija(100, "placeholder");
        Kontroller kontroller = new Kontroller();

        //Kasutame kasutajale mängu lõpust teatamiseks
        Alert mängLäbiAlert = new Alert(Alert.AlertType.INFORMATION);
        mängLäbiAlert.setTitle("Mäng läbi!");
        mängLäbiAlert.setHeaderText("Kahjuks said karvapallid otsa!");

        //Alguses kuvame kaartide taguse, kuna panust pole veel tehtud
        Image kaart1Pilt = new Image("/kaardid/tagus.png");
        Image kaart2Pilt = new Image("/kaardid/tagus.png");
        Image kaart3Pilt = new Image("/kaardid/tagus.png");
        Image kaart4Pilt = new Image("/kaardid/tagus.png");
        Image kaart5Pilt = new Image("/kaardid/tagus.png");

        //Nupud, et kasutaja saaks märkida vastavad kaardid valituks

        Button kaart1Nupp = new Button("O");
        Button kaart2Nupp = new Button("O");
        Button kaart3Nupp = new Button("O");
        Button kaart4Nupp = new Button("O");
        Button kaart5Nupp = new Button("O");
        Button teostaVahetusNupp = new Button("Vaata kaarte");
        teostaVahetusNupp.setDisable(true);
        Button cashOut = new Button("Cash out");
        cashOut.setFont(Font.font(20));

        kaart1Nupp.setFont(Font.font(30));
        kaart2Nupp.setFont(Font.font(30));
        kaart3Nupp.setFont(Font.font(30));
        kaart4Nupp.setFont(Font.font(30));
        kaart5Nupp.setFont(Font.font(30));
        teostaVahetusNupp.setFont(Font.font(20));

        //Lisame panuse sisestus väljale kontrolli, et sisestatu oleks ikka õige
        hetkePanus.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!valideeri(newValue, mangija.getHetkeBalanss())) {
                teostaVahetusNupp.setDisable(true);

                hetkePanus.setStyle(" -fx-text-box-border: red; -fx-focus-color: red ;");
            } else {
                teostaVahetusNupp.setDisable(false);
                hetkePanus.setStyle("");
            }
        });

        //Vahetab kaardi aluseid nuppue vastavalt "X" või "O"
        EventHandler<MouseEvent> kaardiNupuEvent = event -> {
            Button vajutatudNupp = (Button) event.getSource();

            if (vajutatudNupp.getText().equals("O")) {
                vajutatudNupp.setText("X");
            } else {
                vajutatudNupp.setText("O");
            }
        };

        kaart1Nupp.setOnMouseClicked(kaardiNupuEvent);
        kaart2Nupp.setOnMouseClicked(kaardiNupuEvent);
        kaart3Nupp.setOnMouseClicked(kaardiNupuEvent);
        kaart4Nupp.setOnMouseClicked(kaardiNupuEvent);
        kaart5Nupp.setOnMouseClicked(kaardiNupuEvent);

        //Vajalikud piltide kuvamiseks
        ImageView kaart1Kuva = new ImageView(kaart1Pilt);
        ImageView kaart2Kuva = new ImageView(kaart2Pilt);
        ImageView kaart3Kuva = new ImageView(kaart3Pilt);
        ImageView kaart4Kuva = new ImageView(kaart4Pilt);
        ImageView kaart5Kuva = new ImageView(kaart5Pilt);

        //Kui kasutaja vajutab vaheta nuppu
        teostaVahetusNupp.setOnMouseClicked(e -> {
            if (teostaVahetusNupp.getText().equals("Vaheta")) {

                //Leiame indeksid, millel kaardid ära vahetada
                ArrayList<Integer> indeksid = new ArrayList<>();


                if (kaart1Nupp.getText().equals("X")) indeksid.add(0);
                if (kaart2Nupp.getText().equals("X")) indeksid.add(1);
                if (kaart3Nupp.getText().equals("X")) indeksid.add(2);
                if (kaart4Nupp.getText().equals("X")) indeksid.add(3);
                if (kaart5Nupp.getText().equals("X")) indeksid.add(4);

                //Vahetame valitud kaardid välja
                mangija.vahetaKaardid(indeksid.stream().mapToInt(x -> x).toArray());

                kaart1Kuva.setImage(new Image("/kaardid/" + mangija.getKaardid().get(0) + ".png"));
                kaart2Kuva.setImage(new Image("/kaardid/" + mangija.getKaardid().get(1) + ".png"));
                kaart3Kuva.setImage(new Image("/kaardid/" + mangija.getKaardid().get(2) + ".png"));
                kaart4Kuva.setImage(new Image("/kaardid/" + mangija.getKaardid().get(3) + ".png"));
                kaart5Kuva.setImage(new Image("/kaardid/" + mangija.getKaardid().get(4) + ".png"));

                String tulemus = kontroller.kontrolliKaarte(mangija.getKaardid()); //Kontrollime, kas kaartide seas leidub mingi võidu kombinatsioon

                if (!tulemus.equals("Mitte midagi")) {
                    //Kontroller.KÄSITOKORDAJA võimaldab meil kombinatsiooni teades leida vastav kordaja (rohkem leiab Kontroller.java)
                    double võidusumma = Math.round((double) (Kontroller.KÄSITOKORDAJA.get(tulemus)) * Double.parseDouble(hetkePanus.getText()) * 100.0) / 100.0;
                    info.setText(tulemus + "! " + "Palju õnne, võitsite " + võidusumma + " karvapalli!");

                    mangija.setHetkeBalanss(mangija.getHetkeBalanss() + võidusumma);
                    hetkeBilanss.setText("Bilanss: " + mangija.getHetkeBalanss());
                } else {
                    info.setText("Kahjuks seekord ei vedanud. Kaotasite " + Double.parseDouble(hetkePanus.getText()) + " karvapalli.");
                    mangija.setHetkeBalanss(mangija.getHetkeBalanss() - Double.parseDouble(hetkePanus.getText()));

                    if (Math.round(mangija.getHetkeBalanss() * 100.0) / 100.0 <= 0.0) {
                        hetkeBilanss.setText("Bilanss: 0");
                        mängLäbiAlert.showAndWait();
                        stage.setScene(avamenüüStseen);
                    } else {
                        hetkeBilanss.setText("Bilanss: " + mangija.getHetkeBalanss());
                    }
                }

                teostaVahetusNupp.setText("Jaga uued");
            } else if (teostaVahetusNupp.getText().equals("Jaga uued")){ //Jagame uued kaardid
                kaart1Kuva.setImage(new Image("/kaardid/tagus.png"));
                kaart2Kuva.setImage(new Image("/kaardid/tagus.png"));
                kaart3Kuva.setImage(new Image("/kaardid/tagus.png"));
                kaart4Kuva.setImage(new Image("/kaardid/tagus.png"));
                kaart5Kuva.setImage(new Image("/kaardid/tagus.png"));

                kaart1Nupp.setText("O");
                kaart2Nupp.setText("O");
                kaart3Nupp.setText("O");
                kaart4Nupp.setText("O");
                kaart5Nupp.setText("O");

                teostaVahetusNupp.setText("Vaata kaarte");
                info.setText("Sisesta soovitud panus");
                hetkePanus.setDisable(false);

                //Kuna kaotades võib juhtuda, et eelmine sisestatud panus on liiga kõrga, teeme ka siin validatsiooni
                if (!valideeri(hetkePanus.getText(), mangija.getHetkeBalanss())) {
                    teostaVahetusNupp.setDisable(true);

                    hetkePanus.setStyle(" -fx-text-box-border: red; -fx-focus-color: red ;");
                } else {
                    teostaVahetusNupp.setDisable(false);
                    hetkePanus.setStyle("");
                }
            } else { //Asendame kaardi tagused kaartide enditega
                mangija.jagaKaardid();
                hetkePanus.setDisable(true);
                info.setText("Vali kaardid, mida soovid vahetada");

                kaart1Kuva.setImage(new Image("/kaardid/" + mangija.getKaardid().get(0) + ".png"));
                kaart2Kuva.setImage(new Image("/kaardid/" + mangija.getKaardid().get(1) + ".png"));
                kaart3Kuva.setImage(new Image("/kaardid/" + mangija.getKaardid().get(2) + ".png"));
                kaart4Kuva.setImage(new Image("/kaardid/" + mangija.getKaardid().get(3) + ".png"));
                kaart5Kuva.setImage(new Image("/kaardid/" + mangija.getKaardid().get(4) + ".png"));

                teostaVahetusNupp.setText("Vaheta");
            }
        });

        cashOut.setOnMouseClicked(e -> {
            try {
                kirjutaTulemusFaili(mangija.getNimi(), mangija.getHetkeBalanss());
                tulemused.setText(kuvaTulemusFailist("parimadtulemused.txt"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            stage.setScene(avamenüüStseen);
        });

        //Siin seame piltidele vastavad suurused, mis on seotud meie root gridpane-iga, et rakendust oleks võimalik resizeida
        //.subtract(100), et nupule jääks ka ruumi
        kaart1Kuva.fitHeightProperty().bind(kaardidKoosNuppudega.heightProperty().subtract(100));
        kaart2Kuva.fitHeightProperty().bind(kaardidKoosNuppudega.heightProperty().subtract(100));
        kaart3Kuva.fitHeightProperty().bind(kaardidKoosNuppudega.heightProperty().subtract(100));
        kaart4Kuva.fitHeightProperty().bind(kaardidKoosNuppudega.heightProperty().subtract(100));
        kaart5Kuva.fitHeightProperty().bind(kaardidKoosNuppudega.heightProperty().subtract(100));

        //Kaardid peavad hoidma oma algset aspect ratiot kui kasutaja akna suurust muudab
        kaart1Kuva.setPreserveRatio(true);
        kaart2Kuva.setPreserveRatio(true);
        kaart3Kuva.setPreserveRatio(true);
        kaart4Kuva.setPreserveRatio(true);
        kaart5Kuva.setPreserveRatio(true);

        //Jagame 5-ga, kuna meil on vaja ära mahutada 5 kaarti
        kaart1Kuva.fitWidthProperty().bind(kaardidKoosNuppudega.widthProperty().divide(5));
        kaart2Kuva.fitWidthProperty().bind(kaardidKoosNuppudega.widthProperty().divide(5));
        kaart3Kuva.fitWidthProperty().bind(kaardidKoosNuppudega.widthProperty().divide(5));
        kaart4Kuva.fitWidthProperty().bind(kaardidKoosNuppudega.widthProperty().divide(5));
        kaart5Kuva.fitWidthProperty().bind(kaardidKoosNuppudega.widthProperty().divide(5));

        //Asetame vastavad elemendid oma kohtadele
        GridPane.setConstraints(kaart1Kuva, 0, 1);
        GridPane.setConstraints(kaart2Kuva, 1, 1);
        GridPane.setConstraints(kaart3Kuva, 2, 1);
        GridPane.setConstraints(kaart4Kuva, 3, 1);
        GridPane.setConstraints(kaart5Kuva, 4, 1);

        GridPane.setConstraints(kaart1Nupp, 0, 2);
        GridPane.setConstraints(kaart2Nupp, 1, 2);
        GridPane.setConstraints(kaart3Nupp, 2, 2);
        GridPane.setConstraints(kaart4Nupp, 3, 2);
        GridPane.setConstraints(kaart5Nupp, 4, 2);

        GridPane.setConstraints(info, 0, 0);
        GridPane.setConstraints(teostaVahetusNupp, 0, 3);
        GridPane.setConstraints(hetkeBilanss, 1, 3);
        GridPane.setConstraints(hetkePanus, 3, 3);
        GridPane.setConstraints(cashOut, 4, 3);

        //Lubame info label-il kasutada ära kogu horisontaalse ruumi (ainukene element esimesel real)
        GridPane.setColumnSpan(info, 5);

        //Määrame iga veeru suuruse ja seame ka joondamise
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(20);
        column1.setHalignment(HPos.CENTER);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(20);
        column2.setHalignment(HPos.CENTER);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(20);
        column3.setHalignment(HPos.CENTER);
        ColumnConstraints column4 = new ColumnConstraints();
        column4.setPercentWidth(20);
        column4.setHalignment(HPos.CENTER);
        ColumnConstraints column5 = new ColumnConstraints();
        column5.setPercentWidth(20);
        column5.setHalignment(HPos.CENTER);

        kaardidKoosNuppudega.getChildren().addAll(kaart1Kuva, kaart2Kuva, kaart3Kuva, kaart4Kuva, kaart5Kuva,
                kaart1Nupp, kaart2Nupp, kaart3Nupp, kaart4Nupp, kaart5Nupp, info, teostaVahetusNupp,
                hetkeBilanss, cashOut, hetkePanus);
        kaardidKoosNuppudega.getColumnConstraints().addAll(column1, column2, column3, column4, column5);

        Scene kaardid = new Scene(kaardidKoosNuppudega, 1000, 600);
        kaardidKoosNuppudega.setBackground(Background.fill(Color.KHAKI));

        alustaMänguNupp.setOnMouseClicked(e -> {
            TextInputDialog sisend = new TextInputDialog("Mängija nimi");
            sisend.setHeaderText("Sisesta oma nimi");

            //Kontrollib kas kasutaja vajutab ok nuppu, vastasel juhul naaseb avaaknale
            Optional<String> ok = sisend.showAndWait();
            if (ok.isPresent()) {
                mangija.setHetkeBalanss(100);
                mangija.setNimi(sisend.getEditor().getText());

                kaart1Kuva.setImage(new Image("/kaardid/tagus.png"));
                kaart2Kuva.setImage(new Image("/kaardid/tagus.png"));
                kaart3Kuva.setImage(new Image("/kaardid/tagus.png"));
                kaart4Kuva.setImage(new Image("/kaardid/tagus.png"));
                kaart5Kuva.setImage(new Image("/kaardid/tagus.png"));

                kaart1Nupp.setText("O");
                kaart2Nupp.setText("O");
                kaart3Nupp.setText("O");
                kaart4Nupp.setText("O");
                kaart5Nupp.setText("O");

                hetkeBilanss.setText("Bilanss: " + mangija.getHetkeBalanss());

                teostaVahetusNupp.setText("Vaata kaarte");
                info.setText("Sisesta soovitud panus");
                hetkePanus.setDisable(false);

                stage.setScene(kaardid);
            } else {
                stage.setScene(avamenüüStseen);
            }
        });

        //Reegliteaken
        VBox reegliteV = new VBox(15);
        reegliteV.setAlignment(Pos.TOP_CENTER);

        Label reeglitiitel = new Label("Reeglid:");
        reeglitiitel.setFont(Font.font(20));
        Label reegliL = new Label(kuvaTulemusFailist("reeglid.txt"));
        reegliL.setWrapText(true);
        reegliL.setFont(Font.font(15));

        //Tagasi avaaknale
        Button tagasi = new Button("Tagasi");
        tagasi.setFont(Font.font(18));

        reegliteV.getChildren().addAll(reeglitiitel, reegliL, tagasi);
        Scene reegliS = new Scene(reegliteV, 1000, 500);

        reeglid.setOnMouseClicked(e -> {
            stage.setScene(reegliS);
        });

        tagasi.setOnMouseClicked(e -> {
            stage.setScene(avamenüüStseen);
        });

        stage.setScene(avamenüüStseen);
        stage.show();
    }

    /**
     * Valideerib, et sisestatud panus oleks sobilik
     * @param a
     * @return
     */
    private static boolean valideeri(String a, double bilanss) {
        try {
            double sisestatudVäärtus = Double.parseDouble(a);

            if (sisestatudVäärtus <= bilanss && sisestatudVäärtus > 0) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void kirjutaTulemusFaili(String nimi, double tulemus) throws Exception{
        //Kirjutame tulemuse faili vaid siis, kui see mahub top 10sse

        String rida;

        try (BufferedReader sisend = new BufferedReader(new InputStreamReader(new FileInputStream("parimadtulemused.txt"), StandardCharsets.UTF_8))) {
            ArrayList<String> parimadNimed  = new ArrayList<>();
            ArrayList<Double> parimadTulemused = new ArrayList<>();

            while ((rida = sisend.readLine()) != null) {
                String[] jupid = rida.split(":");

                parimadNimed.add(jupid[0]);
                parimadTulemused.add(Double.parseDouble(jupid[1]));
            }

            //Lisame need listi lõpu olukorra jaoks, kui edetabelis pole veel kõiki 10 tulemust
            parimadNimed.add(nimi);
            parimadTulemused.add(tulemus);

            //Nüüd vaatame, kas antud tulemus on top10 seas

            for (int i = 0; i < parimadTulemused.size() && i < 10; i++) {
                if (tulemus > parimadTulemused.get(i)) {
                    parimadTulemused.add(i, tulemus);
                    parimadNimed.add(i, nimi);
                    break;
                }
            }

            //Nüüd tuleb see list lihtsalt faili tagasi kirjutada

            try (BufferedWriter väljund = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("parimadtulemused.txt"), StandardCharsets.UTF_8))) {
                for (int i = 0; i < parimadNimed.size() && i < 10; i++) {
                    väljund.write( parimadNimed.get(i) + ": " + parimadTulemused.get(i) + "\n");
                }
            }
        } catch (FileNotFoundException e) { //Ainukene erind, millest taastuda saame
            //Sisestatud tulemus on esimene, peame uue faili looma
            try (BufferedWriter väljund = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("parimadtulemused.txt"), StandardCharsets.UTF_8))) {
                väljund.write(nimi + ": " + tulemus + "\n");
            }
        }
    }

    //Loeb tulemused failist ja kuvab need
    private static String kuvaTulemusFailist(String fail){
        StringBuilder tulemustetekst = new StringBuilder();
        String tekst;

        try (FileInputStream tulemused = new FileInputStream(fail);
             InputStreamReader isr = new InputStreamReader(tulemused, StandardCharsets.UTF_8);
             BufferedReader tulemusteread = new BufferedReader(isr)
        ) {
            while ((tekst = tulemusteread.readLine()) != null) {
                tulemustetekst.append(tekst).append("\n");
            }
        } catch (FileNotFoundException e) { //Ainukene erind, millest taastuda saame
            //Kui sellist faili pole, kuvame tablool vastava sõnumi
            return "Ühtegi tulemust pole veel salvestatud!";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.valueOf(tulemustetekst);
    }

    public static void main(String[] args) throws Exception{
        launch();
    }
}