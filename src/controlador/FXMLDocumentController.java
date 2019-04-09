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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;


public class FXMLDocumentController implements Initializable {
    private final int DOCTOR = 1;
    private final int PATIENT = 2;
    private final int APPOINTMENT = 3;
    
    private int actual;
    
    private Label label;
    private static ClinicDBAccess database;
    
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
    @FXML
    private TextField texto_filtro;
    @FXML
    private ComboBox<?> cb_tipoFiltro;
    @FXML
    private Button btn_filtrar;
    @FXML
    private Button btn_doctor;
    @FXML
    private Button btn_paciente;
    
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
    public void addPatient(Patient pat) {
        pacientes.add(pat);
    }

    @FXML
    private void show_doctors(MouseEvent event) {
        setBackgroundButton((Button) event.getSource());
        tabla_patient.setVisible(false);
        tabla_doctor.setVisible(true);
        actual = 1;
    }

    @FXML
    private void show_patients(MouseEvent event) {
        setBackgroundButton((Button) event.getSource());        
        tabla_doctor.setVisible(false);
        tabla_patient.setVisible(true);
        actual = 2;
    }
    
    public void setBackgroundButton(Button b) {
        String style = "-fx-background-color: gray;" + 
                        "-fx-border-radius: 24px;" + 
                        "-fx-border-color: gray;" + 
                        "-fx-border-width: 5px;" +
                        "-fx-background-radius: 24px; ";
        
        String initStyle = "-fx-background-color: transparent;" + 
                            "-fx-border-radius: 24px;" + 
                            "-fx-border-color: transparent;" + 
                            "-fx-border-width: 5px;" +
                            "-fx-background-radius: 24px; ";
        
        if (btn_doctor.getStyle().equals(style) && b == btn_paciente) {
            btn_doctor.setStyle(initStyle);
            btn_paciente.setStyle(style);
        }
        else if (btn_paciente.getStyle().equals(style) && b == btn_doctor) {
            btn_doctor.setStyle(style);
            btn_paciente.setStyle(initStyle);
        }
        else {
            if(style.equals(b.getStyle())) {
                b.setStyle(initStyle);
            }
            else {            
                b.setStyle(style);
            }
        }
        
       
    }

    @FXML
    private void show_appointments(MouseEvent event) {
        actual = 3;
    }

    @FXML
    private void add_current(MouseEvent event) throws IOException{
        switch(actual){
            case 1:
                Stage stage_doctor = new Stage();
                FXMLLoader miLoader_doctor = new FXMLLoader(getClass().getResource("/vista/VentanaAñadirDoctor.fxml"));
                Parent root_doctor = miLoader_doctor.load();
                ((VentanaAñadirController) miLoader_doctor.getController()).initListaDoctor(datos_doc);
                Scene scene_doctor = new Scene(root_doctor);
                stage_doctor.setTitle("Añadir doctor");
                stage_doctor.setScene(scene_doctor);
                stage_doctor.showAndWait();
                break;
                
            case 2:
                Stage stage = new Stage();
                FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vista/VentanaAñadirPaciente.fxml"));
                Parent root = miLoader.load();
                ((VentanaAñadirController) miLoader.getController()).initListaPersona(datos_pat);
                Scene scene = new Scene(root);
                stage.setTitle("Añadir paciente");
                stage.setScene(scene);
                stage.showAndWait();
                break;
                
            case 3:
                break;
        }
    }

    
    public static ClinicDBAccess getClinicDBAccess(){
        return database;
    }

    @FXML
    private void eliminar(MouseEvent event) {
        switch(actual){
            case DOCTOR:
                Doctor aux_doctor = tabla_doctor.getSelectionModel().getSelectedItem();
                if (database.hasAppointments(aux_doctor)) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Cita encontrada");
                    alerta.setHeaderText("El cliente ya tiene una cita");
                    alerta.setContentText("Imposible eliminar paciente con una cita asignada");
                    alerta.showAndWait();
                }                
                break;
                
            case PATIENT:
                Patient aux = tabla_patient.getSelectionModel().getSelectedItem();
                if (database.hasAppointments(aux)) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Cita encontrada");
                    alerta.setHeaderText("El cliente ya tiene una cita");
                    alerta.setContentText("Imposible eliminar paciente con una cita asignada");
                    alerta.showAndWait();
                    break;
                }

                database.getPatients().remove(aux);
                datos_pat.remove(aux);
                break;
            case APPOINTMENT:
                break;
        }
    }


    
}
