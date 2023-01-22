import javafx.scene.chart.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class InputController {
    Database database = Main.database;
    public HBox application;

    public Label currentAction;
    public TextField actionName;
    public Button addAction;
    public FlowPane suggestedActions;

    public OutputController outputController = new OutputController();
    public DatePicker startDatePicker, endDatePicker;
    public PieChart averageDuration;
    public LineChart<?, ?> actionsDynamic;

    public void initialize() {
        actionName.setOnKeyTyped(KeyEvent -> suggestActions(actionName.getText()));
        addAction.setOnAction(ActionEvent -> {database.changeAction(actionName.getText());
            actionName.setText("");
            suggestActions("");
            currentAction.setText(database.currentAction);});

        outputController.actionsDynamic = actionsDynamic;
        outputController.averageDuration = averageDuration;
        startDatePicker.setOnAction(ActionEvent -> outputController.checkRequest(startDatePicker.getValue(), endDatePicker.getValue()));
        endDatePicker.setOnAction(ActionEvent -> outputController.checkRequest(startDatePicker.getValue(), endDatePicker.getValue()));

        currentAction.setText(database.currentAction);
        suggestActions("");
        startDatePicker.setValue(database.diary.get(1).date);
        endDatePicker.setValue(LocalDate.now());
        outputController.checkRequest(startDatePicker.getValue(), endDatePicker.getValue());

        application.setOnMouseEntered(ActionEvent -> {});
    }
    public void suggestActions(String keyWord){
        suggestedActions.getChildren().clear();
        var notFound = new ArrayList <String>();
        for (String name : database.LastDay().actions.keySet())
            if (name.startsWith(keyWord)) {
                var changeAction = new Button(name);
                changeAction.setId("actionButton");
                changeAction.setOnAction(ActionEvent -> database.changeAction(changeAction.getText()));
                suggestedActions.getChildren().add(changeAction);
            }
            else
                notFound.add(name);
        for (String name : notFound){
            var changeAction = new Button(name);
            changeAction.setOnAction (ActionEvent -> database.changeAction(changeAction.getText()));
            suggestedActions.getChildren().add (changeAction);
        }
    }
}