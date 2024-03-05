package com.twilightheroes.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.twilightheroes.game.ecs.components.effectComponents.StatusComponent;
import com.twilightheroes.game.ecs.components.effectComponents.StatusEffect;
import com.twilightheroes.game.ecs.components.effectComponents.StatusType;
import com.twilightheroes.game.tools.Mappers;

/**
 * The system that handles all the effects
 */
public class EffectSystem extends IteratingSystem {

    /**
     * Status to remove
     */
    private final Array<StatusEffect> statusABorrar = new Array<>();

    /**
     * Instantiates a new Effect system.
     */
    public EffectSystem() {

        super(Family.all(StatusComponent.class).get());
    }
    /**
     * This method is called on every entity on every update call of the EntitySystem.
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StatusComponent statusComponent = Mappers.statusCom.get(entity);

        for (StatusEffect status : statusComponent.effects) {
            if (!status.getUsed()) {
                applyEffect(status, entity);
                status.setUsed(true);
            } else {
                status.setTimeRemaining(status.getTimeRemaining() - deltaTime);
                if (status.getTimeRemaining() <= 0) {
                    status.buffOrDebuff = !status.buffOrDebuff;
                    applyEffect(status, entity);
                    statusABorrar.add(status);
                }
            }
        }

        for (int i = 0; i < statusABorrar.size; i++) {
            statusComponent.effects.removeValue(statusABorrar.get(i), true);
        }
    }

    /**
     * Apply the effect.
     *
     * @param effect the effect to apply
     * @param entity the entity to apply the effect
     */
    void applyEffect(StatusEffect effect, Entity entity) {

        switch (effect.type) {
            case StatusType.DAMAGE:
                Mappers.statsCom.get(entity).damage += effect.buffOrDebuff ? effect.getValue() : -effect.getValue();
                break;

            case StatusType.SPEED:
                Mappers.statsCom.get(entity).speed += effect.buffOrDebuff ? effect.getValue() : -effect.getValue();
                break;

            case StatusType.ATK_SPEED:
                Mappers.statsCom.get(entity).atkSpeed += effect.buffOrDebuff ? effect.getValue() : -effect.getValue();
                break;

            case StatusType.DAMAGE_REDUCTION:
                Mappers.statsCom.get(entity).damageReduction += effect.buffOrDebuff ? effect.getValue() : -effect.getValue();
                break;

            case StatusType.HP_REGEN:
                Mappers.statsCom.get(entity).hpRegen += effect.buffOrDebuff ? effect.getValue() : -effect.getValue();
                break;

            case StatusType.LIFE_STEAL:
                Mappers.statsCom.get(entity).lifeSteal += effect.buffOrDebuff ? effect.getValue() : -effect.getValue();
                break;

        }
    }


}
