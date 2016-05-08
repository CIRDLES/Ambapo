Ambapo
======
Ambapo allows for the conversion of latitude and longitude, using any geodetic datum, to Universal Transverse Mercator and vice versa. One conversion can be done or a bulk conversion of a CSV file. If converting from UTM to lat/long, the data should be put in the CSV file as follows:
  easting, northing, hemisphere, zone number, zone letter

None of the numbers should contain commas, as that is how the values are separated. The hemisphere should be denoted as N or S. You do not need both a hemisphere AND a zone letter, but you do need one or the other. If either of this information is missing, put an asterisk (*) in its place. 
