/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fontys.tuut.gui;

import tuut.TestResult;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Twan Blum
 */
public class TestResultRow
{
    private final SimpleStringProperty name;
    private final SimpleStringProperty result;
    private final SimpleStringProperty weight;
    private final SimpleStringProperty description;
    
    public TestResultRow(TestResult result) {
        String test   = result.getTestBehavior().getTestClass().getClassName();
        String method = test.substring(0, test.indexOf("TestError")) + "." + result.getTestBehavior().getName().replace("_", "() ");
       
        this.name        = new SimpleStringProperty(method);
        this.result      = new SimpleStringProperty(result.wasSuccessful() ? "passed" : "failed"); 
        this.weight      = new SimpleStringProperty(String.valueOf(result.getTestBehavior().getWeight()));
        this.description = new SimpleStringProperty(result.wasSuccessful() ? "" :result.getTestBehavior().getDescription());
    }
    
    public SimpleStringProperty getName() {
        return this.name;
    }
    
    public SimpleStringProperty getResult() {
        return this.result;
    }
    
    public SimpleStringProperty getWeight() {
        return this.weight;
    }
    
    public SimpleStringProperty getDescription() {
        return this.description;
    }
}
