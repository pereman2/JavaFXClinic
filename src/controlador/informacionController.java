/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.ClinicDBAccess;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Days;
import model.Doctor;
import model.ExaminationRoom;
import model.Patient;

/**
 *
 * @author deblack
 */
public class informacionController implements Initializable {    
    public static final int DNI = 0;
    public static final int NOMBRE_APELLIDOS = 1;
    public static final int TELEFONO = 2;
    
    ObservableList<Patient> pacientes_local;
    ObservableList<Doctor> doctor_local;
    
    
    private boolean isDoctor = false;
    private boolean[] days;  
    private String vboxDaysBackground = "-fx-background-color: gray;" + 
                                        "-fx-border-radius: 50px;" + 
                                        "-fx-border-color: gray;" + 
                                        "-fx-border-width: 3px;" +
                                        "-fx-background-radius: 24px; ";
    
    @FXML
    private ImageView img;
    @FXML
    private Hyperlink hyperlink_img;
    @FXML
    private TextField field_nombre;
    @FXML
    private TextField field_dni;
    @FXML
    private TextField field_apellidos;
    @FXML
    private TextField field_telefono;
    ClinicDBAccess db;
    @FXML
    private ComboBox<Integer> combo_consulta;
    @FXML
    private ComboBox<Integer> combo_hora;
    @FXML
    private ComboBox<Integer> combo_min;
    @FXML
    private HBox hbox_days;
    @FXML
    private ComboBox<Integer> combo_hora1;
    @FXML
    private ComboBox<Integer> combo_min1;
    
    private ArrayList<ExaminationRoom> examination_rooms;
    
    private ArrayList<VBox> vbox_Days;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = FXMLDocumentController.getClinicDBAccess();
        examination_rooms = db.getExaminationRooms();
        field_nombre.setPromptText("Nombre");
        field_apellidos.setPromptText("Apellidos");
        field_dni.setPromptText("DNI");
        field_telefono.setPromptText("Telefono");
        FXMLLoader fmxl = new FXMLLoader(url);
        if (fmxl.equals(new FXMLLoader(
                getClass().getResource(
                "/vista/VentanaAñadirDoctor.fxml")))) {
            days = new boolean[7];            
        } 
        
        
    }
    
    @FXML
    private void aceptar(ActionEvent event) { 
        
    }
    
    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) (
                (Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    //Mostrar pacientes
    public void initPatient(Patient pat){
        field_nombre.setText(pat.getName());
        field_apellidos.setText(pat.getSurname());
        field_dni.setText(pat.getIdentifier());
        field_telefono.setText(pat.getTelephon());
        img.setImage(pat.getPhoto());
        field_nombre.setEditable(false);
        field_apellidos.setEditable(false);
        field_dni.setEditable(false);
        field_telefono.setEditable(false);
        img.setDisable(false);
    }
    
    public void initDoctor(Doctor doc) {
        field_nombre.setText(doc.getName());
        field_apellidos.setText(doc.getSurname());
        field_dni.setText(doc.getIdentifier());
        field_telefono.setText(doc.getTelephon());
        img.setImage(doc.getPhoto());
        field_nombre.setEditable(false);
        field_apellidos.setEditable(false);
        field_dni.setEditable(false);
        field_telefono.setEditable(false);
        img.setDisable(false);
        combo_consulta.setPromptText(doc.getExaminationRoom().toString());
        combo_consulta.setEditable(false);
        initDoctor(doc);
        seleccionarDays(doc);
    }
    
    private void initComboBox(Doctor doc) {
        LocalTime horaInicial = doc.getVisitStartTime();
        LocalTime horaFinal = doc.getVisitEndTime();        
       
        combo_hora.setPromptText(Integer.toString(horaInicial.getHour()));
        combo_hora1.setPromptText(Integer.toString(horaFinal.getHour()));        
        
        combo_min.setPromptText(Integer.toString(horaInicial.getMinute()));
        combo_min1.setPromptText(Integer.toString(horaFinal.getMinute()));
    }
    
    private void getDays() {
        vbox_Days = new ArrayList<VBox>();
        for (Node v : hbox_days.getChildren()) {
            vbox_Days.add((VBox) v);
        }
    }
    
    private void seleccionarDays(Doctor doc) {
        getDays();
        ArrayList<Days> aux = doc.getVisitDays();
        for (int i = 0; i < aux.size(); i++) {
            if(null != aux.get(i)) {
                switch (aux.get(i)) {
                    case Monday:
                        vbox_Days.get(0).setStyle(vboxDaysBackground);
                        break;
                    case Tuesday:
                        vbox_Days.get(1).setStyle(vboxDaysBackground);
                        break;
                    case Wednesday:
                        vbox_Days.get(2).setStyle(vboxDaysBackground);
                        break;
                    case Thursday:
                        vbox_Days.get(3).setStyle(vboxDaysBackground);
                        break;
                    case Friday:
                        vbox_Days.get(4).setStyle(vboxDaysBackground);
                        break;
                    case Saturday:
                        vbox_Days.get(5).setStyle(vboxDaysBackground);
                        break;
                    case Sunday:
                        vbox_Days.get(6).setStyle(vboxDaysBackground);
                        break;
                    default:
                        break;
                }
            }
        }
        
    }
}
