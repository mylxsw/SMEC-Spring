package name.orionis.app;

import java.util.HashMap;
import java.util.Map;

import name.orionis.helper.cache.Cached;

import org.springframework.stereotype.Component;


@Component
public class App {
	@Cached(value="cache_demo", expiration=30)
	public String getCacheObject(String key, String key2){
		System.out.println("正在生成结果...");
		return key + ":" +  key2;
	}
	
	@Cached(value="cache_demo_clear")
	public String getCacheObject2(String key1, int key2){
		System.out.println("正在生成结果...");
		return "你好啊";
	}
	@Cached(value="cache_demo_clear", reload=true)
	public void clearCacheObject2(String key1, int key2){
		System.out.println("清理缓存...");
	}
	
	@Cached(value="cache_demo_3", appendParam=false)
	public Map<String , String> getCacheObject3(){
		System.out.println("正在生成结果...");
		return new HashMap<String ,String>();
	}
	
	
	@Cached(value="cache_demo_4", identifyParams={"key2"})
	public String getCacheObject4(String key1, String key2 ,String key3){
		System.out.println("正在生成结果...");
		return key1 + key2 + key3;
	}
	
	@Cached(value="cache_demo*", reload=true)
	public void clearAllCache(){
		System.out.println("清理全部缓存...");
	}
	
}
