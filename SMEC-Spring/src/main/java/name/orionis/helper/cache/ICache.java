package name.orionis.helper.cache;
/**
 * 缓存操作接口
 * @author code.404
 * @2013-5-11
 * Site : http://blog.orionis.name
 *
 */
public interface ICache {
	/**
	 * 缓存写入
	 * @param key
	 * @param value
	 */
	public void put(String key , Object value);
	/**
	 * 缓存写入，带缓存时间
	 * @param key
	 * @param value
	 * @param expiration
	 */
	public void put(String key, Object value, long expiration);
	/**
	 * 读取缓存内容
	 * @param key
	 * @return
	 */
	public Object get(String key);
	/**
	 * 是否存在key的缓存
	 * @param key
	 * @return
	 */
	public boolean isExist(String key);
	/**
	 * 移除缓存
	 * @param key
	 * @param lazy
	 */
	public void remove(String key, boolean lazy);
	
	/**
	 * 是否是开发模式
	 * 开发模式缓存均无效，方便调试
	 * @param devMode
	 */
	public void setDevMode(boolean devMode);
	/**
	 * 是否支持过期时间
	 * @return
	 */
	public boolean isSupportExpiration();
}
