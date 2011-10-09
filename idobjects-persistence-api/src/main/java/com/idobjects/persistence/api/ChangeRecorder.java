package com.idobjects.persistence.api;

import java.util.ArrayList;
import java.util.List;

import com.idobjects.api.IdObject;
import com.idobjects.api.IdObjectReference;
import com.idobjects.api.ModelScope;
import com.idobjects.api.ModelScopeListener;
import com.idobjects.api.ObjectIdentifier;

public class ChangeRecorder{

    private final ModelScope modelScope;
    private final ChangeListener changeListener = new ChangeListener();

    private final List<ObjectIdentifier> changedObjects = new ArrayList<ObjectIdentifier>();
    private final List<ObjectIdentifier> newObjects = new ArrayList<ObjectIdentifier>();
    private final List<ObjectIdentifier> removedObjects = new ArrayList<ObjectIdentifier>();
    private final List<IdObjectReference> newReferences = new ArrayList<IdObjectReference>();
    private final List<IdObjectReference> removedReferences = new ArrayList<IdObjectReference>();

    public ChangeRecorder( ModelScope modelScope ){
        this.modelScope = modelScope;
        modelScope.addModelScopeListener( changeListener );
    }

    public void reset(){
        changedObjects.clear();
        newObjects.clear();
        removedObjects.clear();
        newReferences.clear();
        removedReferences.clear();

    }

    public void delete(){
        modelScope.removeModelScopeListener( changeListener );
    }

    private class ChangeListener implements ModelScopeListener{

        public void objectAdded( IdObject newObject ){
            // TODO Auto-generated method stub

        }

        public void objectChanged( IdObject changedObject ){
            // TODO Auto-generated method stub

        }

        public void objectRemoved( IdObject removedObject ){
            // TODO Auto-generated method stub

        }

        public void referenceAdded( IdObjectReference newReference ){
            // TODO Auto-generated method stub

        }

        public void referenceRemoved( IdObjectReference removedReference ){
            // TODO Auto-generated method stub

        }

    }

}
