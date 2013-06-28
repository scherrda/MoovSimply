package fr.duchesses.moov.apis;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;


public class FileReader {

    private static final Logger logger = Logger.getLogger(FileReader.class);

    //private constructor
    private FileReader() {
    }

    public static List<String[]> getLines(String fileName, Charset charset, char separator, int excludedLines) {
        List<String[]> rawLines = Lists.newArrayList();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(FileReader.class.getClassLoader().getResourceAsStream(fileName), charset);
            CSVReader reader = new CSVReader(inputStreamReader, separator);
            rawLines = reader.readAll();
            // Excludes header lines
            rawLines = rawLines.subList(excludedLines, rawLines.size());
            reader.close();
        } catch (IOException e) {
            logger.error("I/O error on file " + fileName, e);
        }
        return rawLines;
    }
}
