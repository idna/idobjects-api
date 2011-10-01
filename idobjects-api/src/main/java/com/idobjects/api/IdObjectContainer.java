package com.idobjects.api;

import java.util.Collection;


public interface IdObjectContainer{
    
    void add(IdObject idObject);
    
    void remove(IdObject idObject);
    
    boolean contains(ObjectIdentifier objectIdentifier);
    
    IdObject get(ObjectIdentifier objectIdentifier);
    
    void idChanged( IdObject idObject, ObjectIdentifier oldId, ObjectIdentifier newId );
    
    int size();
    
    void changeId( ObjectIdentifier oldId, ObjectIdentifier newId, IdObject idObject);
    
    
    
    Collection<IdObject> elements();

}
