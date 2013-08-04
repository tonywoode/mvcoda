package themes;

import lombok.Getter;
import lombok.Setter;

public class AnimationData {

	@Getter @Setter boolean reverse;
	@Getter @Setter boolean loop;
	@Getter @Setter boolean numbers;
	@Getter @Setter int speed = 1; //all other variables are fine as defaults but speed should default to 1x not 0x

	public AnimationData(boolean reverse, boolean loop, boolean numbers, int speed) { //TODO:use lombok allargs constructor annotation
		this.reverse = reverse;
		this.loop = loop;
		this.numbers = numbers;
		this.speed = speed;
	}
}
