package cn.kerison.kit.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by kerison on 2015/5/20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Event {

	public static enum Type {
		MAIN, ALL
	}

	public Type value() default Type.MAIN;
}
