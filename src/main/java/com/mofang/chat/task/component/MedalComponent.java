package com.mofang.chat.task.component;

import com.mofang.chat.task.global.GlobalConfig;
import com.mofang.chat.task.global.GlobalObject;
import com.mofang.framework.net.http.HttpClientSender;

/**
 * 
 * @author zhaodx
 *
 */
public class MedalComponent
{
	public static String completed(long userId, int medalEvent)
	{
		try
		{
			String contentType = "application/x-www-form-urlencoded";
			StringBuilder postData = new StringBuilder();
			postData.append("uid=" + userId + "&type=" + medalEvent + "&hand=0");
			String result = HttpClientSender.post(GlobalObject.HTTP_CLIENT_MEDAL, GlobalConfig.MEDAL_COMPLETED_URL, postData.toString(), contentType);
			GlobalObject.INFO_LOG.info("completed api response:" + ((null == result) ? "" : result));
			return result;
		}
		catch(Exception e)
		{
			GlobalObject.ERROR_LOG.error("MedalComponent.completed throw an error. ", e);
			return null;
		}
	}
}