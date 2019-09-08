/*
 * 8. Nodes(T)
 * 9. Path(t) 
 */
public class Nodes extends retreetBaseVisitor<String> {
	static String nodes[]=new String[50];
	int index=0;
	
	public String visitLexpr(retreetParser.LexprContext ctx) {
		int flag=0;
		String node = ctx.getText();
		for(int i=0;i<index;i++){
			if(node.equals(nodes[i]))
				flag=1;     //1 means the node has been recorded while 0 means oppositely
		}
		if(flag==0){
			nodes[index]=node;
			System.out.println("node "+(index+1)+": "+node);
			index++;
		}
		return visitChildren(ctx); 
	}
}
