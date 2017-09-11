/*
 * Copyright 2006-2016 CIRDLES.org.
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

import java.io.File;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.cirdles.ambapo.ConversionFileHandler;

/**
 *
 * @author evc1996
 */
public class Ambapo extends Application{
    
    public static final String VERSION;
    public static final String RELEASE_DATE;

    static {
        /*ResourceExtractor ambapoResourceExtractor
                = new ResourceExtractor(Ambapo.class);*/

        String version = "0.1.0";
        String releaseDate = "Nov. 25, 2016";

        /* get version number and release date written by pom.xml
        Path resourcePath = ambapoResourceExtractor.extractResourceAsPath("version.txt");
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(resourcePath, charset)) {
            String[] versionText = reader.readLine().split("=");
            version = versionText[1];

            String[] versionDate = reader.readLine().split("=");
            releaseDate = versionDate[1];
        } catch (IOException x) {
            version = "version";
            releaseDate = "date";

            System.err.format("IOException: %s%n", x);
        }*/

        VERSION = version;
        RELEASE_DATE = releaseDate;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) {

        org.cirdles.ambapo.ConversionFileHandler conversionFileHandler = new ConversionFileHandler();
        
        launch(args);
    }
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Parent root = new AnchorPane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        
        Window primaryStageWindow = primaryStage.getScene().getWindow();
        
        primaryStage.setOnCloseRequest((WindowEvent e) -> {
            Platform.exit();
            System.exit(0);
        });
        
        scene.setRoot(FXMLLoader.load(getClass().getResource("AmbapoUIController.fxml")));
                
        primaryStage.show();
        primaryStage.setMinHeight(scene.getHeight() + 15);
        primaryStage.setMinWidth(scene.getWidth());

        primaryStage.show();
        primaryStage.setMinHeight(scene.getHeight() + 15);
        primaryStage.setMinWidth(scene.getWidth());

    }
}
