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
    static int flg = 0;
    static ExecutorService executorService = Executors.newFixedThreadPool(15);
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

        BufferedImage subImage = image.getSubimage(0, 0, 967, 576);
        return subImage;

    }

    public static void main(String[] args) throws Exception {
        Long start = System.currentTimeMillis();
        //从对比库中拿出对比图
        BufferedImage smallImage1 = ImageIO.read(new File("C:\\Users\\王先生\\Desktop\\game\\下一章.png"));

        fff(smallImage1);



        //System.out.println( "相似度："+ f1.getSimilarity());

        System.out.println(System.currentTimeMillis() - start +"ms");


//       Robot robot = new Robot();
//        robot.mouseMove(f1.getX(),f1.getY());
//        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//        robot.delay(500);
//        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//        robot.delay(500);
//        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public static Result fff(BufferedImage smallImage) throws Exception {
        //得到当前屏幕主图
        BufferedImage bigImage = captureScreen();
        //小图宽
        int width = smallImage.getWidth();
        //小图高
        int height = smallImage.getHeight();
        //大图宽
        int widthBig = bigImage.getWidth();
        //大图高
        int heightBig = bigImage.getHeight();

        //图片矩阵像素点
        int[][] bigData = getData(bigImage);
        //转差值
        differenceData( bigData );
        //图片矩阵像素点
        int[][] smallData = getData(smallImage);
        //转差值
        differenceData( smallData );

        FindPicture f1 = new FindPicture(bigData,smallData);

        Thread s1 = new Thread(f1);
        s1.start();
        s1.join();
        Result result = f1.getResult();
        System.out.println(result.getSimilarity());
        if(result.getSimilarity()>0.5){

            return result;
        }
        return null;
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


}
