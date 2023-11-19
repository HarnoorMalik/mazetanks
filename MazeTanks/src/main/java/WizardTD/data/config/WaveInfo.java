package WizardTD.data.config;

import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Stores data from config.json about monster waves
public class WaveInfo {
    public final int durationSec;
    public final int preWavePauseSec;
    public final List<MonsterWaveInfo> monsters = new ArrayList<>();

    // Initialize with date from JSON
    public WaveInfo(JSONObject o) {
        durationSec = o.getInt("duration");
        preWavePauseSec = o.getInt("pre_wave_pause");
        JSONArray jsonArray = o.getJSONArray("monsters");
        for (int i = 0; i < jsonArray.size(); i++) {
            monsters.add(new MonsterWaveInfo(jsonArray.getJSONObject(i)));
        }
    }

    public WaveInfo(int durationSec, int preWavePauseSec, List<MonsterWaveInfo> monsters) {
        this.durationSec = durationSec;
        this.preWavePauseSec = preWavePauseSec;

        // copy, because I will modify MonsterWaveInfo.quantity field
        this.monsters.addAll(monsters.stream()
                .map(MonsterWaveInfo::copy)
                .collect(Collectors.toList()));
    }

    public WaveInfo copy() {
        return new WaveInfo(durationSec, preWavePauseSec, monsters);
    }

    public int getPreWavePauseMs() {
        return preWavePauseSec * 1000;
    }

    public int getDurationMs() {
        return durationSec * 1000;
    }

    public int getTotalMonstersCount() {
        int sum = 0;
        for (MonsterWaveInfo monster : monsters) {
            sum += monster.quantity;
        }
        return sum;
    }
}
