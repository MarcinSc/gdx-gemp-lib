package com.gempukku.libgdx.fbx.handler.objects.geometry.element;

import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.fbx.handler.AbstractEnumRecordHandler;

public class MappingInformationTypeRecordHandler extends AbstractEnumRecordHandler<MappingInformationType> {
    public MappingInformationTypeRecordHandler(Consumer<MappingInformationType> mappingInformationTypeConsumer) {
        super(mappingInformationTypeConsumer);
    }

    protected MappingInformationType mapToEnum(String valueString) {
        switch (valueString) {
            case "None":
                return MappingInformationType.None;
            case "ByControlPoint":
                return MappingInformationType.ByControlPoint;
            case "ByPolygonVertex":
                return MappingInformationType.ByPolygonVertex;
            case "ByPolygon":
                return MappingInformationType.ByPolygon;
            case "ByEdge":
                return MappingInformationType.ByEdge;
            case "AllSame":
                return MappingInformationType.AllSame;
            default:
                throw new UnsupportedOperationException("Unknown type of MappingInformationType: "+valueString);
        }
    }
}
