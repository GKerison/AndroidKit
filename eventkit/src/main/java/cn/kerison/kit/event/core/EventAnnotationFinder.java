package cn.kerison.kit.event.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.kerison.kit.event.annotation.Event;


/**
 * Created by kerison on 2015/5/20.
 */
public class EventAnnotationFinder {

	private static final Map<Class<?>, Map<Class<?>, Set<EventHandler>>> EVENT_CACHE = new HashMap<Class<?>, Map<Class<?>, Set<EventHandler>>>();

	private EventAnnotationFinder() {
	}

	public static Map<Class<?>, Set<EventHandler>> findAllEvents(Object target) {
		// TODO Auto-generated method stub
		Class<?> targetClass = target.getClass();
		if (!EVENT_CACHE.containsKey(targetClass)) {
			return loadTargetMethods(target);
		} else {
			return EVENT_CACHE.get(targetClass);
		}
	}

	private static Map<Class<?>, Set<EventHandler>> loadTargetMethods(Object target) {
		// TODO Auto-generated method stub
		Class<?> targetClass = target.getClass();
		Map<Class<?>, Set<EventHandler>> targetEventMethods = new HashMap<Class<?>, Set<EventHandler>>();
		for (Method method : targetClass.getDeclaredMethods()) {

			if (method.isBridge()) {
				continue;
			}

			if (method.isAnnotationPresent(Event.class)) {

				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length != 1) {
					throw new IllegalArgumentException(
							"Method "
									+ method
									+ " with @Event annotation has "
									+ parameterTypes.length
									+ " arguments.  Methods must require a single argument.");
				}

				Class<?> eventType = parameterTypes[0];
				if (eventType.isInterface()) {
					throw new IllegalArgumentException(
							"Method "
									+ method
									+ " with @Event annotation parameter "
									+ eventType
									+ " which is an interface.  EventObject must be on a concrete class type.");
				}

				Event event = method.getAnnotation(Event.class);
				Set<EventHandler> methods = targetEventMethods.get(eventType);
				EventHandler eventMap = new EventHandler(target, method, event.value());
				if (methods == null) {
					methods = new HashSet<EventHandler>();
					targetEventMethods.put(eventType, methods);
				}
				methods.add(eventMap);
			}
		}
		EVENT_CACHE.put(targetClass, targetEventMethods);
		return targetEventMethods;
	}
}
