/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package devtests;

/**
 * @author jackpark
 *
 */
public class BootTest extends TestRoot {

	/**
	 * 
	 */
	public BootTest() {
		super();
		// just boot the topicmap
		environment.shutDown();
		System.exit(0);
	}

}
