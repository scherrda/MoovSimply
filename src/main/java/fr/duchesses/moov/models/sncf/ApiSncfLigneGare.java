package fr.duchesses.moov.models.sncf;

import com.google.common.primitives.Ints;
import lombok.Data;

@Data
public class ApiSncfLigneGare {

    private static final int INDEX_UIC = 0;
    private static final int INDEX_NAME = 1;
    private static final int INDEX_IS_TRAIN = 2;
    private static final int INDEX_IS_RER = 3;
    private static final int INDEX_IS_TRAM = 4;
    private static final int INDEX_IS_BUS = 5;
    private static final int INDEX_IS_A = 6;
    private static final int INDEX_IS_B = 7;
    private static final int INDEX_IS_C = 8;
    private static final int INDEX_IS_D = 9;
    private static final int INDEX_IS_E = 10;
    private static final int INDEX_IS_H = 11;
    private static final int INDEX_IS_J = 12;
    private static final int INDEX_IS_K = 13;
    private static final int INDEX_IS_L = 14;
    private static final int INDEX_IS_N = 15;
    private static final int INDEX_IS_P = 16;
    private static final int INDEX_IS_R = 17;
    private static final int INDEX_IS_U = 18;
    private static final int INDEX_IS_T4 = 19;
    private static final int INDEX_IS_TER = 20;

    private int uic;
    private String name;
    private String type;
    private String lineNumber;

    public ApiSncfLigneGare(String[] rawStop) {
        uic = Ints.tryParse(rawStop[INDEX_UIC]);
        name = rawStop[INDEX_NAME];

        if ("1".equals(rawStop[INDEX_IS_TRAIN])) type = "TRAIN";
        if ("1".equals(rawStop[INDEX_IS_RER])) type = "RER";
        if ("1".equals(rawStop[INDEX_IS_TRAM])) type = "TRAM";
        if ("1".equals(rawStop[INDEX_IS_BUS])) type = "BUS";

        if ("1".equals(rawStop[INDEX_IS_A])) lineNumber = "A";
        if ("1".equals(rawStop[INDEX_IS_B])) lineNumber = "B";
        if ("1".equals(rawStop[INDEX_IS_C])) lineNumber = "C";
        if ("1".equals(rawStop[INDEX_IS_D])) lineNumber = "D";
        if ("1".equals(rawStop[INDEX_IS_E])) lineNumber = "E";
        if ("1".equals(rawStop[INDEX_IS_H])) lineNumber = "H";
        if ("1".equals(rawStop[INDEX_IS_J])) lineNumber = "J";
        if ("1".equals(rawStop[INDEX_IS_K])) lineNumber = "K";
        if ("1".equals(rawStop[INDEX_IS_L])) lineNumber = "L";
        if ("1".equals(rawStop[INDEX_IS_N])) lineNumber = "N";
        if ("1".equals(rawStop[INDEX_IS_P])) lineNumber = "P";
        if ("1".equals(rawStop[INDEX_IS_R])) lineNumber = "R";
        if ("1".equals(rawStop[INDEX_IS_U])) lineNumber = "U";
        if ("1".equals(rawStop[INDEX_IS_T4])) lineNumber = "T4";
        if ("1".equals(rawStop[INDEX_IS_TER])) lineNumber = "TER";

    }

}
