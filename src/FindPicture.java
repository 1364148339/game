
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
	private volatile int cnt = 0;
    //存每次遍历中符合点次数最多的一次
	private int[] res;
	//记录坐标
	private int[] resMap;
	//相似度
	private Double Similarity = 0D;
	//坐标
	private int x,y;

	public FindPicture(int[][] bigData,int[][] smallData)
	{
		this.bigData = bigData;
		this.smallData = smallData;
		this.res = new int[bigData.length-smallData.length];
		this.resMap = new int[bigData.length-smallData.length];
	}

	/**
	 * 寻图
	 */
	@Override
	public void run() {

		//循环匹配
		for (int i=0;i<bigData.length-smallData.length;i++)
		{

			//开启子线程
			int finalI = i;
			new Thread(){
				public void run()
				{
					int min = -1;
					int y = 0;
					try {
						  for(int j=0;j<bigData[0].length-smallData[0].length;j++)
						  {
							  int cnt = compared(finalI,j,bigData,smallData );
							  if(cnt>min)
							  {
								  min = cnt;
								  y = j;
							  }
						  }
					}finally {
						//每个线程执行完 就cn++
						cnt++;
						//存次数
						res[finalI]=min;
						//存坐标
						resMap[finalI]=y;
					}
				}

			}.start();
		}

		/**
		 * 如果cnt==bigData.length-smallData.length 说明循环结束
		 */
		while (cnt!=(bigData.length-smallData.length))
		{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//拿到序号
        int index = getMax(res);
		int max = res[index];
		//拿到坐标x,y
		x = index+smallData.length/2+240;
		y = resMap[index]+smallData[0].length/2+93;
		//计算相似度
		Similarity = max*1.0/(smallData.length*smallData[0].length);
//        1161 1587
		System.out.println(max+" "+smallData.length*smallData[0].length);
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

			if(i>=(smallData.length+x)/2 && cnt<(smallData.length*smallData[0].length)/7)
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
	 * 返回最大次数的序号
	 * @param res
	 * @return
	 */
	public static int getMax(int[] res)
	{
		int cnt = Integer.MIN_VALUE;
		int index = -1;
		for(int i=0;i<res.length;i++)
		{
			if(res[i]>cnt)
			{
				cnt=res[i];
				index = i;
			}
		}
		return index;
	}


	public Double getSimilarity() {
		return Similarity;
	}

	public void setSimilarity(Double similarity) {
		Similarity = similarity;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
