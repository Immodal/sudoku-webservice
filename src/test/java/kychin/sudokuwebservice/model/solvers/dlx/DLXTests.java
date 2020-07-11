package kychin.sudokuwebservice.model.solvers.dlx;

import static org.junit.jupiter.api.Assertions.*;

import kychin.sudokuwebservice.model.Solver;
import kychin.sudokuwebservice.model.State;
import org.junit.jupiter.api.Test;
import java.util.*;

public class DLXTests {
    @Test
    void initializesAsExpected() {
        String state = "1400004121000012";
        String dlmExp = "Column 2: 9,10,\n" +
                "Column 3: 14,\n" +
                "Column 4: 18,\n" +
                "Column 5: 21,22,\n" +
                "Column 10: 42,\n" +
                "Column 11: 46,47,\n" +
                "Column 12: 50,51,\n" +
                "Column 13: 54,\n" +
                "Column 17: 9,\n" +
                "Column 18: 10,14,\n" +
                "Column 21: 21,\n" +
                "Column 22: 18,22,\n" +
                "Column 26: 42,46,\n" +
                "Column 27: 47,\n" +
                "Column 30: 50,54,\n" +
                "Column 31: 51,\n" +
                "Column 34: 18,50,\n" +
                "Column 35: 51,\n" +
                "Column 37: 21,\n" +
                "Column 38: 22,54,\n" +
                "Column 41: 9,\n" +
                "Column 42: 10,42,\n" +
                "Column 46: 14,46,\n" +
                "Column 47: 47,\n" +
                "Column 49: 21,\n" +
                "Column 50: 18,22,\n" +
                "Column 53: 9,\n" +
                "Column 54: 10,14,\n" +
                "Column 58: 50,54,\n" +
                "Column 59: 51,\n" +
                "Column 62: 42,46,\n" +
                "Column 63: 47,\n";

        assertTrue(State.isValid(state));
        DLX dlx = new DLX(state);
        assertEquals(dlx.getDLMString(), dlmExp);
    }

    @Test
    void findsSolutions() {
        // 9x9
        String state = "295743861431865900876192543387459216612387495549216738763524189928671354154938600";
        assertTrue(State.isValid(state));
        Solver dlx = new DLX(state);
        dlx.search(1000, 10000);
        checkSolutions(dlx.getSolutions(), 2);

        // 4x4
        state = "0000000000000000";
        assertTrue(State.isValid(state));
        dlx = new DLX(state);
        dlx.search(1000, 10000);
        checkSolutions(dlx.getSolutions(), 288);

        // 16x16
        state = "f01a004d3080000e0509001ac0207046006bc9ge0401083004308b20075g9f00a8000g50001fe0000020040000b0a7gccb70e0816000f9000040a0b020e0100d00000dc0f00b00019cb2g003100a40e7700100e2000d800000000760g2003c9fg9000cd4b106000832056100e0a0g0006d00be0050900070000f030g78d060a2";
        assertFalse(State.isValid(state));
        assertTrue(State.isValid(state.toUpperCase()));
        dlx = new DLX(state.toUpperCase());
        dlx.search(1000, 10000);
        checkSolutions(dlx.getSolutions(), 1);

        // 25x25
        state = "070M3L20KGI05OF9100P08BA00J000F0BM018000DL00G0000E00BE000I01C00P030HN0D000L5P20HD0N0890AL00BJ0C00000I1L407H000000K00O0M00CNGF05070K60G00P1CA0000B0O00J06I0K000275MBH0J0OA30000400JP0M0500000I370G0200F6AC00004000J6K70E00MF0300822M3A4CID00000F8N00P1G0070M3N0G1P68005K00000J40H0FC100L00300OPA00M000B6E4DN08040AGC0B000E000709M603050C75EJD0003009OH00100M00KB20605N00000J800K3DLP0G170B00M9L0048H000006K5FGCJOHK00L07G00M0I46E0009000019G800O0H05ED0000020A0IL00700008KE0A09L51MG000N06H0400C0B0000FGOA00D0H8030E06E5200B0700O000000GD0PJIN00000N0LP00C80I60907M0EOGL00090G80F0700P40B0006K00J0000A0056000DB0IE0H000C00IGF0E00HD4L3051P0ONA90B0";
        assertTrue(State.isValid(state));
        dlx = new DLX(state);
        dlx.search(100, 10000);
        checkSolutions(dlx.getSolutions(), 1);
    }

    // Helper for findsSolutions()
    private void checkSolutions(List<String> solutions, int nSols) {
        assertEquals(nSols, solutions.size());
        for (String solution : solutions) {
            assertTrue(State.isValid(solution));
        }
    }
}
