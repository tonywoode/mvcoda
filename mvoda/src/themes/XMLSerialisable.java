package themes;

/**
 * Used as a shared type for XML serialisable classes i.e.: so we can have generic reader and writer
 * @author tony
 *
 */
public interface XMLSerialisable {
	
	String getItemName();
	
	@Override
	String toString();	
	
	
}
