package com.twilightheroes.game.tools;

import com.badlogic.ashley.core.ComponentMapper;
import com.twilightheroes.game.ecs.components.AnimationComponent;
import com.twilightheroes.game.ecs.components.AttackComponent;
import com.twilightheroes.game.ecs.components.B2dBodyComponent;
import com.twilightheroes.game.ecs.components.BulletComponent;
import com.twilightheroes.game.ecs.components.CollisionComponent;
import com.twilightheroes.game.ecs.components.DialogueComponent;
import com.twilightheroes.game.ecs.components.DoorComponent;
import com.twilightheroes.game.ecs.components.EnemyComponent;
import com.twilightheroes.game.ecs.components.ExitComponent;
import com.twilightheroes.game.ecs.components.HazardComponent;
import com.twilightheroes.game.ecs.components.InteractiveObjectComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.StatsComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;
import com.twilightheroes.game.ecs.components.effectComponents.StatusComponent;
import com.twilightheroes.game.ecs.components.spells.SpellComponent;

/**
 * The type Mappers.
 */
public class Mappers {


    /**
     * The constant animCom.
     */
    public static final ComponentMapper<AnimationComponent> animCom = ComponentMapper.getFor(AnimationComponent.class);
    /**
     * The constant atkCom.
     */
    public static final ComponentMapper<AttackComponent> atkCom = ComponentMapper.getFor(AttackComponent.class);

    /**
     * The constant b2dCom.
     */
    public static final ComponentMapper<B2dBodyComponent> b2dCom = ComponentMapper.getFor(B2dBodyComponent.class);
    /**
     * The constant collisionCom.
     */
    public static final ComponentMapper<CollisionComponent> collisionCom = ComponentMapper.getFor(CollisionComponent.class);
    /**
     * The constant enemyCom.
     */
    public static final ComponentMapper<EnemyComponent> enemyCom = ComponentMapper.getFor(EnemyComponent.class);
    /**
     * The constant exitCom.
     */
    public static final ComponentMapper<ExitComponent> exitCom = ComponentMapper.getFor(ExitComponent.class);
    /**
     * The constant playerCom.
     */
    public static final ComponentMapper<PlayerComponent> playerCom = ComponentMapper.getFor(PlayerComponent.class);
    /**
     * The constant stateCom.
     */
    public static final ComponentMapper<StateComponent> stateCom = ComponentMapper.getFor(StateComponent.class);
    /**
     * The constant texCom.
     */
    public static final ComponentMapper<TextureComponent> texCom = ComponentMapper.getFor(TextureComponent.class);
    /**
     * The constant typeCom.
     */
    public static final ComponentMapper<TypeComponent> typeCom = ComponentMapper.getFor(TypeComponent.class);
    /**
     * The constant statusCom.
     */
    public static final ComponentMapper<StatusComponent> statusCom = ComponentMapper.getFor(StatusComponent.class);
    /**
     * The constant statsCom.
     */
    public static final ComponentMapper<StatsComponent> statsCom = ComponentMapper.getFor(StatsComponent.class);

    /**
     * The constant spellCom.
     */
    public static final ComponentMapper<SpellComponent> spellCom = ComponentMapper.getFor(SpellComponent.class);
    /**
     * The constant bullCom.
     */
    public static final ComponentMapper<BulletComponent> bullCom = ComponentMapper.getFor(BulletComponent.class);
    /**
     * The constant dialogueCom.
     */
    public static final ComponentMapper<DialogueComponent> dialogueCom = ComponentMapper.getFor(DialogueComponent.class);
    /**
     * The constant interactiveCom.
     */
    public static final ComponentMapper<InteractiveObjectComponent> interactiveCom = ComponentMapper.getFor(InteractiveObjectComponent.class);
    /**
     * The constant doorCom.
     */
    public static final ComponentMapper<DoorComponent> doorCom = ComponentMapper.getFor(DoorComponent.class);
    /**
     * The constant hazardCom.
     */
    public static final ComponentMapper<HazardComponent> hazardCom = ComponentMapper.getFor(HazardComponent.class);


}
