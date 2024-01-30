package listener;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Log4j
public class ListenerTestApi implements ITestListener {
  Logger logger = LogManager.getLogger(listener.ListenerTestApi.class);

  @Override
  public void onTestFailure(ITestResult result) {
    logger.info("---------------------------------------------------------------");
    logger.info("Failed because of - " + result.getThrowable());
    logger.info("---------------------------------------------------------------");
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    logger.info("---------------------------------------------------------------");
    logger.info("Skipped because of - " + result.getThrowable());
    logger.info("---------------------------------------------------------------");
  }

  @Override
  public void onTestStart(ITestResult result) {
    logger.info("---------------------------------------------------------------");
    logger.info(result.getMethod().getMethodName() + " Started");
    logger.info("---------------------------------------------------------------");
    logger.info(result.getMethod().getDescription());
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    logger.info("---------------------------------------------------------------");
    logger.info(result.getMethod().getMethodName() + " Passed");
    logger.info("---------------------------------------------------------------");
  }

  @Override
  public void onStart(ITestContext context) {
    logger.info("===============================================================");
    logger.info("     On Start :-" + context.getName() + "                      ");
    logger.info("===============================================================");
  }

  @Override
  public void onFinish(ITestContext context) {
    logger.info("===============================================================");
    logger.info("     On Finish :-" + context.getName() + "                     ");
    logger.info("===============================================================");
  }
}
