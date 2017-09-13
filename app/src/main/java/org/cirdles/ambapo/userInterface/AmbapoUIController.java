/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cirdles.ambapo.userInterface;

import com.apple.eawt.Application;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
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
    private TextField latitudeText;
    @FXML
    private TextField longitudeText;
    @FXML
    private Button sourceFileButton;
    @FXML
    private TextField sourceFileText;
    @FXML
    private Button convertButton;
    @FXML
    private Button openConvertedFileButton;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private MenuItem githubMenuItem;
    @FXML
    private Text title;
    @FXML
    private Text bulkConversionTitle;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private ChoiceBox<Character> zoneLetterChooser;
    @FXML
    private ChoiceBox<Character> hemisphereChooser;
    
    public ConversionFileHandler conversionFileHandler;
    
    public BigDecimal latitude_val;
    public BigDecimal longitude_val;
    public BigDecimal easting_val;
    public BigDecimal northing_val;
    
    public char hemisphere_val;
    public char zoneletter_val;
    
    public int zonenumber_val;
    
    public String datumSoloConvertUTMLatLong;
    
    public File fileToConvert;
    public File convertedFile;
    @FXML
    private Button convertToUTMButton;
    @FXML
    private Button convertToLatLongButton;
    @FXML
    private ChoiceBox<String> datumChooserUTMAndLatLong;
    @FXML
    private ChoiceBox<String> bulkConversionChooser;
    @FXML
    private ChoiceBox<Integer> zoneNumberChooser;
    @FXML
    private ImageView logo;
    @FXML
    private ImageView logo1;
    @FXML
    private Label latitudeLabel1;
    @FXML
    private Label longitudeLabel1;
    @FXML
    private TextField fromLatitude;
    @FXML
    private TextField fromLongitude;
    @FXML
    private Button convertFromLatLongToLatLongButton;
    @FXML
    private ChoiceBox<String> datumChooserLatLongFrom;
    @FXML
    private Label latitudeLabel11;
    @FXML
    private Label longitudeLabel11;
    @FXML
    private TextField toLatitude;
    @FXML
    private TextField toLongitude;
    @FXML
    private ChoiceBox<String> datumChooserLatLongTo;
    @FXML
    private Menu ambapoMenuBarOptionFile;
    @FXML
    private MenuItem openTemplateLatLongToUTM;
    @FXML
    private MenuItem openTemplateUTMToLatLong;
    @FXML
    private MenuItem openTemplateLatLongToLatLong;
    @FXML
    private Menu ambapoMenuBarOptionHelp;
    

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
        
        ObservableList<String> toFromDatum = FXCollections.observableArrayList(
        "To/From Datum");
        toFromDatum.addAll(datumChoices);
        
        ObservableList<String> toDatum = FXCollections.observableArrayList("To Datum");
        toDatum.addAll(datumChoices);
        
        ObservableList<String> fromDatum = FXCollections.observableArrayList("From Datum");
        fromDatum.addAll(datumChoices);
        
        //A, B, I, O, Y, and Z aren't valid zone letters
        ObservableList<Character> zoneLetters = FXCollections.observableArrayList(
            '*', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X');
        
        ObservableList<Character> hemispheres = FXCollections.observableArrayList(
            '*', 'N', 'S');
        
        ObservableList<Integer> zoneNumbers = FXCollections.observableArrayList(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 
                37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 
                53, 54, 55, 56, 57, 58, 59, 60);
        
        ObservableList<String> bulkConversionChoices = FXCollections.observableArrayList(
        "From UTM to LatLong", "From LatLong to UTM", "From LatLong to LatLong");
        
        bulkConversionChooser.setItems(bulkConversionChoices);
        bulkConversionChooser.getSelectionModel().selectFirst();
        
        datumChooserUTMAndLatLong.setItems(toFromDatum);
        datumChooserLatLongTo.setItems(toDatum);
        datumChooserLatLongFrom.setItems(fromDatum);
        
        datumChooserUTMAndLatLong.setValue("To/From Datum");
        datumChooserLatLongTo.setValue("To Datum");
        datumChooserLatLongFrom.setValue("From Datum");
        
        zoneLetterChooser.setItems(zoneLetters);
        zoneLetterChooser.getSelectionModel().selectFirst();
        
        hemisphereChooser.setItems(hemispheres);
        hemisphereChooser.getSelectionModel().selectFirst();
        
        zoneNumberChooser.setItems(zoneNumbers);
        zoneNumberChooser.getSelectionModel().selectFirst();
        
        // check for MacOS
        String lcOSName = System.getProperty("os.name").toLowerCase();
        boolean MAC_OS_X = lcOSName.startsWith("mac os x");
        if (MAC_OS_X) {
            Application myAboutHandler = new MacOSAboutHandler();
        }

        this.conversionFileHandler = new ConversionFileHandler();
        
        openConvertedFileButton.setDisable(true);
        convertButton.setDisable(true);
        
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
            latitudeText.setText("0");
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
        datumSoloConvertUTMLatLong = datumChooserUTMAndLatLong.getValue();
        
        //do conversion
        UTM utm = null;
        try {
            utm = LatLongToUTM.convert(latitude_val, longitude_val, datumSoloConvertUTMLatLong);
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
        zoneNumberChooser.setValue(utm.getZoneNumber());
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
        zonenumber_val = zoneNumberChooser.getValue();
        
        
        //Set Datum to Convert To
        //System.out.println("DATUM: " + datumSoloConvertUTMLatLong.getSelectedItem().toString() + "\n");
        datumSoloConvertUTMLatLong = datumChooserUTMAndLatLong.getValue();
        
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
            latlong = UTMToLatLong.convert(utm, datumSoloConvertUTMLatLong);
        } catch (Exception ex) {
            Logger.getLogger(AmbapoUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        latitudeText.setText(latlong.getLatitude().toString());
        longitudeText.setText(latlong.getLongitude().toString());
    }

    @FXML
    private void sourceFileButtonClicked(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File result = fileChooser.showOpenDialog(mainAnchorPane.getScene().getWindow());
        
        if (result != null) {
            String resultString = result.toString();
            if(!resultString.endsWith(".csv"))
                resultString = resultString.concat(".csv");
            
            sourceFileText.setText(resultString);   
            convertButton.setDisable(false);
        }
  
    }

    @FXML
    private void convertFileClicked(MouseEvent event) {
        fileToConvert = new File(sourceFileText.getText());
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Specify a file to save");   
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        convertedFile = fileChooser.showSaveDialog(mainAnchorPane.getScene().getWindow());
        
        if (convertedFile != null) {
            try {
                
                conversionFileHandler.setCurrentFileLocation(fileToConvert.getCanonicalPath());
                convertButton.setDisable(false);
            } catch (IOException ex) {
                Logger.getLogger(AmbapoUI.class.getName()).log(Level.SEVERE, null, ex);
                if(!conversionFileHandler.currentFileLocationToConvertIsFile()){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Not a file.");

                    alert.showAndWait();
                }
            }
            
            if(bulkConversionChooser.getValue().equals(bulkConversionChooser.getItems().get(0))){
                try {
                    conversionFileHandler.writeConversionsUTMToLatLong(convertedFile);
                    openConvertedFileButton.setDisable(false);
                } catch (Exception ex) {
                    Logger.getLogger(AmbapoUI.class.getName()).log(Level.SEVERE, null, ex);
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Conversion could not be completed.");
                    alert.setContentText("Check if csv file is formatted properly.");

                    alert.showAndWait();
                }
            }
            
            else if(bulkConversionChooser.getValue().equals(bulkConversionChooser.getItems().get(1))){
                try {
                    conversionFileHandler.writeConversionsLatLongToUTM(convertedFile);
                    openConvertedFileButton.setDisable(false);
                } catch (Exception ex) {
                    Logger.getLogger(AmbapoUI.class.getName()).log(Level.SEVERE, null, ex);
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Conversion could not be completed.");
                    alert.setContentText("Check if csv file is formatted properly.");

                    alert.showAndWait();
                }
            }
            
            else{
                try{
                    conversionFileHandler.writeConversionsLatLongToLatLong(convertedFile);
                    openConvertedFileButton.setDisable(false);  
                } catch (Exception ex) {
                    Logger.getLogger(AmbapoUI.class.getName()).log(Level.SEVERE, null, ex);
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Conversion could not be completed.");
                    alert.setContentText("Check if csv file is formatted properly.");

                    alert.showAndWait();
                }
                
            }
            
        
        }
    }

    @FXML
    private void openConvertedFileClicked(MouseEvent event) {
        try {
            // TODO add your handling code here:
            Desktop.getDesktop().open(convertedFile);
        } catch (IOException ex) {
            Logger.getLogger(AmbapoUI.class.getName()).log(Level.SEVERE, null, ex);
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to open file.");
            alert.setContentText("Check if you have a valid file name.");
            alert.showAndWait();
        }
    }

    @FXML
    private void clickOpenTemplateLatLongToUTM(ActionEvent event) throws IOException {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        File file = new File(getClass().getResource("latLongToUTMTemplate.csv").getFile());
        
        if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
            desktop.open(file);
        } else {
            throw new UnsupportedOperationException("Open action not supported");
        }
    }

    @FXML
    private void clickOpenTemplateUTMToLatLong(ActionEvent event) throws IOException {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        File file = new File(getClass().getResource("utmToLatLongTemplate.csv").getFile());
        
        if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
            desktop.open(file);
        } else {
            throw new UnsupportedOperationException("Open action not supported");
        }
    }

    @FXML
    private void clickOpenTemplateLatLongToLatLong(ActionEvent event) throws IOException {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        File file = new File(getClass().getResource("latLongToLatLongTemplate.csv").getFile());
        
        if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
            desktop.open(file);
        } else {
            throw new UnsupportedOperationException("Open action not supported");
        }
    }

    @FXML
    private void clickAboutMenuItem(ActionEvent event) {
    }

    @FXML
    private void clickGithubMenuItem(ActionEvent event) {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

        try
        {
            URI uri = new URI("https://github.com/CIRDLES/Ambapo");
            Desktop dt = Desktop.getDesktop();
            dt.browse(uri);
        }
        catch(IOException | URISyntaxException ex){}
    }
    
}
