package com.heaven7.databinding.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicate the method is used for data-binding. this is not required.
 * just help you to fast find the relatives of data-binding.
 * Created by heaven7 on 2015/12/13.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface DatabindingMethod {
}
