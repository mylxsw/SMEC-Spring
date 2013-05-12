package name.orionis.helper.cache;
/**
 * Cache operation interface
 * @author code.404
 * @2013-5-11
 * Site : http://blog.orionis.name
 *
 */
public interface ICache {
	/**
	 * Write To Cache
	 * @param key
	 * @param value
	 */
	public void put(String key , Object value);
	/**
	 * Write To Cache With Expiration Time
	 * @param key
	 * @param value
	 * @param expiration
	 */
	public void put(String key, Object value, long expiration);
	/**
	 * Read Cache Content
	 * @param key
	 * @return
	 */
	public Object get(String key);
	/**
	 * Whether The Key Exist In Cache
	 * @param key
	 * @return
	 */
	public boolean isExist(String key);
	/**
	 * Remove Cache
	 * @param key
	 * @param lazy
	 */
	public void remove(String key, boolean lazy);
	
	/**
	 * Whether Enable Develop Mode
	 * @param devMode
	 */
	public void setDevMode(boolean devMode);
	/**
	 * Whether Support Expiration Time
	 * @return
	 */
	public boolean isSupportExpiration();
}
