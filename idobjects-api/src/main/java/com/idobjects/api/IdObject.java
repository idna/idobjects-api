package com.idobjects.api;

import java.util.List;

import com.idobjects.api.md.IdObjectReferenceMD;

public interface IdObject{

    void setId( ObjectIdentifier objectIdentifier );

    ObjectIdentifier getId();

    ModelScope getModelScope();

    void addReference( IdObjectReferenceMD referenceMD, ObjectIdentifier destinationId );

    void removeReference( IdObjectReferenceMD referenceMd, ObjectIdentifier destinationId );
    
    List<IdObjectReference> getReferences( IdObjectReferenceMD referenceMD );

}
