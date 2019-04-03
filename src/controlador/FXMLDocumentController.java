/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import DBAccess.ClinicDBAccess;
import java.util.ArrayList;
import model.*;
/**
 *
 * @author pereman2
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private Button bot_añadir;
    private ClinicDBAccess database;
    private ArrayList<Patient> pacientes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = ClinicDBAccess.getSingletonClinicDBAccess();
        
    }    
    
}
