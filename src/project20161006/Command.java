package project20161006;


import org.w3c.dom.Element;

public abstract class Command {

	public abstract void describe();
	public abstract void execute() throws ProcessException, Exception;
	public abstract void parse(Element element) throws ProcessException;
	public abstract String getId();

}
