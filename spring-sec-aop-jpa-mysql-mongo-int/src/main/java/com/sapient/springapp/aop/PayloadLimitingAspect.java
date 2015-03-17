package com.sapient.springapp.aop;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@Aspect
public class PayloadLimitingAspect {

	final Logger logger = LoggerFactory.getLogger(PayloadLimitingAspect.class);	
	
	private String listSizeLimit;
	
    public String getListSizeLimit() {
		return listSizeLimit;
	}

	public void setListSizeLimit(String listSizeLimit) {
		this.listSizeLimit = listSizeLimit;
	}

	@Around("within(com.sapient.springapp..*)")
    public Object limitPayload(ProceedingJoinPoint  joinPoint) throws Exception,Throwable
    {
		logger.info("Annotation driven:Around advice");
		Object[] args = joinPoint.getArgs();
		if (args.length > 0) {
			System.out.print("Arguments passed:");
			for (int i = 0; i < args.length; i++) {
				System.out.print("Arg" + (i + 1) + ":" + args[i]);
				// args[i]=":Annotation driven argument";
			}
		}
		Object result = null;

		result = joinPoint.proceed(args);

		if (result != null) {
			// Do something with result size
			logger.info("Result size" + result.toString().length());
			// Do something with result byte size
			logger.info("Result byte size"
					+ result.toString().getBytes().length);

			// Check return object type and results being returned , I have
			// taken taken 2 as example to cut the response circuit
			if (result instanceof List && ((List) result).size() >= Integer.parseInt(listSizeLimit)) {
				User user = (User) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				String name = user.getUsername(); // get logged in username

				logger.error("WARNING !!!! User " + name
						+ " get payload is more then allowed size"
						+ ((List) result).size());
				// or create security exception
				throw new IllegalStateException();
			}
		}

		return result;
	}
}
