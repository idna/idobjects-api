package com.idobjects.api;

public interface ModelScopeListener{

    void objectAdded( IdObject newObject );

    void objectChanged( IdObject changedObject );

    void objectRemoved( IdObject removedObject );

    void referenceAdded( IdObjectReference newReference );

    void referenceRemoved( IdObjectReference removedReference );

}
