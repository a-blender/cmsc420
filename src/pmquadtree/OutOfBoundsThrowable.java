package cmsc420.pmquadtree;

public class OutOfBoundsThrowable extends Throwable {
	private static final long serialVersionUID = 1L;
	
	public OutOfBoundsThrowable() {
    }

    public OutOfBoundsThrowable(String msg) {
    	super(msg);
    } 
}
