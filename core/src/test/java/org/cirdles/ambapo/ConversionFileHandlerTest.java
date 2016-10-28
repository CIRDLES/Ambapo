/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cirdles.ambapo;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
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
            = "/org/cirdles/ambapo/convertfiles/utmToLatLongBulk.csv";

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
        String[] actualLine1 = {"500000", "1658326", "N", "25", "P", "WGS84"};
        String[] actualLine2 = {"500000", "1658326", "N", "25", "P", "WGS84"};
       
        ArrayList<String[]> actualExtractedData = (ArrayList<String[]>) conversionFileHandler.extractDataToConvert();
        List<String[]> expectedExtractedData = new ArrayList<>();
        expectedExtractedData.add(actualLine1);
        expectedExtractedData.add(actualLine2);
        
        assertArrayEquals(expectedExtractedData.toArray(), actualExtractedData.toArray());

    }
    
}
