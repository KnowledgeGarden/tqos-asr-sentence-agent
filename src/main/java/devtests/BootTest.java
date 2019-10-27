/**
 * 
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
