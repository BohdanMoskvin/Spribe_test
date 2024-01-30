package listener;

import lombok.extern.log4j.Log4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Log4j
public class Retry implements IRetryAnalyzer {

  int initCount = 0;
  int maxRetryCount = 1;

  @Override
  public boolean retry(ITestResult result) {
    if (initCount < maxRetryCount) {
      initCount++;
      log.error("Test " + result.getTestName() + " failed. Start attempt " + initCount);
      return true;
    }
    return false;
  }
}
