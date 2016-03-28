package com.heaven7.databinding.core.expression;


import java.util.ArrayList;
import java.util.List;

public final class ExpressionParser {
	
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

	private static final List<int[]> sBigQuotes;

	static{
		sBigQuotes = new ArrayList<>(3);
	}
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
        if(str.charAt(0) == ExpressionParser.AT)
			str = str.substring(1);
		//starts with { , end with }
        if(str.charAt(0) == ExpressionParser.BRACKET_BIG_LEFT &&
				str.charAt(1) != AT &&
				str.charAt(str.length()-1) == ExpressionParser.BRACKET_BIG_RIGHT ) {
			str = str.substring(1,str.length()-1);
		}
		if (sDebug)
			System.out.println("begin parse : " + str);
//xxx ? {android:anim/xxx} : xxx2
		// parse  ? :
		int index_problem = str.indexOf("?");
		//may have : {android:anim/xxx}, may cause bug. //TODO how to differentiate ' ? :' with '{@android:color/xxx}'   ?

		/** just test '? :' with nested "{@android:color/holo_red_light}" :
		 IDataResolver resolver = new BaseDataResolver();
		 resolver.setCurrentBindingView(findViewById(R.id.bt100));
		 color = (int) ExpressionParser.parse("{ true ? {@android:color/holo_red_light} : {@color/c_eb4e7b} }").evaluate(resolver);
		 Logger.i("Test","test", " color = " + color);
		 mDataBinder.getViewHelper().setTextColor(R.id.bt3, color);
		 color = (int) ExpressionParser.parse("{ false ? {@android:color/holo_red_light} : {@android:color/holo_red_light} }").evaluate(resolver);
		 Logger.i("Test", "test", " color = " + color);
		 */
		//index of ' : '
		int index_colon = -1;
		//find all pair of '{ }'
		List<int[]> bigList = findAllBigQuote(str);
		if(bigList != null && bigList.size() >0){
			 int[] arr; //big quote index '{ } '
			 int offset;
			 loop_out:
             for(int i = 0, size = bigList.size() ; i<size ;i++){
				 arr = bigList.get(i);
				 offset = 0;
				 while ( (index_colon = str.indexOf(":",offset)) != -1 ){
					 if(index_colon > arr[0] && index_colon < arr[1] ){
						 offset = index_colon + 1;
					 }else{
						 //find
						 break loop_out;
					 }
				 }
			 }
			bigList.clear();
		}else {
			index_colon = str.indexOf(":");
		}
        //check is all exist.
		if(index_problem * index_colon <= 0 ){
			throw new ExpressionParseException("'?' and ':' must exist at the same time ,"
					+ "or expression is incorrect");
		}
		if(index_problem > 0 && index_colon > 0){
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

	private static List<int[]> findAllBigQuote(String str) {
		//nested  '{ }' is not support , such as: '{xxx {xxx}xxx}'
		int index_big_quote_l;
		int index_big_quote_r;
		int start = 0;
		//sBigQuoteList
		while( (index_big_quote_l = str.indexOf("{",start))!= -1){
			index_big_quote_r = str.indexOf("}",start);
			if(index_big_quote_r != -1){
				//System.out.println("findAllBigQuote : index_big_quote_l = " + index_big_quote_l);
                sBigQuotes.add(new int[]{ index_big_quote_l ,index_big_quote_r });
				start = index_big_quote_l + 1;
			}else{
				break;
			}
		}
		return sBigQuotes;
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
