package WizardTD.game.logic;

import WizardTD.data.config.Config;
import WizardTD.game.board.object.Monster;

public class ManaController {
    public float mana;
    public int maximumMana;

    public float manaPerSecond;

    public int upgradeCost;
    private int upgradeLevel = 0;
    private final Config config;

    public boolean isLost = false;

    public ManaController(Config config) {
        this.config = config;

        mana = config.initialMana;
        maximumMana = config.initialManaCap;
        manaPerSecond = config.initialManaGainedPerSecond;
        upgradeCost = config.manaPoolSpellInitialCost;
    }

    public void update(long deltaMs) {
        mana += manaPerSecond * (deltaMs / 1000f);
    }

    public void onMonsterKilled(Monster m) {
        float manaToAdd = m.manaGainedOnKill;

        // mana_pool_spell_mana_gained_multiplier: eg. 1.1 is
        // +10% bonus to mana gained, which would become +20% if
        // the spell is cast twice, or +30% if cast 3 times
        float bonus = manaToAdd * config.manaPoolSpellManaGainedMultiplier;
        for (int i = 0; i < upgradeLevel; i++) {
            manaToAdd += bonus;
        }

        mana += manaToAdd;
    }

    public void onMonsterArrived(Monster m) {
        mana -= m.health;
        if (mana < 0) {
            isLost = true;
            mana = 0;
        }
    }

    /**
     * Removes mana only if it has enough
     *
     * @return true if had enough mana to use it
     */
    public boolean tryConsumeMana(int amount) {
        if (mana >= amount) {
            mana -= amount;
            return true;
        }
        return false;
    }

    public boolean tryUpgradeManaPool() {
        if (!tryConsumeMana(upgradeCost)) {
            return false;
        }

        upgradeLevel++;
        maximumMana = Math.round(maximumMana * config.manaPoolSpellCapMultiplier);
        upgradeCost += config.manaPoolSpellCostIncreasePerUse;
        return true;
    }
}
