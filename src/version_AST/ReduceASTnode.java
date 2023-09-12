package version_AST;

public abstract class ReduceASTnode {
	private static int node_count = 0;
	private int id;
	
	public ReduceASTnode()
	{
		this.id=node_count++;
	}
	
	public void setID(int id)
	{
		this.id=id;
	}
	
	
	public int getID()
	{
		return this.id;
	}
	
	public abstract String genast2xml(String xmlcontent, int structurelevel);
	public abstract String getExpreesion();
}
