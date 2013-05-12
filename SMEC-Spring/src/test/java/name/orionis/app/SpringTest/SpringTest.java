package name.orionis.app.SpringTest;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/applicationContext-*.xml"})
public abstract class SpringTest {
	public void debug(Object o	){
		System.out.println(o.toString());
	}
}
