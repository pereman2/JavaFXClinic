/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.ClinicDBAccess;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import model.Appointment;
import model.Days;
import model.Doctor;
import model.Patient;

/**
 * FXML Controller class
 *
 * @author pereman2
 */
public class AñadirCita2Controller implements Initializable {

    @FXML
    private TableView<Days> tabla;
    @FXML
    private TextField field_paciente;
    @FXML
    private TextField field_doctor;
    @FXML
    private ComboBox<String> combo_paciente;
    @FXML
    private ComboBox<String> combo_doctor;
    @FXML
    private DatePicker date_picker;
    @FXML
    private ComboBox<Integer> combo_hora;
    @FXML
    private ComboBox<Integer> combo_min;
    
    private final int DOCTOR = 1;
    private final int PATIENT = 2;
    
    private ClinicDBAccess db;
    private ArrayList<Patient> pacientes;
    private ArrayList<String> current_pacientes;
    private ArrayList<Doctor> doctores;
    private ArrayList<String> current_doctores;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*TableColumn<Days, String> x = new TableColumn<>();
        x.setText("xdd");
        tabla.getColumns().add(x);*/
        db = FXMLDocumentController.getClinicDBAccess();
        pacientes = db.getPatients();
        doctores = db.getDoctors();
        current_doctores = new ArrayList<>();
        current_pacientes = new ArrayList<>();
        
        //inits
        initCurrent();
        initComboBox();
        initListeners();
        
        
    }    

    @FXML
    private void text_change(InputMethodEvent event) {
        /*TextField aux = (TextField) event.getSource();
        ArrayList<String> lista;
        if(event.getSource().equals(field_doctor)){
            lista = filtrar(DOCTOR);
            combo_doctor.getItems().remove(0, 
                                            combo_doctor.getItems().size() - 1);
            combo_doctor.getItems().addAll(lista);
        }
        else {
            lista = filtrar(PATIENT);
            combo_paciente.getItems().remove(0, 
                                              combo_paciente.getItems().size() - 1);
            combo_paciente.getItems().addAll(lista);
        }*/
        
        
        
        
    }
    private void initListeners(){
        field_paciente.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            ArrayList<String> lista = filtrar(PATIENT);
            
            combo_paciente.getItems().remove(0, 
                                            combo_paciente.getItems().size() - 1);
            combo_paciente.getItems().addAll(lista);
            
            
        });
        field_doctor.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            ArrayList<String> lista = filtrar(DOCTOR);
            
            combo_doctor.getItems().remove(0, 
                                            combo_doctor.getItems().size() - 1);
            combo_doctor.getItems().addAll(lista);
        });
    }
    
    private void initComboBox(){
        for (int i = 0; i < current_pacientes.size(); i++) {
            String aux_pat = current_pacientes.get(i);
            combo_paciente.getItems().add(i, current_pacientes.get(i));
        }
        for (int i = 0; i < current_doctores.size(); i++) {
            String aux_doc = current_doctores.get(i);
            combo_doctor.getItems().add(i, aux_doc);
        }
        
        for (int x = 0; x <= 23; x++) {
            combo_hora.getItems().add(x,x);
        }
        
        for (int x = 0; x <= 3; x++) {
            combo_min.getItems().add(x, x * 15);
        }
    }
    private void initCurrent(){
        for (int i = 0; i < pacientes.size(); i++) {
            Patient aux_pat = pacientes.get(i);
            current_pacientes.add(aux_pat.getName() + " " + aux_pat.getSurname());
            
        }
        for (int i = 0; i < doctores.size(); i++) {
            Doctor aux_doc = doctores.get(i);
            current_doctores.add(aux_doc.getName() + " " + aux_doc.getSurname());
        }
    }
    private ArrayList<String> filtrar(int agente){
        ArrayList<String> res = new ArrayList<>();
        if(agente == DOCTOR){
            ArrayList<String> aux_doctores = current_doctores;
            String texto_doctor = field_doctor.getText();
            for(int i = 0; i < aux_doctores.size(); i++){
                String doc = aux_doctores.get(i);
                if(texto_doctor.equals(doc.substring(0, texto_doctor.length() - 1))){
                    res.add(doc);
                }
            }
        }
        else {
            ArrayList<String> aux_pacientes = current_pacientes;
            String texto_paciente = field_paciente.getText();
            for(int i = 0; i < aux_pacientes.size(); i++){
                String pac = aux_pacientes.get(i);
                if(texto_paciente.equals(pac.substring(0, texto_paciente.length() - 1))){
                    res.add(pac);
                }
            }
        }
        
        return res;
    }
}
