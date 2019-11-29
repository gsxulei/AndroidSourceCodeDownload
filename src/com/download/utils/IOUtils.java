package com.download.utils;

import java.io.Closeable;
import java.io.Flushable;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO工具
 *
 * @author 徐雷
 * @since 2017-08-16
 */
public class IOUtils
{
	/**
	 * 关闭流
	 *
	 * @param closeables
	 */
	public static void close(Closeable... closeables)
	{
		if(closeables==null)
		{
			return;
		}

		for(Closeable closeable : closeables)
		{
			if(closeable==null)
			{
				continue;
			}

			try
			{
				if(closeable instanceof Flushable)
				{
					((Flushable)closeable).flush();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				closeable.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 复制文件
	 *
	 * @param from
	 * @param to
	 */
	public static void copy(InputStream from,OutputStream to)
	{
		try
		{
			int len=0;
			byte[] buffer=new byte[1024*1024];
			while((len=from.read(buffer))!=-1)
			{
				to.write(buffer,0,len);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(from,to);
		}
	}
}