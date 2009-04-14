/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsr296;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.application.Application;
import org.jdesktop.application.Task;


/**
 *
 * @author Sorrentino
 */
class ListFilesTask extends Task<Void, File> {
	private final File root;
	private final int bufferSize;
	private final List<File> buffer;

	public ListFilesTask(Application app, File root) {
	    super(app, "ListFilesTask");
	    this.root = root;
	    bufferSize = 10;
	    buffer = new ArrayList<File>(bufferSize);
	}
	private void expand(File root) {
	    if (isCancelled()) {
		return;
	    }
	    if (root.isDirectory()) {
		message("directoryMessage", root.toString());
		for(File file : root.listFiles()) {
		    expand(file);
		}
	    }
	    else {
		buffer.add(root);
		if (buffer.size() >= bufferSize) {
		    File bufferFiles[] = new File[buffer.size()];
		    publish(buffer.toArray(bufferFiles));
		    buffer.clear();
		}
	    }
	}
	public Void doInBackground() {
	    expand(root);
	    if (!isCancelled()) {
		File bufferFiles[] = new File[buffer.size()];
		publish(bufferFiles);
	    }
	    return null;
	}
    }
