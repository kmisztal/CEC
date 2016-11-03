package utils;

import cec.examples.configuration.LoggingUsage;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple rule, defining retry mechanism for failed tests. Failed tests are re-executed till
 * the retryCount is reached.
 *
 * Note:
 * To make it more usable, consider creating separate annotation which can be placed on a single test.
 */
public class RetryRule implements TestRule
{
    private static final Logger logger = LoggerFactory.getLogger(LoggingUsage.class);

    private int retryCount;

    public RetryRule(int retryCount)
    {
        this.retryCount = retryCount;
    }

    public Statement apply(Statement base, Description description)
    {
        return statement(base, description);
    }

    private Statement statement(final Statement base, final Description description)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                Throwable caughtThrowable = null;

                // implement retry logic here
                for (int i = 0; i < retryCount; i++)
                {
                    try
                    {
                        base.evaluate();
                        return;
                    }
                    catch (Throwable t)
                    {
                        caughtThrowable = t;
                        logger.error("{} : run {} failed, trying again", description.getDisplayName(), (i + 1));
                    }
                }
                logger.error("{} : giving up after {} failures", description.getDisplayName(), retryCount);
                throw caughtThrowable;
            }
        };
    }
}
