package com.idobjects.api;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultIdObjectContainer implements IdObjectContainer{

    private final Map<ObjectIdentifier, IdObject> idObjects = new LinkedHashMap<ObjectIdentifier, IdObject>();

    @Override
    public void add( IdObject idObject ){
        idObjects.put( idObject.getId(), idObject );
    }

    @Override
    public void remove( IdObject idObject ){
        idObjects.remove( idObject.getId() );
    }

    @Override
    public boolean contains( ObjectIdentifier objectIdentifier ){
        return idObjects.containsKey( objectIdentifier );
    }

    @Override
    public IdObject get( ObjectIdentifier objectIdentifier ){
        return idObjects.get( objectIdentifier );
    }

    @Override
    public void idChanged( IdObject idObject, ObjectIdentifier oldId, ObjectIdentifier newId ){
        idObjects.remove( oldId );
        idObjects.put( newId, idObject );
    }

    @Override
    public int size(){
        return idObjects.size();
    }

    @Override
    public void changeId( ObjectIdentifier oldId, ObjectIdentifier newId, IdObject idObject ){
        idObjects.remove( oldId );
        idObjects.put( newId, idObject );

    }

    @Override
    public Collection<IdObject> elements(){
        return Collections.unmodifiableCollection( idObjects.values() );
    }

}
