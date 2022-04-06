package com.gempukku.libgdx.ai.fst;

import com.gempukku.libgdx.ai.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FiniteStateMachineTest extends LibGDXTest {
    @Test
    public void machineWithOneState() {
        MachineStateImpl state = new MachineStateImpl();
        TriggerMachineState triggerState = new TriggerMachineState(state);
        FiniteStateMachine fsm = new FiniteStateMachine("initial");
        fsm.addState("initial", triggerState);
        fsm.update(0f);

        assertEquals("initial", fsm.getCurrentState());
        assertNull(state.getTransitionedFrom());
        assertNull(state.getTransitionedTo());
        assertEquals(1, state.getUpdateCount());
    }

    @Test
    public void machineTransition() {
        MachineStateImpl initialState = new MachineStateImpl();
        TriggerMachineState initialTriggerState = new TriggerMachineState(initialState);
        initialTriggerState.addTransition("second", new GoTriggerCondition());

        MachineStateImpl secondState = new MachineStateImpl();
        TriggerMachineState secondTriggerState = new TriggerMachineState(secondState);

        FiniteStateMachine fsm = new FiniteStateMachine("initial");
        fsm.addState("initial", initialTriggerState);
        fsm.addState("second", secondTriggerState);
        fsm.update(0f);

        assertEquals("second", fsm.getCurrentState());

        assertEquals("second", initialState.getTransitionedTo());
        assertNull(initialState.getTransitionedFrom());
        assertEquals(0, initialState.getUpdateCount());

        assertNull(secondState.getTransitionedTo());
        assertEquals("initial", secondState.getTransitionedFrom());
        assertEquals(1, secondState.getUpdateCount());
    }

    @Test
    public void noTransition() {
        MachineStateImpl initialState = new MachineStateImpl();
        TriggerMachineState initialTriggerState = new TriggerMachineState(initialState);
        initialTriggerState.addTransition("second", new NoTriggerCondition());

        MachineStateImpl secondState = new MachineStateImpl();
        TriggerMachineState secondTriggerState = new TriggerMachineState(secondState);

        FiniteStateMachine fsm = new FiniteStateMachine("initial");
        fsm.addState("initial", initialTriggerState);
        fsm.addState("second", secondTriggerState);
        fsm.update(0f);

        assertEquals("initial", fsm.getCurrentState());

        assertNull(initialState.getTransitionedTo());
        assertNull(initialState.getTransitionedFrom());
        assertEquals(1, initialState.getUpdateCount());

        assertNull(secondState.getTransitionedTo());
        assertNull(secondState.getTransitionedFrom());
        assertEquals(0, secondState.getUpdateCount());
    }

    @Test
    public void machineTwoTransitions() {
        MachineStateImpl initialState = new MachineStateImpl();
        TriggerMachineState initialTriggerState = new TriggerMachineState(initialState);
        initialTriggerState.addTransition("second", new GoTriggerCondition());

        MachineStateImpl secondState = new MachineStateImpl();
        TriggerMachineState secondTriggerState = new TriggerMachineState(secondState);
        secondTriggerState.addTransition("third", new GoTriggerCondition());

        MachineStateImpl thirdState = new MachineStateImpl();
        TriggerMachineState thirdTriggerState = new TriggerMachineState(thirdState);

        FiniteStateMachine fsm = new FiniteStateMachine("initial");
        fsm.addState("initial", initialTriggerState);
        fsm.addState("second", secondTriggerState);
        fsm.addState("third", thirdTriggerState);
        fsm.update(0f);

        assertEquals("third", fsm.getCurrentState());

        assertEquals("second", initialState.getTransitionedTo());
        assertNull(initialState.getTransitionedFrom());
        assertEquals(0, initialState.getUpdateCount());

        assertEquals("third", secondState.getTransitionedTo());
        assertEquals("initial", secondState.getTransitionedFrom());
        assertEquals(0, secondState.getUpdateCount());

        assertNull(thirdState.getTransitionedTo());
        assertEquals("second", thirdState.getTransitionedFrom());
        assertEquals(1, thirdState.getUpdateCount());
    }

    private static class NoTriggerCondition implements TriggerCondition {
        @Override
        public void reset() {

        }

        @Override
        public boolean isTriggered() {
            return false;
        }
    }

    private static class GoTriggerCondition implements TriggerCondition {
        @Override
        public void reset() {

        }

        @Override
        public boolean isTriggered() {
            return true;
        }
    }

    private static class MachineStateImpl implements TriggerState {
        private String transitionedTo;
        private String transitionedFrom;
        private int updateCount;
        private float deltaSum;

        @Override
        public void transitioningTo(String newState) {
            transitionedTo = newState;
        }

        @Override
        public void transitioningFrom(String oldState) {
            transitionedFrom = oldState;
        }

        @Override
        public void update(float delta) {
            updateCount++;
            deltaSum += delta;
        }

        public String getTransitionedTo() {
            return transitionedTo;
        }

        public String getTransitionedFrom() {
            return transitionedFrom;
        }

        public int getUpdateCount() {
            return updateCount;
        }

        public float getDeltaSum() {
            return deltaSum;
        }
    }
}
