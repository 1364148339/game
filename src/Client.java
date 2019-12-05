import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @Author: 王桥
 * @Date: 2019/12/4 20:50
 * @Version 1.0
 */
public class Client {
	//刷小怪
	static BufferedImage blameImage;
	//刷boss
	static BufferedImage bossBlameImage;
	//结束 成功
	static BufferedImage endSuccessImage;
	//打斗画面
	static BufferedImage fightImage;
    //宝箱
	static BufferedImage chestImage;
    //探索
	static BufferedImage exploreImage;
    //下一章
	static BufferedImage carryImage;

	static Robot robot;

	static {
		try {
			robot = new Robot();
			blameImage =ImageIO.read(new File("C:\\Users\\王先生\\Desktop\\game\\打怪.png"));
			bossBlameImage = ImageIO.read(new File("C:\\Users\\王先生\\Desktop\\game\\首领.png"));
            endSuccessImage = ImageIO.read(new File("C:\\Users\\王先生\\Desktop\\game\\结束成功.png"));
		    fightImage = ImageIO.read(new File("C:\\Users\\王先生\\Desktop\\game\\进入打斗.png"));
            chestImage = ImageIO.read(new File("C:\\Users\\王先生\\Desktop\\game\\宝箱.png"));
            exploreImage = ImageIO.read(new File("C:\\Users\\王先生\\Desktop\\game\\探索.png"));
            carryImage =  ImageIO.read(new File("C:\\Users\\王先生\\Desktop\\game\\下一章.png"));

		} catch (AWTException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		//1.识别探索
        while (true){
        	//识别到怪物标记
	        boolean flg = false;
        	//识别小怪
	        for(int i=0;i<5;i++)
	        {
	        	//识别成功 退出
		        if(blame(blameImage))
		        {
			        flg = true;
			        System.out.println("-----------识别成功-----------------");
			        break;
		        }
		        Thread.sleep(500);
	        }
	        //判断是否识别到小怪
	        if(flg==false)
	        {
		        System.out.println("-----------没有识别到小怪-----------------");
		        System.out.println("-----------开始识别boss-----------------");
		        //识别boss
		        for(int j=0;j<3;j++)
		        {
			        if(blame(bossBlameImage))
			        {
				        flg = true;
				        System.out.println("-----------识别成功-----------------");
				        break;
			        }

			        Thread.sleep(500);
		        }
	        }
	        //小怪 boss 都没有  识别宝箱  是否刷图结束
	        if(flg==false){

		        System.out.println("-----------没有识别到boss-----------------");
		        System.out.println("-----------开始识别宝箱-----------------");
		        for(int i=0;i<10;i++)
		        {
		        	//识别完  进入下一张图
                    if(!isChest(chestImage))
                    {
                    	break;
                    }
		        }
		        System.out.println("开始识别下一章-----------------");
		        Thread.sleep(3000);
		        Result result = demo.fff(carryImage);
		        if(result!=null)
		        {
			        System.out.println("下一章识别成功并点击-----------------");
			        click(result);
			        Thread.sleep(2000);
		        }else {
			        System.out.println("下一章识别失败-----------------");
		        }

		        Thread.sleep(1000);
                //开始识别探索
		        if(!isExplore(exploreImage))
		        {

				     System.out.println("游戏结束-----------------");
				     return;
		        }
		        //识别成功
		        else {
		        	Thread.sleep(3000);
			        //从新识别小怪
			        continue;
		        }


	        }


	        //判断是否进入打斗  如果没有 再次识别小怪 和boss
            if(!isFighting(fightImage)) continue;
            //进入 打斗 ---等待战斗是否结束
            isEnd();
            //等待一会
	        Thread.sleep(1237);
        }


	}

	//刷小怪 || boss
	public static boolean blame(BufferedImage image) throws Exception
	{
		System.out.println("-----------识别怪物-----------------");
		Result result = demo.fff(image);
		if(result==null){
			System.out.println("-----------没有识别到小怪-----------------");
		}
		else {
			click(result);
			return true;
		}
		return false;
	}

	/**
	 * 判断是否进入打斗
	 * @throws Exception
	 */
	public static boolean isFighting(BufferedImage image) throws Exception {
		Thread.sleep(3000);
        //识别两次 怕卡顿
		for(int i=0;i<5;i++)
		{
			Result result = demo.fff(image);
			if(result!=null) //进入打斗
			{
				System.out.println("-----------------进入打斗-----------------");
				return true;
			}
			//识别失败 暂停2s再次识别
			Thread.sleep(2000);
		}
		System.out.println("-----------------没有进入打斗-----------------");
		return false;
	}

	/**
	 * 判断是否打斗结束
	 * @throws Exception
	 */
	public static void isEnd() throws Exception {
		System.out.println("监视画面------->是否结束");
		while (true)
		{
			Result result = demo.fff(endSuccessImage);
			if(result!=null)
			{
				click(result);
				break;
			}
			//每两秒检查一下
			Thread.sleep(2000);
		}

		System.out.println("-----------------打斗结束-----------------");

	}

	/**
	 * 识别宝箱并点击
	 */
	public static boolean isChest(BufferedImage image) throws Exception {
		Result result = demo.fff(image);
		if(result!=null)
		{
			//点击
			click(result);
			Thread.sleep(300);
			//点击
			Result result1 = new Result();
			result1.setX(100);
			result1.setY(100);
			click(result1);
			Thread.sleep(1000);

			return true;
		}

		return false;
	}

	/**
	 * 识别探索并点击
	 */
	public static boolean isExplore(BufferedImage image) throws Exception {
		//识别探索
		System.out.println("-----------开始识别探索-----------------");

		for(int i=0;i<3;i++)
		{
			Result result = demo.fff(exploreImage);
			if(result!=null)
			{
				click(result);
				System.out.println("-----------识别成功并点击-----------------");
				return true;
			}
		}
		System.out.println("-----------识别探索失败-----------------");
		return false;
	}


	/**
	 * 点击
	 */
	public static void click(Result result)
	{
		robot.delay(200);
		robot.mouseMove(result.getX(),result.getY());
		robot.delay(500);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(680);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

}
