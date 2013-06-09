
package org.moovsimply.api.common;

/**
 * Define a REST API Exception
 * @author namrouche
 *
 */
public class RestAPIException extends Exception {
	private static final long serialVersionUID = 1L;

	public RestAPIException(String message) {
		super(message);
	}

	public RestAPIException(Exception e) {
		super(e.getMessage());
	}

}
