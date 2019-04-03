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
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author pereman2
 */
public class VentanaAñadirController implements Initializable {    
    
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
        
    }

    @FXML
    private void cancelar(ActionEvent event) {
    }
    
}
