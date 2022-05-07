package com.example.pokkergraafiline;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("VideoPokker");

        VBox avamenüü = new VBox(10); //Menüü, mis rakenduse käivitudes kasutajale kuvatakse
        avamenüü.setAlignment(Pos.BASELINE_CENTER);

        Button alustaMänguNupp = new Button("Alusta mängu"); //Nupu vajutusel vahetub stseen mängu tseeniga
        Button kuvaEdetabelNupp = new Button("Edetabel"); //Kuvab edetabeli stseeni

        avamenüü.getChildren().addAll(alustaMänguNupp, kuvaEdetabelNupp);

        Scene avamenüüStseen = new Scene(avamenüü, 500, 500);

        //Layoutina kasutame gridpane-i, kus esimene rida on kaardi pildid ja teine rida vastavad nupud
        GridPane kaardidKoosNuppudega = new GridPane();
        kaardidKoosNuppudega.setHgap(10);
        kaardidKoosNuppudega.setVgap(20);

        Label info = new Label("Vali kaardid, mida soovid välja vahetada"); //Seda kasutame kasutajaga suhtlemiseks
        info.setFont(Font.font(20));
        info.setAlignment(Pos.CENTER);

        //5 kaarti koos vastavate nuppudega, mille seast kasutaja peab valiku tegema
        //Igasse kaarti tuleb lisada pilt vastavast kaardist ja nupp
        //Lisame kaartidele vastavad pildid

        //Hetkel kõigil sama pilt, aga lõpuks tuleb muidugi pilt valida vastavalt kaardile
        Image kaart1Pilt = new Image("/kaardid/K♥.png");
        Image kaart2Pilt = new Image("/kaardid/K♥.png");
        Image kaart3Pilt = new Image("/kaardid/K♥.png");
        Image kaart4Pilt = new Image("/kaardid/K♥.png");
        Image kaart5Pilt = new Image("/kaardid/K♥.png");

        //Nupud, et kasutaja saaks märkida vastavad kaardid valituks

        Button kaart1Nupp = new Button("O");
        Button kaart2Nupp = new Button("O");
        Button kaart3Nupp = new Button("O");
        Button kaart4Nupp = new Button("O");
        Button kaart5Nupp = new Button("O");

        kaart1Nupp.setFont(Font.font(30));
        kaart2Nupp.setFont(Font.font(30));
        kaart3Nupp.setFont(Font.font(30));
        kaart4Nupp.setFont(Font.font(30));
        kaart5Nupp.setFont(Font.font(30));

        //Siia võiks midagi huvitavamat mõelda, nt. Button.setStyle()
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
                kaart1Nupp, kaart2Nupp, kaart3Nupp, kaart4Nupp, kaart5Nupp, info);
        kaardidKoosNuppudega.getColumnConstraints().addAll(column1, column2, column3, column4, column5);

        Scene kaardid = new Scene(kaardidKoosNuppudega, 1000, 500);

        stage.setScene(kaardid);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}