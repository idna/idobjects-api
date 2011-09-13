package com.idobjects.api;

import java.util.List;

import com.idobjects.api.md.IdObjectReferenceMD;

public interface IdObject{

    void setId( ObjectIdentifier objectIdentifier );

    ObjectIdentifier getId();

    ModelScope getModelScope();

    void addReference( IdObjectReferenceMD referenceMD, IdObject value );

    void removeReference( IdObjectReferenceMD referenceMd, IdObject value );
    
    List<IdObject> getReferences( IdObjectReferenceMD referenceMD );

}
