package WizardTD.util;

import WizardTD.data.Point;

import java.util.*;

/**
 * Contains different utility methods to work with grid of cells (like a game board)
 */
public class GridUtils {
    /**
     * Find path in 2D grid by using BFS (Breadth-first search) algorithm
     * <br>
     * Breadth-first search (BFS) is a method for exploring a tree or graph.
     * In a BFS, you first explore all the nodes one step away, then all the nodes two steps away, etc.
     * Breadth-first search is like throwing a stone in the center of a pond.
     * The nodes you explore "ripple out" from the starting point.
     *
     * @param grid     - array of strings representing char grid
     * @param startPos - position to start search from
     * @param wall     - char in grid, which represents a wall
     * @param goal     - char in grid, which you need to find
     * @return path - list of points, which leads to the goal
     */
    public static List<Point> bfs(String[] grid, Point startPos, char wall, char goal) {
        int width = grid.length;
        int height = grid[0].length();

        Queue<List<Point>> queue = new ArrayDeque<>();
        queue.add(Collections.singletonList(startPos));

        Set<Point> seen = new HashSet<>();
        seen.add(startPos);

        while (!queue.isEmpty()) {
            List<Point> path = queue.poll();
            Point lastPoint = path.get(path.size() - 1);
            int x = lastPoint.x;
            int y = lastPoint.y;

            if (grid[y].charAt(x) == goal) {
                return path;
            }

            Point[] pointsToCheck = new Point[]{
                    new Point(x + 1, y),
                    new Point(x - 1, y),
                    new Point(x, y + 1),
                    new Point(x, y - 1)
            };

            for (Point point : pointsToCheck) {
                int x2 = point.x;
                int y2 = point.y;

                if (0 <= x2 && x2 < width && 0 <= y2 && y2 < height && grid[y2].charAt(x2) != wall && !seen.contains(point)) {
                    ArrayList<Point> newPath = new ArrayList<>(path);
                    newPath.add(point);
                    queue.add(newPath);
                    seen.add(point);
                }
            }
        }

        return null;
    }

    /**
     * For each cell in a grid, provides information about connection between cells
     * <p>
     * t - cell has top connection
     * b - cell has bottom connection
     * l - cell has left connection
     * r - cell has right connection
     * 0 - cell has no connection (or is a wall)
     * <p>
     * For example, for the following grid:
     * ["..#"
     * "#.#"]
     * the result would be
     * [["r", "lb", "0"],
     * ["0", "t", "0"]
     * where "lb" - left and bottom connection, "t" - top connections, "r " - right connection
     *
     * @param grid - array of strings representing char grid
     * @param wall - char in grid, which represents a wall
     * @return grid, where each cell is a string that specifies info about connections
     */
    public static String[][] makeCellConnectionTable(String[] grid, char wall) {
        String[][] table = new String[grid.length][];

        for (int i = 0; i < grid.length; i++) {
            table[i] = new String[grid[i].length()];

            for (int j = 0; j < grid[i].length(); j++) {
                char cell = grid[i].charAt(j);
                String res = "";
                if (cell == wall) {
                    res = "0";
                } else {
                    // If the (t)op cell is not a wall, add (t) to specify it
                    if (isFree(grid, wall, i - 1, j)) {
                        res += "t";
                    }
                    // If the (b)ottom cell is not a wall, add (b) to specify it
                    if (isFree(grid, wall, i + 1, j)) {
                        res += "b";
                    }
                    // If the (l)eft cell is not a wall, add (r) to specify it
                    if (isFree(grid, wall, i, j - 1)) {
                        res += "l";
                    }
                    // If the (r)ight cell is not a wall, add (r) to specify it
                    if (isFree(grid, wall, i, j + 1)) {
                        res += "r";
                    }
                }
                table[i][j] = res;
            }
        }
        return table;
    }

    /**
     * Finds points from which it is possible to enter the grid.
     * For example, for the grid:
     * ["##.#
     * "##.#
     * "##.#]
     * you can enter the grid from top or bottom, therefore result would be:
     * [(2, 0), (2, 2)]
     *
     * @param grid - array of strings representing char grid
     * @param wall - char in grid, which represents a wall
     * @return list of points from which it is possible to enter the grid
     */
    public static List<Point> findEntryPoints(String[] grid, char wall) {
        List<Point> res = new ArrayList<>();

        // first row
        for (int i = 0; i < grid[0].length(); i++) {
            if (grid[0].charAt(i) != wall) {
                res.add(new Point(i, 0));
            }
        }

        // last row
        int lastRowY = grid.length - 1;
        for (int i = 0; i < grid[lastRowY].length(); i++) {
            if (grid[lastRowY].charAt(i) != wall) {
                res.add(new Point(i, lastRowY));
            }
        }

        // first column (without top-left and bottom-left cell)
        for (int i = 1; i < grid.length - 1; i++) {
            if (grid[i].charAt(0) != wall) {
                res.add(new Point(0, i));
            }
        }

        // last column (without top-left and bottom-left cell)
        for (int i = 1; i < grid.length - 1; i++) {
            if (grid[i].charAt(grid[0].length() - 1) != wall) {
                res.add(new Point(grid[0].length() - 1, i));
            }
        }

        return res;
    }

    /**
     * Checks if the specified position in a grid is not a wall
     *
     * @param grid - array of strings representing char grid
     * @param wall - char in grid, which represents a wall
     * @param i    - row index
     * @param j    - column index
     * @return true if the specified position in a grid is not a wall
     */
    private static boolean isFree(String[] grid, char wall, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length()) {
            return false; // out of range
        }
        return grid[i].charAt(j) != wall;
    }
}
