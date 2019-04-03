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
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
/**
 *
 * @author pereman2
 */
public class FXMLDocumentController implements Initializable {
    private int DOCTOR = 1;
    private int PATIENT = 2;
    private int APPOINTMENT = 3;
    
    private int actual;
    
    private Label label;
    private ClinicDBAccess database;
    //arraylist de doctores,pacientes
    private static ArrayList<Doctor> doctores;
    public static ArrayList<Patient> pacientes;
    private static ObservableList<Patient> datos_pat = null;
    private static ObservableList<Doctor> datos_doc = null;
    @FXML
    private TableColumn<Patient, String> col_nombre;
    @FXML
    private TableColumn<Patient, String> col_apellidos;
    @FXML
    private TableView<Patient> tabla_patient;
    @FXML
    private TableView<Doctor> tabla_doctor;
    @FXML
    private TableColumn<Doctor, String> col_nombre_doctor;
    @FXML
    private TableColumn<Doctor, String> col_apellidos_doctor;
    
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
        
        tabla_doctor.setVisible(false);
        tabla_patient.setVisible(false);
        
        initDoctors();
        initPatients();
        
        actual = 0;
    }   
    //inicializa tabla pacientes
    private void initPatients(){
        
        tabla_patient.setItems(datos_pat);
        col_nombre.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getName()));
        col_apellidos.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getSurname()));
    }
    //inicializa tabla doctores
    private void initDoctors(){
        tabla_doctor.setItems(datos_doc);
        col_nombre_doctor.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getName()));
        col_apellidos_doctor.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getSurname()));
    }
    public static ArrayList<Patient> getPatients(){
        return pacientes;
    }
    public static void addPatient(Patient pat) {
        pacientes.add(pat);
    }

    @FXML
    private void show_doctors(MouseEvent event) {
        tabla_patient.setVisible(false);
        tabla_doctor.setVisible(true);
        actual = 1;
    }

    @FXML
    private void show_patients(MouseEvent event) {
        tabla_doctor.setVisible(false);
        tabla_patient.setVisible(true);
        actual = 2;
    }

    @FXML
    private void show_appointments(MouseEvent event) {
        actual = 3;
    }

    @FXML
    private void add_current(MouseEvent event) throws IOException{
        switch(actual){
            case 1:
                
                break;
            case 2:
                Stage stage = new Stage();
                FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vista/VentanaA.fxml"));
                Parent root = miLoader.load();
                Scene scene = new Scene(root);
                stage.setTitle("Añadir paciente");
                stage.setScene(scene);
                stage.show();
                break;
            case 3:
                break;
        }
    }

    @FXML
    private void añadirNuevo(MouseEvent event) {
        FXMLLoader ventanaAñadir = new FXMLLoader (
                        getClass().getResource("/vista/VentanaA.fxml"));
        try {
            Parent vAñadir = (Parent) ventanaAñadir.load();
            Stage s = new Stage();
            s.setScene(new Scene(vAñadir));
            s.setResizable(false);                
            s.show();
        } 
        catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
