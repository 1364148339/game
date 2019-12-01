import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class gxRunable implements Runnable{
    private static int[][] bigData;
    private static int[][] smallData;
    private volatile int min = Integer.MIN_VALUE;
    private volatile int x,y;
    private volatile int cnt = 0;

    public gxRunable(int[][] bigData,int[][] smallData)
    {
       this.bigData = bigData;
       this.smallData = smallData;
    }

    @Override
    public void run() {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //循环匹配
        for (int i=0;i<bigData.length-smallData.length;i++)
        {
            int s = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {

                        for(int j=0;j<bigData[0].length-smallData[0].length;j++)
                        {
                            int cnt = compared( s,j,bigData,smallData );
                            if(cnt>min)
                            {
                                min=cnt;
                                x = s;
                                y = j;
                            }
                        }
                    }finally {
                        cnt++;
                        this.notifyAll();
                    }

                }
            });
        }

        while (cnt!=(bigData.length-smallData.length))
        {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static int compared(int x,int y,int[][] bigData,int[][] smallData)
    {
        int cnt=0;
        for (int i = x; i < smallData.length+x; i++) {

            for (int j = y; j < smallData[0].length+y; j++) {

                if(smallData[i-x][j-y]>0 && bigData[i][j]>0) cnt++;
                if(smallData[i-x][j-y]<0 && bigData[i][j]<0) cnt++;
                if(smallData[i-x][j-y]==0 && bigData[i][j]==0) cnt++;
            }
        }
        return cnt;
    }

    public int getMin()
    {
        return min;
    }
}
