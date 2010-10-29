package org.swixml.contrib;

import org.fife.ui.rtextarea.RTextScrollPane;
import org.swixml.TagLibrary;
import org.swixml.TagLibraryService;

public class TagLibraryProvider implements TagLibraryService {

	public void registerTags(TagLibrary library) {
        library.registerTag("menubutton", JSimpleMenuButton.class);
        library.registerTag("animatedbutton", JAnimatedButton.class);
        
        library.registerTag("RTextScrollPane", RTextScrollPane.class);     
        library.registerTag("RSyntaxTextArea", RSyntaxTextAreaEx.class);
	}

}
