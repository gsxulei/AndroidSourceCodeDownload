package com.download;

import java.io.File;
import java.net.URLDecoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.download.utils.Downloader;

public class Aospxref
{
	/**
	 * 网站URL
	 */
	static String BASE_URL="http://aospxref.com/";

	/**
	 * Android版本号
	 */
	static String version="android-10.0.0_r2";
	static String rootUrl=BASE_URL+version+"/xref/";

	/**
	 * 下载起始目录<br/>
	 * 不填写则为Android源码根目录
	 */
	static String startUrl="frameworks/base/packages/SystemUI/";//frameworks/base/packages/SystemUI/

	/**
	 * 本地路径,根据自己电脑磁盘设置
	 */
	static String basePath="F:\\SourceCode\\Android\\"+version+"\\"+startUrl;

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
			//java.io.IOException: Mark invalid
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
		for(int i=0;i<tr.size();i++)
		{
			Elements tds=tr.get(i).select("td");
			Node node=tds.get(1).childNode(0);
			String href=URLDecoder.decode(node.attr("href"),"utf-8");

			if("..".equals(href)||href==null||href.length()==0)
			{
				continue;
			}
			String currUrl=url+href;
			if(href.endsWith("/"))
			{
				File file=new File(path,href);
				if(!file.exists())
				{
					file.mkdirs();
					System.err.println("创建："+file.getAbsolutePath());
				}
				getSourceCode(currUrl,file.getAbsolutePath());
			}
			else
			{
				File file=new File(path,href);
				String downloadUrl=currUrl.replace("/xref/","/download/");
				if(!file.exists())
				{
					System.err.println("下载："+downloadUrl);
					//+" "+tds.get(4).text()
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
		//下载完毕：耗时->184898
	}
}