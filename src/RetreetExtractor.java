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

    List<String> locseq = new LinkedList<String>();		// the location sequence of a lexpr, the first element is the node, the rest of elements are pointers

    // TODO: may need to distinguish the field (e.g. n.v and n.left.v)
    Set<String> currRead = new HashSet<String>();	// read set for this block
	Set<String> currWrite = new HashSet<String>();	// write set for this block

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

    public void enterMain(RetreetParser.MainContext ctx) {
    	String name = ctx.getChild(0).getText();
    	funcs.add(name);
    	currFunc = name;
    	List<String> blocksInFunc = new LinkedList<String>();
    	funcBlock.put(currFunc, blocksInFunc);
    }

    public void enterFunc(RetreetParser.FuncContext ctx) {
    	String name = ctx.getChild(0).getText();
    	funcs.add(name);
    	currFunc = name;
    	List<String> blocksInFunc = new LinkedList<String>();
    	funcBlock.put(currFunc, blocksInFunc);
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
    	currRead.clear();
		currWrite.clear();
    }

    public void enterLexpr(RetreetParser.LexprContext ctx) {
    	locseq.clear();
    }

    public void exitLexpr(RetreetParser.LexprContext ctx) {
    	if (ctx.id() != null) {
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
    	block.read.addAll(currRead);
    	block.write.addAll(currWrite);
    	blocks.set(currindex, block);
    }

    public void exitCall(RetreetParser.CallContext ctx) {
    	//currWrite.add(ctx.getChild(0).getText());
    }

    public void enterAssgn(RetreetParser.AssgnContext ctx) {
    	if (ctx.getChildCount() == 4) {
    		currWrite.add(ctx.getChild(0).getText());
    	}
    }

    public void exitAexpr(RetreetParser.AexprContext ctx) {
    	if (ctx.id() != null || ctx.field() != null) {
    		currRead.add(ctx.getChild(0).getText());
    	}
    }

}