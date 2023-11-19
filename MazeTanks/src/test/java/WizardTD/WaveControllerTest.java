package WizardTD;


import WizardTD.data.config.MonsterWaveInfo;
import WizardTD.data.config.WaveInfo;
import WizardTD.game.logic.WaveController;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WaveControllerTest {
    private static final int DELAY_BETWEEN_FRAMES_MS = 100;

    @Test
    public void testIfTheLastWaveHasBegunThenDisplayNoTitle() {
        // GIVEN
        int waveDuration = 5;
        int preWavePause = 0;
        int waveCount = 1;
        WaveController waveController = new WaveController(generateWaves(waveDuration, preWavePause, waveCount));

        String expectedTitle = ""; // title should be empty because it's the last wave
        int expectedWaveNumber = 1;

        // WHEN
        waveController.update(DELAY_BETWEEN_FRAMES_MS);

        // THEN
        assertEquals(expectedTitle, waveController.getWaveTitle());
        assertEquals(expectedWaveNumber, waveController.getCurrentWaveNumber());
    }

    @Test
    public void testIfNoWavesLeftThenDisplayNoTitle() {
        // GIVEN
        int waveDuration = 5;
        int preWavePause = 0;
        int waveCount = 0;
        WaveController waveController = new WaveController(generateWaves(waveDuration, preWavePause, waveCount));

        String expectedTitle = ""; // title should be empty because it's the last wave
        int expectedWaveNumber = WaveController.WAVE_NUMBER_AFTER_LAST_WAVE;

        // WHEN
        waveController.update(DELAY_BETWEEN_FRAMES_MS);

        // THEN
        assertEquals(expectedTitle, waveController.getWaveTitle());
        assertEquals(expectedWaveNumber, waveController.getCurrentWaveNumber());

    }

    @Test
    public void testWhenBeforeFirstWaveThenDisplayCorrectTitle() {
        // GIVEN
        int waveDuration = 5;
        int preWavePause = 1;
        int waveCount = 1;
        WaveController waveController = new WaveController(generateWaves(waveDuration, preWavePause, waveCount));

        String expectedTitle = "Wave 1 starts: " + preWavePause;
        int expectedWaveNumber = WaveController.WAVE_NUMBER_BEFORE_FIRST_WAVE;

        // WHEN
        waveController.update(DELAY_BETWEEN_FRAMES_MS);

        // THEN
        assertEquals(expectedTitle, waveController.getWaveTitle());
    }

    @Test
    public void testTransitionFromBeforeSingleWaveToAfterSingleWave() {
        // GIVEN
        int waveDuration = 5;
        int preWavePause = 1;
        int waveCount = 1;
        WaveController waveController = new WaveController(generateWaves(waveDuration, preWavePause, waveCount));

        String expectedSingleWaveTitle = "Wave 1 starts: " + preWavePause;
        String expectedAfterSingleWaveTitle = ""; // Empty because of after the last wave

        // TEST

        // 1. The controller have been created, therefore it's "before first wave"
        assertEquals(WaveController.WAVE_NUMBER_BEFORE_FIRST_WAVE, waveController.getCurrentWaveNumber());

        // 2. Update until first wave is started
        do {
            waveController.update(DELAY_BETWEEN_FRAMES_MS);
            pauseAfterUpdate();
        } while (waveController.getCurrentWaveNumber() != 1);

        // check if title is correct
        assertEquals(expectedSingleWaveTitle, waveController.getWaveTitle());

        // 3. Update until single wave is finished
        do {
            waveController.update(DELAY_BETWEEN_FRAMES_MS);
            pauseAfterUpdate();
        } while (waveController.getCurrentWaveNumber() != WaveController.WAVE_NUMBER_AFTER_LAST_WAVE);

        // check if title is correct
        assertEquals(expectedAfterSingleWaveTitle, waveController.getWaveTitle());
    }

    @Test
    public void testWaveTransitionWith3Waves() {
        // GIVEN
        int waveDuration = 5;
        int preWavePause = 1;
        int waveCount = 3;
        WaveController waveController = new WaveController(generateWaves(waveDuration, preWavePause, waveCount));

        // TEST

        // 1. The controller have been created, therefore it's "before first wave"
        assertEquals(WaveController.WAVE_NUMBER_BEFORE_FIRST_WAVE, waveController.getCurrentWaveNumber());

        // 2. Update until first wave is started
        do {
            waveController.update(DELAY_BETWEEN_FRAMES_MS);
            pauseAfterUpdate();
        } while (waveController.getCurrentWaveNumber() != 1);

        // 3. Update until second wave is started
        do {
            waveController.update(DELAY_BETWEEN_FRAMES_MS);
            pauseAfterUpdate();
        } while (waveController.getCurrentWaveNumber() != 2);

        // 4. Update until third wave is started
        do {
            waveController.update(DELAY_BETWEEN_FRAMES_MS);
            pauseAfterUpdate();
        } while (waveController.getCurrentWaveNumber() != 3);

        // 4. Update until third wave is finished
        do {
            waveController.update(DELAY_BETWEEN_FRAMES_MS);
            pauseAfterUpdate();
        } while (waveController.getCurrentWaveNumber() != WaveController.WAVE_NUMBER_AFTER_LAST_WAVE);
    }


    private static List<WaveInfo> generateWaves(int waveDuration, int preWavePause, int waveCount) {
        WaveInfo waveInfo = generateWave(waveDuration, preWavePause, 1);

        List<WaveInfo> res = new ArrayList<>();
        for (int i = 0; i < waveCount; i++) {
            res.add(waveInfo);
        }

        return res;
    }

    private static WaveInfo generateWave(int waveDuration, int preWavePause, int monsterQuantity) {
        return new WaveInfo(waveDuration, preWavePause, Collections.singletonList(
                new MonsterWaveInfo(
                        MonsterWaveInfo.Type.GREMLIN,
                        100, 1, 1, 10, monsterQuantity
                )));
    }

    // To left CPU rest a little, while running tests which depends on a real world time
    // (like wait 1 sec before spawn a wave)
    private static void pauseAfterUpdate() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
