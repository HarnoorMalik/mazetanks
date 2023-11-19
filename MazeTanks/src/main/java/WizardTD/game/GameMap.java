package WizardTD.game;

import WizardTD.App;
import WizardTD.data.Point;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    public static final char OBJECT_GRASS = ' ';
    public static final char OBJECT_SHRUB = 'S';
    public static final char OBJECT_ROAD = 'X';
    public static final char OBJECT_WIZARD_HOUSE = 'W';
    public static final char OBJECT_TOWER = 'T';

    public final List<Point> grassPoints = new ArrayList<>();
    public final List<Point> shrubPoints = new ArrayList<>();
    public final List<Point> roadPoints = new ArrayList<>();
    public Point wizardHousePoint;
    public final MapPathInfo pathInfo;

    private final String[] map;

    public GameMap(String[] map) {
        this.map = map.clone();
        setupMap(map);
        pathInfo = new MapPathInfo(map);
    }

    public char get(int x, int y) {
        return map[y].charAt(x);
    }

    public char get(Point position) {
        return get(position.x, position.y);
    }

    public void put(int x, int y, char value) {
        char[] chars = map[y].toCharArray();
        chars[x] = value;
        map[y] = String.valueOf(chars);
    }

    public void put(Point position, char value) {
        put(position.x, position.y, value);
    }

    private void setupMap(String[] map) {
        for (int i = 0; i < App.BOARD_CELLS; i++) {
            for (int j = 0; j < App.BOARD_CELLS; j++) {
                Point point = new Point(j, i);
                char objectType = map[i].charAt(j);

                switch (objectType) {
                    case OBJECT_GRASS:
                        grassPoints.add(point);
                        break;
                    case OBJECT_SHRUB:
                        shrubPoints.add(point);
                        break;
                    case OBJECT_ROAD:
                        roadPoints.add(point);
                        break;
                    case OBJECT_WIZARD_HOUSE:
                        wizardHousePoint = point;
                        break;
                    default:
                        throw new RuntimeException("Unknown objectType: " + objectType);
                }
            }
        }
    }
}


