package com.gordolio.budgie.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class MainController implements Initializable {

    @FXML
    private Label pitchLabel;
    @FXML
    private Slider pitchSlider;
    @FXML
    private Label volumeLabel;
    @FXML
    private Slider volumeSlider;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.volumeLabel.textProperty().bind(Bindings.format("%1$,.2f",this.volumeSlider.valueProperty()));
        this.pitchLabel.textProperty().bind(Bindings.format("%1$,.2f", this.pitchSlider.valueProperty()));
    }
}
