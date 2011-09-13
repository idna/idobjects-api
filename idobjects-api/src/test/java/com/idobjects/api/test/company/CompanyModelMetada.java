package com.idobjects.api.test.company;

import java.util.ArrayList;
import java.util.List;

import com.idobjects.api.IdObjectReference;
import com.idobjects.api.md.IdObjectMD;
import com.idobjects.api.md.IdObjectPropertyMD;
import com.idobjects.api.md.IdObjectReferenceMD;
import com.idobjects.api.md.ModelMetadata;

public class CompanyModelMetada extends ModelMetadata{

    public CompanyModelMetada(){
        IdObjectPropertyMD employeeFirstName = new IdObjectPropertyMD( "firstName", String.class );
        IdObjectPropertyMD employeeLastName = new IdObjectPropertyMD( "lastName", String.class );
        
        
        
        List<IdObjectPropertyMD> properties = new ArrayList<IdObjectPropertyMD>();
        properties.add( employeeFirstName );
        properties.add( employeeLastName );
        
        List<IdObjectReferenceMD> references = new ArrayList<IdObjectReferenceMD>();
        
        IdObjectMD employeeMD = new IdObjectMD( "Employee.class.getName()", properties, references );
    }
    
    

}
