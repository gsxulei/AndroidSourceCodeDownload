package com.download;

import java.io.File;
import java.net.URLDecoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.download.utils.Downloader;

public class AndroidosNetCn
{
	static String BASE_URL="http://androidos.net.cn/android/";

	static String version="10.0.0_r6";//android-10.0.0_r2
	static String rootUrl=BASE_URL+version+"/xref/";

	//frameworks/base/
	//libcore/
	static String startUrl="external/bouncycastle/";

	static String basePath="F:\\SourceCode\\Android\\android-"+version+"\\"+startUrl;

	static void getSourceCode(String url,String path)throws Exception
	{
		File parent=new File(path);
		if(!parent.exists())
		{
			parent.mkdirs();
		}

		System.err.println("解析："+url);
		Document doc=null;
		try
		{
			doc=Jsoup.connect(url).get();
		}
		catch(Exception e)
		{
			e.printStackTrace();

			doc=Jsoup.parse(Downloader.get(url));
			System.err.println("重新解析："+url);
		}
		if(doc==null)
		{
			System.err.println("无法解析："+url);
			return;
		}
		Elements tbody=doc.select("tbody");
		Elements tr=tbody.select("tr");
		//System.err.println(tr.get(tr.size()-2));
		for(int i=0;i<tr.size();i++)
		{
			Elements tds=tr.get(i).select("td");
			if(tds.size()==1)
			{
				continue;
			}
			String href=URLDecoder.decode(tds.get(1).text(),"utf-8");

			if("..".equals(href))
			{
				continue;
			}

			String size=tds.get(3).text();
			String currUrl=url+href;
			if("-".equals(size))
			{
				File file=new File(path,href);
				if(!file.exists())
				{
					file.mkdirs();
					System.err.println("创建："+file.getAbsolutePath());
				}
				getSourceCode(currUrl+"/",file.getAbsolutePath());
			}
			else
			{
				File file=new File(path,href);
				String downloadUrl=currUrl.replace("/xref/","/download/");
				if(!file.exists())
				{
					System.err.println("下载："+downloadUrl+" "+size);
					// 下载
					Downloader.download(downloadUrl,file.getAbsolutePath());
				}
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		long start=System.currentTimeMillis();
		getSourceCode(rootUrl+startUrl,basePath);
		long end=System.currentTimeMillis();
		System.err.println(rootUrl+startUrl+"下载完毕：耗时->"+(end-start));
		// 下载完毕：耗时->184898
	}
}