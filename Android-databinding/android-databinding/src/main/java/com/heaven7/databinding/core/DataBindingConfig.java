package com.heaven7.databinding.core;

import com.heaven7.databinding.core.expression.ExpressionParser;

/**
 * the all config of 'Android-databinding'
 * Created by heaven7 on 2016/7/31.
 */
public final class DataBindingConfig {

    /** max reflect method size of pool ,default is 32 */
    public static int sMaxReflectMethodSize = 32;

    /** the expression parser config ,can't be null   */
    public static ExpressionParser.ParserConfig sParserConfig = new ExpressionParser.ParserConfig();
}
