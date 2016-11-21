package finalVersion;

import static org.junit.Assert.*;
import org.junit.Test;

public class calculationTest {
	@Test
	public void testSimplify() {
		assertEquals("error",calculation.simplify("!simplify x=-1", "2*x*x+y*y*y+3*z"));
	}
}
