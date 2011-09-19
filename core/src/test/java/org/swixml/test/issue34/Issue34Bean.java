/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.test.issue34;

/**
 *
 * @author softphone
 */
public class Issue34Bean {

    private String description;
    private String dn;

    public Issue34Bean() {
    }

    public Issue34Bean(String description, String dn) {
        this.description = description;
        this.dn = dn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }


}
