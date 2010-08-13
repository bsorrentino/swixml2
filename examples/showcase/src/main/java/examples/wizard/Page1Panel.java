/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples.wizard;

import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jdesktop.application.Action;
import org.netbeans.spi.wizard.WizardController;


/**
 *
 * @author sorrentino
 */
public class Page1Panel extends JPanel {

    final WizardController wizardController;
    final Map<String,Object> parameters ;

    public JCheckBox cb;

    public Page1Panel( WizardController wizardController, Map<?,?> parameters ) {
        this.wizardController = wizardController;
        this.parameters = (Map<String, Object>) parameters;
    }

    @Action()
    public void cbSelect() {
           boolean sel = cb.isSelected();
           wizardController.setProblem( sel ? null : "please select the checkbox");
           parameters.put("boxSelected",  sel ? Boolean.TRUE : Boolean.FALSE);
    }

    @Override
    public void addNotify() {
        super.addNotify();

        cbSelect();
    }


}
