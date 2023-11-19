package WizardTD.data.config;

import processing.data.JSONObject;

// Stores data from config.json about monster characteristics and quantity in a specific wave
public class MonsterWaveInfo {
    public Type type;
    public int hp;
    public float speed;
    public float armour;
    public int manaGainedOnKill;
    public int quantity;

    // Initialize with date from JSON
    public MonsterWaveInfo(JSONObject o) {
        // get "type" field and convert it to Type enum
        String typeString = o.getString("type").toUpperCase();
        type = Type.valueOf(typeString);

        hp = o.getInt("hp");
        speed = o.getFloat("speed");
        armour = o.getFloat("armour");
        manaGainedOnKill = o.getInt("mana_gained_on_kill");
        quantity = o.getInt("quantity");
    }

    public MonsterWaveInfo(Type type, int hp, float speed, float armour, int manaGainedOnKill, int quantity) {
        this.type = type;
        this.hp = hp;
        this.speed = speed;
        this.armour = armour;
        this.manaGainedOnKill = manaGainedOnKill;
        this.quantity = quantity;
    }

    public MonsterWaveInfo copy() {
        return new MonsterWaveInfo(type, hp, speed, armour, manaGainedOnKill, quantity);
    }

    public enum Type {
        GREMLIN, BEETLE, WORM
    }
}
