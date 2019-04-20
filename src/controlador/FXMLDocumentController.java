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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;


public class FXMLDocumentController implements Initializable {
    private final int DOCTOR = 1;
    private final int PATIENT = 2;
    private final int APPOINTMENT = 3;
    
    private int combodoc;
    public static int actual;
    
    private Label label;
    private static ClinicDBAccess database;
    
    //arraylist de doctores,pacientes
    private static ArrayList<Doctor> doctores;
    public static ArrayList<Patient> pacientes;
    private static ObservableList<Patient> datos_pat = null;
    private static ObservableList<Doctor> datos_doc = null;
    private static ObservableList<Appointment> datos_citas = null;
    private ArrayList<String> current_pacientes;
    private ArrayList<String> current_doctores;
    private Doctor doctor_actual;
    private Patient paciente_actual;
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
    private Button btn_doctor;
    @FXML
    private Button btn_paciente;
    @FXML
    private VBox vbox_cita;
    @FXML
    private ComboBox<String> combo_doc_pat;
    @FXML
    private TextField field_buscar;
    @FXML
    private ComboBox<String> combor_esultado;
    @FXML
    private TableColumn<Appointment, String> col_nombre_cita;
    @FXML
    private TableColumn<Appointment, String> col_apellido_cita;
    @FXML
    private TableColumn<Appointment, String> col_fecha;
    @FXML
    private TableView<Appointment> table_cita;
    private Button btn_citas;
    private String initStyle;
    @FXML
    private Label text_clinic;
    
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
        
        current_doctores = new ArrayList<>();
        current_pacientes = new ArrayList<>();
        
        tabla_doctor.setVisible(false);
        tabla_patient.setVisible(false);
        vbox_cita.setVisible(false);
        
        initDoctors();
        initPatients();
        initComboCita();
        initCurrent();
        
        text_clinic.setText(database.getClinicName());
        
        initStyle = btn_doctor.getStyle();
        
        combodoc = 0;
        actual = 0;        
    }  
    public void initComboCita(){
        field_buscar.textProperty().addListener((observable, oldValue, newValue) -> {
            if(combodoc == DOCTOR){
                ArrayList<String> lista = filtrar(DOCTOR, newValue);
            
                combor_esultado.getItems().clear();
                combor_esultado.getItems().addAll(lista);
            }
            else if(combodoc == PATIENT){
                ArrayList<String> lista = filtrar(PATIENT, newValue);
            
                combor_esultado.getItems().clear();
                combor_esultado.getItems().addAll(lista);
            }
            
        });
        combor_esultado.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){}
            else if(combodoc == DOCTOR){
                
                doctor_actual = doctores.get(current_doctores.indexOf(newValue));
                datos_citas = FXCollections.observableArrayList(
                        database.getDoctorAppointments(doctor_actual.getIdentifier()));
                table_cita.setItems(datos_citas);
                col_nombre_cita.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getDoctor().getName()));
                col_apellido_cita.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getDoctor().getSurname()));
                col_fecha.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getAppointmentDateTime().toString()));
            }
            else if(combodoc == PATIENT){
                paciente_actual = pacientes.get(current_pacientes.indexOf(newValue));
                datos_citas = FXCollections.observableArrayList(
                        database.getPatientAppointments(paciente_actual.getIdentifier()));
                table_cita.setItems(datos_citas);
                col_nombre_cita.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getPatient().getName()));
                col_apellido_cita.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getPatient().getSurname()));
                col_fecha.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getAppointmentDateTime().toString()));
            }
            
        });
        combo_doc_pat.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(combo_doc_pat.getSelectionModel().getSelectedItem().equals("Doctor")){
                combodoc = DOCTOR;
                combor_esultado.getItems().clear();
                combor_esultado.getItems().addAll(current_doctores);
                
            }
            else if(combo_doc_pat.getSelectionModel().getSelectedItem().equals("Paciente")){
                combodoc = PATIENT;
                combor_esultado.getItems().clear();
                combor_esultado.getItems().addAll(current_pacientes);
                
            }
        });
        combo_doc_pat.getItems().add("Doctor");
        combo_doc_pat.getItems().add("Paciente");
    }
    
    public void initCitas(){
        
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
        vbox_cita.setVisible(false);
        actual = 1;
    }

    @FXML
    private void show_patients(MouseEvent event) {
        setBackgroundButton((Button) event.getSource());        
        tabla_doctor.setVisible(false);
        tabla_patient.setVisible(true);
        vbox_cita.setVisible(false);
        actual = 2;
    }
    
    public void setBackgroundButton(Button b) {
        String style = "-fx-background-color: gray;" + 
                        "-fx-border-radius: 24px;" + 
                        "-fx-border-color: gray;" + 
                        "-fx-border-width: 5px;" +
                        "-fx-background-radius: 24px; ";    
        
        if (b == btn_paciente) {            
            btn_doctor.setStyle(initStyle);
            btn_citas.setStyle(initStyle);
            btn_paciente.setStyle(style);
        }
        else if (b == btn_doctor) {            
            btn_doctor.setStyle(style);
            btn_paciente.setStyle(initStyle);
            btn_citas.setStyle(initStyle);
        }
        else if (b == btn_citas) {             
            btn_doctor.setStyle(initStyle);
            btn_paciente.setStyle(initStyle);
            btn_citas.setStyle(style);
        }        
        
       
    }

    @FXML
    private void show_appointments(MouseEvent event) throws IOException{
        setBackgroundButton((Button) event.getSource()); 
        tabla_patient.setVisible(false);
        tabla_doctor.setVisible(false);
        vbox_cita.setVisible(true);
        actual = 3;
    }

    @FXML
    private void add_current(MouseEvent event) throws IOException{
        switch(actual){
            case 1:
                Stage stage_doctor = new Stage();
                FXMLLoader miLoader_doctor = new FXMLLoader(getClass().getResource("/vista/VentanaAñadirDoctor.fxml"));
                VentanaAñadirController auxC = new VentanaAñadirController();
                miLoader_doctor.setController(auxC);
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
                VentanaAñadirController auxP = new VentanaAñadirController();
                miLoader.setController(auxP);
                Parent root = miLoader.load();
                ((VentanaAñadirController) miLoader.getController()).initListaPersona(datos_pat);
                Scene scene = new Scene(root);                
                stage.setTitle("Añadir paciente");
                stage.setScene(scene);
                stage.showAndWait();
                break;                
            case 3:
                Stage stg_citas = new Stage();
                FXMLLoader loaderCitas = new FXMLLoader(getClass().getResource("/vista/AñadirCita2.fxml"));
                Parent root_cita = loaderCitas.load();
                Scene scnCitas = new Scene(root_cita);
                stg_citas.setTitle("Citas");
                stg_citas.setScene(scnCitas);
                stg_citas.showAndWait();
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
                    alerta.setHeaderText("El doctor ya tiene una cita");
                    alerta.setContentText("Imposible eliminar doctor con una cita asignada");
                    alerta.showAndWait();
                    break;
                }         
                database.getDoctors().remove(aux_doctor);
                datos_doc.remove(aux_doctor);
                break;
                
            case PATIENT:
                Patient aux = tabla_patient.getSelectionModel().getSelectedItem();
                if (database.hasAppointments(aux)) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Cita encontrada");
                    alerta.setHeaderText("El paciente ya tiene una cita");
                    alerta.setContentText("Imposible eliminar paciente con una cita asignada");
                    alerta.showAndWait();
                    break;
                }

                database.getPatients().remove(aux);
                datos_pat.remove(aux);
                break;
            case APPOINTMENT:
                Appointment aux_cita = table_cita.getSelectionModel().getSelectedItem();
                break;
        }
    }

    @FXML
    private void visualizar(MouseEvent event) throws IOException {
        switch(actual){
            case DOCTOR:
                Doctor aux_doctor = tabla_doctor.getSelectionModel().getSelectedItem();
                Stage stage_doctor = new Stage();
                FXMLLoader miLoader_doctor = new FXMLLoader(getClass().getResource("/vista/VentanaAñadirDoctor.fxml"));
                
                informacionController auxC = new informacionController();
                miLoader_doctor.setController(auxC);
                
                Parent root_doctor = miLoader_doctor.load();                
                ((informacionController) miLoader_doctor.getController()).initDoctor(aux_doctor);
                Scene scene_doctor = new Scene(root_doctor);
                stage_doctor.setTitle("Visualizar doctor");
                stage_doctor.setScene(scene_doctor);
                stage_doctor.showAndWait();             
                break;
                
            case PATIENT:
                Patient aux = tabla_patient.getSelectionModel().getSelectedItem();
                Stage stage = new Stage();
                FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vista/VentanaAñadirPaciente.fxml"));
                informacionController auxP = new informacionController();
                miLoader.setController(auxP);
                Parent root = miLoader.load();                
                ((informacionController) miLoader.getController()).initPatient(aux);
                Scene scene = new Scene(root);
                stage.setTitle("Visualizar paciente");
                stage.setScene(scene);
                stage.showAndWait();
                break;
            case APPOINTMENT:
                //Appointment aux_cita = table_cita.getSelectionModel().getSelectedItem();
                Stage stage_cita = new Stage();
                FXMLLoader miLoader_cita = new FXMLLoader(getClass().getResource("/vista/AñadirCita2.fxml"));
                Parent root_cita = miLoader_cita.load();                
                ((AñadirCita2Controller) miLoader_cita.getController()).initCita(table_cita.getSelectionModel().getSelectedItem());
                Scene scene_cita = new Scene(root_cita);
                stage_cita.setTitle("Visualizar paciente");
                stage_cita.setScene(scene_cita);
                stage_cita.showAndWait();
                break;
        }
    }
    
    private ArrayList<String> filtrar(int agente, String newValue) throws StringIndexOutOfBoundsException{
        ArrayList<String> res = new ArrayList<>();
        if(agente == DOCTOR){
            ArrayList<String> aux_doctores = current_doctores;
            //lowercase string para facilitar las comparaciones
            String texto_doctor = newValue.toLowerCase();
            //caso texto vacio
            if(texto_doctor.equals("")){
               res = current_doctores; 
            }
            //caso texto solo tiene una letra
            else if(texto_doctor.length() == 1){
                for(int i = 0; i < aux_doctores.size(); i++){
                    String doc = aux_doctores.get(i);
                    if(doc.toLowerCase().startsWith(texto_doctor)){
                        res.add(doc);
                    }
                }
            }
            //caso global
            else {
                for(int i = 0; i < aux_doctores.size(); i++){
                    String doc = aux_doctores.get(i);
                    if(texto_doctor.length() <= doc.length()){
                        String sub = doc.toLowerCase().substring(0, texto_doctor.length());
                        if(sub.equals(texto_doctor)){
                            res.add(doc);
                        }
                    }
                    
                }
            }
            
        }
        else {
            ArrayList<String> aux_pacientes = current_pacientes;
            String texto_paciente = newValue;
            if(texto_paciente.equals("")){
                res = current_pacientes;
            }
            else if(texto_paciente.length() == 1){
                for(int i = 0; i < aux_pacientes.size(); i++){
                    String pac = aux_pacientes.get(i);
                    if(pac.toLowerCase().startsWith(texto_paciente)){
                        res.add(pac);
                    }
                }
            }
            else{
                for(int i = 0; i < aux_pacientes.size(); i++){
                    String pac = aux_pacientes.get(i);
                    if(texto_paciente.length() <= pac.length()){
                        String sub = pac.toLowerCase().substring(0, texto_paciente.length());
                        if(sub.equals(texto_paciente)){
                            res.add(pac);
                        }
                    }
                    
                }
            }
            
        }
        
        return res;
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
}
