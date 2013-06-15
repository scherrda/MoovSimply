package fr.duchesses.moov.models.ratp;

import com.google.common.primitives.Ints;
import lombok.Data;

@Data
public class RatpLineModel {

    private static final int LINE_INDEX_STOP_ID = 0;
    private static final int LINE_INDEX_NUMBER_NAME = 1;
    private static final int LINE_INDEX_TYPE = 2;

    private int stopId;
    private String number;
    private String name;
    private String type;

    public RatpLineModel(String[] rawLine) {
        stopId = Ints.tryParse(rawLine[LINE_INDEX_STOP_ID]);
        String[] numberAndName = rawLine[LINE_INDEX_NUMBER_NAME].split(" ", 2);
        number = numberAndName[0];
        if (numberAndName.length > 1) {
            name = numberAndName[1];
        }
        type = rawLine[LINE_INDEX_TYPE];
    }
}
