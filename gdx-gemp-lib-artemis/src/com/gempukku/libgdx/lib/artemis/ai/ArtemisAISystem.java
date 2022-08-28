package com.gempukku.libgdx.lib.artemis.ai;

import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.ObjectMap;

public class ArtemisAISystem extends BaseSystem {
    private final ObjectMap<String, ArtemisFiniteStateMachine> finiteStateMachines = new ObjectMap<>();

    private EntitySubscription finiteStateMachineAIs;
    private ComponentMapper<FiniteStateMachineAIComponent> finiteStateMachineAIComponentMapper;
    private ComponentMapper<FiniteStateMachineComponent> finiteStateMachineComponentMapper;

    @Override
    protected void initialize() {
        finiteStateMachineAIs = world.getAspectSubscriptionManager().get(Aspect.all(FiniteStateMachineAIComponent.class));

        world.getAspectSubscriptionManager().get(Aspect.all(FiniteStateMachineComponent.class)).addSubscriptionListener(
                new EntitySubscription.SubscriptionListener() {
                    @Override
                    public void inserted(IntBag entities) {
                        for (int i = 0, s = entities.size(); s > i; i++) {
                            finiteStateMachineAdded(entities.get(i));
                        }
                    }

                    @Override
                    public void removed(IntBag entities) {
                        for (int i = 0, s = entities.size(); s > i; i++) {
                            finiteStateMachineRemoved(entities.get(i));
                        }
                    }
                });
    }

    @Override
    protected void processSystem() {
        float deltaTime = world.getDelta();

        IntBag entities = finiteStateMachineAIs.getEntities();
        for (int i = 0, s = entities.size(); s > i; i++) {
            int fsmEntityId = entities.get(i);
            FiniteStateMachineAIComponent finiteStateMachineAIComponent = finiteStateMachineAIComponentMapper.get(fsmEntityId);
            String state = finiteStateMachineAIComponent.getState();
            ObjectMap<String, Object> blackboard = finiteStateMachineAIComponent.getBlackboard();

            ArtemisFiniteStateMachine artemisFiniteStateMachine = finiteStateMachines.get(finiteStateMachineAIComponent.getFsmName());
            String newState = artemisFiniteStateMachine.update(deltaTime, state, world.getEntity(fsmEntityId), blackboard);
            finiteStateMachineAIComponent.setState(newState);
        }
    }

    private void finiteStateMachineAdded(int entityId) {
        Entity entity = world.getEntity(entityId);
        FiniteStateMachineComponent fsmComponent = finiteStateMachineComponentMapper.get(entity);

        finiteStateMachines.put(fsmComponent.getName(), createFiniteStateMachine(fsmComponent));
    }

    private ArtemisFiniteStateMachine createFiniteStateMachine(FiniteStateMachineComponent fsmComponent) {
        String initialState = fsmComponent.getInitialState();
        ArtemisFiniteStateMachine finiteStateMachine = new ArtemisFiniteStateMachine(initialState);
        for (ObjectMap.Entry<String, ArtemisStateDefinition> finiteStateMachineComponentState : fsmComponent.getStates()) {
            ArtemisTriggerState state = finiteStateMachineComponentState.value.getState();
            state.initializeForWorld(world);
            ArtemisTriggerMachineState triggerMachineState = new ArtemisTriggerMachineState(state);

            ObjectMap<String, ArtemisTriggerCondition> conditions = finiteStateMachineComponentState.value.getConditions();
            if (conditions != null) {
                for (ObjectMap.Entry<String, ArtemisTriggerCondition> condition : conditions) {
                    condition.value.initializeForWorld(world);
                    triggerMachineState.addTransition(condition.key, condition.value);
                }
            }

            finiteStateMachine.addState(finiteStateMachineComponentState.key, triggerMachineState);
        }
        return finiteStateMachine;
    }

    private void finiteStateMachineRemoved(int entityId) {
        Entity entity = world.getEntity(entityId);
        FiniteStateMachineComponent fsmComponent = finiteStateMachineComponentMapper.get(entity);

        finiteStateMachines.remove(fsmComponent.getName());
    }
}
