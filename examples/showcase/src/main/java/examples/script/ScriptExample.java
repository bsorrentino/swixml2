package examples.script;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.swixml.ApplicationPropertiesEnum;
import org.swixml.SwingEngine;
import org.swixml.jsr296.SWIXMLApplication;

public class ScriptExample extends SWIXMLApplication {

    
    
    @Override
    protected void startup() {

        try {
            JDialog dialog = render( new ScriptDialog() );

            setup( dialog );
            
            show(dialog);


        } catch (Exception e) {

            e.printStackTrace();
            exit();
        }
    }

 
    public static JDialog setup( JDialog dialog ) {
    
            final SwingEngine.Predicate addAncestorListenerPredicate =  new SwingEngine.Predicate() {

                public boolean evaluate(JComponent c) {

                    c.addAncestorListener( new AncestorListener() {

                        public void ancestorAdded(AncestorEvent ae) {
                            System.out.printf("==> ancestorAdded[%s]\n", ae.getComponent());
                        }

                        public void ancestorRemoved(AncestorEvent ae) {
                            //System.out.printf("==> ancestorRemoved[%s]\n", ae.getComponent());
                        }

                        public void ancestorMoved(AncestorEvent ae) {
                            //System.out.printf("==> ancestorMoved[%s]\n", ae.getComponent());
                        }
                    });

                    return true;
                }
            };
      
            SwingEngine.traverse( dialog, addAncestorListenerPredicate );
            
            dialog.addContainerListener(new ContainerListener() {

                public void componentAdded(ContainerEvent ce) {
                    System.out.printf("==> componentAdded[%s]\n", ce.getComponent());
                }

                public void componentRemoved(ContainerEvent ce) {
                    System.out.printf("==> componentRemoved[%s]\n", ce.getComponent());
                }
            });
            
            return dialog;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {

        ApplicationPropertiesEnum.IGNORE_RESOURCES_PREFIX.set(true);
        ApplicationPropertiesEnum.AUTO_INJECTFIELD.set(true);

        SWIXMLApplication.launch(ScriptExample.class, args);
    }
}
