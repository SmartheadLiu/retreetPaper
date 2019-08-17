public class ShowPath extends retreetBaseListener {
	static int func_sign = 0;
	String func;
	String block;
	int count = 0;
	static int if_sign = 0;
	static int else_sign = 0;
	String[] if_block = new String[10];
	String[] block_node = new String[50];
	int if_count=0;
	int index=0;
	int flag=0;
	

	public ShowPath(String f, String b) {
		this.func = f;
		this.block = b;
	}
	
	public void enterMain(retreetParser.MainContext ctx) {
		String funcid = "main";
		if (funcid.equals(func)) {
			func_sign = 1;
			System.out.println("Path of block (" + block + ") is:");
		}
	}
	public void exitMain(retreetParser.MainContext ctx) {
		func_sign=0;
		for(String x:if_block){
			x=null;
		};
		for(String x:block_node){
			x=null;
		}
	}
	
	public void enterCalledfuncwithaexpr(retreetParser.CalledfuncwithaexprContext ctx) {
		String funcid = ctx.funcid().getText();
		if (funcid.equals(func)) {
			func_sign = 1;
			System.out.println("Path of block (" + block + ") is:");
		}
	}
	public void exitCalledfuncwithaexpr(retreetParser.CalledfuncwithaexprContext ctx) {
		func_sign=0;
		for(String x:if_block){
			x=null;
		};
		for(String x:block_node){
			x=null;
		}
	}
	public void enterCalledfuncnoaexpr(retreetParser.CalledfuncnoaexprContext ctx) {
		String funcid = ctx.funcid().getText();
		if (funcid.equals(func)) {
			func_sign = 1;
			System.out.println("Path of block (" + block + ") is:");
		}
	}
	
	public void exitCalledfuncnoaexpr(retreetParser.CalledfuncnoaexprContext ctx) {
		func_sign=0;
		for(String x:if_block){
			x=null;
		};
		for(String x:block_node){
			x=null;
		}
	}
	
	public void enterIfpart(retreetParser.IfpartContext ctx) {
		if (func_sign != 0){
			if_sign=1;
			flag=1;
			String condition = ctx.bexpr().getText();
			for(int i=0;i<block_node.length;i++){
				if(block_node[i]==null){
					block_node[i]=condition;
					index=i;
					break;
				}
			}
			if_count++;
		}
	}
	public void exitIfpart(retreetParser.IfpartContext ctx) {
		if_sign=0;
	}
	public void enterElsepart(retreetParser.ElsepartContext ctx) {
		if (func_sign != 0){
			if_sign=0;
			else_sign=1;
		}	
	}
	public void exitElsepart(retreetParser.ElsepartContext ctx) {
		else_sign=0;
		if_count++;
	}
	
	
	public void enterBlock(retreetParser.BlockContext ctx) {
		if(func_sign==1){
			String currentBlock = ctx.getText();
			
			if(if_sign==1&&else_sign==0){  //in if
				if(currentBlock.equals(block)){  //arrived
					for(int i=0;i<block_node.length;i++){
						if(block_node[i]!=null)
							System.out.println((i+1)+": "+block_node[i]);
						else{
							System.out.println((i+1)+": "+currentBlock+"  --> arrived");
							System.out.println();
							break;
						}
							
							
					}
					func_sign=0;
				}	
				else
					for(int i=0;i<block_node.length;i++){
						if(block_node[i]==null){
							block_node[i]=currentBlock;
						}
						break;
					}
				}
			if(if_sign==0&&else_sign==1){ // in else
			
				if(flag!=0){
					block_node[index]='!'+block_node[index];
					for(int i=index+1;i<block_node.length;i++){
						if(block_node[i]!=null)
							block_node[i]=null;
						else
							break;
					}
					flag=0;
				}
				if(currentBlock.equals(block)){
					for(int i=0;i<block_node.length;i++){
						if(block_node[i]!=null)
							System.out.println((i+1)+": "+block_node[i]);
						else{
							System.out.println((i+1)+": "+currentBlock+"  --> arrived");
							System.out.println();
							break;
						}
					}
					func_sign=0;
				}	
				else
					for(int i=0;i<block_node.length;i++){
						if(block_node[i]==null){
							block_node[i]=currentBlock;
							break;
						}
					}
				}
			if(if_sign==0&&else_sign==0)  //not in ifstmt
			{
				if(currentBlock.equals(block)){
					for(int i=0;i<block_node.length;i++){
						if(block_node[i]!=null)
							System.out.println((i+1)+": "+block_node[i]);
						else{
							System.out.println((i+1)+": "+currentBlock+"  --> arrived");
							System.out.println();
							break;
						}	
					}
				}
				else
					for(int i=0;i<block_node.length;i++){
						if(block_node[i]==null){
							block_node[i]=currentBlock;
							break;
						}
					}
			}
		}
	}
}