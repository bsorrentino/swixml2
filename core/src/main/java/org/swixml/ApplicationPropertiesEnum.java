package org.swixml;

import org.jdesktop.application.Application;

public enum ApplicationPropertiesEnum {
    AUTO_INJECTFIELD( "injectFields" ),
    IGNORE_RESOURCES_PREFIX("ignore.resources.prefix"),
    USE_COMMON_LOCALIZER("use.common.localizer")
    ;
    
    String value;
    
    ApplicationPropertiesEnum( String value ) {
        this.value = Application.class.getName()
                .concat(".")
                .concat(value);
    }
    
    public boolean getBoolean() {
        return Boolean.getBoolean(this.value);
    }
    
    public void set( boolean value ) {
        System.setProperty( this.value, Boolean.toString(value) );
    }
}
