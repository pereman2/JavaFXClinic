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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Days;
import model.Patient;
import model.Doctor;
import model.ExaminationRoom;

/**
 * FXML Controller class
 *
 * @author pereman2
 */
public class VentanaAñadirController implements Initializable {   
    
    public static final int DNI = 0;
    public static final int NOMBRE_APELLIDOS = 1;
    public static final int TELEFONO = 2;
    
    ObservableList<Patient> pacientes_local;
    ObservableList<Doctor> doctor_local;
    
    
    private boolean isDoctor = false;
    private boolean[] days;        
    private String redBackground = "-fx-border-color: red;";
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
    @FXML
    private Button button_aceptar;
    @FXML
    private Button button_cancelar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = FXMLDocumentController.getClinicDBAccess();
        examination_rooms = db.getExaminationRooms();
        field_nombre.setPromptText("Nombre");
        field_apellidos.setPromptText("Apellidos");
        field_dni.setPromptText("DNI");
        field_telefono.setPromptText("Telefono");
        //initImage();
        FXMLLoader fmxl = new FXMLLoader(url);
        if (fmxl.equals(new FXMLLoader(
                getClass().getResource(
                "/vista/VentanaAñadirDoctor.fxml")))) {
            days = new boolean[7];
            isDoctor = true;
            initComboBox();
            initDays();
        }        
    }    

    @FXML
    private void aceptar(ActionEvent event) {
        if (esCorrecto()) {
            String name = field_nombre.getText();
            String surname = field_apellidos.getText();
            String identifier = field_dni.getText();
            String telephon = field_telefono.getText();
            Image photo = img.getImage();
            if (isDoctor) {            
                Doctor aux = new Doctor();
                aux.setName(name);
                aux.setSurname(surname);
                aux.setTelephon(telephon);
                aux.setIdentifier(identifier);
                aux.setVisitDays(getDays());
//                aux.setExaminationRoom(examination_rooms.get(
//                                        combo_consulta.getSelectionModel().getSelectedItem().intValue()));
                LocalTime lt1 = LocalTime.of(combo_hora.getSelectionModel().getSelectedItem().intValue(),
                                            combo_min.getSelectionModel().getSelectedItem().intValue());
                aux.setVisitStartTime(lt1);
                LocalTime lt2 = LocalTime.of(combo_hora1.getSelectionModel().getSelectedItem().intValue(),
                                            combo_min1.getSelectionModel().getSelectedItem().intValue());
                aux.setVisitEndTime(lt2);
                aux.setVisitDays(getDays());
                
                
                db.getDoctors().add(aux);
                doctor_local.add(aux);
                FXMLDocumentController.getClinicDBAccess().getDoctors().add(aux);
                ((Stage) img.getScene().getWindow()).close();               
            }
            else {                
                Patient aux = new Patient(identifier, name, surname, telephon, photo);
                FXMLDocumentController.getClinicDBAccess().getPatients().add(aux);
                pacientes_local.add(aux);
                ((Stage) img.getScene().getWindow()).close();
            }
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Carracteres introducidos incorrectos");
            alert.showAndWait();
        }
    }
    
    //Devuelve el arraylist<Days> necesario para crear el doctor :)))
    public ArrayList<Days> getDays() {        
        ArrayList<Days> aux = new ArrayList<Days>();
        for (Node v : hbox_days.getChildren()) {
            VBox auxV = (VBox) v;
            if (auxV.getStyle().equals(vboxDaysBackground)) {
                switch (auxV.getId()) {
                    case "0":
                        aux.add(Days.Monday);                       
                        break;
                    case "1":
                        aux.add(Days.Tuesday);
                        break;
                    case "2":
                        aux.add(Days.Wednesday);
                        break;
                    case "3":
                        aux.add(Days.Thursday);
                        break;
                    case "4":
                        aux.add(Days.Friday);
                        break;
                    case "5":
                        aux.add(Days.Saturday);
                        break;
                    case "6":
                        aux.add(Days.Sunday);
                        break;
                }
                
            }
        } 
        return aux;
    }
    
    private void initDays() {
        for (int x = 0; x < hbox_days.getChildren().size(); x++) {
            VBox aux = (VBox) hbox_days.getChildren().get(x);
            aux.setId(Integer.toString(x));
        }
    }
    
    

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) (
                (Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void cambiarImagen(ActionEvent event) {        
        JFileChooser exploradorJPG = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(
            "JPG & PNG Images","jpg", "png");
        exploradorJPG.setFileFilter(filtro);
        exploradorJPG.showOpenDialog(null);
        if (exploradorJPG.getSelectedFile() != null) {                      
            img.setImage(new Image(
                    exploradorJPG.getSelectedFile().toURI().toString()));
        }
        hyperlink_img.setText("Cambiar imagen");
    }    
    
    
    //Inicializa todos los comboBox del fxml doctor
    private void initComboBox() {        
        for (int x = 0; x <= 23; x++) {
            combo_hora.getItems().add(x,x);
            combo_hora1.getItems().add(x,x);
        }
        
        for (int x = 0; x <= 3; x++) {
            combo_min.getItems().add(x, x * 15);
            combo_min1.getItems().add(x, x * 15);
        }
        
        for (int x = 0; x < examination_rooms.size(); x++) {
            combo_consulta.getItems().add(x, examination_rooms.get(x).getIdentNumber());
        }
    }    
   
    //Comprueba si los datos introducidos son correctos.
    private boolean esCorrecto() {
        if(FXMLDocumentController.actual == 1){
            Integer prueba = combo_hora.getSelectionModel().getSelectedItem();
            return (field_nombre.getStyle() != redBackground) &&
                (field_apellidos.getStyle() != redBackground) &&
                (field_dni.getStyle() != redBackground) &&
                (field_telefono.getStyle() != redBackground) &&
                (combo_hora.getSelectionModel().getSelectedItem() != null)&&
                (combo_min.getSelectionModel().getSelectedItem() != null) &&
                (combo_hora1.getSelectionModel().getSelectedItem() != null) &&
                (combo_min1.getSelectionModel().getSelectedItem() != null) &&
                // cambiar a != quan ja no es fagen proves    
                (combo_consulta.getSelectionModel().getSelectedItem() == null) && 
                hayDiasSeleccionados();
        }
        return (field_nombre.getStyle() != redBackground) &&
                (field_apellidos.getStyle() != redBackground) &&
                (field_dni.getStyle() != redBackground) &&
                (field_telefono.getStyle() != redBackground);
    }
    
    @FXML
    //Cambia el color del fondo cuando se escribe un
    //caracter incorrecto
    private void colorFondoCaracter(KeyEvent event) {        
        TextField id = (TextField) event.getSource();
        boolean aux = true;        
        if (id == field_nombre || id == field_apellidos) {
            aux = caracteresCompatibles(id.getText(), NOMBRE_APELLIDOS);
        }
        else if (id == field_dni) {
            aux = caracteresCompatibles(id.getText(), DNI);
        }
        else if (id == field_telefono){
            aux = caracteresCompatibles(id.getText(), TELEFONO);
        }       
        
        
        if (!aux) {
            id.setStyle(redBackground);
        }
        else {
            id.setStyle(null);
        }
    }
    
    private boolean hayDiasSeleccionados() {
        boolean aux = false;
        for (Node v : hbox_days.getChildren()) {
            VBox auxV = (VBox) v;
            if (auxV.getStyle().equals(vboxDaysBackground)) {
                aux = true;
            }
        }
        return aux;
    }
    
    //Conjunto de caracteres o cadenas aceptadas en el 
    //textField segun el tipo
    private boolean caracteresCompatibles (String cadena, int tipo) { 
        boolean res = false;
        switch (tipo) {
            case 0:
                res = cadena.matches("[0-9]{7,8}[A-Za-z]{1}");
                break;
            case 1:
                res = cadena.matches("([A-Za-z]*[ñ]*[ ]*){1,2}");
                break;
            case 2:
                res = cadena.matches("[0-9]{9}");
                break;            
        }
        return res;
    }    
    
    //Inicializa las observable list del paciente y el doctor.
    public void initListaPersona(ObservableList<Patient> pat) {
        pacientes_local = pat;
    }     
    public void initListaDoctor(ObservableList<Doctor> doc) {
        doctor_local = doc;
    }   
  
    public void initDoctor(Doctor doc){
        //fields
        field_nombre.setText(doc.getName());
        field_apellidos.setText(doc.getSurname());
        field_dni.setText(doc.getIdentifier());
        field_telefono.setText(doc.getTelephon());
        img.setImage(doc.getPhoto());
        combo_hora.getSelectionModel().select(doc.getVisitStartTime().getHour());
        combo_min.getSelectionModel().select(doc.getVisitStartTime().getMinute() / 15);
        combo_hora1.getSelectionModel().select(doc.getVisitEndTime().getHour());
        combo_min1.getSelectionModel().select(doc.getVisitEndTime().getMinute() / 15);
        combo_consulta.getSelectionModel().select(doc.getExaminationRoom().getIdentNumber());
        ArrayList<Days> aux_days = doc.getVisitDays();
        //visualizar_dias(aux_days);
        
        //editable
        field_nombre.setEditable(false);
        field_apellidos.setDisable(false);
        field_dni.setEditable(false);
        field_telefono.setEditable(false);
        combo_hora.setDisable(true);
        combo_hora1.setDisable(true);
        combo_min.setDisable(true);
        combo_min1.setDisable(true);
        combo_consulta.setDisable(true);
        
        button_aceptar.setDisable(true);
        button_aceptar.setVisible(false);
        button_cancelar.setText("Salir");
        
        
        img.setDisable(true);
    }
    
    
    @FXML
    private void diasClicked(MouseEvent event) {
        VBox id = (VBox) event.getSource();
        if (estabaSeleccionado(event)) {
            id.setStyle("-fx-background-color: transparent;"+ 
                             "-fx-border-radius: 50px;" + 
                             "-fx-border-color: gray;" + 
                             "-fx-border-width: 3px;" +
                             "-fx-background-radius: 24px;" );
            days[Integer.parseInt(id.getId())] = true;
        }
        else {
            id.setStyle(vboxDaysBackground);
            days[Integer.parseInt(id.getId())] = false;
        }               
    }
    
    private boolean estabaSeleccionado(MouseEvent e) {
        String aux_style = ((VBox) e.getSource()).getStyle();
        return aux_style.matches(vboxDaysBackground);
    } 

    void initPatient(Patient aux) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
