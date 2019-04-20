/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.ClinicDBAccess;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import com.sun.prism.paint.Color;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;
import javafx.util.Callback;
import model.Appointment;
import model.Days;
import model.Doctor;
import model.Patient;
import sun.plugin2.jvm.RemoteJVMLauncher.CallBack;
import utils.SlotAppointmentsWeek;
import utils.SlotWeek;

/**
 * FXML Controller class
 *
 * @author pereman2
 */
public class AñadirCita2Controller implements Initializable {

    @FXML
    private TextField field_paciente;
    @FXML
    private TextField field_doctor;
    @FXML
    private ComboBox<String> combo_paciente;
    @FXML
    private ComboBox<String> combo_doctor;    
    @FXML
    private ComboBox<Integer> combo_hora;
    @FXML
    private ComboBox<Integer> combo_min;
    
    private final int DOCTOR = 1;
    private final int PATIENT = 2;
    private int mode = 1;
    
    private ClinicDBAccess db;
    private ArrayList<Patient> pacientes;
    private ArrayList<String> current_pacientes;
    private ArrayList<Doctor> doctores;
    private ArrayList<String> current_doctores;
    private ArrayList<SlotWeek> semana_doctor;
    private Doctor doctor_actual;
    private Appointment appointment;
    private Text hora;
    private Text ok;
    @FXML
    private HBox hbox_picker;
    @FXML
    private Button btn_aceptar;
    @FXML
    private Button btn_cancelar;
    @FXML
    private DatePicker date2;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(mode == 1){
            init();
        }
        
    } 
    
    private void init(){
        db = FXMLDocumentController.getClinicDBAccess();
        pacientes = db.getPatients();
        doctores = db.getDoctors();
        current_doctores = new ArrayList<>();
        current_pacientes = new ArrayList<>();
        doctor_actual = null;        
            
        System.out.println("ea");
        
        
        //inits
        initCurrent();
        initComboBox();
        initListeners();        
    }
    private void dispDataPicker() {
        ArrayList<Days> days = doctor_actual.getVisitDays();        
        ArrayList<Appointment> aux = db.getDoctorAppointments(
                                        doctor_actual.getIdentifier());
        
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    for (Days d : days) {
                        System.out.println(d.toString());
                        boolean comp = d.ordinal() + 1 == item.getDayOfWeek().getValue();
                        if (comp) {
                            this.setStyle("-fx-background-color: green;");                            
                        }                   
                    }
                    if (item.isBefore(LocalDate.now())){
                        this.setStyle("-fx-background-color: gray;");
                    }
                }
        };
        
        date2.setDayCellFactory(dayCellFactory);
        
            
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
            ArrayList<String> lista = filtrar(PATIENT, newValue);
            
            combo_paciente.getItems().clear();
            combo_paciente.getItems().addAll(lista);
            
            
        });
        field_doctor.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            ArrayList<String> lista = filtrar(DOCTOR, newValue);
            
            combo_doctor.getItems().clear();
            combo_doctor.getItems().addAll(lista);
        });
        combo_doctor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            doctor_actual= doctores.get(current_doctores.indexOf(newValue));
            ajustarHora();
            //hora.setText(doctor_actual.getVisitStartTime().toString() + doctor_actual.getVisitEndTime().toString());
            dispDataPicker();
            update();
            
        });
        date2.promptTextProperty().addListener((observable, oldValue, newValue) -> {
            update();
        });
        combo_min.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            
            update();
        });
        combo_hora.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ajustarMinuto();
            update();
        });
        
    }
    private void update(){
        
        if(combo_min.getSelectionModel().getSelectedItem() != null &&
                    combo_hora.getSelectionModel().getSelectedItem() != null &&
                    date2.getValue() != null &&
                    combo_doctor.getSelectionModel().getSelectedItem() != null){
                    SlotWeek aux = slotMatch();
                    LocalTime time1 = doctor_actual.getVisitStartTime();
                    LocalTime time2 = doctor_actual.getVisitEndTime();
                    LocalTime time3 = LocalTime.of(combo_hora.getSelectionModel().getSelectedItem(),
                            combo_min.getSelectionModel().getSelectedItem());
                    if(time3.compareTo(time1) >= 0 && time3.compareTo(time2) <= 0) {
                        //ok.setText(checkDisponible(aux));
                        System.out.println("True");
                    }
                    else {
                        //ok.setText("Hora incorrecta");
                        System.out.println("False");
                    }
                    
            }
    }
    private SlotWeek slotMatch() {
            ArrayList<Days> visitDays = doctor_actual.getVisitDays();
            LocalTime visitStartTime = doctor_actual.getVisitStartTime();
            LocalTime visitEndTime = doctor_actual.getVisitEndTime();
            LocalDate date = date2.getValue();
            int semana = getDiaSemana(date);
            ArrayList<Appointment> appointments = db.getDoctorAppointments(doctor_actual.getIdentifier());
            semana_doctor = SlotAppointmentsWeek.getAppointmentsWeek(semana, visitDays, visitStartTime, visitEndTime, appointments);
            int hora = combo_hora.getSelectionModel().getSelectedItem();
            int min = combo_min.getSelectionModel().getSelectedItem();
            SlotWeek res = null;
            for (int i = 0; i < semana_doctor.size(); i++) {
            SlotWeek aux = semana_doctor.get(i);
            if(checkSlot(aux.getSlot(), hora, min)){
                res = aux;
                break;
            }
        }
        return res;
    }
    private String checkDisponible(SlotWeek slot){
        int dia = date2.getValue().getDayOfWeek().getValue();
        String str_dia = "";
        switch(dia){
            case 1:
                str_dia = slot.getMondayAvailability();
                break;
            case 2:
                str_dia = slot.getTuesdayAvailability();
                break;
            case 3:
                str_dia = slot.getWednesdayAvailability();
                break;
            case 4:
                str_dia = slot.getThursdayAvailability();
                break;
            case 5:
                str_dia = slot.getFridayAvailability();
                break;
            case 6:
                str_dia = slot.getSaturdayAvailability();
                break;
            case 7:
                str_dia = slot.getSundayAvailability();
                break;
        }
        return str_dia;
    }
    private boolean checkSlot(LocalTime date, int hora, int min){
        boolean res = false;
        if(date.getHour() == hora && date.getMinute() == min){
            res = true;
        }
        return res;
    }
    
    private int getDiaSemana(LocalDate date){
        int dayYear = date.getDayOfYear();
        int dayWeek = date.getDayOfWeek().getValue();
        LocalDate first = LocalDate.of(date.getYear(), 1, 1);
        int dayWeekFirst = first.getDayOfWeek().getValue();
        int numWeek = (dayYear + 6) / 7;
        
        if(dayWeek < dayWeekFirst){
            ++numWeek;
        }
        return numWeek;
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
    
    
    //dado el agente(cliente/doctor) y string devuelve la lista filtrada
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
    public void initCita(Appointment ap){     
        mode = 0;
        String doc = ap.getDoctor().getName() + " " + ap.getDoctor().getSurname();
        String pat = ap.getPatient().getName() + " " + ap.getPatient().getSurname();
        combo_doctor.getItems().clear();
        combo_doctor.getItems().add(doc);
        combo_doctor.getSelectionModel().select(0);
        
        combo_paciente.getItems().clear();
        combo_paciente.getItems().add(pat);
        combo_paciente.getSelectionModel().select(0);
        
        combo_hora.getItems().clear();
        combo_hora.getItems().add(ap.getAppointmentDateTime().getHour());
        combo_hora.getSelectionModel().select(0);
        combo_min.getItems().clear();
        combo_min.getItems().add(ap.getAppointmentDateTime().getMinute());
        combo_min.getSelectionModel().select(0);
        
        date2.setValue(ap.getAppointmentDateTime().toLocalDate());
        
        date2.setDisable(true);
        combo_hora.setEditable(false);
        combo_min.setEditable(false);
        combo_paciente.setEditable(false);
        combo_doctor.setEditable(false);
        field_doctor.setDisable(true);
        field_paciente.setDisable(true);
    }
    private void ajustarMinuto(){
        int inih = doctor_actual.getVisitStartTime().getHour();
        int finh = doctor_actual.getVisitEndTime().getHour();
        int inim = doctor_actual.getVisitStartTime().getMinute();
        int finm = doctor_actual.getVisitEndTime().getMinute();
        ArrayList<Integer> lista = new ArrayList<>();
        
        if(combo_hora.getSelectionModel().getSelectedItem() != null) {
            int actual = combo_hora.getSelectionModel().getSelectedItem();
            if(actual == inih){
                for(int i = 0; i < 4; i++){
                    Integer m = (Integer) i * 15;
                    if(m >= inim){
                        lista.add(m);
                    }
                }
            }
            else if(actual == finh) {
                for(int i = 0; i < 4; i++){
                    Integer m = (Integer) i * 15;
                    if(m <= finm){
                        lista.add(m);
                    }
                }
            }
            else{
                for(int i = 0; i < 4; i++){
                    Integer m = (Integer) i * 15;
                    lista.add(m);
                }
            }
        }
        combo_min.getItems().clear();
        combo_min.getItems().addAll(lista);
    }
    private void ajustarHora(){
        int inih = doctor_actual.getVisitStartTime().getHour();
        int finh = doctor_actual.getVisitEndTime().getHour();
        ArrayList<Integer> lista = new ArrayList<>();
        if(inih < finh){
            for(int i = 0; i < 24; i++){
                if(i >= inih && i <= finh){
                    lista.add(i);
                }
            }
        }
        else {
            for(int i = 0; i < 24; i++){
                if(i <= inih && i >= finh){
                    lista.add(i);
                }
            }
        }
        combo_hora.getItems().clear();
        combo_hora.getItems().addAll(lista);


    }
    @FXML
    private void aceptar(MouseEvent event) {
        int hora = combo_hora.getSelectionModel().getSelectedItem();
        int min = combo_min.getSelectionModel().getSelectedItem();
        LocalDate date = date2.getValue();
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hora, min));
        String str_pac = combo_paciente.getSelectionModel().getSelectedItem();
        Patient pat = pacientes.get(current_pacientes.indexOf(str_pac));
        Appointment aux = new Appointment(dateTime, doctor_actual, pat);
        db.getAppointments().add(aux);       
        cancelar(event);
    }

    @FXML
    private void cancelar(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void eatobe(MouseEvent event) {        
        dispDataPicker();
    }
}
