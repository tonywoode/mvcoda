package xstream;

import lombok.Getter;

public interface XMLSerialisable {

	@Getter public String itemName = ""; //TODO: hang on why am I having to declare this final static thing?
}
