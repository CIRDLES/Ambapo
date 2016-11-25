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
package org.cirdles.ambapo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import org.cirdles.commons.util.ResourceExtractor;

/**
 *
 * @author evc1996
 */
public class Ambapo {
    
    public static final String VERSION;
    public static final String RELEASE_DATE;

    static {
        ResourceExtractor calamariResourceExtractor
                = new ResourceExtractor(Ambapo.class);

        String version;
        String releaseDate;

        // get version number and release date written by pom.xml
        Path resourcePath = calamariResourceExtractor.extractResourceAsPath("version.txt");
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
        }

        VERSION = version;
        RELEASE_DATE = releaseDate;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {

        org.cirdles.ambapo.ConversionFileHandler conversionFileHandler = new ConversionFileHandler();

        //Path listOfConversionFiles = conversionFileResourceExtractor.extractResourceAsPath("listOfPrawnFiles.txt");
        
        // Set up default folder for reports
        File defaultAmbapoConversionsFolder = new File("AmbapoConversions_v" + VERSION);
        defaultAmbapoConversionsFolder.mkdir();
        conversionFileHandler.setAFileLocationToWriteTo(defaultAmbapoConversionsFolder.getCanonicalPath());
        
        /* Create and display the form */
            java.awt.EventQueue.invokeLater(() -> {
                new org.cirdles.ambapo.userInterface.AmbapoUI(conversionFileHandler).setVisible(true);
            });
    }
    
}
