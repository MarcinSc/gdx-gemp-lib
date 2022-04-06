package com.gempukku.libgdx.ai.fst;

import com.gempukku.libgdx.ai.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FiniteStateMachineTest extends LibGDXTest {
    @Test
    public void machineWithOneState() {
        MachineStateImpl state = new MachineStateImpl();
        FiniteStateMachine fsm = new FiniteStateMachine("initial", state);
        fsm.update(0f);

        assertEquals("initial", fsm.getCurrentState());
        assertNull(state.getTransitionedFrom());
        assertNull(state.getTransitionedTo());
        assertEquals(1, state.getUpdateCount());
    }

    @Test
    public void machineTransition() {
        MachineStateImpl initialState = new MachineStateImpl();
        MachineStateImpl secondState = new MachineStateImpl();
        FiniteStateMachine fsm = new FiniteStateMachine("initial", initialState);
        fsm.addState("second", secondState);
        fsm.addStateTransition("initial", new GoTransition("second"));
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
        MachineStateImpl secondState = new MachineStateImpl();
        FiniteStateMachine fsm = new FiniteStateMachine("initial", initialState);
        fsm.addState("second", secondState);
        fsm.addStateTransition("initial", new NoTransition("second"));
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
    public void machineTwoTransitionS() {
        MachineStateImpl initialState = new MachineStateImpl();
        MachineStateImpl secondState = new MachineStateImpl();
        MachineStateImpl thirdState = new MachineStateImpl();
        FiniteStateMachine fsm = new FiniteStateMachine("initial", initialState);
        fsm.addState("second", secondState);
        fsm.addState("third", thirdState);
        fsm.addStateTransition("initial", new GoTransition("second"));
        fsm.addStateTransition("second", new GoTransition("third"));
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

    private static class NoTransition implements MachineStateTransition {
        private String state;

        public NoTransition(String state) {
            this.state = state;
        }

        @Override
        public String getState() {
            return state;
        }

        @Override
        public boolean isTriggered() {
            return false;
        }
    }

    private static class GoTransition implements MachineStateTransition {
        private String state;

        public GoTransition(String state) {
            this.state = state;
        }

        @Override
        public String getState() {
            return state;
        }

        @Override
        public boolean isTriggered() {
            return true;
        }
    }

    private static class MachineStateImpl implements MachineState {
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
