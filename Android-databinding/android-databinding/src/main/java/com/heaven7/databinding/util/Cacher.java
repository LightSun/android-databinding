package com.heaven7.databinding.util;
/**
 * help to cache the number of T,you'd call {@link #obtain()} and {@link #recycle(Object)},
 * and may override {@link #onRecycleSuccess(Object)} when you recycle success!
 * @author heaven7
 * @param <T> the number t to cache
 * @param <P> the param to create T, can be null if not need.
 * @Note if the T is not big. you may not need to cache it
 */
public abstract class Cacher<T,P> implements ICacher<T, P>{
	
	private static final int DEFAULT_MAX_POOL_SIZE = 10;
	
	private Node<T> node;
	private int mCurrentPoolSize;
	private final int mMaxPoolSize;
	
	public Cacher(int maxPoolSize) {
		this.node = new Node<T>();
		this.mMaxPoolSize = maxPoolSize;
	}
	public Cacher(){
		this(DEFAULT_MAX_POOL_SIZE);
	}
	
	public int getMaxPoolSize() {
		return mMaxPoolSize;
	}
	
	public int getCurrentPoolSize(){
		return mCurrentPoolSize;
	}
	/** equal to prepare(null)*/
	public void prepare(){
		prepare(null);
	}
	@Override
	public void prepare(P p) {
		synchronized (this) {
			Node<T> n     = this.node ;
			int current   = this.mCurrentPoolSize ;
			final int max = this.mMaxPoolSize ;
			
			while(current < max){
				if(n.t == null){
					n.t = create(p);
				}else{
					Node<T> n1 = new Node<T>();
					n1.next = n;
					n1.t = create(p);
					n = n1; //new node is the front
				}
				current++ ;
			}
			this.node = n;
			this.mCurrentPoolSize = current;
		}
	}
	
    /*** equals to </pre> obtain(null) </pre>*/
	public T obtain(){
		return obtain(null);
	}
	
	@Override
	public T obtain(P p) {
		synchronized (this) {
			if(node.t!=null){
				Node<T> tmp = this.node;
				T t = tmp.t;
				node = tmp.next;
				tmp.next = null;
				mCurrentPoolSize --;
				return t;
			}
		}
		return create(p);
	}
	
	public void recycle(T t){
		synchronized (this) {
            if (mCurrentPoolSize < mMaxPoolSize) {
            	Node<T> nodeNew = new Node<T>();
            	nodeNew.next = node ;
            	nodeNew.t = t;
            	this.node = nodeNew;
            	mCurrentPoolSize ++ ;
            	onRecycleSuccess(t);
            }
        }
	}
	public void clear(){
		synchronized (this) {
			Node<T> node = this.node;
			while( node != null ){
				node.t = null;
				node = node.next;
			}
			this.node = new Node<T>();
			mCurrentPoolSize = 0;
		}
	}
	
	/** when {@link #recycle(Object)} success ,this will be called */
	protected void onRecycleSuccess(T t){ };
	
	@SuppressWarnings("hiding")
	public class Node<T>{
		 T t;
		 Node<T> next;
	}
	
}
