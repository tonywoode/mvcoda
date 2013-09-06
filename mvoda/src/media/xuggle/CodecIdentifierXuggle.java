package media.xuggle;

import com.xuggle.xuggler.ICodec;

import media.CodecIdentifier;

public class CodecIdentifierXuggle extends CodecIdentifier{
	private ICodec.ID codecIdentifier;
	
	public CodecIdentifierXuggle(ICodec.ID codecIdentifier) {
		this.codecIdentifier = codecIdentifier;
	}
}
