package com.heaven7.databinding.core.expression;


import com.heaven7.databinding.core.DataBindException;
import com.heaven7.databinding.core.IDataResolver;

public class BooleanExpr implements IExpression{
	
	private Boolean val;
	
	public BooleanExpr(Boolean val) {
		super();
		this.val = val;
	}

	@Override
	public void reset() {
		
	}

	@Override
	public Object evaluate(IDataResolver dataResolver) throws DataBindException {
		return val;
	}

}
