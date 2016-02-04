package com.heaven7.databinding.core.expression;

import com.heaven7.databinding.core.DataBindException;
import com.heaven7.databinding.core.IDataResolver;
import com.heaven7.databinding.util.StringUtil2;

/**
 * Created by heaven7 on 2015/12/10.
 */
public class RResourceExpr implements IExpression {

    private String expr;

    public RResourceExpr(String expr) {
        this.expr = expr;
    }

    @Override
    public Object evaluate(IDataResolver dataResolver) throws DataBindException {
      /*  final String packageName = dataResolver.getApplicationContext().getApplicationInfo().packageName;
        final String[] strs = expr.split("\\.");
        try {
            return Class.forName(packageName +".R$" + strs[1]).getField(strs[2]).getInt(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        return StringUtil2.getResId(expr, dataResolver.getApplicationContext());
    }

    @Override
    public void reset() {
    }
}
