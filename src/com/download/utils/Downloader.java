package com.download.utils;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Downloader
{
	private static OkHttpClient.Builder clientBuilder=new OkHttpClient.Builder();

	static
	{
		clientBuilder.retryOnConnectionFailure(true);
	}

	public static void download(String url,String localPath)
	{
		OkHttpClient client=clientBuilder.build();

		Request.Builder reqBuilder=new Request.Builder();
		reqBuilder.url(url);
		reqBuilder.get();

		File file=new File(localPath);
		try
		{
			Call call=client.newCall(reqBuilder.build());
			Response response=call.execute();

			FileOutputStream fos=new FileOutputStream(file);

			ResponseBody body=response.body();
			if(body!=null)
			{
				IOUtils.copy(body.byteStream(),fos);
			}
		}
		catch(Exception e)
		{
			file.delete();
			e.printStackTrace();
		}
	}

	public static String get(String url)
	{
		OkHttpClient client=clientBuilder.build();

		Request.Builder reqBuilder=new Request.Builder();
		reqBuilder.url(url);
		reqBuilder.get();

		String html="";
		try
		{
			Call call=client.newCall(reqBuilder.build());
			Response response=call.execute();
			html=response.body().string();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return html;
	}
}