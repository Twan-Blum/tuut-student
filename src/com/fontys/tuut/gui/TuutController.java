/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fontys.tuut.gui;

import com.fontys.tuut.Test;
import com.fontys.tuut.TestResult;
import com.fontys.tuut.runner.Tuut;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author Twan Blum
 */
public class TuutController implements Initializable
{
    private File project;
    private final ObservableList<TestResultRow> testResults = FXCollections.observableArrayList();
    
    @FXML private AnchorPane root;
    
    @FXML private AnchorPane projectPane;
    @FXML private Button     openButton;
    
    @FXML private AnchorPane testPane;
    @FXML private AnchorPane testActionPane;
    @FXML private AnchorPane testResultPane;
    @FXML private AnchorPane testResultLoadingPane;
    
    @FXML private ProgressIndicator testResultProgress;
    @FXML private ScrollPane testResultTablePane;
    @FXML private TableView  testResultTable;
    @FXML private TableColumn<TestResultRow, String> testNameColumn;
    @FXML private TableColumn<TestResultRow, String> testResultColumn;
    @FXML private TableColumn<TestResultRow, String> testWeightColumn;
    @FXML private TableColumn<TestResultRow, String> testDescriptionColumn;
    
    
    @FXML
    private void openTestAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(this.root.getScene().getWindow());

        if (selectedDirectory != null) {
            this.project = selectedDirectory.getAbsoluteFile();
            this.testPane.setVisible(true);
        }
    }
    
    @FXML
    private void runTestAction(ActionEvent event) {
        this.testResults.clear();

        final File path = this.project;
        
        Task runTestTask = new Task<Void>() {
            @Override protected Void call() throws Exception {
                Tuut tuut = new Tuut();
                tuut.addPath(path);
                tuut.run().forEach((Test test, List<TestResult> results) -> {
                    results.stream().forEach((result) -> {
                        testResults.add(new TestResultRow(test, result));
                    });
                });

               return null;
            }
        };

        this.testResultProgress.visibleProperty().bind(runTestTask.runningProperty());
        
        Thread runTestThread = new Thread(runTestTask);          
        runTestThread.start();
    }
    
    @FXML
    private void closeTestAction(ActionEvent event) {
        this.testResults.clear();
        this.testPane.setVisible(false);
    }
    
    private void showException(Exception ex) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Oops something went wrong!");
        alert.setHeaderText(null);
        alert.setContentText(ex.getMessage());

        alert.showAndWait();
        
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.testPane.setVisible(false);
        
        testNameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
        testResultColumn.setCellValueFactory(cellData -> cellData.getValue().getResult());
        testWeightColumn.setCellValueFactory(cellData -> cellData.getValue().getWeight());
        testDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescription());
        
        testResultColumn.setCellFactory(cellData -> {
            return new TableCell<TestResultRow, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((empty) ? "" : getItem());
                    
                    ObservableList<String> style= getTableRow().getStyleClass();
                    
                    if (!empty && item.equals("failed")) {
                        style.remove("result-status-success");
                        style.add("result-status-failed");
                    } else {
                        style.remove("result-status-failed");
                        style.add("result-status-success");
                    }
                }
            };
        });
        
        testResultTable.setPlaceholder(new Label(""));
        testResultTable.setItems(this.testResults);
    }    
    
}