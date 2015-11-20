package com.heaven7.databinding.core.expression;


import com.heaven7.databinding.core.DataBindException;
import com.heaven7.databinding.core.IDataResolver;
import com.heaven7.databinding.util.IResetable;

public interface IExpression  extends IResetable {

	 Object evaluate(IDataResolver dataResolver) throws DataBindException;
}
