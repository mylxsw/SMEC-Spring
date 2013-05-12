package name.orionis.helper.cache.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import name.orionis.helper.cache.ICache;

/**
 * 基于内存的缓存
 * @author code.404
 *
 */
public class MemoryCache implements ICache {
	
	Map<String, Object> map = new HashMap<String, Object>();
	private boolean devMode = false;
	private boolean isSupportExpiration = true;
	
	@Override
	public void put(String key, Object value) {
		put(key, value, -1);
	}
	
	@Override
	public void put(String key, Object value, long expiration) {
		CacheObject cObj = new CacheObject();
		cObj.expiration = expiration;
		cObj.object = value;
		cObj.createTime = System.currentTimeMillis();
		
		map.put(key, cObj);
	}
	@Override
	public Object get(String key) {
		CacheObject cObj = (CacheObject) map.get(key);
		return cObj.object;
	}

	@Override
	synchronized public boolean isExist(String key) {
		
		// 开发模式
		if(devMode){
			return false;
		}
		
		// 如果不包含key，则直接false
		if(!map.containsKey(key)){
			return false;
		}
		// 读取缓存对象，判断是否超时
		CacheObject cObj = (CacheObject) map.get(key);
		// 超时时间-1则表明永不超时
		if(cObj.expiration == -1){
			return true;
		}
		// 判断是否超时，超时则清理key
		long curT = System.currentTimeMillis();
		if(curT - cObj.createTime  >= cObj.expiration){
			map.remove(key);
			return false;
		}
		return true;
	}

	@Override
	public void remove(String key, boolean lazy) {
		synchronized (map) {
			if(lazy){
				if(map.containsKey(key)){
					map.remove(key);
				}
			}else{
				List<String> deleteKeys = new ArrayList<String>();
				
				//先查找出需要删除的key列表
				Set<String> keySet = map.keySet();
				for(String k : keySet){
					if(k.matches(key)){
						deleteKeys.add(k);
					}
				}
				//对需要删除的key进行操作
				for(String deleteKey: deleteKeys){
					map.remove(deleteKey);
				}
			}
		}
	}

	@Override
	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}

	@Override
	public boolean isSupportExpiration() {
		return isSupportExpiration;
	}

	public class CacheObject {
		public long expiration;
		public long createTime;
		public Object object;
	}

}
