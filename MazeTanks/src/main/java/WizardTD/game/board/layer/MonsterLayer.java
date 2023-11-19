package WizardTD.game.board.layer;

import WizardTD.App;
import WizardTD.data.Point;
import WizardTD.data.config.MonsterWaveInfo;
import WizardTD.game.board.Board;
import WizardTD.game.board.object.Monster;
import WizardTD.game.board.object.Monster.Status;
import WizardTD.util.RandomUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds, draws and updates monsters. Should be placed in the Board
 */
public class MonsterLayer extends BoardLayer {
    public List<Monster> monsters;
    private List<List<Point>> screenPointPaths;

    public Callbacks callbacks;

    public MonsterLayer(Board board) {
        super(board);
    }

    @Override
    public void create() {
        monsters = new ArrayList<>();

        screenPointPaths = board.gameMap.pathInfo.listOfPaths.stream().map(path ->
                path.stream()
                        .map(board::boardPointToScreenPoint)
                        .collect(Collectors.toList())
        ).collect(Collectors.toList());
    }

    public void spawnMonster(MonsterWaveInfo monsterToSpawn) {
        List<Point> screenPointPath = RandomUtils.choice(screenPointPaths);

        Monster m = new Monster(board.applet,
                App.CELL_SIZE,
                board.resources.monsterImages.get(monsterToSpawn.type.name().toLowerCase()),
                board.resources.deathAnimation,
                screenPointPath,
                monsterToSpawn.speed * App.OBJECT_SPEED_SCALE,
                monsterToSpawn.hp,
                monsterToSpawn.manaGainedOnKill,
                monsterToSpawn.armour
        );
        monsters.add(m);
    }

    // removes arrived and dead monsters, updates the actual ones
    public void update(long deltaMs) {
        for (Iterator<Monster> iterator = monsters.iterator(); iterator.hasNext(); ) {
            Monster monster = iterator.next();
            if (monster.status == Status.ARRIVED || monster.status == Status.DEAD) {
                if (callbacks != null) {
                    callbacks.onMonsterRemoved(monster);
                }
                iterator.remove();
            }
            monster.update();
        }
    }


    public void draw() {
        for (Monster monster : monsters) {
            monster.draw();
        }
    }

    public interface Callbacks {
        void onMonsterRemoved(Monster monster);
    }
}
