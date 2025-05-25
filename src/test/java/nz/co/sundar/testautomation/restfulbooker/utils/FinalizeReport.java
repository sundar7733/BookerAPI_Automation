package nz.co.sundar.testautomation.restfulbooker.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FinalizeReport {
    @AfterAll
    public void flushReport() {
        ReportManager.flush();
    }
}
