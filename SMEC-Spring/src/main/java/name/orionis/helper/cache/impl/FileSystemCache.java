package name.orionis.helper.cache.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import name.orionis.helper.cache.ICache;

/**
 * 基于文件系统的缓存
 * @author code.404
 *
 */
public class FileSystemCache implements ICache {
	private String suffix = "._cache";
	private String path = "C:\\";
	private boolean devMode = false;
	
	@Override
	public void put(String key, Object value, long expiration) {
		put(key, value);
	}
	@Override
	public void put(String key, Object value) {
		ObjectOutputStream oos = null;
		try{
			oos = new ObjectOutputStream(new FileOutputStream(_getFileName(key)));
			oos.writeObject(value);
		} catch(IOException e){ 
			e.printStackTrace();
		}finally{
			if(oos != null){
				try {
					oos.close();
				} catch (IOException e) {
					
				}
			}
		}
	}

	@Override
	public Object get(String key) {
		ObjectInputStream ois = null;
		Object object = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(_getFileName(key)));
			object = ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			if(ois != null){
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return object;
	}

	@Override
	public boolean isExist(String key) {
		if(devMode){
			return false;
		}
		return new File(_getFileName(key)).exists();
	}
	private String _getFileName(String key){
		return path + key + suffix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void remove(String key, boolean lazy) {
		// 实现：删除文件，暂未实现。
	}

	@Override
	public void setDevMode(boolean devMode) {
		this.devMode  = devMode;
	}

	@Override
	public boolean isSupportExpiration() {
		return false;
	}

}
