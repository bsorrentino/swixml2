/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples.spinner;

import static org.swixml.LogUtil.logger;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

import org.jdesktop.application.Action;
import org.swixml.jsr296.SWIXMLApplication;
/**
 *
 * @author bsorrentino
 */
public class SpinnerExample extends SWIXMLApplication {

    public static void main(String args []) {
            SWIXMLApplication.launch(SpinnerExample.class, args);
    }


    public class SpinnerDialog extends JDialog {

        JSpinner spinner;

        Calendar calendar = Calendar.getInstance();


        Date dateValue = new Date();
        int numberValue = 5;
        String stringValue;;

        List<String> months = Arrays.asList(getMonthStrings());

        /**
         * model returned from ${spinnerListModel} evaluation
         *
         * @return
         */
        public SpinnerModel getSpinnerListModel() {
            return new SpinnerListModel(months);
        }

        /**
         * model returned from ${spinnerDateModel} evaluation
         *
         * @return
         */
        public SpinnerModel getSpinnerDateModel() {
            Date initDate = calendar.getTime();
            calendar.add(Calendar.YEAR, -100);
            Date earliestDate = calendar.getTime();
            calendar.add(Calendar.YEAR, 200);
            Date latestDate = calendar.getTime();
            SpinnerModel dateModel = new SpinnerDateModel(initDate,
                                         earliestDate,
                                         latestDate,
                                         Calendar.YEAR);//ignored for user input
            return dateModel;
        }

        public SpinnerDialog() {
            //
            // Init spinner list
            //
            stringValue = months.get(3);
        }


        /**
         *
         * @return
         */
        public Date getDateValue() {
            return dateValue;
        }

        /**
         *
         * @param date
         */
        public void setDateValue(Date date) {
            logger.info( "setDate " + date );
            this.dateValue = date;
            firePropertyChange("dateValue", null,null);
        }

        /**
         *
         * @return
         */
        public int getNumberValue() {
            return numberValue;
        }

        /**
         *
         * @param value
         */
        public void setNumberValue(int value) {

            this.numberValue = value;
        }

        /**
         *
         * @return
         */
        public String getStringValue() {
            return stringValue;
        }

        /**
         *
         * @param value
         */
        public void setStringValue(String value) {
            this.stringValue = value;
        }

        @Action
        public void submit() {
            try {
                spinner.commitEdit();
                logger.info("value is valid");
            } catch (ParseException ex) {
                logger.severe(ex.getMessage());
            }
        }
        
        @Action
        public void test() {
            calendar.setTime(getDateValue());
            calendar.set( Calendar.HOUR, 16);
            calendar.set( Calendar.MINUTE, 01);
            Date dt = calendar.getTime();
            setDateValue(dt);

        }

        @Action
        public void close() {
            dispose();
        }
    }

    /**
     * DateFormatSymbols returns an extra, empty value at the
     * end of the array of months.  Remove it.
     */
    static protected String[] getMonthStrings() {
        String[] months = new java.text.DateFormatSymbols().getMonths();
        int lastIndex = months.length - 1;

        if (months[lastIndex] == null
           || months[lastIndex].length() <= 0) { //last item empty
            String[] monthStrings = new String[lastIndex];
            System.arraycopy(months, 0,
                             monthStrings, 0, lastIndex);
            return monthStrings;
        } else { //last item not empty
            return months;
        }
    }

    @Override
    protected void startup() {

        try {

                JDialog dialog = render( new SpinnerDialog(), "examples/spinner/SpinnerDialog.xml");

                show( dialog );

        } catch (Exception e) {

                e.printStackTrace();
                exit();
        }
    }

}
