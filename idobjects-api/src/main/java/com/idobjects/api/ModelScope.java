package com.idobjects.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModelScope{

    private final ModelScopeIdentifier modelScopeId;

    private final IdObjectContainer idObjectContainer;

    private final Map<ObjectIdentifier, List<IdChangeListener>> listenersById = new LinkedHashMap<ObjectIdentifier, List<IdChangeListener>>();

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

    public boolean containsObject( ObjectIdentifier objectId ){
        return idObjectContainer.contains( objectId );
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
        idObjectContainer.add( idObject );
    }

    void idChanged( ObjectIdentifier oldId, ObjectIdentifier newId, IdObject idObject ){
        if( oldId.equals( newId ) ) return;
        if( idObjectContainer.contains( newId ) ){ throw new IdObjectException( "Duplicate object with id " + newId ); }
        fireIdChanged( oldId, newId );
    }

    private void fireIdChanged( ObjectIdentifier oldId, ObjectIdentifier newId ){
        if( !listenersById.containsKey( oldId ) ) return;
        listenersById.put( newId, listenersById.get( oldId ) );
        listenersById.remove( oldId );

        for( IdChangeListener listener : listenersById.get( newId ) ){
            listener.idChanged( oldId, newId );
        }

    }

    static interface IdChangeListener{

        void idChanged( ObjectIdentifier oldId, ObjectIdentifier newId );
    }

    void removeChangeListener( ObjectIdentifier objectId, IdChangeListener changeListener ){
        List<IdChangeListener> listeners = listenersById.get( objectId );
        listeners.remove( changeListener );
        if(listeners.size() == 0) listenersById.remove( objectId );
        
    }

}
