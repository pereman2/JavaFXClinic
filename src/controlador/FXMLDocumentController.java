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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
/**
 *
 * @author pereman2
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TableView<Person> tabla;
    @FXML
    private TableColumn<Person, String> col_nombre;
    @FXML
    private TableColumn<Person, String> col_apellidos;
    @FXML
    private ImageView img_medico;
    @FXML
    private ImageView img_paciente;
    @FXML
    private ImageView img_citas;
    @FXML
    private ImageView img_nuevo;
    @FXML
    private ImageView img_filtrar;
    @FXML
    private ImageView img_eliminar;
    @FXML
    private TextField texto_filtro;
    @FXML
    private ComboBox<String> cb_tipoFiltro;
    @FXML
    private Button btn_filtrar;
    
    private Label label;
    private ClinicDBAccess database;
    private ArrayList<Doctor> doctores;
    private ArrayList<Patient> pacientes;
    private ObservableList<Patient> datos_pat = null;
    private ObservableList<Doctor> datos_doc = null;
    private String imgBackground = "-fx-background-color:  #b49a94;";
    
    
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
    }   
    //inicializa tabla pacientes
    private void initPatients(){
        
        //tabla.setItems(datos_pat);
        col_nombre.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getName()));
        col_apellidos.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getSurname()));
    }
    //inicializa tabla doctores
    private void initDoctors(){
        //tabla.setItems(datos_doc);
        col_nombre.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getName()));
        col_apellidos.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getSurname()));
    }

    @FXML
    private void añadirNuevo(MouseEvent event) {
        FXMLLoader ventanaAñadir = new FXMLLoader (
                        getClass().getResource("/vista/VentanaA.fxml"));
        try {
            Parent vAñadir = (Parent) ventanaAñadir.load();
            Stage s = new Stage();
            s.setScene(new Scene(vAñadir));
            s.setResizable(false);                
            s.show();
        } 
        catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
