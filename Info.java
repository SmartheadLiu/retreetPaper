/* 1. AllFuncs
 * 2. AllParams
 * 3. AllBlocks
 * 4. AllCalls
 * 5. AllNonCalls
 * 6. Blocks(f)
 * 7. Params(f)
 * 8. Nodes(T)
 * 9. Path(t) 
 */
public class Info extends retreetBaseListener {

	static String[] allfuncs = new String[10]; // save AllFuncs
	static String[] allparams = new String[10]; // save AllParams
	static String[] allblocks = new String[50];//save AllBlocks
	static String[] allcalls = new String[50];//save AllCalls
	static String[] allnocalls = new String[50];//save AllNoCalls
	static int funcs_count = 0; // counter of functions
	static int params_count = 0;
	static int blocks_count = 0;
	static int calls_count = 0;
	static int assigns_count = 0;


	public void enterMain(retreetParser.MainContext ctx) {
		/* 1. main function */
		allfuncs[0] = "main";
		funcs_count++;
		System.out.println("enter main function \t\tname£º" + allfuncs[0]);
	}

	public void enterCalledfuncnoaexpr(retreetParser.CalledfuncnoaexprContext ctx) {
		/* 1. called functions without aexpr */
		String calledfunc = ctx.funcid().getText();
		for (int i = 0; i < allfuncs.length; i++) {
			if (allfuncs[i] == null) {
				allfuncs[i] = calledfunc;
				break;
			}
		}
		funcs_count++;
		System.out.println("enter called function No." + (funcs_count - 1) + "\tname£º" + calledfunc);
	}

	public void exitCalledfuncwithaexpr(retreetParser.CalledfuncwithaexprContext ctx) {
		/* 1. callde function with aexpr */
		String calledfunc = ctx.funcid().getText();
		for (int i = 0; i < allfuncs.length; i++) {
			if (allfuncs[i] == null) {
				allfuncs[i] = calledfunc;
				break;
			}
		}
		funcs_count++;
		System.out.println("enter called function No." + (funcs_count - 1) + "\tname£º" + calledfunc);

		/* 2. AllParams */
		String intParam = ctx.aexpr().getText();
		for (int i = 0; i < allparams.length; i++) {
			if (allparams[i] == null) {
				allparams[i] = intParam;
				break;
			}
		}
		params_count++;
	}
	
	public void enterBlock(retreetParser.BlockContext ctx) {
		/* 3. AllBlocks */
		String block= ctx.getText();
		for (int i = 0; i < allblocks.length; i++) {
			if (allblocks[i] == null) {
				allblocks[i] = block;
				break;
			}
		}
		blocks_count++;
	}

	public void enterFunccallwithaexpr(retreetParser.FunccallwithaexprContext ctx) {
		/* 4. AllCalls */ 
		String call = ctx.getText();
		for (int i = 0; i < allcalls.length; i++) {
			if (allcalls[i] == null) {
				allcalls[i] = call;
				break;
			}
		}
		calls_count++;
	}
	
	public void enterFunccallnoaexpr(retreetParser.FunccallnoaexprContext ctx) {
		/* 4. AllCalls */ 
		String call = ctx.getText();
		for (int i = 0; i < allcalls.length; i++) {
			if (allcalls[i] == null) {
				allcalls[i] = call;
				break;
			}
		}
		calls_count++;
	}
	
	 public void enterAssgn(retreetParser.AssgnContext ctx) { 
		/* 5. AllNonCalls*/
		String assign = ctx.getText();
		for (int i = 0; i < allnocalls.length; i++) {
			if (allnocalls[i] == null) {
				allnocalls[i] = assign;
				break;
			}
		}
		assigns_count++;
	}
	
}
