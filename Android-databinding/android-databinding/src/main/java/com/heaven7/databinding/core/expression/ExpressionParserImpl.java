package com.heaven7.databinding.core.expression;

import com.heaven7.databinding.util.IResetable;
import com.heaven7.databinding.util.StringUtil2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.heaven7.databinding.core.expression.ExpressionParser.BRACKET_MINI_LEFT;
import static com.heaven7.databinding.core.expression.ExpressionParser.BRACKET_MINI_RIGHT;
import static com.heaven7.databinding.core.expression.ExpressionParser.BRACKET_SQUARE_LEFT;
import static com.heaven7.databinding.core.expression.ExpressionParser.BRACKET_SQUARE_RIGHT;
import static com.heaven7.databinding.core.expression.ExpressionParser.COMMA;
import static com.heaven7.databinding.core.expression.ExpressionParser.DOT;
import static com.heaven7.databinding.core.expression.ExpressionParser.INVALID_INDEX;
import static com.heaven7.databinding.core.expression.ExpressionParser.QUOTE;
import static com.heaven7.databinding.core.expression.ExpressionParser.TAG_DOT;
import static com.heaven7.databinding.core.expression.ExpressionParser.TAG_MINI_LEFT;
import static com.heaven7.databinding.core.expression.ExpressionParser.TAG_MINI_RIGHT;
import static com.heaven7.databinding.core.expression.ExpressionParser.TAG_SQUARE_LEFT;
import static com.heaven7.databinding.core.expression.ExpressionParser.TAG_SQUARE_RIGHT;
import static com.heaven7.databinding.core.expression.ExpressionParser.getInternalPool;
import static com.heaven7.databinding.core.expression.ExpressionParser.sDebug;

class ExpressionParserImpl implements IResetable {

	private final List<ExpressionInfo> mInfos ;
	private final LinkedList<Integer> mMiniStack ;   // ()
	private final LinkedList<Integer> mSquareStack ; // []
	
	public ExpressionParserImpl() {
		mInfos = new ArrayList<ExpressionInfo>();
		mMiniStack = new LinkedList<Integer>();
		mSquareStack = new LinkedList<Integer>();
	}
	
	public ExpressionInfo getLastExpressionInfo(){
		return mInfos.size()!=0 ? mInfos.get(mInfos.size()-1) : null;
	}
	
    public void reset() {
    	List<ExpressionInfo> mInfos = this.mInfos;
    	final InternalPool pool = ExpressionParser.getInternalPool();
    	for(int i=0 ,size = mInfos.size() ;i<size ;i++){
    		pool.recycle(mInfos.get(i));
    	}
		mInfos.clear();
		mMiniStack.clear();
		mSquareStack.clear();
	}
	
	private static ExpressionInfo obtain(){
		return ExpressionParser.getInternalPool().obtainExpressionInfo();
	}

	/**
	 * 
	 * @param str   the string to parse. "xxx.xxx()..." indicate is mMethod param
	 * @param isMethodParam  is the param of mMethod.
	 * @throws ExpressionParseException
	 */
	public static List<IExpression> parse(String str,boolean isMethodParam)
			throws ExpressionParseException {

		str = str.trim();

		List<IExpression> exprs = null;

		if(isMethodParam){
			exprs = new ArrayList<IExpression>();
		}
		//check null
		if(StringUtil2.isNull(str)){
			if(exprs == null) exprs = new ArrayList<IExpression>();
			exprs.add(new ObjectExpression(null));
			return exprs;
		}
		//check float ,  max bits like: 20 0000 0000.666666 = 17
		if(StringUtil2.isFloat(str)){
			//may be only a float
			if(exprs == null) exprs = new ArrayList<IExpression>();
			exprs.add( new FloatExpr(Float.valueOf(str)) );
			return exprs;
		}
		//check int
		if(StringUtil2.isInteger(str)){
			if(exprs == null) exprs = new ArrayList<IExpression>();
			exprs.add( new IntExpre(Integer.valueOf(str)) );
			return exprs;
		}
		//check boolean
		if(Boolean.TRUE.toString().equals(str)){
			if(exprs == null) exprs = new ArrayList<IExpression>();
			exprs.add( new BooleanExpr(Boolean.TRUE) );
			return exprs;
		}else if(Boolean.FALSE.toString().equals(str)){
			if(exprs == null) exprs = new ArrayList<IExpression>();
			exprs.add( new BooleanExpr(Boolean.FALSE) );
			return exprs;
		}
		//check constant string like "hello"
		if(str.startsWith(QUOTE+"") && str.endsWith(QUOTE+"")){
			if(exprs == null) exprs = new ArrayList<IExpression>();
			exprs.add( new ObjectExpression(str.substring(1, str.length() - 1 )));
			return exprs;
		}
		if(StringUtil2.isResourceReferOfR(str)){
			if(exprs == null) exprs = new ArrayList<IExpression>();
			exprs.add( new RResourceExpr(str));
			return exprs;
		}

		// . ,() , []
		ExpressionParserImpl impl = ExpressionParser.getInternalPool().obtainParser();
		char[] chs = str.toCharArray();
		
		boolean miniPaired = true;
		// is inside '[]'
		boolean isInSquare = false;
		int lastDotIndex = INVALID_INDEX;
		
		int tempMiniPos = 0;
		int miniPos = 0;
		int tempSquarePos = 0;
		int squarePos = 0;
		
		int lastTag = 0;
		int lastCommaIndex = INVALID_INDEX;
		
		// "()" the position of right bracket must the nearest left bracket's
		LinkedList<Integer> minBracketStack = impl.mMiniStack;
		// "[]"
		LinkedList<Integer> squareStack     = impl.mSquareStack;
				//new LinkedList<Integer>();
		
		for (int i = 0, size = chs.length; i < size; i++) {
			
			switch (chs[i]) {
			case BRACKET_MINI_LEFT:
			{
				if(isInSquare)
					continue;
				miniPos++;
				minBracketStack.push(miniPos);
				if(miniPaired){
					miniPaired = false;
					tempMiniPos = miniPos;
					ExpressionInfo info = impl.getLastExpressionInfo();
					info.miniBracketLeftIndex = i;
					info.accessName = str.substring(lastDotIndex+1,i);
				}
				//ignore !miniPaired
				lastTag = TAG_MINI_LEFT;
				break;
			}
			case BRACKET_MINI_RIGHT:
			{
				if(isInSquare)
					continue;
				if(miniPaired){
					throw new ExpressionParseException("'(' and ')' must be pair of.");
				}
				if(minBracketStack.pop() == tempMiniPos){
					miniPaired = true;
					ExpressionInfo info = impl.getLastExpressionInfo();
					info.miniBracketRightIndex = i;
				}
				lastTag = TAG_MINI_RIGHT;
				break;
			}
				
			case BRACKET_SQUARE_LEFT:
				squarePos ++;
				squareStack.push(squarePos);
				if(isInSquare)
					continue;
				if(lastTag == TAG_MINI_RIGHT || lastTag == TAG_DOT){
					//xxx.xxx()[] or xxx.xxx[]
					impl.getLastExpressionInfo().compactSquareLeftIndex = i;
				}else{
					throw new IllegalStateException();
				}
				isInSquare = true;
				tempSquarePos = squarePos;
				lastTag = TAG_SQUARE_LEFT;
				break;
			case BRACKET_SQUARE_RIGHT:
				Integer val = squareStack.pop();
				if(val == null)
					throw new ExpressionParseException("'[' and ']' must be pair of.");
				if( !isInSquare ) 
					continue ;
				if(val.intValue() == tempSquarePos){
					isInSquare = false;
					impl.getLastExpressionInfo().compactSquareRightIndex = i;
					lastTag = TAG_SQUARE_RIGHT;
				}
				break;

			case DOT:
				{
					if(isInSquare) continue;
					//Xxx.xxx()[] / .xxx()[] /.xxx[] / .xxx  
					// 如果( 还没有找到配对的 ) ignore
					if(miniPaired){
						ExpressionInfo info = obtain();
						info.dotIndex = i;
						if(lastDotIndex == INVALID_INDEX){ //first dot
							String s = str.substring(0, i);
							if(StringUtil2.isFirstUpperCase(s)){
								info.staticClassname = s;
							}else{
								info.variableName = s;
								//next 
								impl.mInfos.add(info);
								info = obtain();
								info.dotIndex = i;
							}
							//第一个dot前面不可能包含() or []
						}
						impl.mInfos.add(info);
						lastDotIndex = i;
					}
					lastTag = TAG_DOT;
					break;
				}
				
			case COMMA:// xx.xxx(),xxxx,xxx.xxx.xxx()
				if(isInSquare )
					continue;
				if(miniPaired && isMethodParam){
					String newStr = str.substring(lastCommaIndex +1, i);
					exprs.add(parse(newStr, false).get(0));
					lastCommaIndex = i;
				}
				break;
			}
		}
		//mMethod param
		if(isMethodParam){
			// one "," indicate two param. but previous only add one
			exprs.add( parse( str.substring(lastCommaIndex +1), false).get(0));
		}else{
			if(lastTag == TAG_DOT){
				String accessName = str.substring(lastDotIndex+1);
				ExpressionInfo info = impl.getLastExpressionInfo();
				if(info!=null && info.accessName == null){
					info.accessName = accessName;
				}else{
					info = obtain();
					info.accessName = str.substring(lastDotIndex+1);
					info.dotIndex = lastCommaIndex;
					impl.mInfos.add(info);
				}
			}else if(lastTag == 0){
				//just a variable (may be integer)
				ExpressionInfo info = obtain();
				info.variableName = str.substring(lastDotIndex+1);
				impl.mInfos.add(info);
			}
		}
			
		if(exprs!=null){
			getInternalPool().recycle(impl);
			return exprs;
		}
		
		exprs = new ArrayList<IExpression>();
		List<ExpressionInfo> infos = impl.mInfos;
		
		Expression previous = null;
		Expression first = null;
		
		for(int i=0,size = infos.size() ; i<size ;i++){
			ExpressionInfo info = infos.get(i);
			if(sDebug){
				System.out.println("the raw string = " +str);
				System.out.println("begin convert ExpressionInfo --> Expression, i = "+ i);
				System.out.println(info);
			}
			Expression expr = new Expression.Builder()
			     .setVariable(info.variableName)
			     .setAccessName(info.accessName)
			     .setStaticAccessClassname(info.staticClassname)
			     .setIsMethod(info.isMethod())
			     .setParamAccessInfos(info.isIncludeMethodParam() ?
			    		 parse(str.substring(info.miniBracketLeftIndex + 1, 
			    				 info.miniBracketRightIndex), true) 
			    		 :null)
			    
			     .setArrayIndexExpression(info.isArray() ? parse(str.substring(
						info.compactSquareLeftIndex + 1,info.compactSquareRightIndex),false
						).get(0) :null)
			     .build();
			
			if(i == 0){
				first = expr; 
			}else{
				previous.setNextAccessInfo(expr);
			}
			previous = expr;
		}
		exprs.add(first);
		getInternalPool().recycle(impl);
		return exprs;
	}


}
