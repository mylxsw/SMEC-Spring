package name.orionis.app;

import name.orionis.helper.cache.Cached;

import org.springframework.stereotype.Component;


@Component
public class App {
	@Cached
	public String getCacheObject(String key, String key2){
		System.out.println("正在生成结果...");
		return key + ":" +  key2;
	}
}
