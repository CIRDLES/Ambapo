/*
 * Copyright 2017 CIRDLES.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.cirdles.ambapo.userInterface;

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
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.cirdles.ambapo.ConversionFileHandler;
import org.cirdles.ambapo.Coordinate;
import org.cirdles.ambapo.Datum;
import org.cirdles.ambapo.LatLongToLatLong;
import org.cirdles.ambapo.LatLongToUTM;
import org.cirdles.ambapo.UTM;
import org.cirdles.ambapo.UTMToLatLong;
import org.apache.commons.lang3.math.NumberUtils;

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
    private Label sourceFileText;
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
    @FXML
    private Button convertRightLatLongToLeftLatLongButton;
    @FXML
    private Label sourceFileLabel;
        

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ObservableList<String> datumChoices = FXCollections.observableArrayList(
                Datum.DATUMS);
        
        //A, B, I, O, Y, and Z aren't valid zone letters
        ObservableList<Character> zoneLetters = FXCollections.observableArrayList(
            UTM.ZONE_LETTERS);
        
        ObservableList<Character> hemispheres = FXCollections.observableArrayList(
            '*', 'N', 'S');
        
        ObservableList<Integer> zoneNumbers = FXCollections.observableArrayList(
        UTM.ZONE_NUMBERS);
        
        ObservableList<String> bulkConversionChoices = FXCollections.observableArrayList(
        "From UTM to LatLong", "From LatLong to UTM", "From LatLong to LatLong");
        
        bulkConversionChooser.setItems(bulkConversionChoices);
        bulkConversionChooser.getSelectionModel().selectFirst();
        
        datumChooserUTMAndLatLong.setItems(datumChoices);
        datumChooserLatLongTo.setItems(datumChoices);
        datumChooserLatLongFrom.setItems(datumChoices);
        
        datumChooserUTMAndLatLong.setValue(Datum.WGS84.getDatum());
        datumChooserLatLongTo.setValue(Datum.WGS84.getDatum());
        datumChooserLatLongFrom.setValue(Datum.WGS84.getDatum());
        
        zoneLetterChooser.setItems(zoneLetters);
        zoneLetterChooser.getSelectionModel().selectFirst();
        
        hemisphereChooser.setItems(hemispheres);
        hemisphereChooser.getSelectionModel().selectFirst();
        
        zoneNumberChooser.setItems(zoneNumbers);
        zoneNumberChooser.getSelectionModel().selectFirst();

        this.conversionFileHandler = new ConversionFileHandler();
        
        openConvertedFileButton.setDisable(true);
        convertButton.setDisable(true);
                
        setupListeners();
    }    
    
    private void setupListeners(){
        eastingText.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            checkUTMToLatLongCorrect();
        });
        
        northingText.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            checkUTMToLatLongCorrect();
        });
        
        latitudeText.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            checkLatLongToUTMCorrect();
        });
        
        longitudeText.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            checkLatLongToUTMCorrect();
        });
        
        zoneLetterChooser.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
            checkUTMToLatLongCorrect();
            populateHemisphere();
        });
        
        hemisphereChooser.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
            checkZoneLetter();
            checkUTMToLatLongCorrect();
        });
        
        datumChooserUTMAndLatLong.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
            checkUTMToLatLongCorrect();
            checkLatLongToUTMCorrect();
        });
        
        toLatitude.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            checkLatLongToLatLongCorrect();
        });
        
        fromLatitude.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            checkLatLongToLatLongCorrect();
        });
        
        toLongitude.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            checkLatLongToLatLongCorrect();
        });
        
        fromLongitude.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            checkLatLongToLatLongCorrect();
        });
        
        datumChooserLatLongFrom.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
            checkLatLongToLatLongCorrect();
        });
        
        datumChooserLatLongTo.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
            checkLatLongToLatLongCorrect();
        });
        
    }
    

    @FXML
    private void toUTMClicked(MouseEvent event) {
        //Set latitude
        latitude_val = new BigDecimal(latitudeText.getText());
        
        //-Set longitude
        longitude_val = new BigDecimal(longitudeText.getText());
        
        //Set Datum
        datumSoloConvertUTMLatLong = datumChooserUTMAndLatLong.getValue();
        
        //do conversion
        UTM utm = null;
        try {
            utm = LatLongToUTM.convert(latitude_val, longitude_val, datumSoloConvertUTMLatLong);
        } catch (Exception ex) {
            Logger.getLogger(AmbapoUIController.class.getName()).log(Level.SEVERE, null, ex);
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
        easting_val = new BigDecimal(eastingText.getText());
        
        //Set northing value
        northing_val = new BigDecimal(northingText.getText());
        
        //Set hemisphere value
        hemisphere_val = hemisphereChooser.getValue();
                
        //Set zone letter value
        zoneletter_val = zoneLetterChooser.getValue();
        
        //Set zone number value
        zonenumber_val = zoneNumberChooser.getValue();
        
        //Set Datum to Convert To
        datumSoloConvertUTMLatLong = datumChooserUTMAndLatLong.getValue();
        
        //create UTM object
        UTM utm = null;
        try {
            utm = new UTM(easting_val, northing_val, hemisphere_val, zonenumber_val, zoneletter_val);
        } catch (Exception ex) {
            Logger.getLogger(AmbapoUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Do the conversion
        Coordinate latlong = null;
        try {
            latlong = UTMToLatLong.convert(utm, datumSoloConvertUTMLatLong);
        } catch (Exception ex) {
            Logger.getLogger(AmbapoUIController.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(AmbapoUIController.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(AmbapoUIController.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(AmbapoUIController.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(AmbapoUIController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AmbapoUIController.class.getName()).log(Level.SEVERE, null, ex);
            
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
    private void clickAboutMenuItem(ActionEvent event) throws IOException {
        Ambapo.aboutWindow.showAboutWindow();
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

    @FXML
    private void convertFromLeftLatLongToRightLatLongButtonClicked(MouseEvent event) {
        try {
            Coordinate result = LatLongToLatLong.convert(new BigDecimal(fromLatitude.getText()),
                    new BigDecimal(fromLongitude.getText()), datumChooserLatLongFrom.getValue(),
                    datumChooserLatLongTo.getValue());
            
            toLatitude.setText(result.getLatitude().toString());
            toLongitude.setText(result.getLongitude().toString());
            
            toLatitude.setDisable(false);            
            toLongitude.setDisable(false);
            
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to complete conversion.");
            alert.setContentText("Check if you have valid coordinates.");
            alert.showAndWait();
            Logger.getLogger(AmbapoUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void convertFromRightLatLongToLeftLatLongButtonClicked(MouseEvent event) {
        try {
            Coordinate result = LatLongToLatLong.convert(new BigDecimal(toLatitude.getText()),
                    new BigDecimal(toLongitude.getText()), datumChooserLatLongTo.getValue(),
                    datumChooserLatLongFrom.getValue());
            
            fromLatitude.setText(result.getLatitude().toString());
            fromLongitude.setText(result.getLongitude().toString());
            
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to complete conversion.");
            alert.setContentText("Check if you have valid coordinates.");
            alert.showAndWait();
            Logger.getLogger(AmbapoUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void checkUTMToLatLongCorrect() {
        if((zoneLetterChooser.getSelectionModel().getSelectedIndex() > 0 || 
            hemisphereChooser.getSelectionModel().getSelectedIndex() > 0) &&
            !eastingText.getText().isEmpty() && !northingText.getText().isEmpty() &&
            NumberUtils.isNumber(eastingText.getText()) && 
            NumberUtils.isNumber(northingText.getText()) &&
            Double.parseDouble(eastingText.getText()) >= UTM.MIN_EASTING &&
            Double.parseDouble(eastingText.getText()) <= UTM.MAX_EASTING &&
            Double.parseDouble(northingText.getText()) >= UTM.MIN_NORTHING &&
            Double.parseDouble(northingText.getText()) <= UTM.MAX_NORTHING &&
            datumChooserUTMAndLatLong.getSelectionModel().getSelectedIndex() > 0){
               convertToLatLongButton.setDisable(false);
        } 
        else
            convertToLatLongButton.setDisable(true);
    }
    
    private void checkLatLongToUTMCorrect() {
        if(!latitudeText.getText().isEmpty() && !longitudeText.getText().isEmpty() &&
                datumChooserUTMAndLatLong.getSelectionModel().getSelectedIndex() > 0 &&
                NumberUtils.isNumber(latitudeText.getText()) && 
                NumberUtils.isNumber(longitudeText.getText()) &&
                Double.parseDouble(latitudeText.getText()) >= Coordinate.MIN_LATITUDE && 
                Double.parseDouble(latitudeText.getText()) <= Coordinate.MAX_LATITUDE &&
                Double.parseDouble(longitudeText.getText()) >= Coordinate.MIN_LONGITUDE &&
                Double.parseDouble(longitudeText.getText()) <= Coordinate.MAX_LONGITUDE)
            convertToUTMButton.setDisable(false);
        else
            convertToUTMButton.setDisable(true);
    }
    
    private void checkLatLongToLatLongCorrect() {
        if(!fromLatitude.getText().isEmpty() && !fromLongitude.getText().isEmpty() &&
            NumberUtils.isNumber(fromLatitude.getText()) && 
            NumberUtils.isNumber(fromLongitude.getText()) &&
            Double.parseDouble(fromLatitude.getText()) >= Coordinate.MIN_LATITUDE && 
            Double.parseDouble(fromLatitude.getText()) <= Coordinate.MAX_LATITUDE &&
            Double.parseDouble(fromLongitude.getText()) >= Coordinate.MIN_LONGITUDE &&
            Double.parseDouble(fromLongitude.getText()) <= Coordinate.MAX_LONGITUDE &&
            datumChooserLatLongFrom.getSelectionModel().getSelectedIndex() > 0 &&
            datumChooserLatLongTo.getSelectionModel().getSelectedIndex() > 0){
            convertFromLatLongToLatLongButton.setDisable(false);
        }else
            convertFromLatLongToLatLongButton.setDisable(true);
        
        if(!toLatitude.getText().isEmpty() && !toLongitude.getText().isEmpty() &&
            NumberUtils.isNumber(toLatitude.getText()) && 
            NumberUtils.isNumber(toLongitude.getText()) &&
            Double.parseDouble(toLatitude.getText()) >= Coordinate.MIN_LATITUDE && 
            Double.parseDouble(toLatitude.getText()) <= Coordinate.MAX_LATITUDE &&
            Double.parseDouble(toLongitude.getText()) >= Coordinate.MIN_LONGITUDE &&
            Double.parseDouble(toLongitude.getText()) <= Coordinate.MAX_LONGITUDE &&
            datumChooserLatLongFrom.getSelectionModel().getSelectedIndex() > 0 &&
            datumChooserLatLongTo.getSelectionModel().getSelectedIndex() > 0){
            convertRightLatLongToLeftLatLongButton.setDisable(false);
        }else
            convertRightLatLongToLeftLatLongButton.setDisable(true);
    }

    private void populateHemisphere() {
        if(zoneLetterChooser.getSelectionModel().getSelectedIndex() >= 11)
            hemisphereChooser.setValue(hemisphereChooser.getItems().get(1));
        else
            hemisphereChooser.setValue(hemisphereChooser.getItems().get(2));
    }
    
    private void checkZoneLetter() {
        int currentZoneLetter = zoneLetterChooser.getSelectionModel().getSelectedIndex();
        
        if(currentZoneLetter != 0){
            int currentHemisphere = hemisphereChooser.getSelectionModel().getSelectedIndex();
            
            if( (currentHemisphere == 1 && currentZoneLetter < 11) || 
            (currentHemisphere == 2 && currentZoneLetter >= 11) )
            {
                zoneLetterChooser.setValue(zoneLetterChooser.getItems().get(0));
            }
        }
    }
    
}
