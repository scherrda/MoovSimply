package fr.duchesses.moov.apis;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

@Component
public class FileReader {

    private static final Logger logger = Logger.getLogger(FileReader.class);

    public List<String[]> getLines(String fileName, Charset charset, char separator, int excludedLines) {
        List<String[]> rawLines = Lists.newArrayList();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(FileReader.class.getClassLoader().getResourceAsStream(fileName), charset);
            CSVReader reader = new CSVReader(inputStreamReader, separator);
            rawLines = reader.readAll();
            // Excludes header lines
            rawLines = rawLines.subList(excludedLines, rawLines.size());
        } catch (IOException e) {
            logger.error("I/O error on file " + fileName, e);
        }
        return rawLines;
    }
}
