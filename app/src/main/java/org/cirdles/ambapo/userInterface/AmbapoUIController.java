/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cirdles.ambapo.userInterface;

import com.apple.eawt.Application;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.cirdles.ambapo.ConversionFileHandler;
import org.cirdles.ambapo.Coordinate;
import org.cirdles.ambapo.Datum;
import org.cirdles.ambapo.LatLongToUTM;
import org.cirdles.ambapo.UTM;
import org.cirdles.ambapo.UTMToLatLong;

/**
 * FXML Controller class
 *
 * @author evc1996
 */
public class AmbapoUIController implements Initializable {

    @FXML
    private TextField eastingText;
    @FXML
    private Label eastingLabel;
    @FXML
    private Label northingLabel;
    @FXML
    private Label zoneLetterLabel;
    @FXML
    private Label hemisphereLabel;
    @FXML
    private Label zoneNumberLabel;
    @FXML
    private Label latitudeLabel;
    @FXML
    private Label longitudeLabel;
    @FXML
    private TextField northingText;
    @FXML
    private TextField zoneNumberText;
    @FXML
    private TextField latitudeText;
    @FXML
    private TextField longitudeText;
    @FXML
    private Button sourceFileButton;
    @FXML
    private TextField sourceFileText;
    @FXML
    private RadioButton fromUTMRadioButton;
    @FXML
    private RadioButton fromLatLongRadioButton;
    @FXML
    private RadioButton toUTMRadioButton;
    @FXML
    private RadioButton toLatLongRadioButton;
    @FXML
    private Button convertButton;
    @FXML
    private Button openConvertedFileButton;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu ambapoMenuBarOption;
    @FXML
    private MenuItem openTemplateLatLong;
    @FXML
    private MenuItem openTemplateUTM;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private MenuItem githubMenuItem;
    @FXML
    private MenuItem cirdlesWebsiteMenuItem;
    @FXML
    private Text title;
    @FXML
    private Button convertToLeftButton;
    @FXML
    private Button convertTotRightButton;
    @FXML
    private Text bulkConversionTitle;
    @FXML
    private ChoiceBox<String> datumChooser;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private ChoiceBox<Character> zoneLetterChooser;
    @FXML
    private ChoiceBox<Character> hemisphereChooser;
    
    public ConversionFileHandler conversionFileHandler;
    
    public boolean toLatLong;
    public boolean toUTM;
    public boolean fromLatLong;
    public boolean fromUTM;
    
    public BigDecimal latitude_val;
    public BigDecimal longitude_val;
    public BigDecimal easting_val;
    public BigDecimal northing_val;
    
    public char hemisphere_val;
    public char zoneletter_val;
    
    public int zonenumber_val;
    
    public String datumSoloConvert;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ObservableList<String> datumChoices = FXCollections.observableArrayList(
                Datum.WGS84.getDatum(),
                Datum.WGS72.getDatum(),
                Datum.NAD83.getDatum(),
                Datum.NAD27.getDatum(),
                Datum.KRASOVSKY_1940.getDatum(),
                Datum.IN24.getDatum(),
                Datum.HAYFORD_1909.getDatum(),
                Datum.GRS80.getDatum(),
                Datum.EVEREST_1830.getDatum(),
                Datum.CLARKE_1880.getDatum(),
                Datum.CLARKE_1866.getDatum(),
                Datum.BESSEL_1841.getDatum(),
                Datum.AIRY_1830.getDatum(),
                Datum.AGD65.getDatum());
        
        //A, B, I, O, Y, and Z aren't valid zone letters
        ObservableList<Character> zoneLetters = FXCollections.observableArrayList(
            '*', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X');
        
        ObservableList<Character> hemispheres = FXCollections.observableArrayList(
            '*', 'N', 'S');
        
        datumChooser.setItems(datumChoices);
        datumChooser.getSelectionModel().selectFirst();
        
        zoneLetterChooser.setItems(zoneLetters);
        zoneLetterChooser.getSelectionModel().selectFirst();
        
        hemisphereChooser.setItems(hemispheres);
        hemisphereChooser.getSelectionModel().selectFirst();
        
        // check for MacOS
        String lcOSName = System.getProperty("os.name").toLowerCase();
        boolean MAC_OS_X = lcOSName.startsWith("mac os x");
        if (MAC_OS_X) {
            Application myAboutHandler = new MacOSAboutHandler();
        }

        this.conversionFileHandler = new ConversionFileHandler();
        
        fromUTM = true;
        toLatLong = true;
        
        openConvertedFileButton.setDisable(true);
        
        setupListeners();
    }    

    @FXML
    private void eastingTextAction(ActionEvent event) {
    }
    
    private void setupListeners(){
        
    }

    @FXML
    private void toUTMClicked(MouseEvent event) {
        //Set latitude
        if(latitudeText.getText().length() <= 0){
            latitude_val = BigDecimal.ZERO;
            latitudeText.setText("0");
        }
        else if(Double.parseDouble(latitudeText.getText()) < -90){
            latitude_val = new BigDecimal(-90);
            latitudeText.setText("-90");
        }
        else if(Double.parseDouble(latitudeText.getText()) > 90){
            latitude_val = new BigDecimal(90);
            latitudeText.setText("00");
        }
        else{
            latitude_val = new BigDecimal(latitudeText.getText());
        }
        
        //-Set longitude
        if(longitudeText.getText().length() <= 0){
            longitude_val = BigDecimal.ZERO;
            longitudeText.setText("0");
        }
        else if(Double.parseDouble(longitudeText.getText()) < -180){
            longitude_val = new BigDecimal(-180);
            longitudeText.setText("-180");
        }
        else if(Double.parseDouble(longitudeText.getText()) > 180){
            longitude_val = new BigDecimal(180);
            longitudeText.setText("180");
        }
        else{
            longitude_val = new BigDecimal(longitudeText.getText());
        }
        
        //Set Datum
        datumSoloConvert = datumChooser.getValue();
        
        //do conversion
        UTM utm = null;
        try {
            utm = LatLongToUTM.convert(latitude_val, longitude_val, datumSoloConvert);
        } catch (Exception ex) {
            Logger.getLogger(AmbapoUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        eastingText.setText(utm.getEasting().toString());
        northingText.setText(utm.getNorthing().toString());
        
        if(utm.getHemisphere() == 'N'){
            hemisphereChooser.getSelectionModel().select(1);
        }else{
            hemisphereChooser.getSelectionModel().select(2);
        }
        
        zoneLetterChooser.setValue(utm.getZoneLetter());
        zoneNumberText.setText(String.valueOf(utm.getZoneNumber()));
    }

    @FXML
    private void toLatLongClicked(MouseEvent event) {
        
        //Set easting value
        //System.out.println("EASTING: " + easting.getText() + "\n");
        if(eastingText.getText().length() <= 0){            
            easting_val = BigDecimal.ZERO;
            eastingText.setText("0");
        }else{
            easting_val = new BigDecimal(eastingText.getText());
        }
        
        //Set northing value
         //System.out.println("NORTHING: " + northing.getText() + "\n");
        if(northingText.getText().length() <= 0){
            northing_val = BigDecimal.ZERO;
            northingText.setText("0");
        }else{
            northing_val = new BigDecimal(northingText.getText());
        }
        
        //Set hemisphere value
        hemisphere_val = hemisphereChooser.getValue();
                
        //Set zone letter value
        //System.out.println("ZONE LETTER: " + zoneLetter.getText() + "\n");
        zoneletter_val = zoneLetterChooser.getValue();
        
        //Set zone number value
        //System.out.println("ZONE Number: " + zoneNumber.getText() + "\n");
        if(zoneNumberText.getText().length() <= 0 || Integer.parseInt(zoneNumberText.getText()) < 1){
            zonenumber_val = 1;
        }
        else if(Integer.parseInt(zoneNumberText.getText()) > 60){
            zonenumber_val = 60;
        }
        else{
            zonenumber_val = Integer.parseInt(zoneNumberText.getText());
        }
        
        //Set Datum to Convert To
        //System.out.println("DATUM: " + datumSoloConvert.getSelectedItem().toString() + "\n");
        datumSoloConvert = datumChooser.getValue();
        
        //create UTM object
        UTM utm = null;
        try {
            utm = new UTM(easting_val, northing_val, hemisphere_val, zonenumber_val, zoneletter_val);
        } catch (Exception ex) {
            Logger.getLogger(AmbapoUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Do the conversion
        Coordinate latlong = null;
        try {
            latlong = UTMToLatLong.convert(utm, datumSoloConvert);
        } catch (Exception ex) {
            Logger.getLogger(AmbapoUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        latitudeText.setText(latlong.getLatitude().toString());
        longitudeText.setText(latlong.getLongitude().toString());
    }
    
}
