package org.faqtong.myjobschedulor.mockdata;

import org.faqtong.myjobschedulor.model.ExecutorGroup;
import org.faqtong.myjobschedulor.model.Job;
import org.faqtong.myjobschedulor.model.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Mock data
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
public class MockData {

    public static Map<String, ExecutorGroup> executorGroupMap = null;
    public static Map<String, Job> jobMap = null;
    public static List<Log> logList = null;

    public void init() {
        executorGroupMap = new TreeMap<>();
        jobMap = new TreeMap<>();
        logList = new ArrayList<>();
    }
}
