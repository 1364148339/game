import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class demo {

    //得到当前屏幕主图
    static int [][] bigData = null;
    //从对比库中拿出对比图
    static int [][] smallData = null;
    volatile static int min = Integer.MIN_VALUE;
    volatile static int x = 0,y = 0;
    volatile static int cntt = 0;
    static int k;//宽
    static int h;//高

    /**
     * 返回当前屏幕主图
     * @return
     * @throws Exception
     */
    public static BufferedImage captureScreen() throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);

        // 指定屏幕区域，参数为截图左上角坐标(100,100)+右下角坐标(500,500)

        BufferedImage subImage = image.getSubimage(240, 130, 1440, 810);

        return subImage;

    }

    public static void main(String[] args) throws Exception {
        Long start = System.currentTimeMillis();

        //得到当前屏幕主图
        BufferedImage bigImage = ImageIO.read(new File("C:\\Users\\王先生\\Desktop\\game\\主屏幕.png"));
        //从对比库中拿出对比图
        BufferedImage smallImage = ImageIO.read(new File("C:\\Users\\王先生\\Desktop\\game\\打怪.png"));
        //小图宽
        int width = smallImage.getWidth();
        //小图高
        int height = smallImage.getHeight();
        //大图宽
        int widthBig = bigImage.getWidth();
        //大图高
        int heightBig = bigImage.getHeight();


        //图片矩阵像素点
        bigData = getData(bigImage);
        //转差值
        differenceData( bigData );
        //图片矩阵像素点
        smallData = getData(smallImage);
        //转差值
        differenceData( smallData );

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //循环匹配
        for (int i=0;i<bigData.length-width;i++)
        {
            int s = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {

                        for(int j=0;j<bigData[0].length-height;j++)
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
                        cntt++;
                    }

                }
            });
        }

        while (cntt!=bigData.length-width)
        {
            Thread.sleep(200);
        }
        //求中心点
        x = x+width/2+240; //宽
        y = y+height/2+93; //高
        Robot robot = new Robot();
        robot.mouseMove(x,y);

        System.out.println( "相似度："+min*1.0/(width*height) );

        System.out.println(System.currentTimeMillis() - start +"ms");
    }


    /**
     * 得到图片像素矩阵
     * @param image
     * @return
     */
    public static int[][] getData(BufferedImage image)
    {
        //宽
        int imageWidth = image.getWidth();
        //高
        int imageHeight = image.getHeight();
        //图片矩阵像素点
        int [][] data = new int[imageWidth][imageHeight];
        for(int i=0;i<imageWidth;i++)
        {
            for(int j=0;j<imageHeight;j++)
            {
                data[i][j]=image.getRGB(i,j);
            }
        }
        return data;
    }

    /**
     * 得到 宽差值
     * @param data
     * @return
     */
    public static void differenceData(int[][] data)
    {
        for(int i=0;i<data.length;i++)
        {
            for(int j=0;j<data[0].length-1;j++)
            {
                data[i][j] = data[i][j] - data[i][j+1];
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


}
