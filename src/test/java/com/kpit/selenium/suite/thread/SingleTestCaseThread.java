package com.kpit.selenium.suite.thread;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import com.kpit.selenium.suite.testscript.Keywords;

/**
 * @author milindw3
 */
public class SingleTestCaseThread implements Callable {
	private String keyword;

	public SingleTestCaseThread(String keyword){
		this.keyword = keyword;
	}
	@Override
	public String call() throws Exception {
		 Method method = Keywords.class.getMethod(keyword);
         String result = (String) method.invoke(method);
		 return result;
	}

}
