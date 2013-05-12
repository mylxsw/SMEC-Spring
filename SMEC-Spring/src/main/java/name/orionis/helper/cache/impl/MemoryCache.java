package name.orionis.helper.cache.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import name.orionis.helper.cache.ICache;

/**
 * Memory based cache
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
		
		// Develop mode
		if(devMode){
			return false;
		}
		
		// If not contained the key, return false
		if(!map.containsKey(key)){
			return false;
		}
		// Read cached object, and judge if out of date
		CacheObject cObj = (CacheObject) map.get(key);
		// If expiration time is -1, means never be out of date
		if(cObj.expiration == -1){
			return true;
		}
		// If cached object out of date, we need clear the cache related to the key
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
				
				// Search for keys we want to delete
				Set<String> keySet = map.keySet();
				for(String k : keySet){
					if(k.matches(key)){
						deleteKeys.add(k);
					}
				}
				// Execute the delete operation对
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
