/**
 * 寻图结果集
 * @Author: 王桥
 * @Date: 2019/12/3 15:06
 * @Version 1.0
 */
public class Result {
	//坐标
	private int x,y;
	//相似点个数
	private int count;
	//相似度
	private Double Similarity;


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

	public int getCount() {

		return count;
	}

	public void setCount(int count) {

		this.count = count;
	}

	public Double getSimilarity() {
		return Similarity;
	}

	public void setSimilarity(Double similarity) {
		Similarity = similarity;
	}
}
