package com.heaven7.databinding.core.expression;


import com.heaven7.databinding.core.DataBindException;
import com.heaven7.databinding.core.IDataResolver;

/*public*/ class TernaryExpression implements IExpression {
	
	private IExpression booleanExpr;
	private IExpression trueExpr;
	private IExpression falseExpr;

	public TernaryExpression(IExpression booleanExpr, IExpression trueExpr,
			IExpression falseExpr) {
		super();
		set(booleanExpr, trueExpr, falseExpr);
	}
	
	public void set(IExpression booleanExpr, IExpression trueExpr,
			IExpression falseExpr) {
		this.booleanExpr = booleanExpr;
		this.trueExpr = trueExpr;
		this.falseExpr = falseExpr;
	}

	
	@Override
	public void reset() {
		booleanExpr.reset();
		trueExpr.reset();
		falseExpr.reset();
		this.booleanExpr = null;
		this.trueExpr    = null;
		this.falseExpr   = null;
	}

	@Override
	public Object evaluate(IDataResolver dataResolver) throws DataBindException {
		if((Boolean) booleanExpr.evaluate(dataResolver)){
			return trueExpr.evaluate(dataResolver);
		}else {
			return falseExpr.evaluate(dataResolver);
		}
	}

}
