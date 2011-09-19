package org.swixml.contrib.gmap;

public class Validate {

	protected Validate() {
		super();
	}

	public static void notNull( Object o, String message ) {
		 if( o==null ) throw new IllegalArgumentException(message);
	}

	public static void notEmpty( String o, String message ) {
		 if( o==null || o.isEmpty()) throw new IllegalArgumentException(message);
	}
}
