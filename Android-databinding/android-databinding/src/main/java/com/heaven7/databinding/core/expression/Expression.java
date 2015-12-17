package com.heaven7.databinding.core.expression;


import android.view.View;

import com.heaven7.databinding.R;
import com.heaven7.databinding.core.DataBindException;
import com.heaven7.databinding.core.IDataResolver;
import com.heaven7.databinding.util.ArrayUtil;
import com.heaven7.databinding.util.ReflectUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by heaven7 on 2015/8/13.
 */
public class Expression implements IExpression {

	static final boolean sDebug = ExpressionParser.sDebug;

//eg: list.get(0) .name    mHolder = concrete list,accessName = get .isMethod = true, arrayIndex = 0
	// user

	public static final int INVALID_INDEX = -1;

	private String mVariable;
	private Object mHolder;                   //variable -> Holder

	private String mStaticAccessClassname;     //used for static access,nullable

	private String mAccessName;
	private boolean mIsMethod;                //true is isMethod, false is field
	private int mArrayIndex = INVALID_INDEX;  // if it is array, this must be valid
	private IExpression mArrayIndexExpr;

	private Expression mNextAccessInfo;

	// used for mMethod access
	private List<IExpression> mParamAccessInfos;

	public Object getHolder(){
		return mHolder;
	}
	public String getVariable(){
		return mVariable;
	}
	public String getAccessName() {
		return mAccessName;
	}
	public boolean isMethod() {
		return mIsMethod;
	}
	public int getArrayIndex() {
		return mArrayIndex;
	}
	public Expression getNextAccessInfo() {
		return mNextAccessInfo;
	}
	public String getStaticAccessClassname() {
		return mStaticAccessClassname;
	}
	/** get the expressions,used for the param of mMethod */
	public List<IExpression> getParamAccessInfos() {
		return mParamAccessInfos;
	}

	private Object performNextExpressionIfNeed(IDataResolver dataResolver,
											   Object nextHolder) {
		if (mNextAccessInfo != null) {
			mNextAccessInfo.mHolder = nextHolder;
			nextHolder = mNextAccessInfo.evaluate(dataResolver);
		}
		return nextHolder;
	}

	public void setHolder(Object holder){
		this.mHolder = holder;
	}
	public void setVariable(String variable){
		this.mVariable = variable;
	}

	public void setStaticAccessClassname(String staticAccessClassname) {
		this.mStaticAccessClassname = staticAccessClassname;
	}
	/** 方法名称或者字段名称*/
	public void setAccessName(String accessName) {
		this.mAccessName = accessName;
	}

	public void setIsMethod(boolean isMethod) {
		this.mIsMethod = isMethod;
	}

	public void setArrayIndex(int arrayIndex) {
		if(arrayIndex < 0)
			throw new IllegalArgumentException();
		this.mArrayIndex = arrayIndex;
	}

	public void setNextAccessInfo(Expression nextAccessInfo) {
		this.mNextAccessInfo = nextAccessInfo;
	}

	public void setParamAccessInfos(List<IExpression> params) {
		this.mParamAccessInfos = params;
	}

	@Override
	public void reset() {
		mStaticAccessClassname = null;
		mVariable         = null;
		mAccessName       = null;
		mArrayIndex       = INVALID_INDEX;
		mIsMethod         = false;

		if( mHolder != null ){
			if(mHolder instanceof Expression){
				((Expression) mHolder).reset();
			}
			mHolder = null;
		}

		if(mNextAccessInfo != null){
			mNextAccessInfo.reset();
			mNextAccessInfo = null;
		}
		if(mArrayIndexExpr != null){
			mArrayIndexExpr.reset();
			mArrayIndexExpr = null;
		}
		if(mParamAccessInfos != null && mParamAccessInfos.size() > 0 ){
			for(int i = 0, size = mParamAccessInfos.size() ; i<size ;i++){
				mParamAccessInfos.get(i).reset();
			}
			mParamAccessInfos.clear();
		}
	}

	@Override
	public String toString() {
		return "Expression [variable=" + mVariable + ", mHolder=" + mHolder
				+ ", staticAccessClassname=" + mStaticAccessClassname
				+ ", accessName=" + mAccessName + ", isMethod=" + mIsMethod
				+ ", arrayIndex=" + mArrayIndex + ", nextAccessInfo="
				+ mNextAccessInfo + ", paramAccessInfos=" + mParamAccessInfos
				+ "]";
	}
	@Override
	public Object evaluate(IDataResolver dataResolver) throws DataBindException {
		try {
			if(sDebug){
				System.out.println("============== begin call ---> evaluate(IDataResolver) ===========");
				System.out.println("mHolder           = " + mHolder);
				System.out.println("staticClassname  = " + mStaticAccessClassname);
				System.out.println("variable         = " + mVariable);
				System.out.println("accessName       = " + mAccessName);
				System.out.println("arrayIndex       = " + mArrayIndex);
			}

			Object objHolder = null;
			// just variable expression
			if(getVariable()!=null){
				// current is variable check is event handler,true to add current binding view to param
				if(dataResolver.isEventHandlerOfView(mVariable) && dataResolver.getCurrentBindingView() !=null){
					Expression next = getNextAccessInfo();
					next.setIsMethod(true);

					final View currBindView = (View) dataResolver.getCurrentBindingView();
					Object tag ;
                    if( (tag = currBindView.getTag(R.id.key_adapter_hash)) !=null) {
						//add item and position for item bind,
						// that means onClick in adapter  is (view, position, item, AdapterManager...etc)
						next.addExtraParamTofirst(dataResolver.getAdapterManager((Integer)tag),true);
						next.addExtraParamTofirst(dataResolver.getCurrentItem(), true);
						next.addExtraParamTofirst(dataResolver.getCurrentPosition(), true);
					}

					// make view at first onclickxxx(view v, IDataBinder b,...)
					next.addExtraParamTofirst(currBindView, true);
				}
				objHolder = performNextExpressionIfNeed(dataResolver,
						dataResolver.resolveVariable(mVariable));
				return objHolder;
			}
			//mHolder is IExpression
			if( mHolder instanceof IExpression){
				objHolder = performNextExpressionIfNeed(dataResolver,
						((IExpression)mHolder).evaluate(dataResolver));
				return objHolder;
			}
			int arrayIndex =  mArrayIndexExpr!=null ?
					(Integer)mArrayIndexExpr.evaluate(dataResolver) :this.mArrayIndex;

			final Class<?> clazz = mStaticAccessClassname != null ? Class.forName(
					dataResolver.getClassname(mStaticAccessClassname) ): mHolder.getClass();
			if (mIsMethod) {
				final List<IExpression> mParamAccessInfos = this.mParamAccessInfos;

				final int len = mParamAccessInfos == null ? 0 : mParamAccessInfos.size();
				Object[] params = new Object[len];
				//mParams[i].getClass().isPrimitive() //if wrapped i don't know is int or Integer
				IExpression ie ;
				for (int i = 0; i < len; i++) {
					ie = mParamAccessInfos.get(i);
					params[i] = ie.evaluate(dataResolver);
					if(sDebug){
						System.out.println("Param value: "+params[i]);
					}
				}

				//reset and clear occasional ObjectExpression
				if(len > 0 ) {
					for (int i = 0; i < mParamAccessInfos.size(); ) {
						ie = mParamAccessInfos.get(i);
						if (ie instanceof ObjectExpression && ((ObjectExpression)ie).isOccasional() ) {
							ie.reset();
							mParamAccessInfos.remove(ie);
						}else{
							i++;
						}
					}
				}

				//invokeCallback mMethod or callback if is event
				Object holder = this.mHolder;
				Object result = null;

				if(params.length > 0 && params[0] instanceof View){
					Method method  = ReflectUtil.getAppropriateMethod(clazz,mAccessName,ArrayUtil.getTypes(params));
					/*try {
						method = clazz.getDeclaredMethod(mAccessName, ArrayUtil.getTypes(params));
					}catch (NoSuchMethodException e){
						List<Method> ms = dataResolver.getMethod(clazz, mAccessName);
						final int size = ms.size();
						if( size == 0){
                             throw new DataBindException("event handler must have the method name = " + mAccessName);
						}
						if(size > 1){
							throw new DataBindException("event handler can only have one method with the name = " +
									mAccessName + " ,but get " + size +"( this means burden method in event handler" +
									" is not support !)");
						}
						method = ms.get(0);
					}*/
                    dataResolver.getEventEvaluateCallback().onEvaluateCallback(holder,method,params);
				}else {
					final boolean useStaticClassname = mStaticAccessClassname != null;
					//just find mMethod by mMethod name,so care burden
					final List<Method> ms = dataResolver.getMethod(clazz, mAccessName);//mMethod can't burden
					for (int i = 0, size = ms.size(); i < size; i++) {
						try {
							if (sDebug) {
								System.out.println(">>>>> begin invokeCallback mMethod: " + ms.get(i));
							}
							result = useStaticClassname ? ms.get(i).invoke(null, params)
									: ms.get(i).invoke(holder, params);
							break;
						} catch (Exception e) {
							if (i == size - 1) {// at last still exception,throw it
								throw e;
							}
						}
					}
					if(sDebug){
						System.out.println(">>>>> mMethod invokeCallback: result = " +result);
					}
					if (arrayIndex != INVALID_INDEX && result!=null && result.getClass().isArray()) {
						objHolder = Array.get(result, arrayIndex);
					} else {
						objHolder = result;
					}
				}

			} else {
				final Field f = dataResolver.getField(clazz, mAccessName);
				final Object result = mStaticAccessClassname != null ? f.get(null)
						: f.get(mHolder);
				if (arrayIndex != INVALID_INDEX && result.getClass().isArray()) {
					objHolder = Array.get(result, arrayIndex);
				} else {
					objHolder = result;
				}
			}
			objHolder = performNextExpressionIfNeed(dataResolver, objHolder);
			if(sDebug){
				System.out.println("============== end call ========================");
			}
			return objHolder;
		} catch (Exception e) {
			if(e instanceof DataBindException)
				throw (DataBindException)e;
			else
				throw new DataBindException(e);
		}
	}

	/** add the param to the fist of param accessInfo
	 * @param param the extra param to add to first , maybe view or {@link com.heaven7.databinding.core.IDataBinder}*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addExtraParamTofirst(Object param,boolean occasional) {

		final ObjectExpression expr = new ObjectExpression(param);
		expr.setIsOccasional( occasional);

		if(mParamAccessInfos == null){
			mParamAccessInfos = new ArrayList();
			mParamAccessInfos.add(expr);
		}else {
			if(mParamAccessInfos.size() > 0)
			    mParamAccessInfos.add(0, expr);
			else
				mParamAccessInfos.add(expr);
		}
	}

	public static class Builder{

		private final Expression expr = Expression.obtain();

		public Builder() {
		}

		public Builder setHolder(Object holder){
			expr.setHolder(holder);
			return this;
		}
		public Builder setVariable(String variable){
			expr.setVariable(variable);
			return this;
		}

		public Builder setStaticAccessClassname(String staticAccessClassname) {
			expr.setStaticAccessClassname(staticAccessClassname);
			return this;
		}
		/** 方法名称或者字段名称*/
		public Builder setAccessName(String accessName) {
			expr.setAccessName(accessName);
			return this;
		}

		public Builder setIsMethod(boolean isMethod) {
			expr.setIsMethod(isMethod);
			return this;
		}

		public Builder setArrayIndex(int arrayIndex) {
			expr.setArrayIndex(arrayIndex);
			return this;
		}
		public Builder setArrayIndexExpression(IExpression e) {
			expr.setArrayIndexExpression(e);
			return this;
		}

		public Builder setNextAccessInfo(Expression nextAccessInfo) {
			expr.setNextAccessInfo(nextAccessInfo);
			return this;
		}

		@SuppressWarnings("unchecked")
		public <T extends IExpression>Builder setParamAccessInfos(List<T> params) {
			expr.setParamAccessInfos((List<IExpression>) params);
			return this;
		}

		public Expression build(){
			return expr;
		}
	}

	public void setArrayIndexExpression(IExpression expr) {
		mArrayIndexExpr = expr;
	}

	public static Expression obtain(){
		return ExpressionParser.getInternalPool().obtainExpression();
	}

	public void recycle(){
		ExpressionParser.getInternalPool().recycle(this);
	}

	@Override
	protected void finalize() throws Throwable {
		recycle();
		super.finalize();
	}


}
