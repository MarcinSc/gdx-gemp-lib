package com.gempukku.libgdx.lib.artemis.shape;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;

public class ShapeSystem extends BaseEntitySystem {
    private ObjectMap<String, ObjectMap<String, ShapeDataDefinition>> shapeDataMap = new ObjectMap<>();
    private ObjectIntMap<String> shapeVertexCountMap = new ObjectIntMap<>();
    private ObjectMap<String, short[]> shapeIndicesMap = new ObjectMap<>();

    private ComponentMapper<ShapeComponent> shapeComponentMapper;
    private ComponentMapper<ShapeDataComponent> shapeDataComponentMapper;

    public ShapeSystem() {
        super(Aspect.all(ShapeComponent.class, ShapeDataComponent.class));
    }

    protected void inserted(int entityId) {
        shapeAdded(world.getEntity(entityId));
    }

    protected void removed(int entityId) {
        shapeRemoved(world.getEntity(entityId));
    }

    public int getVertexCount(String shapeName) {
        return shapeVertexCountMap.get(shapeName, -1);
    }

    public short[] getIndices(String shapeName) {
        return shapeIndicesMap.get(shapeName);
    }

    public ShapeDataDefinition getShapeVertexData(String shapeName, String dataType) {
        return shapeDataMap.get(shapeName).get(dataType);
    }

    private void shapeAdded(Entity entity) {
        ShapeComponent shape = shapeComponentMapper.get(entity);
        ShapeDataComponent shapeData = shapeDataComponentMapper.get(entity);

        String shapeName = shape.getName();
        if (!shapeVertexCountMap.containsKey(shapeName)) {
            shapeVertexCountMap.put(shapeName, shape.getVertexCount());
            shapeIndicesMap.put(shapeName, shape.getIndices());
        }

        ObjectMap<String, ShapeDataDefinition> shapeDatumMap = shapeDataMap.get(shapeName);
        if (shapeDatumMap == null) {
            shapeDatumMap = new ObjectMap<>();
            shapeDataMap.put(shapeName, shapeDatumMap);
        }

        for (ObjectMap.Entry<String, ShapeDataDefinition> nameData : shapeData.getNamedData()) {
            shapeDatumMap.put(nameData.key, nameData.value);
        }
    }

    private void shapeRemoved(Entity entity) {
        ShapeComponent shape = shapeComponentMapper.get(entity);
        ShapeDataComponent shapeData = shapeDataComponentMapper.get(entity);

        String shapeName = shape.getName();
        ObjectMap<String, ShapeDataDefinition> shapeDataMap = this.shapeDataMap.get(shapeName);

        for (String key : shapeData.getNamedData().keys()) {
            shapeDataMap.remove(key);
        }

        // The last data has been removed
        if (shapeDataMap.isEmpty()) {
            shapeVertexCountMap.remove(shapeName, 0);
            shapeIndicesMap.remove(shapeName);
        }
    }

    @Override
    protected void processSystem() {

    }
}
