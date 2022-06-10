package com.example.roulette;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import roulette.BetType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
//třída aplikace
public class App extends Application {

    BetType type;
//integer pro vsazené číslo
    Integer betNumber;

    static List<Integer>
            blackNumbers = new ArrayList<>();

    static {
        blackNumbers.addAll(Arrays.asList(6, 15, 24, 33, 2, 8, 11, 17, 20, 26, 29, 35, 4, 10, 13, 22, 28, 31));

    }
//tyhle int nastaví vzhled sázecích tlačítek

    private final static Color BET_BTN_COLOR = Color.TRANSPARENT;
    private final static Color CARPET_GREEN = Color.rgb(23, 92, 12);
    private final static Color BUTTON_RED = Color.rgb(23, 92, 12);
    private final static Color BUTTON_BLACK = Color.rgb(23, 92, 12);
    private final static Font BET_BTN_FONT = Font.font("Lucida Sans Unicode", FontWeight.BOLD, FontPosture.REGULAR, 17);
    private final static Font NUM_BTN_FONT = Font.font("Lucida Sans Unicode", FontWeight.BOLD, FontPosture.REGULAR, 17);

    final static int BETBTN_MAX_HEIGHT = 50;
    final static int BETBTN_MAX_WIDTH = 105;

//tato metoda vytvoří stage, do které se vloží hrací plocha a tlačítka

    public void start(Stage stage) throws IOException {

//zde se vytvoří stage

        stage.setTitle("Ruleta");
        stage.setWidth(800);
        stage.setHeight(500);
        stage.setResizable(false);
        stage.show();

//zde se vytváří scéna a přidá se do stage

        Group root = new Group();
        Scene scene = new Scene(root, 300, 600, CARPET_GREEN);
        stage.setScene(scene);

//toto tlačítko umožňuje roztočit pomyslné kolo a začít hru, poté, co je daná sázka

        Button spinbtn = new Button();
        spinbtn.setText("SPIN");
        spinbtn.setPrefSize(105, 50);
        spinbtn.setAlignment(Pos.CENTER);
        spinbtn.setBorder((new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(3)))));
        spinbtn.setTextFill(Color.WHITE);
        spinbtn.setStyle("-fx-background-color: 0");
        spinbtn.setLayoutY(350);
        spinbtn.setLayoutX(335);
        spinbtn.setFont(BET_BTN_FONT);

//zde se udává co se stane, když se tlačítko stiskne, vygeneruje náhodné číslo, které se poté srovnává se sázkou

        spinbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    TimeUnit.SECONDS.sleep(2);

                } catch (InterruptedException e){
                    System.out.println(e.getMessage());
                }
                int min = 0;
                int max = 36;
                Integer result = (int)Math.floor(Math.random()*(max-min+1)+min);
                boolean isblack = blackNumbers.contains(result);


                Scene resultScene = createResultScene(winOrLose(result));
                stage.setScene(resultScene);

            }
        });
//vytvoří se gridpane, který dává dohromady gridpane s čísly a gridpane s ostatními tlačítky
        GridPane all = new GridPane();
        all.setLayoutX(50);
        all.setLayoutY(100);

//vytvoří se gridpane pro čísla, u kterého se určí vzhled
        GridPane grid = new GridPane();

        for(int i = 0; i < 12; i++) {
            for (int j = 0; j < 3; j++) {

                Button button = createBasicBtn();
                Integer text = 3 - j + i * 3;
                button.setText(text.toString());


//na tlačítku je číslo, které se rovná sázce, která bude položena, když se tlačítko stiskne
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Integer number = text;
                        betNumber = number;

                    }
                });
//upraví se vzhled gridpane
                grid.setStyle("-fx-background-radius: 0");
                grid.setAlignment(Pos.CENTER);
                grid.setStyle("-fx-background-color: #FFFFFF;");
                grid.setHgap(2);
                grid.setVgap(2);
                grid.setBorder((new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY, new BorderWidths(2)))));
//určí se, jakou bude mít tlačítko barvu, polde čísla, které na něm je
                if (blackNumbers.contains(text)) {
                    button.setStyle("-fx-background-color: #000000");
                } else {
                    button.setStyle("-fx-background-color: #DF2403");

                }
//přidá se tlačítko do gridpane
                grid.add(button, i, j);

            }
        }
//vytvoří se druhý gridpane s ostatními sázkami
        GridPane gridtwo = new GridPane();
//vytvoří se tlačítka sázek
        Button redbtn = createRedBtn();
        Button blackbtn = createBlackBtn();
        Button oddbtn = createOddBtn();
        Button evenbtn = createEvenBtn();
        Button firstbtn = createOneTo18();
        Button secbtn = createNineteenTo36();
//nastaví se vzhled gridpane
        gridtwo.setStyle("-fx-background-radius: #32CD32");
        gridtwo.setPrefWidth(625);
        gridtwo.setAlignment(Pos.CENTER);
        gridtwo.setStyle("-fx-background-color: #32CD32");
        gridtwo.setHgap(0);
        gridtwo.setVgap(1);






//spojí se tlačítko s typem sázky
        redbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                type = BetType.RED;
            }
        });


        blackbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                type = BetType.BLACK;
            }
        });

        oddbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                type = BetType.ODD;
            }
        });

        evenbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                type = BetType.EVEN;
            }
        });

        firstbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                type = BetType.ONE_TO_18;
            }
        });

        secbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                type = BetType.NINETEEN_TO_36;
            }
        });


//vyvtvoří se samostatné tlačítko 0, které se nachází v gridpane all
        Button zerobtn = createZeroBtn();
//spojí se tlačítko s typem sázky
        zerobtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                type = BetType.ZERO;
            }
        });
//přidájí se do gridpane všechny tlačítka
        gridtwo.add(redbtn, 0, 0);
        gridtwo.add(blackbtn, 1, 0);
        gridtwo.add(oddbtn, 2, 0);
        gridtwo.add(evenbtn, 3, 0);
        gridtwo.add(firstbtn, 4, 0);
        gridtwo.add(secbtn, 5, 0);
//gridape s cisly a gridpane s ostatními tlačítky se vloží do celkového gridpane, navíc s tlačítkem 0
        all.add(grid, 1, 0);
        all.add(gridtwo, 1,1 );
        all.add(zerobtn, 0, 0);


        stage.setX(900);
//přidá se náš složený gridpane a spin tlačítko do stage
        root.getChildren().addAll(all, spinbtn);

    }
//vytvoření scény s výsledkem
    private Scene createResultScene(boolean win) {
        Group resultgroup = new Group();
//        když se vygenerované číslo bude rovnat sázce, tak vyhraje, když ne, tak prohraje
        Label label = new Label();
        if (win) {
            label.setText("You win!");
        } else {
            label.setText("You lose!");
        }
        label.setTextFill(Color.BLACK);
        label.setMaxSize(50, 50);
//        přidá se nápis do scény
        resultgroup.getChildren().addAll(label);
        Scene resultscene = new Scene(resultgroup, 400, 300);
        return resultscene;
    }
//boolean pro výhru a prohru porovná typy a přiřadí k nim, v jakém případě na danou sázku vyhraje
    private boolean winOrLose(int result) {
        boolean win = false;
        switch (type) {
//            když se stiskne tlačítko 19 to 36, výhra se uskuteční, když výsledek bude větší než 18
            case NINETEEN_TO_36:
                win = result > 18;
                break;
//            když se stiskne tlačítko 1 to 18, výhra se uskuteční, když výsledek bude menší než 19
            case ONE_TO_18:
                win = result < 19;
                break;
//když se stiskne tlačítko Red, výhra se uskuteční, když výsledek nebude obsahovat černé čísla
            case RED:
                win = !blackNumbers.contains(result);
                break;
//když se stiskne tlačítko Black, výhra se uskuteční, když výsledek bude patřit do černých čísel
            case BLACK:
                win = blackNumbers.contains(result);
                break;
//když se stiskne tlačítko s jakýmkoliv číslem, kromě 0, výhra se uskuteční, když se výsledek bude rovnat danému vsazenému číslu
            case NUMBER:
                win = betNumber.equals(result);
                break;
//když se stiskne tlačítko 0, výhra se uskuteční, když výsledek bude 0
            case ZERO:
                win = result == 0;
                break;
//když se stiskne tlačítko odd, výhra se uskuteční, když výsledek bude lichý
            case ODD:
                win = result % 2 == 1;
                break;
//když se stiskne tlačítko even, výhra se uskuteční, když výsledek bude sudý
            case EVEN:
                win = result % 2 == 0;
                break;
//pokud bude přidán další typ do BetType, ale ne do tohoto booleanu, vypíše nám to chybu
            default:
                System.out.println("I do not know this type");
        }


        return win;
    }
//metoda pro tlačítko s čísly
    private Button createBasicBtn() {
//        vytvoření tlačítka
        Button button = new Button();
        button.setPrefSize(50, 50);
        button.setFont(BET_BTN_FONT);
        button.setTextFill(Color.WHITE);
        button.setAlignment(Pos.CENTER);

        return button;

    }

//metoda pro 0 tlačítko
    private Button createZeroBtn() {
        Button zerobtn = new Button();
        zerobtn.setText("0");
        zerobtn.setTextFill(Color.WHITE);
        zerobtn.setPrefSize(50, 160);
        zerobtn.setAlignment(Pos.CENTER);
        zerobtn.setFont(NUM_BTN_FONT);
        zerobtn.setBorder((new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(3)))));
        zerobtn.setStyle("-fx-background-color: #3CB371");
        return zerobtn;
    }
//metoda pro červené tlačítko
    private Button createRedBtn() {
        Button redbtn = new Button();
        redbtn.setText("  Red  ");
        redbtn.setTextFill(Color.WHITE);
        redbtn.setPrefSize(BETBTN_MAX_WIDTH, BETBTN_MAX_HEIGHT);
        redbtn.setAlignment(Pos.CENTER);
        redbtn.setFont(BET_BTN_FONT);
        redbtn.setBorder((new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(3)))));
        redbtn.setStyle("-fx-background-color: #3CB371");
        return redbtn;


    }
//metoda pro černé tlačítko
    private Button createBlackBtn() {
        Button blackbtn = new Button();
        blackbtn.setText(" Black ");
        blackbtn.setTextFill(Color.WHITE);
        blackbtn.setAlignment(Pos.CENTER);
        blackbtn.setPrefSize(BETBTN_MAX_WIDTH, BETBTN_MAX_HEIGHT);
        blackbtn.setStyle("-fx-background-color: #3CB371");
        blackbtn.setBorder((new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(3)))));
        blackbtn.setFont(BET_BTN_FONT);

        return blackbtn;
    }
//metoda pro liché tlačítko
    private Button createOddBtn() {
        Button oddbtn = new Button();
        oddbtn.setText("  Odd  ");
        oddbtn.setTextFill(Color.WHITE);
        oddbtn.setAlignment(Pos.CENTER);
        oddbtn.setPrefSize(BETBTN_MAX_WIDTH, BETBTN_MAX_HEIGHT);
        oddbtn.setBorder((new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(3)))));
        oddbtn.setStyle("-fx-background-color: #32CD32");
        oddbtn.setFont(BET_BTN_FONT);

        return oddbtn;
    }
//metoda pro sudé tlačítko
    private Button createEvenBtn() {
        Button evenbtn = new Button();
        evenbtn.setText(" Even ");
        evenbtn.setTextFill(Color.WHITE);
        evenbtn.setAlignment(Pos.CENTER);
        evenbtn.setPrefSize(BETBTN_MAX_WIDTH, BETBTN_MAX_HEIGHT);
        evenbtn.setBorder((new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(3)))));
        evenbtn.setStyle("-fx-background-color: #3CB371");
        evenbtn.setFont(BET_BTN_FONT);

        return evenbtn;

    }
//metoda pro tlačítko 1 to 18
    private Button createOneTo18() {
        Button firstbtn = new Button();
        firstbtn.setText("1 to 18");
        firstbtn.setTextFill(Color.WHITE);
        firstbtn.setAlignment(Pos.CENTER);
        firstbtn.setPrefSize(BETBTN_MAX_WIDTH, BETBTN_MAX_HEIGHT);
        firstbtn.setBorder((new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(3)))));
        firstbtn.setStyle("-fx-background-color: #3CB371");
        firstbtn.setFont(BET_BTN_FONT);

        return firstbtn;


    }
//metoda pro tlačítko 19 to 36
    private Button createNineteenTo36() {
        Button secbtn = new Button();
        secbtn.setText("19 to 36");
        secbtn.setTextFill(Color.WHITE);
        secbtn.setAlignment(Pos.CENTER);
        secbtn.setStyle("-fx-background-color: #3CB371");
        secbtn.setPrefSize(BETBTN_MAX_WIDTH, BETBTN_MAX_HEIGHT);
        secbtn.setBorder((new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(3)))));
        secbtn.setFont(BET_BTN_FONT);

        return secbtn;
    }
}



