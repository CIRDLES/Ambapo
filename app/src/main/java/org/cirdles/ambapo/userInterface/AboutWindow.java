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

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 *
 * @author evc1996
 */
public class AboutWindow {
    
    private final Stage aboutWindow;
    private Window ownerWindow;

    public AboutWindow(Stage ownerStage) throws IOException {
        this.aboutWindow = new Stage();
        this.ownerWindow = ownerStage.getOwner();
        initialize();
    }

    public void initialize() throws IOException {
        Parent aboutPage = FXMLLoader.load(getClass().getResource("AmbapoAbout.fxml"));
        Scene scene = new Scene(aboutPage);
        //primaryStage.setScene(scene);
                
        aboutWindow.setResizable(false);
        aboutWindow.setScene(scene);
            
        aboutWindow.requestFocus();
        aboutWindow.initModality(Modality.WINDOW_MODAL);
        aboutWindow.initOwner(ownerWindow);
        
        aboutWindow.setOnCloseRequest((WindowEvent e) -> {
            aboutWindow.close();
        });

    }
    
    public void showAboutWindow(){
        aboutWindow.show();
    }
    
}
