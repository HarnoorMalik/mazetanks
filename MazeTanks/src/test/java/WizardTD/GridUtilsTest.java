package WizardTD;

import WizardTD.data.Point;
import WizardTD.util.GridUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GridUtilsTest {

    @Test
    public void testEntryPointsOnTop() {
        testEntryPoints(
                new String[]{
                        "#.#.",
                        "####",
                        "####",
                },
                Arrays.asList(
                        new Point(1, 0),
                        new Point(3, 0)
                )
        );
    }

    @Test
    public void testEntryPointsOnBottom() {
        testEntryPoints(
                new String[]{
                        "####",
                        "####",
                        "#.#.",
                },
                Arrays.asList(
                        new Point(1, 2),
                        new Point(3, 2)
                )
        );
    }

    @Test
    public void testEntryPointsOnLeft() {
        testEntryPoints(
                new String[]{
                        ".###",
                        ".###",
                        ".###",
                },
                Arrays.asList(
                        new Point(0, 0),
                        new Point(0, 1),
                        new Point(0, 2)
                )
        );
    }

    @Test
    public void testEntryPointsOnRight() {
        testEntryPoints(
                new String[]{
                        "###.",
                        "###.",
                        "####",
                },
                Arrays.asList(
                        new Point(3, 0),
                        new Point(3, 1)
                )
        );
    }

    @Test
    public void testEntryPointsSquare3() {
        testEntryPoints(
                new String[]{
                        "...",
                        "...",
                        "...",
                },
                Arrays.asList(
                        new Point(0, 0),
                        new Point(0, 1),
                        new Point(0, 2),
                        new Point(1, 0),
                        new Point(1, 2),
                        new Point(2, 0),
                        new Point(2, 1),
                        new Point(2, 2)
                )
        );
    }

    @Test
    public void testEntryPointsSquare4() {
        testEntryPoints(
                new String[]{
                        "....",
                        "....",
                        "....",
                        "....",
                },
                Arrays.asList(
                        // top row
                        new Point(0, 0),
                        new Point(1, 0),
                        new Point(2, 0),
                        new Point(3, 0),

                        // bottom row
                        new Point(0, 0),
                        new Point(1, 0),
                        new Point(2, 0),
                        new Point(3, 0),

                        // left column
                        new Point(0, 1),
                        new Point(0, 2),

                        // right column
                        new Point(3, 1),
                        new Point(3, 2)
                )
        );
    }

    private void testEntryPoints(String[] grid, List<Point> expected) {
        // WHEN
        List<Point> actualPoints = GridUtils.findEntryPoints(grid, '#');

        // THEN
        assertEquals(expected.size(), actualPoints.size());

        for (Point expectedPoint : expected) {
            assertTrue(actualPoints.contains(expectedPoint));
        }
    }
}
