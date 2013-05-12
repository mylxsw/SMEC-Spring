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
 * Usage：
 * 		使用该annotation标注的方法的返回值会被缓存。
 * 		value: 指定了缓存使用的key，默认程序会根据方法签名以及参数列表的取值
 * 	自动生成MD5散列加密的key，如果需要使用重载缓存功能的话，必须手动指定一个
 *  value。
 * 		appendParam: 标注了是否是在缓存key上增加方法参数，默认情况下是自动追
 *  加的，如果该参数设为false，则不会追加，同时identifyParams参数无效。需要
 *  注意的是，如果该参数设为了false，则只会对方法缓存一次，随后不管参数是什么
 *  ，都是返回相同结果，这就可能造成非预期的效果，因此，除非缓存方法没有参数
 *  列表，不要指定该参数为false。
 * 		expiraction: 参数标注了该方法的缓存时间，默认值是-1，永不过期，缓存
 * 时间单位为毫秒。
 * 		reload标注了该方法为重载缓存方法，该参数开启的情况下，需要标注的方法
 * 返回值为void，并且没有方法参数。要该reload参数生效的话，必须自定义value属
 * 性，否则抛出异常。同时，该方法标注的方法一般方法体为空，不需要增加额外的处
 * 理逻辑。
 * 		enableRegex: 参数与reload参数相关，只在reload为true的情况下有效，表
 * 明value属性所指的key是支持正则表达式的，该参数默认值为false，不支持正则表
 * 达式，但是可以使用*作为通配符匹配任意字符。
 * 		identifyParams: 该参数是一个字符数组，取值应为方法参数的参数名，用于
 * 标识该方法的缓存key追加指定参数的值哈希值作为后缀，如果没有指定参数列表的话
 * ，则使用所有参数的值得哈希值追加。
 *  注意的是，该参数仅在appendParam参数为true的情况下可用。
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
