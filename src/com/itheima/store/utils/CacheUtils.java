package com.itheima.store.utils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 缓存的工具类
 * @author 趴布里克
 *
 */
public class CacheUtils {
	private static CacheManager manager;
//	private String
	
	static{
		manager = CacheManager.create(CacheUtils.class.getClassLoader().getResourceAsStream("ehcache.xml"));
	}
	
	public static boolean inCache(String cacheName,String key){
		boolean inCache = false;
		try{
			Cache  cache = manager.getCache(cacheName);
			Element  element = cache.get(key);
			if(element!=null){
				inCache  = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return inCache;
	}
	public static Object get(String cacheName,String key){
		try{
			System.out.println("使用了工具类查询");
			Cache  cache = manager.getCache(cacheName);
			Element  element = cache.get(key);
			if(element!=null){
				return element.getObjectValue();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static <T> void save(String cacheName,String key,T value){
		try{
			System.out.println("使用了工具类保存");
			Cache  cache = manager.getCache(cacheName);
			Element  element = new Element(key, value);
			cache.put(element);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void delete(String cacheName,String key){
		try{
			System.out.println("使用了工具类删除");
			Cache  cache = manager.getCache(cacheName);
			cache.remove(key);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		CacheUtils.save("categoryCache", "aaa", "aaa");
		String aa = (String)CacheUtils.get("categoryCache", "aaa");
		System.out.println(aa);
		CacheUtils.save("categoryCache", "aaa", "bbb");
		String bb = (String)CacheUtils.get("categoryCache", "aaa");
		System.out.println(bb);
	}
}
