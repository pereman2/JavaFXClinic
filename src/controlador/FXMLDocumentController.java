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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private ArrayList<Doctor> doctores;
    private ArrayList<Patient> pacientes;
    private ObservableList<Patient> datos = null;
    @FXML
    private TableView<Patient> tabla;
    @FXML
    private TableColumn<Patient, String> col_nombre;
    @FXML
    private TableColumn<Patient, String> col_apellidos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = ClinicDBAccess.getSingletonClinicDBAccess();
        pacientes = database.getPatients();
        doctores = database.getDoctors();
        datos = FXCollections.observableArrayList(pacientes);
        tabla.setItems(datos);
        col_nombre.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getName()));
        col_apellidos.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getSurname()));
    }    
    
}
