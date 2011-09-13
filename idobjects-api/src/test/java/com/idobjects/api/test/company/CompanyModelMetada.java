package com.idobjects.api.test.company;

import com.idobjects.api.md.ModelMetadata;

public final class CompanyModelMetada extends ModelMetadata{

    public static final EmployeeMD EMPLOYEE = EmployeeMD.instance();
    public static final DepartmentMD DEPARTMENT = DepartmentMD.instance();

    private static final CompanyModelMetada instance = new CompanyModelMetada();

    private CompanyModelMetada(){
        addIdObject( EMPLOYEE );
        addIdObject( DEPARTMENT );
    }

    public static CompanyModelMetada instance(){
        return instance;
    }

}
