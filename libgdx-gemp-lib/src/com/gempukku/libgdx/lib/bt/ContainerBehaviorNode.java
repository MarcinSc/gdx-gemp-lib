package com.gempukku.libgdx.lib.bt;

public interface ContainerBehaviorNode extends BehaviorNode {
    Iterable<BehaviorNode> getChildren();
}
