package kychin.sudokuwebservice.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ExactCoverTests {
    @Test
    void makesExactCoverMatrix () {
        String expEcm = "" +
                "1               1               1               1               \n" +
                "1                1               1               1              \n" +
                "1                 1               1               1             \n" +
                "1                  1               1               1            \n" +
                " 1              1                   1           1               \n" +
                " 1               1                   1           1              \n" +
                " 1                1                   1           1             \n" +
                " 1                 1                   1           1            \n" +
                "  1             1                       1           1           \n" +
                "  1              1                       1           1          \n" +
                "  1               1                       1           1         \n" +
                "  1                1                       1           1        \n" +
                "   1            1                           1       1           \n" +
                "   1             1                           1       1          \n" +
                "   1              1                           1       1         \n" +
                "   1               1                           1       1        \n" +
                "    1               1           1               1               \n" +
                "    1                1           1               1              \n" +
                "    1                 1           1               1             \n" +
                "    1                  1           1               1            \n" +
                "     1              1               1           1               \n" +
                "     1               1               1           1              \n" +
                "     1                1               1           1             \n" +
                "     1                 1               1           1            \n" +
                "      1             1                   1           1           \n" +
                "      1              1                   1           1          \n" +
                "      1               1                   1           1         \n" +
                "      1                1                   1           1        \n" +
                "       1            1                       1       1           \n" +
                "       1             1                       1       1          \n" +
                "       1              1                       1       1         \n" +
                "       1               1                       1       1        \n" +
                "        1               1       1                       1       \n" +
                "        1                1       1                       1      \n" +
                "        1                 1       1                       1     \n" +
                "        1                  1       1                       1    \n" +
                "         1              1           1                   1       \n" +
                "         1               1           1                   1      \n" +
                "         1                1           1                   1     \n" +
                "         1                 1           1                   1    \n" +
                "          1             1               1                   1   \n" +
                "          1              1               1                   1  \n" +
                "          1               1               1                   1 \n" +
                "          1                1               1                   1\n" +
                "           1            1                   1               1   \n" +
                "           1             1                   1               1  \n" +
                "           1              1                   1               1 \n" +
                "           1               1                   1               1\n" +
                "            1               1   1                       1       \n" +
                "            1                1   1                       1      \n" +
                "            1                 1   1                       1     \n" +
                "            1                  1   1                       1    \n" +
                "             1              1       1                   1       \n" +
                "             1               1       1                   1      \n" +
                "             1                1       1                   1     \n" +
                "             1                 1       1                   1    \n" +
                "              1             1           1                   1   \n" +
                "              1              1           1                   1  \n" +
                "              1               1           1                   1 \n" +
                "              1                1           1                   1\n" +
                "               1            1               1               1   \n" +
                "               1             1               1               1  \n" +
                "               1              1               1               1 \n" +
                "               1               1               1               1\n";
        boolean[][] exm = ExactCover.MATRICES.get(4);
        assertEquals(expEcm, ExactCover.toString(exm));
    }
}
