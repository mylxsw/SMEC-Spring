package name.orionis.helper.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate that the annotated method will be cached.
 * @author code.404
 * @2013-5-11
 * Site : http://blog.orionis.name
 * 
 * Usage : 
 * 		The returned value of method with this annotation will be cached
 * 		value : Indicate that the key will be used to cache object, by default,
 *  system will automatically generate one with md5 hashed according to 
 *  the method signature and the parameters list. If you want to rebuild 
 *  cache , you must specified one manual.
 *  
 * 		appendParam : Indicate that whether appended method parameters to the 
 * cache key, by default, system will automatically appended one. If you set this 
 * parameter to false, will not appended and meanwhile, identifyParams will be 
 * invalid. Something important , if we set the parameter to false, system will 
 * cache the method result once with the first time we access it , then when we 
 * access it, we will get the same result always. this will be unexpected result,
 * so unless the method have not parameters, do not set to false.
 * 
 * 		expiration: Indicate the method cached time. by default it is -1, will be
 * never out of date.(ms)
 *  
 * 		reload : Indicate the method is a reload method, if the value is true, 
 * generally we need set the method return type  to "void". we must set the
 * value attribute manual for reload method take effect, otherwise, an 
 * IllegalArgumentException will be thrown.
 * 
 * 		enableRegex :  related to reload attribute, invalid only when reload attribute
 * is set to true, indicate that the regular expression is supported by value attribute.
 * By default , this attribute is false, but we can simply used * as an wildcard
 * character to matching arbitrary characters.
 * 
 * 		identifyParams :  This attribute is an array of string. the value of the array 
 * will be method parameter name. the indicate method parameter name will be hashed by 
 * md5 algorithm and appended to the cache key as a suffix.If no parameters specified, 
 * will use all the parameter`s values. more important, this attribute will be invalid 
 * only when the appendParam attribute is true.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cached {
	/**
	 * The key to save or retrieve data from cache
	 * By default, system will auto-generate one according to method signature
	 * If rebuild the cache, you can use regular expression
	 * @return
	 */
	String value() default "";
	/**
	 * Whether append parameters behind the key
	 * By default it is true, and in general, we need it, so we can get different
	 * content with different parameters
	 * @return
	 */
	boolean appendParam() default true;
	/**
	 * Expiration time
	 * By default it is -1, means never be out of date.
	 * @return
	 */
	long expiration() default -1;
	
	/**
	 * Indicate that this method is used to reload specified cache.
	 * @return
	 */
	boolean reload() default false;
	/**
	 * Whether enabled using regular expression for value parameter
	 * If set to true, we can use.
	 * By default , it is false, but we can use * represent arbitary string
	 * @return
	 */
	boolean enableRegex() default false;
	/**
	 * Identified parameters, if not set , used all parameters
	 * @return
	 */
	String[] identifyParams() default {};
}
