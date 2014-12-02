package com.mofang.chat.task.component;

import org.json.JSONObject;

import com.mofang.chat.task.global.GlobalConfig;
import com.mofang.chat.task.global.GlobalObject;
import com.mofang.framework.net.http.HttpClientSender;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class GrowupComponent
{
	public static String increment(long userId, float coin, float diamond, int exp)
	{
		try
		{
			JSONObject json = new JSONObject();
			JSONObject rewardJson = new JSONObject();
			json.put("uid", userId);
			rewardJson.put("coin", coin);
			rewardJson.put("diamond", diamond);
			rewardJson.put("exp", exp);
			json.put("reward", rewardJson);
			String result = HttpClientSender.post(GlobalObject.HTTP_CLIENT_GROWUP, GlobalConfig.GROWUP_USER_UPDATE_URL, json.toString());
			GlobalObject.INFO_LOG.info("add api response:" + ((null == result) ? "" : result));
			return result;
		}
		catch(Exception e)
		{
			GlobalObject.ERROR_LOG.error("GrowupComponent.increment throw an error. ", e);
			return null;
		}
	}
	
	public static String getUserInfo(long userId) 
	{
		try
		{
			String requrl = GlobalConfig.GROWUP_USER_INFO_URL + "?uid=" + userId;
			String result = HttpClientSender.get(GlobalObject.HTTP_CLIENT_GROWUP, requrl);
			GlobalObject.INFO_LOG.info("info api response:" + ((null == result) ? "" : result));
			if(StringUtil.isNullOrEmpty(result))
				return null;
			
			return result;
		}
		catch(Exception e)
		{
			GlobalObject.ERROR_LOG.error("GrowupComponent.getUserInfo throw an error. ", e);
			return null;
		}
	}
}