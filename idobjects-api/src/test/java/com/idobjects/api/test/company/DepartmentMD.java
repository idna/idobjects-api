package com.idobjects.api.test.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.idobjects.api.md.IdObjectMD;
import com.idobjects.api.md.IdObjectPropertyMD;
import com.idobjects.api.md.IdObjectReferenceMD;
import com.idobjects.api.md.ReferenceType;

public class DepartmentMD extends IdObjectMD{

    public static final IdObjectPropertyMD NAME = new IdObjectPropertyMD( "name", String.class );

    public static final IdObjectReferenceMD CHIEF = new IdObjectReferenceMD( DepartmentMD.class, EmployeeMD.class, "chief", null, false, ReferenceType.SINGLE,
            CompanyModelMetada.class );
    public static final IdObjectReferenceMD MEMBERS = new IdObjectReferenceMD( DepartmentMD.class, EmployeeMD.class, "members", "department", true, ReferenceType.LIST,
            CompanyModelMetada.class );

    public static final List<IdObjectPropertyMD> PROPERTIES;
    public static final List<IdObjectReferenceMD> REFERENCES;

    static{
        List<IdObjectPropertyMD> properties = new ArrayList<IdObjectPropertyMD>();
        properties.add( NAME );
        PROPERTIES = Collections.unmodifiableList( properties );

        List<IdObjectReferenceMD> references = new ArrayList<IdObjectReferenceMD>();
        references.add( CHIEF );
        references.add( MEMBERS );
        REFERENCES = Collections.unmodifiableList( references );
    }

    private static final DepartmentMD instance = new DepartmentMD();

    public static final DepartmentMD instance(){
        return instance;
    }

    private DepartmentMD(){
        super( DepartmentMD.class.getName(), PROPERTIES, REFERENCES );
    }

}
