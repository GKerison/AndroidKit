package cn.kerison.kit.event.core;

import java.util.Map;
import java.util.Set;


/**
 * Created by kerison on 2015/5/20.
 */
public interface EventFinder {

	Map<Class<?>, Set<EventHandler>> findAllEventHandlers(Object target);

	public EventFinder TYPE_ANNOTATION = new EventFinder() {
		@Override
		public Map<Class<?>, Set<EventHandler>> findAllEventHandlers(
				Object target) {
			return EventAnnotationFinder.findAllEvents(target);
		}
	};
}
