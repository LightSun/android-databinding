package com.heaven7.databinding.core.expression;


import com.heaven7.databinding.util.IResetable;

//xxx.xxxx()[] or xxx.xxxx[]
/*public*/ class ExpressionInfo implements IResetable {

	public String staticClassname; //may null
	public String accessName;      //may be field /mMethod
	public String variableName; 
	
	public int miniBracketLeftIndex    = ExpressionParser.INVALID_INDEX;
	public int miniBracketRightIndex   = ExpressionParser.INVALID_INDEX;
	//xxx()[]
	public int compactSquareLeftIndex  = ExpressionParser.INVALID_INDEX;
	public int compactSquareRightIndex = ExpressionParser.INVALID_INDEX;
	
	public int dotIndex = ExpressionParser.INVALID_INDEX ;
	
	public boolean isMethod(){
		return miniBracketLeftIndex  != ExpressionParser.INVALID_INDEX;
	}
	
	public boolean isArray(){
		return compactSquareLeftIndex != ExpressionParser.INVALID_INDEX;
	}
	
	public boolean isIncludeMethodParam(){
		return miniBracketRightIndex != ExpressionParser.INVALID_INDEX &&
				(miniBracketRightIndex - miniBracketLeftIndex ) != 1;
	}
	
	@Override
	public void reset() {
		staticClassname = null;
		accessName = null;
		variableName = null;
		miniBracketLeftIndex    = ExpressionParser.INVALID_INDEX;
		miniBracketRightIndex   = ExpressionParser.INVALID_INDEX;
		compactSquareLeftIndex  = ExpressionParser.INVALID_INDEX;
		compactSquareRightIndex = ExpressionParser.INVALID_INDEX;
		dotIndex = ExpressionParser.INVALID_INDEX;
	}
	
	@Override
	public String toString() {
		return "ExpressionInfo [staticClassname=" + staticClassname
				+ ", accessName=" + accessName + ", variableName="
				+ variableName 
				+ ", miniBracketLeftIndex=" + miniBracketLeftIndex
				+ ", miniBracketRightIndex=" + miniBracketRightIndex
				+ ", compactSquareLeftIndex=" + compactSquareLeftIndex
				+ ", compactSquareRightIndex=" + compactSquareRightIndex
				+ ", dotIndex=" + dotIndex + "]";
	}
	
	
}
