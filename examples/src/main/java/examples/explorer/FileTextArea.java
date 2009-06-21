package examples.explorer;

import java.io.IOException;
import java.util.logging.Level;

import javax.swing.JTextArea;
import static org.swixml.LogUtil.logger;

@SuppressWarnings("serial")
public class FileTextArea extends JTextArea {

	
	private String source;

	public final String getSource() {
		return source;
	}

	public final void setSource(String source) {
		this.source = source;
	}
	
	@Override
	public void addNotify() {
		if( source != null ) {
			super.setText( loadResource(source) );
		}
		super.addNotify();
	}
	
	public static String loadResource( String res ) {
		if( res==null ) return null;
		
		java.io.InputStream is = FileTextArea.class.getClassLoader().getResourceAsStream(res);
		if( is==null ) {
			logger.warning( String.format("resource [%s] not found!", res));
		}
		else {
			java.io.StringWriter w = new java.io.StringWriter(4*1024);
			int c;
			try {
				while( -1!=(c=is.read()) ) {
					w.write(c);
				}
				w.flush();
				return w.toString();
			} catch (IOException e) {
				logger.log( Level.WARNING, "error reading resource", e );
			}
		}
		return null;
	}
	
}
