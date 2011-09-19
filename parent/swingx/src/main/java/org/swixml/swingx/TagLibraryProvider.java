package org.swixml.swingx;

import org.swixml.TagLibrary;
import org.swixml.TagLibraryService;

public class TagLibraryProvider implements TagLibraryService {

	public void registerTags(TagLibrary library) {

            library.registerTag("JXTaskPaneContainer", org.jdesktop.swingx.JXTaskPaneContainer.class);
            library.registerTag("JXTaskPane", org.jdesktop.swingx.JXTaskPane.class);
	}

}
