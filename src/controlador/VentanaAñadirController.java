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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void aceptar(ActionEvent event) {
        String name = field_nombre.getText();
        String surname = field_apellidos.getText();
        String identifier = field_dni.getText();
        String telephon = field_telefono.getText();
        Image photo = img.getImage();
        Patient aux = new Patient(identifier, name, surname, telephon, photo);
        FXMLDocumentController.addPatient(aux);
    }

    @FXML
    private void cancelar(ActionEvent event) {
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

    
    
}
