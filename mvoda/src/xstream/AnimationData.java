package xstream;

import lombok.Getter;
import lombok.Setter;

public class AnimationData {
	
	@Getter @Setter boolean reverse;
	@Getter @Setter boolean loop;
	@Getter @Setter int speed = 1; //all other variables are fine as defaults but speed should default to 1x not 0x
	
	public AnimationData(boolean reverse, boolean loop, int speed) { //TODO:use lombok allargs constructor annotation
	this.reverse = reverse;
	this.loop = loop;
	this.speed = speed;
	
	}

}
