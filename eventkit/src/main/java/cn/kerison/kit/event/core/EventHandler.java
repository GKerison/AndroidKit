package cn.kerison.kit.event.core;

import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.kerison.kit.event.annotation.Event;


/**
 * Created by kerison on 2015/5/20.
 */
public class EventHandler {

	private final Object target;
	private final Method method;
	private Event.Type type;

	public EventHandler(Object target, Method method) {
		this(target, method, Event.Type.MAIN);
	}

	public EventHandler(Object target, Method method, Event.Type type) {
		super();

		if (target == null) {
			throw new IllegalArgumentException(
					"EventMap's target can not be null !");
		}

		if (method == null) {
			throw new IllegalArgumentException(
					"EventMap's method can not be null !");
		}

		this.type = type;
		this.target = target;
		this.method = method;
		this.method.setAccessible(true);
	}

	public void handleEvent(Object event) throws InvocationTargetException,
			IllegalAccessException, IllegalArgumentException {

		if (this.type == Event.Type.MAIN
				&& Looper.myLooper() != Looper.getMainLooper()) {
			throw new IllegalStateException(
					"EventKit accessed from non-main thread "
							+ Looper.myLooper());
		}
		method.invoke(target, event);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("%s.%s-%s", target.getClass().getName(),
				method.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventHandler other = (EventHandler) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
}
