package cn.kerison.kit.event;

import android.util.Log;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import cn.kerison.kit.event.core.EventFinder;
import cn.kerison.kit.event.core.EventHandler;

/**
 * Created by kerison on 2015/5/20.
 */
public class EventKit {

	public static final String TAG = EventKit.class.getSimpleName();

	/**
	 * Class : Event
	 */
	private final ConcurrentMap<Class<?>, Set<EventHandler>> mEventMap = new ConcurrentHashMap<Class<?>, Set<EventHandler>>();

	private final ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>> mEventDispatchQueue = new ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>>() {
		@Override
		protected ConcurrentLinkedQueue<EventWithHandler> initialValue() {
			return new ConcurrentLinkedQueue<EventWithHandler>();
		}
	};

	private final ThreadLocal<Boolean> isDispatching = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return false;
		}
	};
	private final ConcurrentMap<Class<?>, Set<Class<?>>> mEventHierarchyCache = new ConcurrentHashMap<Class<?>, Set<Class<?>>>();

	private EventFinder finder;
	private boolean isSupportEventHierarchy = false;

	private static EventKit mInstance;

	public static EventKit get() {
		if (mInstance == null) {
			synchronized (EventKit.class) {
				if (mInstance == null) {
					mInstance = new EventKit(EventFinder.TYPE_ANNOTATION);
				}
			}
		}
		return mInstance;
	}

	private EventKit(EventFinder finder) {
		this.finder = finder;
	}

	public void register(Object target) {
		if (target == null) {
			throw new NullPointerException(
					"Object to register must not be null.");
		}

		Log.i(TAG, "register for" + target.getClass().getName());

		Map<Class<?>, Set<EventHandler>> targetEventHandlers = finder
				.findAllEventHandlers(target);
		for (Class<?> type : targetEventHandlers.keySet()) {
			Set<EventHandler> allEventHandlers = mEventMap.get(type);
			if (allEventHandlers == null) {
				Set<EventHandler> typeHandlers = new CopyOnWriteArraySet<EventHandler>();
				allEventHandlers = mEventMap.putIfAbsent(type, typeHandlers);
				if (allEventHandlers == null) {
					allEventHandlers = typeHandlers;
				}
			}

			// 加入当前类的事件
			final Set<EventHandler> eventHandlers = targetEventHandlers
					.get(type);
			if (!allEventHandlers.addAll(eventHandlers)) {
				Log.e(TAG, "Object already registered.");
			}
		}
	}

	public void unregister(Object target) {
		if (target == null) {
			throw new NullPointerException(
					"Object to unregister must not be null.");
		}
		Log.i(TAG, "unregister for" + target.getClass().getName());
		Map<Class<?>, Set<EventHandler>> targetEventHandlers = finder
				.findAllEventHandlers(target);

		for (Map.Entry<Class<?>, Set<EventHandler>> entry : targetEventHandlers
				.entrySet()) {
			Set<EventHandler> allEventHandlers = mEventMap.get(entry.getKey());

			Collection<EventHandler> currentEventhandlers = entry.getValue();
			if (allEventHandlers == null
					|| !allEventHandlers.containsAll(currentEventhandlers)) {
				Log.i(TAG, "Missing event handler for an event method. Is "
						+ target.getClass() + " registered ?");
				continue;
			}

			if (!allEventHandlers.removeAll(currentEventhandlers)) {
				Log.e(TAG, "Object already unregistered.");
			}
		}
	}

	public void enableSupportEventHierarchy() {
		this.isSupportEventHierarchy = true;
	}

	public void post(Object event) {
		if (event == null) {
			throw new NullPointerException("Event to post must not be null.");
		}

		Log.i(TAG, "ready post event " + event.getClass().getName());

		if (isSupportEventHierarchy) {
			Set<Class<?>> dispatchTypes = getEventHierarchy(event.getClass());
			for (Class<?> eventType : dispatchTypes) {
				Set<EventHandler> eventHandlers = mEventMap.get(eventType);
				if (eventHandlers != null && !eventHandlers.isEmpty()) {
					for (EventHandler handler : eventHandlers) {
						enqueueEvent(event, handler);
					}
				}
			}
		} else {
			Set<EventHandler> eventHandlers = mEventMap.get(event.getClass());
			if (eventHandlers != null && !eventHandlers.isEmpty()) {
				for (EventHandler handler : eventHandlers) {
					enqueueEvent(event, handler);
				}
			}
		}
		dispatchQueuedEvents();
	}

	protected void enqueueEvent(Object event, EventHandler handler) {
		mEventDispatchQueue.get().offer(new EventWithHandler(event, handler));
	}

	protected void dispatchQueuedEvents() {
		if (isDispatching.get()) {
			return;
		}
		isDispatching.set(true);
		try {
			while (true) {
				EventWithHandler eventWithHandler = mEventDispatchQueue.get()
						.poll();
				if (eventWithHandler == null) {
					break;
				}
				Log.i(TAG, "posting event " + eventWithHandler.event);
				dispatch(eventWithHandler.event, eventWithHandler.handler);
			}
		} finally {
			isDispatching.set(false);
		}
	}

	protected void dispatch(Object event, EventHandler handler) {
		try {
			handler.handleEvent(event);
		} catch (Exception ex) {
			throw new RuntimeException("Could not dispatch event: "
					+ event.getClass() + " to handler " + handler, ex);
		}
	}

	private Set<Class<?>> getEventHierarchy(Class<?> clazz) {
		Set<Class<?>> classes = mEventHierarchyCache.get(clazz);
		if (classes == null) {
			Set<Class<?>> classesCreation = getClassesFor(clazz);
			classes = mEventHierarchyCache.putIfAbsent(clazz, classesCreation);
			if (classes == null) {
				classes = classesCreation;
			}
		}
		return classes;
	}

	private Set<Class<?>> getClassesFor(Class<?> clazz) {
		List<Class<?>> parents = new LinkedList<Class<?>>();
		Set<Class<?>> classes = new HashSet<Class<?>>();

		parents.add(clazz);
		while (!parents.isEmpty()) {
			Class<?> tempClazz = parents.remove(0);
			classes.add(tempClazz);
			Class<?> parent = tempClazz.getSuperclass();
			if (parent != null) {
				parents.add(parent);
			}
		}
		return classes;
	}

	static class EventWithHandler {
		final Object event;
		final EventHandler handler;

		public EventWithHandler(Object event, EventHandler handler) {
			this.event = event;
			this.handler = handler;
		}
	}
}
