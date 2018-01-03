/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.sleuth.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration}
 * enables Sleuth related metrics reporting
 *
 * @author Marcin Grzejszczak
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(value = "spring.sleuth.metric.enabled", matchIfMissing = true)
@EnableConfigurationProperties
public class TraceMetricsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SleuthMetricProperties sleuthMetricProperties() {
		return new SleuthMetricProperties();
	}

	@Configuration
	@ConditionalOnClass(MeterRegistry.class)
	@ConditionalOnMissingBean(SpanMetricReporter.class)
	protected static class CounterServiceSpanReporterConfig {
		@Bean
		@ConditionalOnBean(MeterRegistry.class)
		public SpanMetricReporter spanReporterCounterService(SleuthMetricProperties sleuthMetricProperties,
				MeterRegistry meterRegistry) {
			Counter acceptedSpansCounter = Counter.builder(
					sleuthMetricProperties.getSpan().getAcceptedName()).register(meterRegistry);
			Counter droppedSpansCounter = Counter.builder(
					sleuthMetricProperties.getSpan().getDroppedName()).register(meterRegistry);
			return new CounterServiceBasedSpanMetricReporter(acceptedSpansCounter, droppedSpansCounter);
		}

		@Bean
		@ConditionalOnMissingBean(MeterRegistry.class)
		public SpanMetricReporter noOpSpanReporterCounterService() {
			return new NoOpSpanMetricReporter();
		}
	}

	@Bean
	@ConditionalOnMissingClass("io.micrometer.core.instrument.MeterRegistry")
	@ConditionalOnMissingBean(SpanMetricReporter.class)
	public SpanMetricReporter noOpSpanReporterCounterService() {
		return new NoOpSpanMetricReporter();
	}
}
