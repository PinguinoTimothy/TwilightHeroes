package com.twilightheroes.game.ecs.components.effectComponents;

/**
 * The type Status effect.
 */
public class StatusEffect {

    /**
     * The Type of status.
     */
    public int type;
    /**
     * The Buff or debuff.
     */
    public boolean buffOrDebuff;

    /**
     * time remaining of the status
     */
    private float timeRemaining;

    /**
     * value of the status
     */
    private float value;

    /**
     * Indicates if the status is already applied
     */
    private boolean used;

    /**
     * Instantiates a new Status effect.
     *
     * @param type          the type
     * @param buffOrDebuff  the buff or debuff
     * @param timeRemaining the time remaining
     * @param value         the value
     */
    public StatusEffect(int type, boolean buffOrDebuff, float timeRemaining, float value) {
        this.type = type;
        this.buffOrDebuff = buffOrDebuff;
        setTimeRemaining(timeRemaining);
        setValue(value);
    }


    /**
     * Gets time remaining.
     *
     * @return the time remaining
     */
    public float getTimeRemaining() {
        return timeRemaining;
    }


    /**
     * Sets time remaining.
     *
     * @param timeRemaining the time remaining
     */
    public void setTimeRemaining(float timeRemaining) {
        this.timeRemaining = timeRemaining;
    }


    /**
     * Gets used.
     *
     * @return the used
     */
    public boolean getUsed() {
        return used;
    }


    /**
     * Sets used.
     *
     * @param used the used
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public float getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(float value) {
        this.value = value;
    }
}
