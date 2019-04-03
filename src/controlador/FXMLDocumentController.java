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
    private ObservableList<Patient> datos_pat = null;
    private ObservableList<Doctor> datos_doc = null;
    @FXML
    private TableView<Person> tabla;
    @FXML
    private TableColumn<Person, String> col_nombre;
    @FXML
    private TableColumn<Person, String> col_apellidos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //inicializa la base de datos en database
        database = ClinicDBAccess.getSingletonClinicDBAccess();
        //inicializa los arraylist de pacientes y doctores
        pacientes = database.getPatients();
        doctores = database.getDoctors();
        //inicializa observable lists de ambos
        datos_pat = FXCollections.observableArrayList(pacientes);
        datos_doc = FXCollections.observableArrayList(doctores);
    }   
    //inicializa tabla pacientes
    private void initPatients(){
        
        //tabla.setItems(datos_pat);
        col_nombre.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getName()));
        col_apellidos.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getSurname()));
    }
    //inicializa tabla doctores
    private void initDoctors(){
        //tabla.setItems(datos_doc);
        col_nombre.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getName()));
        col_apellidos.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getSurname()));
    }
    
}
