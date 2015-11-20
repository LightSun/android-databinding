package com.heaven7.databinding.core.expression;


import com.heaven7.databinding.core.DataBindException;
import com.heaven7.databinding.core.IDataResolver;

/*public*/ class ObjectExpression implements IExpression{
	
	private Object obj;

	private boolean mIsOccasional; //临时的
	
	public ObjectExpression(Object obj) {
		super();
		this.obj = obj;
	}

	public boolean isOccasional() {
		return mIsOccasional;
	}

	/** set this exception is temped */
	public void setIsOccasional(boolean occasional) {
		this.mIsOccasional = occasional;
	}

	@Override
	public void reset() {
		obj = null;
		mIsOccasional = false;
	}

	@Override
	public Object evaluate(IDataResolver dataResolver) throws DataBindException {
		return obj;
	}

}
