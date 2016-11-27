/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cirdles.ambapo;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author evc1996
 */
public class ConversionFileHandler {
        private String currentFileLocationToConvert;
        private File outputFileLocation;
        private final String[] HEADER_LAT_LONG = {";LATITUDE, LONGITUDE, DATUM\n"};
        private final String[] HEADER_UTM_FROM_LATLONG = {";EASTING, NORTHING, HEMISPHERE, ZONE NUMBER, ZONE LETTER, DATUM CONVERTED FROM"};
    
    public ConversionFileHandler(String currentFileLocationToConvert){
        this.currentFileLocationToConvert = currentFileLocationToConvert;
    }
    
    public ConversionFileHandler(){
    }

    public boolean currentFileLocationToConvertIsFile() {
        return new File(currentFileLocationToConvert).isFile();
    }
    
    /**
     * @return the currentFileLocationToConvert
     */
    public String getCurrentFileLocationToConvert() {
        return currentFileLocationToConvert;
    }
    
    /**
     * @return the outputFileLocation
     */
    public File getOutputFile() {
        return outputFileLocation;
    }

    /**
     * @param aCurrentFileLocationToConvert the currentPrawnFileLocation to set
     */
    public void setCurrentFileLocation(String aCurrentFileLocationToConvert) {
        currentFileLocationToConvert = aCurrentFileLocationToConvert;
    }
    
    private List<String[]> extractDataToConvert() throws FileNotFoundException, IOException{
        List<String[]> listOfDataToConvert = null;
        
        try (CSVReader csvReader = new CSVReader(new FileReader(new File(currentFileLocationToConvert)))) {
            listOfDataToConvert = csvReader.readAll();
        } catch(Exception e){
            System.out.println("\nUnable to extract data from file.");
        }
        
        return listOfDataToConvert;
            
        
    }
    public void writeConversionsUTMToLatLong(String outputFileName) throws Exception{
        
        writeConversionsUTMToLatLong(new File(outputFileName));
        
    }
    public void writeConversionsUTMToLatLong(File outputFile) throws IOException, Exception {
        List<String[]> dataToConvert = extractDataToConvert();
        
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFile))){
            csvWriter.writeNext(HEADER_LAT_LONG);
            Datum datum;
            UTM utm;
            Coordinate latAndLong;
            String[] lineToWrite;
            
            for (String[] utmInfo : dataToConvert) {
                if(utmInfo[0].charAt(0) != ';'){
                    utm = new UTM(
                            new BigDecimal(Double.parseDouble(utmInfo[0].trim().replace("\"", ""))),
                            new BigDecimal(Double.parseDouble(utmInfo[1].trim().replace("\"", ""))),
                            utmInfo[2].trim().replace("\"", "").charAt(0),
                            Integer.parseInt(utmInfo[3].replace("\"", "").trim()),
                            utmInfo[4].trim().replace("\"", "").charAt(0));

                    datum = Datum.valueOf(utmInfo[5].trim().replace("\"", ""));

                    latAndLong = UTMToLatLong.convert(utm, datum.getDatum());
                    lineToWrite = new String[]{latAndLong.getLatitude().toString(),
                        latAndLong.getLongitude().toString(), datum.getDatum()};

                    csvWriter.writeNext(lineToWrite);
                }
            }
            
            csvWriter.close();
            outputFileLocation = outputFile;
        }
        
        
    }
    
    public void writeConversionsLatLongToUTM(String outputFileName) throws Exception{
        writeConversionsLatLongToUTM(new File(outputFileName));
    }
    public void writeConversionsLatLongToUTM(File outputFile) throws IOException, Exception
    {
        List<String[]> dataToConvert = extractDataToConvert();
            BigDecimal latitude;
            BigDecimal longitude;
            String datum;
            UTM utm;
            String[] lineToWrite;
            try (CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFile))){
                csvWriter.writeNext(HEADER_UTM_FROM_LATLONG);
                for(String[] latLongInfo : dataToConvert) {
                    if(latLongInfo[0].charAt(0) != ';'){
                        latitude = new BigDecimal(latLongInfo[0].trim().replace("\"", ""));
                        longitude = new BigDecimal(latLongInfo[1].trim().replace("\"", ""));
                        datum = latLongInfo[2].trim().replace("\"", "");

                        utm = LatLongToUTM.convert(latitude, longitude, datum);

                        lineToWrite = new String[]{
                            utm.getEasting().toString(),
                            utm.getNorthing().toString(),
                            Character.toString(utm.getHemisphere()),
                            Integer.toString(utm.getZoneNumber()),
                            Character.toString(utm.getZoneLetter()),
                            datum
                        };
                        csvWriter.writeNext(lineToWrite);
                    }
                }
                
                csvWriter.close();
                outputFileLocation = outputFile;
            }
    }
    
    
}
