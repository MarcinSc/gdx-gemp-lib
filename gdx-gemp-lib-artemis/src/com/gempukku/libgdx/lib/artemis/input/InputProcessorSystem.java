package com.gempukku.libgdx.lib.artemis.input;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

import java.util.Comparator;

public class InputProcessorSystem extends BaseSystem {
    @Override
    protected void initialize() {
        Gdx.input.setInputProcessor(buildMultiplexer());
    }

    @Override
    protected void processSystem() {

    }

    private InputMultiplexer buildMultiplexer() {
        Array<InputProcessorProvider> inputProcessorProviders = new Array<>();
        for (BaseSystem system : world.getSystems()) {
            if (system instanceof InputProcessorProvider) {
                inputProcessorProviders.add((InputProcessorProvider) system);
            }
        }

        Sort.instance().sort(inputProcessorProviders,
                new Comparator<InputProcessorProvider>() {
                    @Override
                    public int compare(InputProcessorProvider o1, InputProcessorProvider o2) {
                        int priority1 = o1.getInputPriority();
                        int priority2 = o2.getInputPriority();
                        if (priority1 < priority2)
                            return -1;
                        if (priority1 > priority2)
                            return 1;
                        return 0;
                    }
                });

        InputMultiplexer multiplexer = new InputMultiplexer();
        for (InputProcessorProvider inputProcessorProvider : inputProcessorProviders) {
            multiplexer.addProcessor(inputProcessorProvider.getInputProcessor());
        }

        return multiplexer;
    }
}
