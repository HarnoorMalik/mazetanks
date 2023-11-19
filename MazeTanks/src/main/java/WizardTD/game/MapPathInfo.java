package WizardTD.game;

import WizardTD.data.Point;
import WizardTD.util.GridUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Extracts and stores path-related information from map
 */
public class MapPathInfo {
    // Used for choosing the correct picture for a road cell
    public final String[][] cellConnectionTable;

    // Used to select a random enemy spawn point
    public final List<Point> entryPoints;

    // Used to move an enemy from the corresponding entryPoint
    public final List<List<Point>> listOfPaths;

    public MapPathInfo(String[] map) {

        // Replace ' ' and 'S' in a map file to '#'
        // This is needed to simplify the map for the pathfinder, because it
        // supports only 3 types of objects: wall, free space, target
        String[] grid = Arrays.stream(map)
                .map(s -> s.replace(' ', '#').replace('S', '#'))
                .toArray(String[]::new);


        entryPoints = GridUtils.findEntryPoints(grid, '#');
        listOfPaths = new ArrayList<>(entryPoints.size());
        for (Point entryPoint : entryPoints) {
            List<Point> path = GridUtils.bfs(grid, entryPoint, '#', 'W');
            path.add(0, createOutsideOfGridPoint(grid, entryPoint));
            listOfPaths.add(path);
        }

        cellConnectionTable = GridUtils.makeCellConnectionTable(grid, '#');

        // print table
//        for (String[] strings : cellConnectionTable) {
//            for (String string : strings) {
//                System.out.printf("%5s", string);
//            }
//            System.out.println();
//        }
    }

    private Point createOutsideOfGridPoint(String[] grid, Point entryPoint) {
        int mapWidth = grid[0].length();
        int mapHeight = grid.length;

        int newX = entryPoint.x;
        int newY = entryPoint.y;

        if (entryPoint.x == 0) {
            newX = -1;
        } else if (entryPoint.y == 0) {
            newY = -1;
        } else if (entryPoint.x == mapWidth - 1) {
            newX = entryPoint.x + 1;
        } else if (entryPoint.y == mapHeight - 1) {
            newY = entryPoint.y + 1;
        }

        return new Point(newX, newY);
    }
}
