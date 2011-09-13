package com.idobjects.api;

import java.util.ArrayList;
import java.util.List;

import com.idobjects.api.ModelScope.IdChangeListener;
import com.idobjects.api.md.IdObjectReferenceMD;

public class IdObjectReferenceList{

    private ObjectIdentifier source;
    private ObjectIdentifier destination;
    private final IdObjectReferenceMD referenceMD;
    private ModelScope sourceModelScope;
    private ModelScope destinationModelScope;

    private final List<IdObjectReference> references = new ArrayList<IdObjectReference>();

    public IdObjectReferenceList( ObjectIdentifier source, ObjectIdentifier destination, ModelScope sourceModelScope, ModelScope destinationModelScope,
            IdObjectReferenceMD referenceMD ){
        this.source = source;
        this.destination = destination;
        this.sourceModelScope = sourceModelScope;
        this.destinationModelScope = destinationModelScope;
        this.referenceMD = referenceMD;
    }

    public void add( ObjectIdentifier toAdd ){
        
    }

    public void remove( ObjectIdentifier toRemove ){

    }

    public List<IdObject> getDesinitations(){
        List<IdObject> result = new ArrayList<IdObject>();
        for( IdObjectReference idObjectReference : references ){
            result.add( idObjectReference.getDestinationObject() );
        }
        return result;
    }

}
