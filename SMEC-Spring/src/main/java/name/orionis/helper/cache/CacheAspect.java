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
 * Cache Implementation Aspect
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
		
		// The parameter`s names of current access method
		String[] parameterNames = ((MethodSignature) pjp.getSignature()).getParameterNames();
		
		
		// Get the @Cached annotation in method
		Cached cachedAnnotation = pjp.getTarget().getClass()
				.getMethod(pjp.getSignature().getName(),
						((MethodSignature)pjp.getSignature()).getParameterTypes())
						.getAnnotation(Cached.class);
		// User-defined cache key
		String key = cachedAnnotation.value();
		
		/**
		 * Create an string consist of parameters
		 * First , we need to judge the addon parameters, if need not , skip
		 * Secondly, We need to judge if the method has a parameters list, if not , skip
		 * Thirdly, Judged if there explicit specified need addon parameter, if specified,
		 * use it, otherwise, used all parameters
		 */
		String _paramsString = "";
		if(cachedAnnotation.appendParam() && parameterNames.length != 0 ){
			// get the identified parameters
			String[] identifyParams = cachedAnnotation.identifyParams();
			// get the parameter`s values of current 
			Object[] args = pjp.getArgs();
			
			if(identifyParams.length != 0){// if explicit specified the identified parameters
				for(int i = 0; i < identifyParams.length; i ++){
					int index = ArrayUtils.indexOf(parameterNames, identifyParams[i]);
					if(index == -1){
						continue;
					}
					_paramsString += args[index];
				}
			}else{// not explicit specified the identified parameters
				for(Object o : args){
					_paramsString += o.toString();
				}
			}
		}

		/**
		 * Generate an cache key
		 * If not explicit specified a key, automatically generate one, then hash it
		 * Otherwise,we specified one , then use it and append with parameter`s values
		 */
		if(key == null || "".equals(key)){
			key = pjp.getStaticPart().toString();
			// generate md5 hash key
			key = DigestUtils.md5Hex(key + _paramsString);
		} else {
			// append parameters to key
			if(!"".equals(_paramsString))
				key += DigestUtils.md5Hex(_paramsString);
		}
		
		/**
		 * If current operation are reload cache, remove the cache related to current key
		 * when we access the cached method next time, the cache will be rebuild
		 * regular expression is supported, and we must explicit specified the value(key) 
		 * attribute
		 */
		if(cachedAnnotation.reload()){
			if(key == null || "".equals(key)){
				log.error("Not specified an key!");
				throw new IllegalArgumentException("Not specified an key!");
			}
			if(!cachedAnnotation.enableRegex())
				key = key.replaceAll("\\*", "[\\\\w\\\\W]*");
			cache.remove(key, false);
			return pjp.proceed();
		}
		
		/**
		 * Execution 
		 * If we already had an cache, directly return the cached result
		 * otherwise cached it first , the reload value from cache
		 */
		if(!cache.isExist(key)){
			Object proceed = pjp.proceed();
			// cache expiration time
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
