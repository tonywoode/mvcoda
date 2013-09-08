package themes;

import lombok.Getter;
import lombok.Setter;

/**
 * Animation data is used by GFXElements and describes what the animation properties are - these are:
 * 		Reverse - if the element has no animate-out, we will reverese the in-animation to animate out
 * 		Speed - the speed at which we will reverse out, if the above applies
 * 		Loop - some animations have a looping hold point so we need to loop this while at the hold point
 * 		Numbers - numbered animations are groups of animations that describe what chart position the music video is on, these are found differently from non-grouped elements
 * @author tony
 *
 */
public class AnimationData {

	@Getter @Setter boolean reverse;
	@Getter @Setter boolean loop;
	@Getter @Setter boolean numbers;
	@Getter @Setter int speed = 1; //all other variables are fine as defaults but speed should default to 1x not 0x

	public AnimationData(boolean reverse, boolean loop, boolean numbers, int speed) {
		this.reverse = reverse;
		this.loop = loop;
		this.numbers = numbers;
		this.speed = speed;
	}
}
