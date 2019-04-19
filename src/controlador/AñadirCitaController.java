/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.ClinicDBAccess;
import controlador.FXMLDocumentController;
import static controlador.FXMLDocumentController.pacientes;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Doctor;
import model.Patient;


public class AñadirCitaController implements Initializable {

    public static final int DOCTOR = 0;
    public static final int PACIENTE = 1;
    
    private double initW;
    private double initH;
    private Patient pacienteSelecionado;
    private Doctor doctorSelecionado;
    private static ClinicDBAccess database;
    private static ArrayList<Doctor> doctores;
    private static ArrayList<Patient> pacientes;
    
    @FXML
    private TextField nombrePaciente;
    @FXML
    private TextField nombreDoctor;
    @FXML
    private DatePicker fecha;    
    @FXML
    private AnchorPane pane;
    @FXML
    private TableView<Doctor> infoDoctor;    
    @FXML
    private TableView<Patient> infoPaciente;    
    @FXML
    private TableColumn<Doctor, String> nomDoctor;
    @FXML
    private TableColumn<Doctor, String> apellidoDoctor;
    @FXML
    private TableColumn<Patient, String> nomPaciente;
    @FXML
    private TableColumn<Patient, String> apellidoPaciente;
    @FXML
    private Button btnPaciente;
    @FXML
    private Button btnDoctor;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        infoDoctor.setVisible(false);
        infoPaciente.setVisible(false);    
        database = FXMLDocumentController.getClinicDBAccess();
        doctores = database.getDoctors();
        pacientes = database.getPatients();
        nombreDoctor.setPromptText("Nombre doctor");
        nombrePaciente.setPromptText("Nombre paciente");    
    }    

    @FXML
    private void mostrarPaciente(MouseEvent event) { 
        Button btn = (Button) event.getSource();
        if (btn.getText().equals("Modificar")) {
            btn.setText("Buscar");
            nombrePaciente.setEditable(true);
            nombrePaciente.setText("");
            infoPaciente.getItems().clear();
        }
        else {
            infoPaciente.setVisible(true);
            infoDoctor.setVisible(false);        
            ObservableList<Patient> aux = FXCollections.observableArrayList(
                                            filtrarPacientes(nombrePaciente.getText()));        
            if (aux != null) {
                infoPaciente.setItems(aux);
                nomPaciente.setCellValueFactory(c -> 
                        new ReadOnlyObjectWrapper(c.getValue().getName()));
                apellidoPaciente.setCellValueFactory(c -> 
                        new ReadOnlyObjectWrapper(c.getValue().getSurname()));
            }
            else {
                Alert alerta = new Alert(AlertType.INFORMATION);
                alerta.setTitle("Paciente no encontrado");
                alerta.setHeaderText("No existen pacientes con ese nombre.");
                alerta.show();
            }
        }
    }

    @FXML
    private void mostrarDoctor(MouseEvent event) {
        Button btn = (Button) event.getSource();
        if (btn.getText().equals("Modificar")) {
            btn.setText("Buscar");
            nombreDoctor.setEditable(true);
            nombreDoctor.setText("");           
            infoDoctor.getItems().clear();
        }
        else {
            infoDoctor.setVisible(true);
            infoPaciente.setVisible(false); 
            ObservableList<Doctor> aux = FXCollections.observableArrayList(
                                            filtrarDoctor(nombreDoctor.getText()));
            if (aux != null) { 
                infoDoctor.setItems(aux);            
                nomDoctor.setCellValueFactory(c -> 
                        new ReadOnlyObjectWrapper(c.getValue().getName()));
                apellidoDoctor.setCellValueFactory(c -> 
                        new ReadOnlyObjectWrapper(c.getValue().getSurname()));
            }
            else {
                Alert alerta = new Alert(AlertType.INFORMATION);
                alerta.setTitle("Doctor no encontrado");
                alerta.setHeaderText("No existen doctores con ese nombre.");
                alerta.show();
            }
        }
    }    
    
    private ArrayList<Doctor> filtrarDoctor(String nombre) {
        ArrayList<Doctor> aux = null;      
        for (int i = 0; i < doctores.size(); i++) {
            if (doctores.get(i).getName().equals(nombre)) {
                aux.add(doctores.get(i));
            }
        }       
       return aux;
    }
    
    private ArrayList<Patient> filtrarPacientes(String nombre) {
        ArrayList<Patient> aux = new ArrayList<Patient>();
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getName().equals(nombre)) {
                aux.add(pacientes.get(i));
            }
        }          
        return aux;
    }

    @FXML
    private void selecionar(MouseEvent event) {
        TableView aux = (TableView) event.getSource();
        if (aux == infoDoctor) {
            doctorSelecionado = 
                    (Doctor) aux.getSelectionModel().getSelectedItem();
            nombreDoctor.setText(doctorSelecionado.getName() + " " 
                                     + doctorSelecionado.getSurname());
            nombreDoctor.setEditable(false);
            btnDoctor.setText("Modificar");            
        }        
        else if (aux == infoPaciente) {
            pacienteSelecionado = 
                    (Patient) aux.getSelectionModel().getSelectedItem();
            nombrePaciente.setText(pacienteSelecionado.getName() + " " 
                                     + pacienteSelecionado.getSurname());
            nombrePaciente.setEditable(false);
            btnPaciente.setText("Modificar");
        }       
    }    
    
}
