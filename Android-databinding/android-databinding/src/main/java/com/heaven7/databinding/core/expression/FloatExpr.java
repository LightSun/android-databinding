
package com.heaven7.databinding.core.expression;


import com.heaven7.databinding.core.DataBindException;
import com.heaven7.databinding.core.IDataResolver;

public class FloatExpr implements IExpression{
	
	private Float value;
	
	public FloatExpr(float value) {
		super();
		this.value = Float.valueOf(value);
	}
	public FloatExpr(Float value) {
		super();
		this.value = value;
	}

	@Override
	public void reset() {
		
	}

	@Override
	public Object evaluate(IDataResolver dataResolver) throws DataBindException {
		return value;
	}

}
