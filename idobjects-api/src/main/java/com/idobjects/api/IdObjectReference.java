package com.idobjects.api;

import com.idobjects.api.ModelScope.IdChangeListener;
import com.idobjects.api.md.IdObjectReferenceMD;

public class IdObjectReference{

    private ObjectIdentifier sourceId;
    private ObjectIdentifier destinationId;
    private final IdObjectReferenceMD referenceMD;
    private final ModelScope modelScope;

    private final SourceChangeListener sourceChangeListener = new SourceChangeListener();
    private final DestinationChangeListener destinationChangeListener = new DestinationChangeListener();

    public IdObjectReference( ObjectIdentifier source, ObjectIdentifier destination, ModelScope modelScope, IdObjectReferenceMD referenceMD ){
        this.sourceId = source;
        this.destinationId = destination;
        this.referenceMD = referenceMD;
        this.modelScope = modelScope;

        modelScope.addChangeListener( source, sourceChangeListener );
        modelScope.addChangeListener( destination, destinationChangeListener );
    }

    public IdObject getDestinationObject(){
        return modelScope.getObject( destinationId );
    }

    public ObjectIdentifier getDestinationObjectId(){
        return destinationId;
    }

    public void clear(){
        modelScope.removeChangeListener( sourceId, sourceChangeListener );
        modelScope.removeChangeListener( destinationId, destinationChangeListener );
    }

    @Override
    public String toString(){
        return "IdObjectReference [source=" + sourceId + ", destination=" + destinationId + ", referenceMD=" + referenceMD + "]";
    }

    private class SourceChangeListener implements IdChangeListener{

        @Override
        public void idChanged( ObjectIdentifier oldId, ObjectIdentifier newId ){
            sourceId = newId;
        }

        @Override
        public void newObject( ObjectIdentifier newId, IdObject newObject ){

        }

    }

    private class DestinationChangeListener implements IdChangeListener{

        @Override
        public void idChanged( ObjectIdentifier oldId, ObjectIdentifier newId ){
            destinationId = newId;
        }

        @Override
        public void newObject( ObjectIdentifier newId, IdObject newObject ){
            if( !referenceMD.isBidirectional() ) return;
            ( ( AbstractIdObject )newObject ).addReferenceImpl( referenceMD.getInverseReferenceMD(), sourceId, false );
        }

    }

}
