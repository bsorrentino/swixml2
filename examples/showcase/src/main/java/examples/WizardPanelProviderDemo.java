/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.Summary;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardController;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPanelProvider;
import org.swixml.jsr296.SwingApplication;

import examples.wizard.Page1Panel;

public class WizardPanelProviderDemo extends SwingApplication {

   private static final String ID = "page1-id";

   public WizardPanelProviderDemo() {
 }

   protected Object finish(Map settings) throws WizardException {
      StringBuilder sb = new StringBuilder();

       Set keys = settings.keySet();
      Iterator it = keys.iterator();
      while (it.hasNext()) {
         Object key = it.next();
         sb.append( key ).append("=").append(settings.get(key));
      }
      return Summary.create(sb.toString(), settings);
   }

   public static void main(String [] args) {
		SwingApplication.launch(WizardPanelProviderDemo.class, args);
   }

    @Override
    protected void startup() {

       WizardPanelProvider provider = new  WizardPanelProvider( "A Demo Wizard", new String [] {ID}, new String [] {"Page #1"}) {

            @Override
            protected JComponent createPanel(WizardController controller, String id, Map parameters) {
                try {
                    return render(new Page1Panel(controller, parameters), "examples/wizard/page1.xml");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            @Override
            public boolean cancel(Map settings) {
                return true;
            }

            @Override
            protected Object finish(Map settings) throws WizardException {
                return WizardPanelProviderDemo.this.finish(settings);
            }


       };

      Wizard wizard = provider.createWizard();
      Object result = WizardDisplayer.showWizard(wizard);
    }
}

