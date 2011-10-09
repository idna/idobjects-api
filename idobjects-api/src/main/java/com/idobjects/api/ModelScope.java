package com.idobjects.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.idobjects.api.md.IdObjectReferenceMD;

public class ModelScope{

    private final ModelScopeIdentifier modelScopeId;

    private final IdObjectContainer idObjectContainer;

    private final Map<ObjectIdentifier, List<IdChangeListener>> listenersById = new LinkedHashMap<ObjectIdentifier, List<IdChangeListener>>();

    private final List<ModelScopeListener> modelScopeListeners = new ArrayList<ModelScopeListener>();

    public ModelScope( ModelScopeIdentifier modelScopeId ){
        this.modelScopeId = modelScopeId;
        this.idObjectContainer = createIdObjectContainer();
    }

    protected IdObjectContainer createIdObjectContainer(){
        return new DefaultIdObjectContainer();
    }

    public final ModelScopeIdentifier getId(){
        return modelScopeId;
    }

    public IdObject getObject( ObjectIdentifier objectId ){
        return idObjectContainer.get( objectId );
    }

    public List<IdObject> getObjects(){
        return new ArrayList<IdObject>( idObjectContainer.elements() );
    }

    public boolean containsObject( ObjectIdentifier objectId ){
        return idObjectContainer.contains( objectId );
    }

    public int size(){
        return idObjectContainer.size();
    }

    public ModelScope copy( ModelScopeIdentifier identifier ){
        ModelScope newModelScope = new ModelScope( identifier );

        List<AbstractIdObject> newObjects = new ArrayList<AbstractIdObject>();
        for( IdObject idObject : idObjectContainer.elements() ){
            AbstractIdObject abstractIdObject = ( AbstractIdObject )idObject;
            AbstractIdObject newObject = abstractIdObject.copy( newModelScope );
            newObjects.add( newObject );
        }

        for( AbstractIdObject newIdObject : newObjects ){
            copyReferences( getObject( newIdObject.getId() ), newModelScope );
        }

        return newModelScope;

    }

    public void addModelScopeListener( ModelScopeListener listener ){
        modelScopeListeners.add( listener );
    }

    public void removeModelScopeListener( ModelScopeListener toRemove ){
        modelScopeListeners.remove( toRemove );
    }

    private void fireObjectAdded( IdObject newObject ){
        for( ModelScopeListener listener : modelScopeListeners ){
            listener.objectAdded( newObject );
        }
    }

    private void fireObjectChanged( IdObject changedObject ){
        for( ModelScopeListener listener : modelScopeListeners ){
            listener.objectChanged( changedObject );
        }
    }

    private void fireObjectRemoved( IdObject removedObject ){
        for( ModelScopeListener listener : modelScopeListeners ){
            listener.objectRemoved( removedObject );
        }
    }

    private void fireReferenceAdded( IdObjectReference newReference ){
        for( ModelScopeListener listener : modelScopeListeners ){
            listener.referenceAdded( newReference );
        }
    }

    private void fireReferenceRemoved( IdObjectReference removedReference ){
        for( ModelScopeListener listener : modelScopeListeners ){
            listener.referenceRemoved( removedReference );
        }
    }

    private void copyReferences( IdObject source, ModelScope targetModelScope ){
        Map<IdObjectReferenceMD, List<IdObjectReference>> referenceMap = source.getReferences();
        for( IdObjectReferenceMD referenceMD : referenceMap.keySet() ){
            List<IdObjectReference> referenceList = referenceMap.get( referenceMD );
            for( IdObjectReference reference : referenceList ){

                IdObject sourceObject = targetModelScope.getObject( reference.getSourceObjectId() );
                ObjectIdentifier destinationId = reference.getDestinationObjectId();
                sourceObject.addReference( referenceMD, destinationId );
            }
        }

    }

    void addChangeListener( ObjectIdentifier objectId, IdChangeListener changeListener ){
        List<IdChangeListener> listeners = listenersById.get( objectId );
        if( listeners == null ){
            listeners = new ArrayList<ModelScope.IdChangeListener>();
            listenersById.put( objectId, listeners );
        }
        listeners.add( changeListener );
    }

    void idChanged( IdObject idObject, ObjectIdentifier oldId, ObjectIdentifier newId ){
        idObjectContainer.idChanged( idObject, oldId, newId );
    }

    void addObject( AbstractIdObject idObject ){
        ObjectIdentifier newId = idObject.getId();
        if( idObjectContainer.contains( newId ) ){ throw new IdObjectException( "Duplicate object with id " + newId ); }
        idObjectContainer.add( idObject );
        fireNewObject( idObject );
        fireObjectAdded( idObject );
    }

    void idChanged( ObjectIdentifier oldId, ObjectIdentifier newId, IdObject idObject ){
        if( oldId.equals( newId ) ) return;
        if( idObjectContainer.contains( newId ) ){ throw new IdObjectException( "Duplicate object with id " + newId ); }
        fireIdChanged( oldId, newId );
        fireObjectChanged( idObject );
    }

    void idObjectPropertyChanged( Object oldValue, Object newValue, IdObject idObject ){
        fireObjectChanged( idObject );
    }

    void referenceRemoved( IdObjectReference removed ){
        fireReferenceRemoved( removed );
    }

    void referenceAdded( IdObjectReference added ){
        fireReferenceAdded( added );
    }

    private void fireIdChanged( ObjectIdentifier oldId, ObjectIdentifier newId ){
        if( !listenersById.containsKey( oldId ) ) return;
        listenersById.put( newId, listenersById.get( oldId ) );
        listenersById.remove( oldId );

        for( IdChangeListener listener : listenersById.get( newId ) ){
            listener.idChanged( oldId, newId );
        }

    }

    private void fireNewObject( IdObject newObject ){
        if( !listenersById.containsKey( newObject.getId() ) ) return;

        for( IdChangeListener listener : listenersById.get( newObject ) ){
            listener.newObject( newObject.getId(), newObject );
        }

    }

    static interface IdChangeListener{

        void newObject( ObjectIdentifier newId, IdObject newObject );

        void idChanged( ObjectIdentifier oldId, ObjectIdentifier newId );
    }

    void removeChangeListener( ObjectIdentifier objectId, IdChangeListener changeListener ){
        List<IdChangeListener> listeners = listenersById.get( objectId );
        listeners.remove( changeListener );
        if( listeners.size() == 0 ) listenersById.remove( objectId );

    }

    public void removeIdObject( IdObject toRemove ){
        Map<IdObjectReferenceMD, List<IdObjectReference>> references = toRemove.getReferences();
        // First remove all references from the object
        for( IdObjectReferenceMD referenceMD : references.keySet() ){
            for( IdObjectReference reference : references.get( referenceMD ) ){
                toRemove.removeReference( referenceMD, reference.getDestinationObjectId() );
            }
        }

        // ... then remove the object itself
        idObjectContainer.remove( toRemove );
        fireObjectRemoved( toRemove );
    }

}
