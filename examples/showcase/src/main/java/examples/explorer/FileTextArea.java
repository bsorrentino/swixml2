package examples.explorer;

import static org.swixml.LogUtil.logger;

import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

@SuppressWarnings("serial")
public class FileTextArea extends RSyntaxTextArea {

	
	private String source;

	public FileTextArea() {
		super();
		setFont( new Font("Courier New",Font.PLAIN, 13));
	}

	public final String getSource() {
		return source;
	}

	public final void setSource(String source) {
		this.source = source;
	}
	
	@Override
	public void addNotify() {
		if( source != null ) {
			java.io.InputStream is = FileTextArea.class.getClassLoader().getResourceAsStream(source);
			try {
				read( new java.io.InputStreamReader(is), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.setEditable(false);
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
