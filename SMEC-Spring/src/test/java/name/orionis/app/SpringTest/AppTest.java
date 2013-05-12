package name.orionis.app.SpringTest;

import javax.annotation.Resource;

import name.orionis.app.App;

import org.junit.Test;

public class AppTest extends SpringTest{

	@Resource
	private App app;
	
	@Test
	public void testApp() throws InterruptedException{
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
		Thread.sleep(25);
		debug( app.getCacheObject("mylxsw", "orionis") );
		Thread.sleep(30);
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
	}
	@Test
	public void testApp2(){
		debug( app.getCacheObject2("what", 5));
		debug( app.getCacheObject2("what", 5));
		debug( app.getCacheObject2("what", 6));
		debug( app.getCacheObject2("what", 6));
		
		app.clearCacheObject2("what", 5);
		
		debug( app.getCacheObject2("what", 5));
		debug( app.getCacheObject2("what", 5));
		debug( app.getCacheObject2("what", 6));
		debug( app.getCacheObject2("what", 6));
	}
	
	@Test
	public void testApp3() {
		debug( app.getCacheObject2("what", 5));
		debug( app.getCacheObject2("what", 5));
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
		
		debug("------------------------------------");
		app.clearAllCache();
		debug("------------------------------------");
		
		debug( app.getCacheObject2("what", 5));
		debug( app.getCacheObject2("what", 5));
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
		debug( app.getCacheObject("mylxsw", "orionis") );
		
	}
}
