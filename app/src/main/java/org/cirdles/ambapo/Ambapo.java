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
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.cirdles.commons.util.ResourceExtractor;
import org.xml.sax.SAXException;

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
     */
    public static void main(String args[]) {
        // set up folder of example Prawn files
        ResourceExtractor prawnFileResourceExtractor
                = new ResourceExtractor(PrawnFile.class);

        PrawnFileHandler prawnFileHandler = new PrawnFileHandler();

        Path listOfPrawnFiles = prawnFileResourceExtractor.extractResourceAsPath("listOfPrawnFiles.txt");
        if (listOfPrawnFiles != null) {
            File exampleFolder = new File("ExamplePrawnXMLFiles");
            exampleFolder.mkdir();

            try {
                FileUtilities.recursiveDelete(exampleFolder.toPath());
                exampleFolder.mkdir();
                List<String> fileNames = Files.readAllLines(listOfPrawnFiles, ISO_8859_1);
                for (int i = 0; i < fileNames.size(); i++) {
                    // test for empty string
                    if (fileNames.get(i).trim().length() > 0) {
                        File prawnFileResource = prawnFileResourceExtractor.extractResourceAsFile(fileNames.get(i));
                        File prawnFile = new File(exampleFolder.getCanonicalPath() + File.separator + fileNames.get(i));
                        System.out.println("PrawnFile added: " + fileNames.get(i));
                        prawnFileResource.renameTo(prawnFile);
                    }
                }

                // point to directory, but no default choice
                prawnFileHandler.setCurrentPrawnFileLocation(exampleFolder.getCanonicalPath());
            } catch (IOException iOException) {
            }
        }

        // Set up default folder for reports
        File defaultCalamariReportsFolder = new File("CalamariReports_v" + VERSION);
        defaultCalamariReportsFolder.mkdir();
        prawnFileHandler.getReportsEngine().setFolderToWriteCalamariReports(defaultCalamariReportsFolder);

       
        if (args.length == 3) {// remove 4th argument from properties dialog command line arguments to get commandline
            System.out.println("Command line mode");
            try {
                prawnFileHandler.writeReportsFromPrawnFile(args[0], Boolean.valueOf(args[1]), Boolean.valueOf(args[2]), "T");
            } catch (IOException | JAXBException | SAXException exception) {
                System.out.println("Exception extracting data: " + exception.getStackTrace()[0].toString());
            }
        } else {
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(() -> {
                new org.cirdles.calamari.userInterface.CalamariUI(prawnFileHandler).setVisible(true);
            });
        }
    }
    
}
