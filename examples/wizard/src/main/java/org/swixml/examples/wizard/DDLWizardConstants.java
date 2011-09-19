/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.examples.wizard;

/**
 *
 * @author softphone
 */
public interface DDLWizardConstants {

    // PAGE1
    public String PAGE1_STEP = "jdbc";
    public String CONNECTIONURL = "connectionUrl";
    public String DRIVERCLASS = "driverClass";
    public String PASSWORD = "password";
    public String USER = "user";
    public String CREATEDB = "createDB";
    public String FROMDB = "fromDB";
    

    // PAGE2
    public String PAGE2_STEP = "schema";
    public String CONTINUEONERROR = "continueOnError";
    public String DBSCHEMA = "dbSchema";
    public String DROPTABLES = "dropTables";
    public String GENERATESQL = "generateSQL";
    public String SQLFILE = "sqlFile";
    public String DATABASE_MODEL = "databaseModel";


    // PAGE3
    public String PACKAGE_NAME = "packageName";
    public String GENERATE_BEAN = "generateBean";

}
