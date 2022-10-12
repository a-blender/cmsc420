package cmsc420.pmquadtree;

public class RoadAlreadyExistsThrowable extends Throwable {
	private static final long serialVersionUID = 1L;
	
	public RoadAlreadyExistsThrowable() {
    }

    public RoadAlreadyExistsThrowable(String msg) {
    	super(msg);
    } 
}
