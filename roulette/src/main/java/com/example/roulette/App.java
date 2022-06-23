package com.example.roulette;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import roulette.BetType;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
//třída aplikace

public class App extends Application {
    BetType type;
    //integer pro vsazené číslo

    Integer betNumber;

    static List<Integer> blackNumbers = new ArrayList<>();
    static {
        blackNumbers.addAll(Arrays.asList(6, 15, 24, 33, 2, 8, 11, 17, 20, 26, 29, 35, 4, 10, 13, 22, 28, 31));
        Collections.sort(blackNumbers);
    }
//tyhle inty nastaví vzhled sázecích tlačítek
    private final static int MIN_NUM = 0;
    private final static int MAX_NUM = 36;

    private final static int BUTTON_WIDTH = 60;
    private final static int BUTTON_HEIGHT = 60;

    private final static Color CARPET_GREEN = Color.rgb(23, 92, 12);
    private final static Color BUTTON_RED = Color.rgb(23, 92, 12);
    private final static Color BUTTON_BLACK = Color.rgb(23, 92, 12);

    private final static Paint TEXT_BTN_PAINT = Paint.valueOf("#ffffff");

    private final static Font BET_BTN_FONT = Font.font(20.0);
    private final static Font NUM_BTN_FONT = Font.font(20.0);

    private final static String RED_BTN_NAME = "RED";
    private final static String BLACK_BTN_NAME = "BLACK";
    private final static String EVEN_BTN_NAME = "EVEN";
    private final static String ODD_BTN_NAME = "ODD";
    private final static String ONE_TO_18_BTN_NAME = "1 TO 18";
    private final static String NINETEEN_TO_36_BTN_NAME = "19 TO 36";
    private final static String SPIN_BTN_NAME = "SPIN";
    private final static String NEW_GAME_BTN_NAME = "NEW GAME";

    private final static String YOU_WIN_LABEL = "YOU WIN";
    private final static String YOU_LOSE_LABEL = "YOU LOSE";

    private final static String PLACE_YOUR_BET_LABEL = "PLACE YOUR BET :)";

    private final static int SCENE_WIDTH = 13*BUTTON_WIDTH;

    private HashMap<Integer, Button> buttonHashMap = new HashMap<>();

    private HashMap<BetType, Button> betTypeButtonHashMap = new HashMap<>();

//tato metoda vytvoří stage, do které se vloží hrací plocha a tlačítka




    //zde se vytváří scéna a přidá se do stage
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, SCENE_WIDTH, 600, CARPET_GREEN);

        //vytvoří se gridpane pro čísla, u kterého se určí vzhled

        GridPane numGrid = createNumGrid();

        Button zeroBtn = createZeroBtn();

        Button blackBtn = createBlackBtn();
        Button redBtn = createRedBtn();
        Button evenBtn = createEvenBtn();
        Button oddBtn = createOddBtn();
        Button oneTo18Btn = oneTo18Btn();
        Button nineteenTo36Btn = nineteenTo36Btn();
//spojí se tlačítko s typem sázky
        betTypeButtonHashMap.put(BetType.BLACK, blackBtn);
        betTypeButtonHashMap.put(BetType.RED, redBtn);
        betTypeButtonHashMap.put(BetType.ZERO, zeroBtn);
        betTypeButtonHashMap.put(BetType.ODD, oddBtn);
        betTypeButtonHashMap.put(BetType.EVEN, evenBtn);
        betTypeButtonHashMap.put(BetType.ONE_TO_18, oneTo18Btn);
        betTypeButtonHashMap.put(BetType.NINETEEN_TO_36, nineteenTo36Btn);
//přidá se tlačítko do gridpane
        GridPane btnGrid = new GridPane();
        btnGrid.add(oneTo18Btn, 0, 0);
        btnGrid.add(evenBtn, 1, 0);
        btnGrid.add(redBtn, 2, 0);
        btnGrid.add(blackBtn, 3, 0);
        btnGrid.add(oddBtn, 4, 0);
        btnGrid.add(nineteenTo36Btn, 5, 0);
//toto tlačítko umožňuje roztočit pomyslné kolo a začít hru, poté, co je daná sázka


        Button spinBtn = createSpinBtn();

        Label label = new Label(PLACE_YOUR_BET_LABEL);
        label.setFont(BET_BTN_FONT);
        label.setVisible(false);

        spinBtn.setBackground(Background.EMPTY);
        spinBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (betNumber == null && type == null){
                    System.out.println(PLACE_YOUR_BET_LABEL);
                    label.setVisible(true);
                    return;
                }
                if (betNumber != null){
                    numBtnSetStyle(buttonHashMap.get(betNumber), betNumber);
                }
                if (type != null && type != BetType.NUMBER){
                    betBtnSetStyle(betTypeButtonHashMap.get(type));
                }
                Integer result = (int)Math.floor(Math.random()*(MAX_NUM-MIN_NUM+1)+MIN_NUM);
                boolean win = didYouWin(result);
                Scene winLoseScreen = createWinLoseScreen(stage, scene, win, result);
            }
        });


        GridPane firstRowGrid = new GridPane();
        firstRowGrid.add(spinBtn, 0, 0);
        firstRowGrid.add(numGrid, 1, 0);


        GridPane finalGrid = new GridPane();
        finalGrid.add(zeroBtn, 0, 0);
        finalGrid.add(numGrid, 1, 0);
        finalGrid.add(btnGrid, 1, 1);
        finalGrid.add(label, 1, 2);
        centerNode(spinBtn);
        centerNode(label);

        finalGrid.add(spinBtn, 1, 3);

        root.getChildren().addAll(finalGrid);

        stage.setScene(scene);
        stage.setTitle("ROULETTE");
        stage.setWidth(14*BUTTON_WIDTH);
        stage.setHeight(BUTTON_HEIGHT*8);
        stage.setResizable(false);
        stage.show();
    }

    private GridPane createNumGrid(){
        GridPane grid = new GridPane();
        for(int i = 0; i < 12; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
                button.setAlignment(Pos.CENTER);
                Integer text = 3 - j + i * 3 ;
                button.setText(text.toString()) ;
                numBtnSetStyle(button, text);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (betNumber != null){
                            numBtnSetStyle(buttonHashMap.get(betNumber), betNumber);
                        }
                        if (type != null && type != BetType.NUMBER){
                            betBtnSetStyle(betTypeButtonHashMap.get(type));
                        }
                        Integer number = text;
                        betNumber = number;
                        type = BetType.NUMBER;
                        button.setStyle("-fx-background-color: #f1c232; -fx-border-color: white;");
                    }
                });
                grid.add(button, i, j);
                buttonHashMap.put(text, button);
            }
        }
        return grid;
    }
//určí se, jakou bude mít tlačítko barvu, polde čísla, které na něm je

    private void numBtnSetStyle(Button button, int buttonText){
        if (blackNumbers.contains(buttonText)) {

            button.setStyle("-fx-background-color: #000000; -fx-border-color: white;");
        } else {
            button.setStyle("-fx-background-color: #DF2403; -fx-border-color: white;");
        }
        button.setTextFill(Paint.valueOf("#ffffff"));
        button.setFont(BET_BTN_FONT);
    }

    private Scene createWinLoseScreen(Stage stage, Scene newGameScene, boolean win, int result){

        Label label = new Label();
        Label resultLabel = new Label("And the result... " + result);
        resultLabel.setFont(Font.font(60));
        centerNode(resultLabel);

        GridPane gridPane = new GridPane();
        label.setFont(Font.font(30));
        label.setPrefSize(newGameScene.getWidth(), newGameScene.getHeight()/4);
        centerNode(label);
        label.setAlignment(Pos.CENTER);
        gridPane.add(resultLabel, 0, 0);
        gridPane.add(label, 0, 1);

        Button newGameBtn = new Button();
        newGameBtn.setPrefSize(BUTTON_WIDTH*3, BUTTON_HEIGHT*2);

        gridPane.add(newGameBtn, 0, 2);
        centerNode(newGameBtn);
        newGameBtn.setText(NEW_GAME_BTN_NAME);
        newGameBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                betNumber = null;
                type = null;
                stage.setScene(newGameScene);
            }
        });

        if (win){
            label.setText(YOU_WIN_LABEL);
        } else{
            label.setText(YOU_LOSE_LABEL);
        }
        Scene scene1 = new Scene(gridPane, 400, 400);
        stage.setScene(scene1);
        scene1.setFill(Paint.valueOf("#238217"));
        return scene1;
    }

    private Button createBasicBtn(BetType type, String text){
        Button btn = new Button();
        btn.setText(text);
        btn.setStyle("-fx-background-color: #238217; -fx-border-color: white;");
        btn.setTextFill(TEXT_BTN_PAINT);
        btn.setFont(Font.font(20.0));
        btn.setPrefSize(BUTTON_WIDTH*2, BUTTON_HEIGHT);
        btn.setAlignment(Pos.CENTER);
        btn.setOnAction(createEventHandler(type, btn));
        return btn;
    }

    private EventHandler<ActionEvent> createEventHandler(BetType betType, Button button){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(betNumber != null){
                    numBtnSetStyle(buttonHashMap.get(betNumber), betNumber);
                }
                betNumber = null;
                if (type != null && type != BetType.NUMBER){
                    betBtnSetStyle(betTypeButtonHashMap.get(type));
                }
                type = betType;
                if (betType == BetType.ZERO){

                }
                button.setStyle("-fx-background-color: #f1c232; -fx-border-color: white;");

            }
        };
    }

    private void betBtnSetStyle(Button btn){
        btn.setStyle("-fx-background-color: #238217; -fx-border-color: white;");
        btn.setTextFill(TEXT_BTN_PAINT);
        btn.setFont(Font.font(20.0));
    }

    private Button createZeroBtn(){
        Button zerobtn = createBasicBtn(BetType.ZERO, "0");
        zerobtn.setPrefSize(BUTTON_WIDTH, 3*BUTTON_HEIGHT);
        return zerobtn;
    }

    private Button createBlackBtn(){
        Button blackbtn = createBasicBtn(BetType.BLACK, BLACK_BTN_NAME);
        return blackbtn;
    }

    private Button createRedBtn(){
        Button redbtn = createBasicBtn(BetType.RED, RED_BTN_NAME);
        return redbtn;
    }

    private Button createOddBtn(){
        Button btn = createBasicBtn(BetType.ODD, ODD_BTN_NAME);
        return btn;

    }

    private Button createEvenBtn(){
        Button btn = createBasicBtn(BetType.EVEN, EVEN_BTN_NAME);
        return btn;

    }

    private Button oneTo18Btn(){
        Button btn = createBasicBtn(BetType.ONE_TO_18, ONE_TO_18_BTN_NAME);

        return btn;
    }

    private Button nineteenTo36Btn(){
        Button btn = createBasicBtn(BetType.NINETEEN_TO_36, NINETEEN_TO_36_BTN_NAME);
        return btn;
    }

    private Button createSpinBtn(){
        Button spinbtn = new Button(SPIN_BTN_NAME);
        spinbtn.setStyle("-fx-background-color: #238217; -fx-border-color: white;");
        spinbtn.setTextFill(TEXT_BTN_PAINT);
        spinbtn.setFont(Font.font(20.0));

        spinbtn.setPrefSize(BUTTON_WIDTH*4, BUTTON_HEIGHT);
        spinbtn.setAlignment(Pos.CENTER);
        return spinbtn;
    }
    //boolean pro výhru a prohru porovná typy a přiřadí k nim, v jakém případě na danou sázku vyhraje
    private boolean didYouWin(int result) {
        boolean win = false;
        switch(type){
            case NINETEEN_TO_36:
                win = result > 18;
                break;
            case ONE_TO_18:
                win = result < 19;
                break;
            case RED:
                win = !blackNumbers.contains(result);
                break;
            case BLACK:
                win = blackNumbers.contains(result);
                break;
            case NUMBER:
                win = betNumber.equals(result);
                break;
            case ZERO:
                win = result == 0;
                break;
            case ODD:
                win = result % 2 == 1;
                break;
            case EVEN:
                win = result % 2 == 0;
                break;
            ////pokud bude přidán další typ do BetType, ale ne do tohoto booleanu, vypíše nám to chybu
            default: System.out.println("I do not know this type");
        }

        return win;

    }

    private void wait(int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    private void centerNode(Node node){
        GridPane.setHalignment(node, HPos.CENTER);
        GridPane.setValignment(node, VPos.CENTER);
    }

    }













    