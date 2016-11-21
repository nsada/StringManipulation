package finalVersion;

import static org.junit.Assert.*;

import org.junit.Test;

public class calculationTest {

	@Test
	public void testJudgeFun() {		
		assertEquals(false,calculation.judgeFun("35^3")); //1
		assertEquals(true,calculation.judgeFun("35")); //2
		assertEquals(false,calculation.judgeFun("x^3^2")); //3
		assertEquals(true,calculation.judgeFun("x")); //4
		assertEquals(false,calculation.judgeFun("x^yyy")); //5
		assertEquals(true,calculation.judgeFun("x+yy^2*7+x*x")); //6
		assertEquals(true,calculation.judgeFun("134*x")); //7
		assertEquals(false,calculation.judgeFun("x(y")); //8
		assertEquals(false,calculation.judgeFun("")); //9
		assertEquals(false,calculation.judgeFun("*x+y")); //10
		
		assertEquals(false,calculation.judgeFun("x+yy^2^3*7+x*x"));
		assertEquals(false,calculation.judgeFun("x+yy^x^2*7+x*x"));		
		assertEquals(true,calculation.judgeFun("x+yy^2*7+x*x"));
		assertEquals(false,calculation.judgeFun("x+yy^2*7+x*x#"));
		assertEquals(false,calculation.judgeFun("x+yy^2*7+)x*x"));	
	}

}
