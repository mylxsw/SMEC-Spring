package name.orionis.app.SpringTest;

import javax.annotation.Resource;

import name.orionis.app.App;

import org.junit.Test;

public class AppTest extends SpringTest{

	@Resource
	private App app;
	
	@Test
	public void testApp(){
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
	}
}
