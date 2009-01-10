#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ${package}.table;

/**
 *
 * @author sorrentino
 */
public class SimpleBean {
    private String name;
    private int age;

    public SimpleBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                    .append( "name=").append(name)
                    .append(" age=").append(age)
                    .toString();
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
