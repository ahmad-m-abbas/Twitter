package org.example.infra;

import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.java.Log;

@Log
public class Sentry {

    private static Supplier<String> sentryDsnSupplier = () -> null;

    private static Supplier<Boolean> sentryEnabledSupplier = () -> false;

    public static void configure(Consumer<SentryConfiguration> configConsumer) {
        SentryConfiguration config = new SentryConfiguration();
        configConsumer.accept(config);
        Sentry.sentryEnabledSupplier = config.sentryEnabledSupplier;
        Sentry.sentryDsnSupplier = config.sentryDsnSupplier;
    }

    public static void useSentry(Consumer<SentryClient> consumer) {
        try {
            if (sentryEnabledSupplier.get() == true) {
                consumer.accept(SentryClientFactory.sentryClient(sentryDsnSupplier.get()));
            } else {
                log.info("Attempted to use Sentry while disabled");
            }
        } catch (Exception ex) {
            log.severe(String.format("Error While Attempting to send data to sentry : %s", ex.getMessage()));
        }
    }

    public static class SentryConfiguration {
        public Supplier<String> sentryDsnSupplier = () -> null;
        public Supplier<Boolean> sentryEnabledSupplier = () -> false;
    }

}
