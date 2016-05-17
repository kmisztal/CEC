package cec.examples.configuration;

/*
 * Note: When importing the logger classes make sure that they are from org.slf4j library.
 * 
 * From docs: The Simple Logging Facade for Java (SLF4J) serves as a simple facade or abstraction for various logging frameworks
 * (e.g. java.util.logging, logback, log4j) allowing the end user to plug in the desired logging framework at deployment time.
 * 
 * To sum-up, when there is a need to change the logging framework only the proper jar has to replaced. Any code modification is required.
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUsage
{
    // General convention for logger declaration
    private static final Logger logger = LoggerFactory.getLogger(LoggingUsage.class);

    // Few examples with logs usage
    public static void main(String[] args) throws InterruptedException
    {
        basicLoggingUsage();
        loggingWithMessageConcatenation();
        loggingThreadNames();
        loggingWithExceptionHandling();
    }

    private static void basicLoggingUsage()
    {
        //Logging level does matter - try to always select the most accurate:)
        System.out.println("Do not use: System.out for logging purposes");
        logger.trace("Trace message");
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warn message");
        logger.error("Error message");
    }

    private static void loggingWithMessageConcatenation()
    {
        int a = 3;
        int b = 4;
        int sum = a + b;

        logger.info("Try to concatenates values as below: (string expression is evaluated only when INFO level set)");
        logger.info("{} + {} = {}", a, b, sum);

        logger.warn("Try to avoid using '+' operator (string expression is always evaluated)");
        logger.warn(a + " + " + b + " = " + sum);
    }

    private static void loggingThreadNames()
    {
        logger.info("Setting the threads name simplify analysis");
        ExecutorService executorService = Executors.newFixedThreadPool(3, new CustomThreadFactory("my-custom-pool"));

        executorService.submit(() -> logger.info("Processing thread A"));
        executorService.submit(() -> logger.info("Processing thread B"));
        executorService.submit(() -> logger.info("Processing thread C"));

        executorService.shutdown();
    }

    private static void loggingWithExceptionHandling()
    {
        try
        {
            throw new IllegalArgumentException("Sample error");
        }
        catch (Exception exception)
        {
            logger.error("An unexpected error occurred: ", exception);
            logger.error("An unexpected error occurred: (shortest version): {}", exception.getMessage());
        }
    }
}

class CustomThreadFactory implements ThreadFactory
{
    private AtomicInteger counter = new AtomicInteger();
    private String poolName;

    public CustomThreadFactory(String poolName)
    {
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable r)
    {
        return new Thread(r, String.format("%s-%d", poolName, counter.getAndIncrement()));
    }
}
