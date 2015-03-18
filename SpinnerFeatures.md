# Introduction #

Spinner control is a very powerful component. SWIXML2 allow to use it in a very easy way, minimizing the Swing code in your application. Below a complete "HOW TO"
This code could be checkout  from svn : http://swixml2.googlecode.com/svn/trunk/examples the package is **examples.spinner** (use the last swixml2 SNAPSHOT)


### SWIXML SOURCE (examples/spinner/SpinnerDialog.xml) ###
```
<?xml version="1.0" encoding="windows-1252"?>

<dialog 
prefferdSize="500,500"
resizable="false" title="Spinner Dialog"
defaultCloseOperation="JDialog.DISPOSE_ON_CLOSE">

<vbox>

    <panel  layout="GridLayout(3,2,1,10)" border="CompoundBorder(EmptyBorder(10,1,10,10), TitledBorder(Spinners))">

        <label text="Spinner Date : " />
        <spinner.date id="spinner" bindWith="dateValue" model="${spinnerDateModel}" dateFormat="dd/MM/yyyy hh:mm" />

        <label text="Spinner (default)" />
        <spinner  bindWith="numberValue"  />

        <label text="Spinner list " />
        <spinner  bindWith="stringValue" model="${spinnerListModel}" />

    </panel>

   	<box.vstrut height="10"/>

    <hbox>
    	<button text="Test" action="test"/>
    	<box.glue/>
    	<button text="Submit" action="submit"/>
    	<box.hstrut width="5"/>
    	<button text="Close" action="close"/>
    </hbox>
    
</vbox>

</dialog>

```

### JAVA CODE ###

```
/**
 *
 * @author bsorrentino
 */
public class SpinnerSampleApplication extends SwingApplication {

    public static void main(String args []) {
            SwingApplication.launch(SpinnerSampleApplication.class, args);
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
            firePropertyChange("spinnerValue", null,null);
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

```