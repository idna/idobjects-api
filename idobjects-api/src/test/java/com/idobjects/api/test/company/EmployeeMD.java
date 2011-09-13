package com.idobjects.api.test.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.idobjects.api.md.IdObjectMD;
import com.idobjects.api.md.IdObjectPropertyMD;
import com.idobjects.api.md.IdObjectReferenceMD;

public final class EmployeeMD extends IdObjectMD{

    public static final IdObjectPropertyMD FIRST_NAME = new IdObjectPropertyMD( "firstName", String.class );
    public static final IdObjectPropertyMD LAST_NAME = new IdObjectPropertyMD( "lastName", String.class );

    public static final IdObjectReferenceMD DEPARTMENT = new IdObjectReferenceMD( EmployeeMD.class, DepartmentMD.class, "department","members", true );

    public static final List<IdObjectPropertyMD> PROPERTIES;
    public static final List<IdObjectReferenceMD> REFERENCES;

    static{
        List<IdObjectPropertyMD> properties = new ArrayList<IdObjectPropertyMD>();
        properties.add( FIRST_NAME );
        properties.add( LAST_NAME );
        PROPERTIES = Collections.unmodifiableList( properties );

        List<IdObjectReferenceMD> references = new ArrayList<IdObjectReferenceMD>();
        references.add( DEPARTMENT );
        REFERENCES = Collections.unmodifiableList( references );
    }

    private static final EmployeeMD instance = new EmployeeMD();

    public static final EmployeeMD instance(){
        return instance;
    }

    private EmployeeMD(){
        super( Employee.class.getName(), PROPERTIES, REFERENCES );
    }

}
