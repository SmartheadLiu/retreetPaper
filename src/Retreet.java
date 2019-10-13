import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Retreet {
	public static void main( String[] args) throws Exception {
		ANTLRFileStream input = new ANTLRFileStream(args[0]);
		RetreetLexer lexer = new RetreetLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		RetreetParser parser = new RetreetParser(tokens);

		Logger logger = Logger.getLogger("main");

		ANTLRErrorStrategy es = new CustomErrorStrategy();
		parser.setErrorHandler(es);

		ParseTree tree = null;
		try{
			tree = parser.prog();
			logger.info("Accepted");
		} catch(Exception ex) {
			logger.info("Not Accepted");
			return;
		}

		ParseTreeWalker walker = new ParseTreeWalker();
        RetreetExtractor listener = new RetreetExtractor();
        walker.walk(listener, tree);

        List<String> funcs = listener.getFuncs();
        Map<String, Block> blocks = listener.getBlocks();
        Set<String> calls = listener.getCalls();
        Set<String> noncalls = listener.getNoncalls();
        Map<String, List<String>> funcBlock = listener.getFuncBlock();
        Map<String, List<String>> sequential = listener.getSequential();
        List<String> parallel = listener.getParallel();

        System.out.println("Print out AllFuncs:");
        for (String func : funcs) {
        	System.out.println("Function: " + func);
        }
        System.out.println();

        System.out.println("Print out AllBlocks:");
        for (String blockid : listener.getBlocklist()) {
            Block block = blocks.get(blockid);
        	System.out.println("Block id: " + block.getId() + " Text: " + block.getText());
            if (block.getCallFlag()) {
                System.out.println("It's a call block. It calls function: " + block.getCallname());
            }
            if (!block.callseq.isEmpty()) {
                System.out.println("The call sequence is: ");
                for (String s : block.callseq) {
                    System.out.println("  " + s);
                }
            }
            if (!block.readvar.isEmpty()) {
                System.out.println("The read set of variables: ");
                for (String s : block.readvar) {
                    System.out.println("     " + s);
                }
            }
            if (!block.readfield.isEmpty()) {
                System.out.println("The read set of fields: ");
                for (List<String> field : block.readfield) {
                    System.out.println("     field:");
                    for (String s : field) {
                        System.out.println("          " + s);
                    }
                }
            }
            if (!block.writevar.isEmpty()) {
                System.out.println("The write set of variables: ");
                for (String s : block.writevar) {
                    System.out.println("     " + s);
                }
            }
            if (!block.writefield.isEmpty()) {
                System.out.println("The write set of fields: ");
                for (List<String> field : block.writefield) {
                    System.out.println("     field:");
                    for (String s : field) {
                        System.out.println("          " + s);
                    }
                }
            }
        }
        System.out.println();

        System.out.println("Print out AllCalls:");
        for (String call : calls) {
        	System.out.println("Call block id: " + call);
        }
        System.out.println();

        System.out.println("Print out AllNoncalls:");
        for (String noncall : noncalls) {
        	System.out.println("Noncall block id: " + noncall);
        }
        System.out.println();

        System.out.println("Print out mapping of functions to blocks:");
        for (String func : funcBlock.keySet()) {
        	List<String> blockList = funcBlock.get(func);
        	System.out.println("Current function: " + func);
        	for (String b : blockList) {
        		System.out.println("	block: " + b);
        	}
        }
        System.out.println();

        System.out.println("Print out sequential relations in functions:");
        for (String func : sequential.keySet()) {
            List<String> seq = sequential.get(func);
            System.out.println("Current function: " + func);
            for (String b : seq) {
                System.out.println("    block: " + b);
            }
        }
        System.out.println();



        System.out.println("Print out reduced AllBlocks:");
        Map<String, Block> rblocks = listener.getRdcdBlocks();
        for (String blockid : listener.getRdcdBlocklist()) {
            Block block = rblocks.get(blockid);
            System.out.println("Block id: " + block.getId() + " Text: " + block.getText());
            if (block.getCallFlag()) {
                System.out.println("It's a call block. It calls function: " + block.getCallname());
            }
            if (!block.callseq.isEmpty()) {
                System.out.println("The call sequence is: ");
                for (String s : block.callseq) {
                    System.out.println("  " + s);
                }
            }
            if (!block.readvar.isEmpty()) {
                System.out.println("The read set of variables: ");
                for (String s : block.readvar) {
                    System.out.println("     " + s);
                }
            }
            if (!block.readfield.isEmpty()) {
                System.out.println("The read set of fields: ");
                for (List<String> field : block.readfield) {
                    System.out.println("     field:");
                    for (String s : field) {
                        System.out.println("          " + s);
                    }
                }
            }
            if (!block.writevar.isEmpty()) {
                System.out.println("The write set of variables: ");
                for (String s : block.writevar) {
                    System.out.println("     " + s);
                }
            }
            if (!block.writefield.isEmpty()) {
                System.out.println("The write set of fields: ");
                for (List<String> field : block.writefield) {
                    System.out.println("     field:");
                    for (String s : field) {
                        System.out.println("          " + s);
                    }
                }
            }
        }
        System.out.println();

        System.out.println("Print out reduced AllNoncalls:");
        for (String noncall : listener.getRdcdNoncalls()) {
            System.out.println("Noncall block id: " + noncall);
        }
        System.out.println();

        System.out.println("Print out reduced mapping of functions to blocks:");
        Map<String, List<String>> rfuncBlock = listener.getRdcdFuncBlock();
        for (String func : rfuncBlock.keySet()) {
            List<String> blockList = rfuncBlock.get(func);
            System.out.println("Current function: " + func);
            for (String b : blockList) {
                System.out.println("    block: " + b);
            }
        }
        System.out.println();


        Generator generator = new Generator("test", listener);
        generator.genfuse();

	}
}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException err){
		throw err;
	}	
}	