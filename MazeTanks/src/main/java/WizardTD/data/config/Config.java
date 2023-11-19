package WizardTD.data.config;

import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Stores data from config.json file
public class Config {
    public final String layout;
    public final int initialTowerRange;
    public final float initialTowerFiringSpeed;
    public final int initialTowerDamage;
    public final int initialMana;
    public final int initialManaCap;
    public final int initialManaGainedPerSecond;
    public final int towerCost;
    public final int manaPoolSpellInitialCost;
    public final int manaPoolSpellCostIncreasePerUse;
    public final float manaPoolSpellCapMultiplier;
    public final float manaPoolSpellManaGainedMultiplier;

    public final List<WaveInfo> waves = new ArrayList<>();

    // Initialize with date from JSON
    public Config(JSONObject o) {
        layout = o.getString("layout");
        initialTowerRange = o.getInt("initial_tower_range");
        initialTowerFiringSpeed = o.getFloat("initial_tower_firing_speed");
        initialTowerDamage = o.getInt("initial_tower_damage");
        initialMana = o.getInt("initial_mana");
        initialManaCap = o.getInt("initial_mana_cap");
        initialManaGainedPerSecond = o.getInt("initial_mana_gained_per_second");
        towerCost = o.getInt("tower_cost");
        manaPoolSpellInitialCost = o.getInt("mana_pool_spell_initial_cost");
        manaPoolSpellCostIncreasePerUse = o.getInt("mana_pool_spell_cost_increase_per_use");
        manaPoolSpellCapMultiplier = o.getFloat("mana_pool_spell_cap_multiplier");
        manaPoolSpellManaGainedMultiplier = o.getFloat("mana_pool_spell_mana_gained_multiplier");

        JSONArray wavesJson = o.getJSONArray("waves");
        for (int i = 0; i < wavesJson.size(); i++) {
            waves.add(new WaveInfo(wavesJson.getJSONObject(i)));
        }
    }
}
