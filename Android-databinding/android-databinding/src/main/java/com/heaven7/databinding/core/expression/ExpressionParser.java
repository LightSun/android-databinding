package com.heaven7.databinding.core.expression;


public class ExpressionParser {
	
	static boolean sDebug = false;
	
	public static final int INVALID_INDEX = -1;
	public static final char AT = '@';

	public static final char BRACKET_SQUARE_LEFT    = '[';
	public static final char BRACKET_SQUARE_RIGHT   = ']';
	public static final char BRACKET_MINI_LEFT      = '(';
	public static final char BRACKET_MINI_RIGHT     = ')';
	public static final char DOT                    = '.';
	public static final char COMMA                  = ',';
	
	public static final char BRACKET_BIG_LEFT       = '{';
	public static final char BRACKET_BIG_RIGHT      = '}';
	public static final char QUOTE                  = '\"';
	
	public static final int TAG_DOT           = 1;
	public static final int TAG_MINI_LEFT     = 2;
	public static final int TAG_MINI_RIGHT    = 3;
	public static final int TAG_SQUARE_LEFT   = 4;
	public static final int TAG_SQUARE_RIGHT  = 5;
	
	private static final ParserConfig DEFAULT_CONGIG = new ParserConfig();
	
	private static InternalPool sPool;
	
	/** set the parser config , this is useful when parse expression in databinding
	 * @see {@link ParserConfig} */
	public static void setParserConfig(ParserConfig config){
		if(sPool!= null){
			sPool.clearAll();
		}
		sDebug = config.debug;
		sPool = new InternalPool(config);
	}
	/*package*/ static InternalPool getInternalPool(){
		return sPool != null ? sPool : (sPool = new InternalPool(DEFAULT_CONGIG));
	}

	public static void recycleIfNeed(IExpression expression){
		if(expression ==null) return;
		if (expression instanceof  Expression){
			((Expression)expression).recycle();
		}else{
			expression.reset();
		}
	}
	
	public static IExpression parse(String str) throws ExpressionParseException {
		str = str.trim();
		//starts with @
        if(str.startsWith(ExpressionParser.AT+""))
			str = str.substring(1);
		//starts with { , end with }
        if(str.startsWith(ExpressionParser.BRACKET_BIG_LEFT+"") &&
				str.endsWith(ExpressionParser.BRACKET_BIG_RIGHT + "") ) {
			str = str.substring(1,str.length()-1);
		}
		if (sDebug)
			System.out.println("begin parse : " + str);

		// parse  ? :
		int index_problem = str.indexOf("?");
		int index_colon = str.indexOf(":");
		if( index_problem * index_colon <= 0 ){
			throw new ExpressionParseException("'?' and ':' must exist at the same time ,"
					+ "or expression is incorrect");
		}
		if((index_problem + index_colon ) != -2){
			String left =  str.substring(0, index_problem);
			String middle =  str.substring(index_problem + 1, index_colon);
			String right =  str.substring(index_colon + 1);
			IExpression expr_left = ExpressionParserImpl.parse(left, false).get(0);
			IExpression expr_middle = ExpressionParserImpl.parse(middle, false).get(0);
			IExpression expr_right = ExpressionParserImpl.parse(right, false).get(0);
			return new TernaryExpression( expr_left, expr_middle, expr_right );
		}
		
		return ExpressionParserImpl.parse(str, false).get(0);
	}
	
	/**
	 * the parser configuration of expression . such as debug.
	 * @author heaven7
	 *
	 */
	public static class ParserConfig{
		/** the max pool size of ExpressionInfo. */
		public int maxExprInfoPoolSize   = 16 ;
		/** the max pool size of Expression.     */
		public int maxExprPoolSize       = 12 ;
		/** the max pool size of ExpressionParserImpl. */
		public int maxExprParserPoolSize = 8 ;
		/** true means open debug                */
		public boolean debug = false; 
	}
	   
}