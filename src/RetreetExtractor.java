import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class RetreetExtractor extends RetreetBaseListener {

    List<String> funcs = new LinkedList<String>();		// AllFuncs
    List<String> params = new LinkedList<String>();		// TODO
    List<Block> blocks = new LinkedList<Block>();		// AllBlocks
    Set<String> calls = new HashSet<String>();		// AllCalls: a set of ids of the call block
    Set<String> noncalls = new HashSet<String>();	// AllNonCalls: a set of ids of the noncall block

    Map<String, List<String>> funcBlock = new LinkedHashMap<String, List<String>>();		// Blocks(f)
    Map<String, List<String>> funcParam = new LinkedHashMap<String, List<String>>();		// Params(f) TODO

    // Map<String, List<String>> read = new LinkedHashMap<String, List<String>>();		// read set
    // Map<String, List<String>> write = new LinkedHashMap<String, List<String>>();		// write set

    Block mainBlock; 		// should recognize which block is the main block; and the relationship between the calls in main and the functions
    String currFunc;
    String currBlock;

    Map<String, List<String>> sequential = new LinkedHashMap<String, List<String>>();// TODO: to complete in the future
    List<String> parallel = new LinkedList<String>(); // parallelization only happens in main function

    List<String> locseq = new LinkedList<String>();		// the location sequence of a lexpr, the first element is the node, the rest of elements are pointers

    Set<String> currReadVar = new HashSet<String>();	// read set of variables for this block
	Set<String> currWriteVar = new HashSet<String>();	// write set of variables for this block
    Set<List<String>> currReadField = new HashSet<List<String>>();   // read set of fields for this block
    Set<List<String>> currWriteField = new HashSet<List<String>>();  // write set of fields for this block

    public List<String> getFuncs() {
    	return this.funcs;
    }

    public List<Block> getBlocks() {
    	return this.blocks;
    }

    public Set<String> getCalls() {
    	return this.calls;
    }

    public Set<String> getNoncalls() {
    	return this.noncalls;
    }

    public Map<String, List<String>> getFuncBlock() {
    	return this.funcBlock;
    }

    public Map<String, List<String>> getSequential() {
        return this.sequential;
    }

    public List<String> getParallel() {
        return this.parallel;
    }

    public void enterMain(RetreetParser.MainContext ctx) {
    	String name = ctx.getChild(0).getText();
    	funcs.add(name);
    	currFunc = name;
    	List<String> blocksInFunc = new LinkedList<String>();
    	funcBlock.put(currFunc, blocksInFunc);
        List<String> seqInFunc = new LinkedList<String>();
        sequential.put(currFunc, seqInFunc);
    }

    public void enterFunc(RetreetParser.FuncContext ctx) {
    	String name = ctx.getChild(0).getText();
    	funcs.add(name);
    	currFunc = name;
    	List<String> blocksInFunc = new LinkedList<String>();
    	funcBlock.put(currFunc, blocksInFunc);
        List<String> seqInFunc = new LinkedList<String>();
        sequential.put(currFunc, seqInFunc);
    }

    public void enterBlock(RetreetParser.BlockContext ctx) {
    	String blockid = ctx.getParent().getChild(2).getText();
    	Block newblock = new Block(blockid, ctx.getText());
    	if (ctx.call() != null) {
    		newblock.setCall();
    		newblock.setCallname(ctx.call().getChild(0).getText());
    		calls.add(blockid);
    	}
    	if (ctx.assgn_list() != null) {
    		newblock.setNoncall();
    		noncalls.add(blockid);
    	}
    	blocks.add(newblock);
    	// add this block id to the current function-block mapping
    	List<String> blocksInFunc = funcBlock.get(currFunc);
    	blocksInFunc.add(blockid);
    	funcBlock.put(currFunc, blocksInFunc);
    	// reset the read set and write set
    	currReadVar.clear();
		currWriteVar.clear();
        currReadField.clear();
        currWriteField.clear();
        // add this block id to current sequential
        List<String> seqInFunc = sequential.get(currFunc);
        seqInFunc.add(blockid);
        sequential.put(currFunc, seqInFunc);

    }

    // public void enterLexpr(RetreetParser.LexprContext ctx) {
    // 	locseq.clear();
    // }

    public void exitLexpr(RetreetParser.LexprContext ctx) {
    	if (ctx.id() != null) {
            locseq.clear();
    		locseq.add(ctx.id().getText());
    	} else {
    		locseq.add(ctx.getChild(2).getText());
    	}
    }

    public void exitArg_list(RetreetParser.Arg_listContext ctx) {
    	int currindex = blocks.size() - 1;
    	Block block = blocks.get(currindex);
    	block.callseq.clear();
    	block.callseq.addAll(locseq);
    	blocks.set(currindex, block);
    }

    public void exitBlock(RetreetParser.BlockContext ctx) {
    	int currindex = blocks.size() - 1;
    	Block block = blocks.get(currindex);
    	block.readvar.addAll(currReadVar);
    	block.writevar.addAll(currWriteVar);
        block.readfield.addAll(currReadField);
        block.writefield.addAll(currWriteField);
    	blocks.set(currindex, block);
    }

    // public void exitCall(RetreetParser.CallContext ctx) {
    // 	currWrite.add(ctx.getChild(0).getText());
    // }

    public void exitAssgn(RetreetParser.AssgnContext ctx) {
    	if (ctx.id() != null) {
    		currWriteVar.add(ctx.id().getText());
    	}
    }

    public void exitField(RetreetParser.FieldContext ctx) {
        if (ctx.getParent().getChildCount() == 4) {
            // field written in assgn
            List<String> field = new LinkedList<String>();
            field.addAll(locseq);
            field.add(ctx.getChild(2).getText());
            currWriteField.add(field);
        } else if (ctx.getParent().getChildCount() == 1) {
            // field read in aexpr
            List<String> field = new LinkedList<String>();
            field.addAll(locseq);
            field.add(ctx.getChild(2).getText());
            currReadField.add(field);
        }
    }

    public void exitAexpr(RetreetParser.AexprContext ctx) {
    	if (ctx.id() != null) {
    		currReadVar.add(ctx.getChild(0).getText());
    	}
    }

    public void exitIf_part(RetreetParser.If_partContext ctx) {
        // TODO!! 
        // actually sequential should be a set of new data-structures
        // which stores the path condition and the corresponding sequential
        // here we assume there is only one conditional in each function
        // and the if branch is "isNil"
        List<String> seqInFunc = sequential.get(currFunc);
        seqInFunc.clear();
        sequential.put(currFunc, seqInFunc);
    }

    public void enterStmt(RetreetParser.StmtContext ctx) {
        // TODO!!
        // here we assume parallelization only happens in main function
        // and there is only one pair of parallelization in a function
        if (ctx.getChildCount() == 5) {
            parallel.add(ctx.getChild(1).getChild(2).getText());
            parallel.add(ctx.getChild(3).getChild(2).getText());
        }
    }

}