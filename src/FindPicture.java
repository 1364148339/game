import sun.plugin.util.PluginSysUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 寻图
 *
 * @Author: 王桥
 * @Date: 2019/12/1 17:11
 * @Version 1.0
 */
public class FindPicture implements Runnable{
    //主图数组
	private static int[][] bigData;
	//副图数组
	private static int[][] smallData;
	//循环次数
	private volatile int cnt;
	//结果
	private Result result;


	public FindPicture(int[][] bigData,int[][] smallData)
	{
		this.cnt = 0;
		this.bigData = bigData;
		this.smallData = smallData;
	}

	/**
	 * 寻图
	 */
	@Override
	public void run() {

		int i = 0;
        int sum = bigData.length-smallData.length;
        int size = sum;
		List<Result> resultList = new CopyOnWriteArrayList<>();
		for(int j=0;j<size/100+1;j++)
		{
			resultList.add(new Result());
		}

		//循环匹配
		while (i<bigData.length-smallData.length)
		{
			//开启子线程
			int finalI = i;
			if(sum>100)
			{
				i +=100;
				sum = sum-100;
			}
			else i+=sum;
			int finalJ = i;

			demo.executorService.execute(new Runnable() {
			@Override
			public void run() {

				int min = -1;
				int x = 0;
				int y = 0;
				try {
					for(int i = finalI; i< finalJ; i++)
					{
						for(int j=0;j<bigData[0].length-smallData[0].length;j++)
						{
							int cnt = compared(i,j,bigData,smallData );
							if(cnt>min)
							{
								min = cnt;
								y = j;
								x = i;
							}
						}
					}

				}finally {

					result = resultList.get(cnt);
                    result.setX(x);
					result.setY(y);
					result.setCount(min);
					//每个线程执行完 就cnt++
					cnt++;
				}
			}
		});
		}
			 /**
			 * 如果cnt==bigData.length-smallData.length 说明循环结束
			 */
			if(size%100==0) cnt+=1;
			while (cnt!=size/100+1)
			{
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			//拿到结果对象
			result = getMax(resultList);
			//计算相似度
			result.setSimilarity(result.getCount()*1.0/(smallData.length*smallData[0].length));

			//结束
			return;

	}


	/**
	 * 对比
	 * 以x,y为左上角,smallData大小的二维数组和smallData对比
	 * @param x
	 * @param y
	 * @param bigData
	 * @param smallData
	 * @return  两个二维数组中走形一样的点的个数
	 */
	public static int compared(int x,int y,int[][] bigData,int[][] smallData)
	{
		int cnt=0;
		for (int i = x; i < smallData.length+x; i++) {

			if((i-x)>=smallData.length/2 && cnt<(smallData.length*smallData[0].length)/3)
			{
				break;
			}
			for (int j = y; j < smallData[0].length+y; j++) {

				if(smallData[i-x][j-y]>0 && bigData[i][j]>0) cnt++;
				if(smallData[i-x][j-y]<0 && bigData[i][j]<0) cnt++;
				if(smallData[i-x][j-y]==0 && bigData[i][j]==0) cnt++;
			}
		}
		return cnt;
	}


	/**
	 * 返回最大次数的结果对象
	 * @param
	 * @return
	 */
	public static Result getMax(List<Result> res)
	{
		int cnt = Integer.MIN_VALUE;
		int index = -1;
		for(int i=0;i<res.size();i++)
		{
			if(res.get(i).getCount()>cnt)
			{
				cnt=res.get(i).getCount();
				index = i;
			}
		}
		return res.get(index);
	}

	public Result getResult() {
		return result;
	}
}
