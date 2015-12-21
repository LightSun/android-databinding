package com.heaven7.databinding.core;


import android.content.Context;
import android.view.View;

/*public*/ interface IVariableResolver {

    /**
     * Resolves the specified variable. Returns null if the variable is not
     * found.
     *
     * @param pName
     *            the name of the variable to resolve,may be digital. such as: "1.3", "1"
     * @return the result of the variable resolution
     * @throws DataBindException if the pName can't resolve.
     */
    Object resolveVariable(String pName) throws DataBindException;

    /***
     * whether the variable is the view's event handler of view or not.
     * @param variable
     */
    boolean isEventHandlerOfView(String variable);

    /** get current view of binding */
    Object getCurrentBindingView();

    /**set the current bind view ,this  must called before really bind data */
    void setCurrentBindingView(View view);

    Context getApplicationContext();
}
