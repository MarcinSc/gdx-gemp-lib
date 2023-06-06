package com.gempukku.libgdx.fbx.handler.objects.geometry.element;

import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.fbx.handler.AbstractEnumRecordHandler;

public class ReferenceInformationTypeRecordHandler extends AbstractEnumRecordHandler<ReferenceInformationType> {
    public ReferenceInformationTypeRecordHandler(Consumer<ReferenceInformationType> referenceInformationTypeConsumer) {
        super(referenceInformationTypeConsumer);
    }

    protected ReferenceInformationType mapToEnum(String valueString) {
        switch (valueString) {
            case "Direct":
                return ReferenceInformationType.Direct;
            case "Index":
            case "IndexToDirect":
                return ReferenceInformationType.IndexToDirect;
            default:
                throw new UnsupportedOperationException("Unknown type of ReferenceInformationType: "+valueString);
        }
    }
}
