package WizardTD.game.logic;

import WizardTD.data.config.MonsterWaveInfo;
import WizardTD.data.config.WaveInfo;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

public class WaveController {
    public static final int WAVE_NUMBER_BEFORE_FIRST_WAVE = 0;
    public static final int WAVE_NUMBER_AFTER_LAST_WAVE = -1;

    private final Queue<WaveInfo> wavesQueue;
    private WaveInfo currentWave;
    private int currentWaveNumber = WAVE_NUMBER_BEFORE_FIRST_WAVE;
    private int pauseUntilWaveStartsMs;
    private float pauseUntilNextMonsterSpawnsMs;

    private float currentWaveSpawnRate = 0f;

    private final Random random = new Random();

    private MonsterWaveInfo monsterToSpawn;

    public WaveController(List<WaveInfo> wavesQueue) {
        // Copy the source list into the deque (queue),
        // because for a gameplay I have a queue of monster waves,
        // and also I change MonsterWaveInfo.quantity during the gameplay,
        // so I need to leave the original config with waves not changes
        this.wavesQueue = wavesQueue.stream()
                .map(WaveInfo::copy)
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    // should be called each frame
    public void update(long deltaMs) {
        // reset on each frame
        monsterToSpawn = null;

        // if current wave is null, I need to continue with the next wave
        if (currentWave == null) {
            setupNextWave();
            return;
        }

        // if I am waiting for wait start, handle it and stop processing
        pauseUntilWaveStartsMs -= (float) deltaMs;
        if (pauseUntilWaveStartsMs > 0) {
            return;
        }

        // if I am waiting for next monster to spawn, handle it and stop processing
        pauseUntilNextMonsterSpawnsMs -= (float) deltaMs;
        if (pauseUntilNextMonsterSpawnsMs > 0) {
            return;
        }

        // I am ready to spawn a monster
        monsterToSpawn = pollRandomMonster();


        // if no monsters left, it means current wave is over
        if (currentWave.getTotalMonstersCount() == 0) {
            setupNextWave();
            return;
        }

        pauseUntilNextMonsterSpawnsMs = currentWaveSpawnRate;
    }

    private void setupNextWave() {
        // If now waves left, we have nothing to do
        if (wavesQueue.isEmpty()) {
            currentWaveNumber = WAVE_NUMBER_AFTER_LAST_WAVE;
            currentWave = null;
            return;
        }

        // In other case, I am setting up pause counters before wave start and before monster spawn
        currentWave = wavesQueue.poll();
        currentWaveNumber++;
        pauseUntilWaveStartsMs = currentWave.getPreWavePauseMs();

        // spawn rate means amount of ms after I spawn next monster
        currentWaveSpawnRate = (float) currentWave.getDurationMs() / currentWave.getTotalMonstersCount();

        pauseUntilNextMonsterSpawnsMs = currentWaveSpawnRate;
    }

    /**
     * Provides title in format "Wave <x> starts: <s>"
     */
    public String getWaveTitle() {
        int nextWaveNumber = getNextWaveNumber();
        if (nextWaveNumber == WAVE_NUMBER_AFTER_LAST_WAVE) {
            return ""; // if current wave is the last one, title should be empty
        }

        int waveStartsInMs = pauseUntilWaveStartsMs;

        if (nextWaveNumber > currentWaveNumber && currentWave != null) {
            int currentWaveTimeRemaining = currentWave.getDurationMs() + pauseUntilWaveStartsMs;
            currentWaveTimeRemaining = Math.max(currentWaveTimeRemaining, 0);

            int nextWavePauseMs = 0;
            if (!wavesQueue.isEmpty()) {
                nextWavePauseMs = wavesQueue.peek().getPreWavePauseMs();
            }
            waveStartsInMs = currentWaveTimeRemaining + nextWavePauseMs;
        }

        int sec = (int) Math.floor(waveStartsInMs / 1000.0);
        return "Wave " + nextWaveNumber + " starts: " + sec;
    }

    public int getNextWaveNumber() {
        if (currentWaveNumber == WAVE_NUMBER_BEFORE_FIRST_WAVE) {
            return 1;
        }

        if (currentWaveNumber == WAVE_NUMBER_AFTER_LAST_WAVE) {
            return WAVE_NUMBER_AFTER_LAST_WAVE;
        }

        // if waiting for current wave to start
        if (pauseUntilWaveStartsMs > 0) {
            return currentWaveNumber;
        }

        if (wavesQueue.isEmpty()) {
            return WAVE_NUMBER_AFTER_LAST_WAVE;
        }

        return currentWaveNumber + 1;
    }

    /**
     * This method should be called each frame.
     * When it is time to spawn a monster, it returns its config
     * In the other case return null
     */
    public MonsterWaveInfo getMonsterToSpawn() {
        return monsterToSpawn;
    }

    /**
     * Return the number of the current wave (starts with 1)
     * or a special value in specific cases.
     * Example:
     * WAVE_NUMBER_BEFORE_FIRST - when before the first wave
     * 1 - first wave
     * 2 - second wave
     * ...
     * WAVE_NUMBER_AFTER_LAST - when after last wave
     */
    public int getCurrentWaveNumber() {
        return currentWaveNumber;
    }

    private MonsterWaveInfo pollRandomMonster() {
        while (true) {
            int monsterIndex = random.nextInt(currentWave.monsters.size());
            MonsterWaveInfo monsterWaveInfo = currentWave.monsters.get(monsterIndex);
            if (monsterWaveInfo.quantity == 0) continue;
            monsterWaveInfo.quantity--;
            return monsterWaveInfo;
        }
    }
}
