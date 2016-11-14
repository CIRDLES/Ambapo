/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cirdles.ambapo;

import com.opencsv.CSVReader;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import org.cirdles.commons.util.ResourceExtractor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.Timeout;

/**
 *
 * @author evc1996
 */
public class ConversionFileHandlerTest {
    
    private static final String CONVERSION_FILE_RESOURCE
            = "/org/cirdles/ambapo/utmToLatLongBulk.csv";

    private static final ResourceExtractor RESOURCE_EXTRACTOR
            = new ResourceExtractor(ConversionFileHandler.class);

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public Timeout timeout = Timeout.seconds(120);

    private ConversionFileHandler conversionFileHandler;

    @Before
    public void setUp() {
        conversionFileHandler = new ConversionFileHandler(CONVERSION_FILE_RESOURCE);
    }

    @Test
    public void extractDataToConvert() throws Exception {
        File fileToExtract = RESOURCE_EXTRACTOR.extractResourceAsFile(CONVERSION_FILE_RESOURCE);
        
        String[] actualLine1 = {"500000", "1658326", "N", "25", "P", "WGS84"};
        String[] actualLine2 = {"500000", "1658326", "N", "25", "P", "WGS84"};
        List<String[]> expectedExtractedData = null;
        
        expectedExtractedData.add(actualLine1);
        expectedExtractedData.add(actualLine2);
        
        conversionFileHandler.setCurrentFileLocation(fileToExtract.getAbsolutePath());
        
        List<String[]> actualExtractedData = null;
         
        actualExtractedData = conversionFileHandler.extractDataToConvert();
        
        assertArrayEquals(expectedExtractedData.toArray(), actualExtractedData.toArray());
        
    }
    
}
