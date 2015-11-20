package com.heaven7.databinding.core.expression;


import com.heaven7.databinding.util.Cacher;

/*public*/ class InternalPool {
	
	private final Cacher<ExpressionInfo, Void> mExprInfoCache;
	private final Cacher<Expression, Void> mExprCache;
	private final Cacher<ExpressionParserImpl, Void> mParserCache;
	
	/*public*/ InternalPool(ExpressionParser.ParserConfig config){
		this(
			config.maxExprInfoPoolSize,
			config.maxExprPoolSize,
			config.maxExprParserPoolSize
			);
	}

	/**
	 * create the pool
	 * @param maxExprInfoPoolSize  the max pool size of ExpressionInfo.
	 * @param maxExprPoolSize      the max pool size of Expression.
	 * @param maxParserPoolSize    the max pool size of ExpressionParserImpl.
	 */
	/*public*/ InternalPool(int maxExprInfoPoolSize, int maxExprPoolSize ,int maxParserPoolSize) {
		check(maxExprInfoPoolSize , "maxExprInfoPoolSize"  );
		check(maxExprPoolSize ,     "maxExprPoolSize"      );
		check(maxParserPoolSize ,   "maxParserPoolSize"    );
		mExprInfoCache = new Cacher<ExpressionInfo, Void>(maxExprInfoPoolSize) {
			@Override
			public ExpressionInfo obtain() {
				//System.out.println("[ 1 ] , obtain : ExpressionInfo");
				return super.obtain();
			}
			@Override
			public ExpressionInfo create(Void p) {
				//System.out.println("[ 1 ] , create : ExpressionInfo");
				return new ExpressionInfo();
			}
			@Override
			protected void onRecycleSuccess(ExpressionInfo info) {
				//System.out.println("[ 1 ] , recycle : ExpressionInfo");
				info.reset();
			}
		};
		mExprCache = new Cacher<Expression, Void>(maxExprPoolSize) {
			@Override
			public Expression obtain() {
				//System.out.println("[ 2 ] , obtain : Expression");
				return super.obtain();
			}
			@Override
			public Expression create(Void p) {
				//System.out.println("[ 2 ] , create : Expression");
				return new Expression();
			}
			@Override
			protected void onRecycleSuccess(Expression expr) {
				//System.out.println("[ 2 ] , recycle : Expression " +" , poolSize = " +getCurrentPoolSize());
				expr.reset();
			}
		};
		mParserCache = new Cacher<ExpressionParserImpl, Void>(maxParserPoolSize) {
			@Override
			public ExpressionParserImpl obtain() {
				//System.out.println("[ 3 ] , obtain : ParserImpl");
				return super.obtain();
			}
			@Override
			public ExpressionParserImpl create(Void p) {
				//System.out.println("[ 3 ] , create : ParserImpl");
				return new ExpressionParserImpl();
			}
			@Override
			protected void onRecycleSuccess(ExpressionParserImpl t) {
				//System.out.println("[ 3 ] , recycle : ParserImpl");
				t.reset();
			}
		};
	}
	
	private void check(int max, String tag) {
		if(max <=0){
			throw new IllegalStateException(tag +" must above 0");
		}
	}

	public ExpressionInfo obtainExpressionInfo(){
		return mExprInfoCache.obtain();
	}
	public Expression obtainExpression(){
		return mExprCache.obtain();
	}
	public ExpressionParserImpl obtainParser(){
		return mParserCache.obtain();
	}
	
	public void recycle(ExpressionInfo info){
		mExprInfoCache.recycle(info);
	}
	public void recycle(Expression info){
		mExprCache.recycle(info);
	}
	public void recycle(ExpressionParserImpl parser){
		mParserCache.recycle(parser);
	}
	
	public void prepareAll(){
		mExprInfoCache.prepare();
		mExprCache.prepare();
		mParserCache.prepare();
	}
	
	public void clearAll(){
		mExprInfoCache.clear();
		mExprCache.clear();
		mParserCache.clear();
	}
	
	public void printLog(){
    	System.out.println("cache size of ExprssionInfo   = " +mExprInfoCache.getCurrentPoolSize());
    	System.out.println("cache size of Exprssion       = " +mExprCache.getCurrentPoolSize());
    	System.out.println("cache size of ExprssionParser = " +mParserCache.getCurrentPoolSize());
	}
	
	@Override
	protected void finalize() throws Throwable {
		if(mExprInfoCache.getCurrentPoolSize() > 0 ){
			mExprInfoCache.clear();
			//System.err.println("memory leak in ExpressionInfo Cache");
		}
		if(mExprCache.getCurrentPoolSize() > 0 ){
			mExprCache.clear();
			//System.err.println("memory leak in Expression Cache");
		}
		if(mParserCache.getCurrentPoolSize() > 0 ){
			mParserCache.clear();
			//System.err.println("memory leak in ExpressionParser Cache");
		}
		super.finalize();
	}
	
}
