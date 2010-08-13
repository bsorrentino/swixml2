import java.awt.Color;
import java.awt.Font;
import java.util.TimeZone;

import javax.swing.JLabel;



public class RedLabel extends JLabel {
  public RedLabel() {
    this.setForeground( Color.red );
    this.setFont( Font.decode( "VERDANA-BOLD-24" ) );
  }

  public void setTimeZone(TimeZone tz) {

    this.setText( tz.getDisplayName() );
  }
}
