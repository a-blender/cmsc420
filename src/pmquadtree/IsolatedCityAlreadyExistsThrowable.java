package cmsc420.pmquadtree;

public class IsolatedCityAlreadyExistsThrowable extends Throwable {
	private static final long serialVersionUID = 1L;
	
	public IsolatedCityAlreadyExistsThrowable() {
    }

    public IsolatedCityAlreadyExistsThrowable(String msg) {
    	super(msg);
    } 
}
