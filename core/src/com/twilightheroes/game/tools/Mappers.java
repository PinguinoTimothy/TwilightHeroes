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
import com.twilightheroes.game.ecs.components.InteractiveObjectComponent;
import com.twilightheroes.game.ecs.components.PlayerComponent;
import com.twilightheroes.game.ecs.components.StateComponent;
import com.twilightheroes.game.ecs.components.StatsComponent;
import com.twilightheroes.game.ecs.components.TextureComponent;
import com.twilightheroes.game.ecs.components.TypeComponent;
import com.twilightheroes.game.ecs.components.effectComponents.StatusComponent;
import com.twilightheroes.game.ecs.components.spells.SpellComponent;

public class Mappers {


    public static final ComponentMapper<AnimationComponent> animCom = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<AttackComponent> atkCom = ComponentMapper.getFor(AttackComponent.class);

    public static final ComponentMapper<B2dBodyComponent> b2dCom = ComponentMapper.getFor(B2dBodyComponent.class);
    public static final ComponentMapper<CollisionComponent> collisionCom = ComponentMapper.getFor(CollisionComponent.class);
    public static final ComponentMapper<EnemyComponent> enemyCom = ComponentMapper.getFor(EnemyComponent.class);
    public static final ComponentMapper<ExitComponent> exitCom = ComponentMapper.getFor(ExitComponent.class);
    public static final ComponentMapper<PlayerComponent> playerCom = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<StateComponent> stateCom = ComponentMapper.getFor(StateComponent.class);
    public static final ComponentMapper<TextureComponent> texCom = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<TypeComponent> typeCom = ComponentMapper.getFor(TypeComponent.class);
    public static final ComponentMapper<StatusComponent> statusCom = ComponentMapper.getFor(StatusComponent.class);
    public static final ComponentMapper<StatsComponent> statsCom = ComponentMapper.getFor(StatsComponent.class);

    public static final ComponentMapper<SpellComponent> spellCom = ComponentMapper.getFor(SpellComponent.class);
    public static final ComponentMapper<BulletComponent> bullCom = ComponentMapper.getFor(BulletComponent.class);
    public static final ComponentMapper<DialogueComponent> dialogueCom = ComponentMapper.getFor(DialogueComponent.class);
    public static final ComponentMapper<InteractiveObjectComponent> interactiveCom = ComponentMapper.getFor(InteractiveObjectComponent.class);
    public static final ComponentMapper<DoorComponent> doorCom = ComponentMapper.getFor(DoorComponent.class);


}
