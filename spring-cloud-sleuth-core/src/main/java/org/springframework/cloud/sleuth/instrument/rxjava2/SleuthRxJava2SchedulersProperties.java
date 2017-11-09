package org.springframework.cloud.sleuth.instrument.rxjava2;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration tracing properties for RxJava2
 *
 * @author Łukasz Guz
 * @author Jakub Pyda
 */
@ConfigurationProperties("spring.sleuth.rxjava2.schedulers")
public class SleuthRxJava2SchedulersProperties {

	/**
	 * Thread names for which spans will not be sampled.
	 */
	private String[] ignoredthreads = { "HystrixMetricPoller", "^RxComputation.*$" };
	private Hook hook = new Hook();

	public String[] getIgnoredthreads() {
		return this.ignoredthreads;
	}

	public void setIgnoredthreads(String[] ignoredthreads) {
		this.ignoredthreads = ignoredthreads;
	}

	public Hook getHook() {
		return this.hook;
	}

	public void setHook(Hook hook) {
		this.hook = hook;
	}

	private static class Hook {

		/**
		 * Enable support for RxJava via RxJavaSchedulersHook.
		 */
		private boolean enabled = true;

		public boolean isEnabled() {
			return this.enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}
}
