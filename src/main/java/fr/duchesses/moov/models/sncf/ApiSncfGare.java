package fr.duchesses.moov.models.sncf;

import com.google.common.primitives.Ints;
import lombok.Data;

@Data
public class ApiSncfGare {

    private static final int INDEX_UIC = 0;
    private static final int INDEX_STOP_LABEL = 1;
    private static final int INDEX_LABEL = 2;
    private static final int INDEX_STIF_LABEL = 3;
    private static final int INDEX_SMS_LABEL = 4;
    private static final int INDEX_STOP_NAME = 5;
    private static final int INDEX_ADDRESS = 6;
    private static final int INDEX_INSEE_CODE = 7;
    private static final int INDEX_CITY = 8;
    private static final int INDEX_X = 9;
    private static final int INDEX_Y = 10;

    private int uic;
    private String name;
    private String label;
    private String address;

    public ApiSncfGare(String[] rawStop) {
        uic = Ints.tryParse(rawStop[INDEX_UIC]);
        name = rawStop[INDEX_STOP_NAME];
        label = rawStop[INDEX_LABEL];
        address = rawStop[INDEX_ADDRESS];
    }
}
