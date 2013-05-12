package name.orionis.helper.cache;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现基于annotation的缓存
 * @author code.404
 * @2013-5-11
 * Site : http://blog.orionis.name
 *
 */
@Aspect
public class CacheAspect {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	private ICache cache;
	
	@Around("cached()")
	public Object cacheMethod(ProceedingJoinPoint pjp) throws Throwable{
		
		// 当前访问方法的所有参数名称
		String[] parameterNames = ((MethodSignature) pjp.getSignature()).getParameterNames();
		
		
		// 方法上的Cached注解，以及注解的值
		Cached cachedAnnotation = pjp.getTarget().getClass()
				.getMethod(pjp.getSignature().getName(),
						((MethodSignature)pjp.getSignature()).getParameterTypes())
						.getAnnotation(Cached.class);
		// 自定义主键
		String key = cachedAnnotation.value();
		
		/**
		 * 创建参数字符串
		 * 首先，判断是否是需要附加参数，如果不需要，则该步骤可忽略
		 * 第二，判断当前方法是否含有参数列表，如果没有，则该步骤忽略
		 * 第三，判断是否明确指定了需要的附加参数，如果指定了，则使用指定的，
		 * 否则，使用所有的参数
		 */
		String _paramsString = "";
		if(cachedAnnotation.appendParam() && parameterNames.length != 0 ){
			// 定义的标识方法
			String[] identifyParams = cachedAnnotation.identifyParams();
			// 本次访问方法传递的参数值列表
			Object[] args = pjp.getArgs();
			
			if(identifyParams.length != 0){// 已指定标识参数
				for(int i = 0; i < identifyParams.length; i ++){
					int index = ArrayUtils.indexOf(parameterNames, identifyParams[i]);
					if(index == -1){
						continue;
					}
					_paramsString += args[index];
				}
			}else{// 未指定标识参数
				for(Object o : args){
					_paramsString += o.toString();
				}
			}
		}

		/**
		 * 生成缓存主键
		 * 如果没有指定key，则使用自动生成的key，最终key会被生成md5散列存储
		 * 如果指定了key，则会直接使用key并追加参数
		 */
		if(key == null || "".equals(key)){
			key = pjp.getStaticPart().toString();
			// 生成key的md5散列
			key = DigestUtils.md5Hex(key + _paramsString);
		} else {
			// 追加参数到key
			if(!"".equals(_paramsString))
				key += DigestUtils.md5Hex(_paramsString);
		}
		
		/**
		 * 判断当前执行的操作是否是重载缓存，如果是，则直接移除
		 * 指定key对应的缓存，在下次访问相应方法时，会自动重建缓存
		 * 支持正则表达式，必须手动指定value(key)
		 */
		if(cachedAnnotation.reload()){
			if(key == null || "".equals(key)){
				log.error("没有指定重载缓存的主键!");
				throw new IllegalArgumentException("没有指定重载缓存的主键");
			}
			if(!cachedAnnotation.enableRegex())
				key = key.replaceAll("\\*", "[\\\\w\\\\W]*");
			cache.remove(key, false);
			return pjp.proceed();
		}
		
		/**
		 * 执行对象缓存
		 * 如果存在缓存，则直接返回
		 * 否则先缓存，再从缓存获取值
		 */
		if(!cache.isExist(key)){
			Object proceed = pjp.proceed();
			// 缓存过期时间
			long expiration = cachedAnnotation.expiration();
			cache.put(key, proceed, expiration);
		}
		return cache.get(key);
	}
	
	@Pointcut("@annotation(name.orionis.helper.cache.Cached)")
	public void cached(){}
	
	public ICache getCache() {
		return cache;
	}

	public void setCache(ICache cache) {
		this.cache = cache;
	}
}
