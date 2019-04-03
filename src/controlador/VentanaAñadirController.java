/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.ClinicDBAccess;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Patient;

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
            
    private String redBackground = "-fx-background-color: red;";
    
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         db = ClinicDBAccess.getSingletonClinicDBAccess();
    }    

    @FXML
    private void aceptar(ActionEvent event) {
        String name = field_nombre.getText();
        String surname = field_apellidos.getText();
        String identifier = field_dni.getText();
        String telephon = field_telefono.getText();
        Image photo = img.getImage();
        Patient aux = new Patient(identifier, name, surname, telephon, photo);
        FXMLDocumentController.getClinicDBAccess().getPatients().add(aux);
        pacientes_local.add(aux);
        ((Stage) img.getScene().getWindow()).close();
    }

    @FXML
    private void cancelar(ActionEvent event) {
    }
    
    @FXML
    private void cambiarImagen(ActionEvent event) {
        JFileChooser exploradorJPG = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(
            "JPG & PNG Images","jpg", "png");
        exploradorJPG.setFileFilter(filtro);
        exploradorJPG.setVisible(true);
        if (exploradorJPG.getSelectedFile() != null) {
            File imagen = exploradorJPG.getSelectedFile();            
            img.setImage(new Image(imagen.getAbsolutePath()));
        }
        hyperlink_img.setText("Cambiar imagen");
    }
    
    @FXML
    //Cambia el color del fondo cuando se escribe un
    //caracter incorrecto
    private void colorFondoCaracter(KeyEvent event) {        
        TextField id = (TextField) event.getSource();
        boolean aux;
        
        if (id == field_nombre || id == field_apellidos) {
            aux = caracteresCompatibles(id.getText(), NOMBRE_APELLIDOS);
        }
        else if (id == field_dni) {
            aux = caracteresCompatibles(id.getText(), DNI);
        }
        else {
            aux = caracteresCompatibles(id.getText(), TELEFONO);
        }
        
        if (!aux) {
            id.setStyle(redBackground);
        }
        else {
            id.setStyle(null);
        }
    }
    
    
    private boolean caracteresCompatibles (String cadena, int tipo) { 
        boolean res = false;
        switch (tipo) {
            case 0:
                res = cadena.matches("[0-9]{7,8}[A-Za-z]");
                break;
            case 1:
                res = cadena.matches("[A-Za-z]");
                break;
            case 2:
                res = cadena.matches("[0-9]");
                break;            
        }
        return res;
    }
    public void initListaPersona(ObservableList<Patient> pat) {
        pacientes_local = pat;
    }
    

    
    
}
