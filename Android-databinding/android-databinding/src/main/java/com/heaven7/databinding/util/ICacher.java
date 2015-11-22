package com.heaven7.databinding.util;

public interface ICacher<T, P> {
	
	/** prepare for a number of T  if you need*/
	 void prepare(P p);

	/**obtain T from cache . if not exist , create(p) will be called*/
	 T obtain(P p);
	
	/** clear the cache*/
	 void clear();

	/**@hide when cacher havn't , create by this mMethod */
	 T create(P p);

	/** recycle it */
	 void recycle(T t);

}
