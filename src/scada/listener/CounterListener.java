package scada.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public final class CounterListener implements HttpSessionListener {
	private int count = 1;
	private ServletContext context = null;

	public synchronized void sessionCreated(HttpSessionEvent se) {
		count++;
		log("sessionCreated(" + se.getSession().getId() + ") count=" + count);
		se.getSession().setAttribute("count", new Integer(count));
	}

	public synchronized void sessionDestroyed(HttpSessionEvent se) {
		count--;
		log("sessionDestroyed(" + se.getSession().getId() + ") count=" + count);
		se.getSession().setAttribute("count", new Integer(count));
	}

	public int getCount() {
		return this.count;
	}

	public void addCount() {
		count++;
	}

	private void log(String message) {
		if (context != null)
			context.log("SessionListener: " + message);
		else
			System.out.println("SessionListener: " + message);
	}

}
