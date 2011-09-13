package com.idobjects.api;

import com.idobjects.api.ModelScope.IdChangeListener;
import com.idobjects.api.md.IdObjectReferenceMD;

public class IdObjectReferenceList{

    private ObjectIdentifier source;
    private ObjectIdentifier destination;
    private final IdObjectReferenceMD referenceMD;
    private ModelScope sourceModelScope;
    private ModelScope destinationModelScope;

    private final SourceChangeListener sourceChangeListener = new SourceChangeListener();
    private final DestinationChangeListener destinationChangeListener = new DestinationChangeListener();

    public IdObjectReferenceList( ObjectIdentifier source, ObjectIdentifier destination, ModelScope sourceModelScope, ModelScope destinationModelScope, IdObjectReferenceMD referenceMD ){
        this.source = source;
        this.destination = destination;
        this.referenceMD = referenceMD;
        this.sourceModelScope = sourceModelScope;
        this.destinationModelScope = destinationModelScope;

        sourceModelScope.addChangeListener( source, sourceChangeListener );
        sourceModelScope.addChangeListener( destination, destinationChangeListener );
    }

    public IdObject getDestinationObject(){
        return destinationModelScope.getObject( destination );
    }

    public void clear(){
        sourceModelScope.removeChangeListener( source, sourceChangeListener );
        sourceModelScope.removeChangeListener( destination, destinationChangeListener );
    }

    @Override
    public String toString(){
        return "IdObjectReference [source=" + source + ", destination=" + destination + ", referenceMD=" + referenceMD + "]";
    }

    private class SourceChangeListener implements IdChangeListener{

        @Override
        public void idChanged( ObjectIdentifier oldId, ObjectIdentifier newId ){
            source = newId;
        }

    }

    private class DestinationChangeListener implements IdChangeListener{

        @Override
        public void idChanged( ObjectIdentifier oldId, ObjectIdentifier newId ){
            destination = newId;
        }

    }

}
