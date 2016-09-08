/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fontys.tuut.gui;

import com.fontys.tuut.Test;
import com.fontys.tuut.TestResult;
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
    
    public TestResultRow(Test test, TestResult result) {
        this.name        = new SimpleStringProperty(test.getClassName());
        this.result      = new SimpleStringProperty(result.wasSuccessful() ? "passed" : "failed"); 
        this.weight      = new SimpleStringProperty(String.valueOf(result.getTestBehavior().getWeight()));
        this.description = new SimpleStringProperty(result.getTestBehavior().getDescription());
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
