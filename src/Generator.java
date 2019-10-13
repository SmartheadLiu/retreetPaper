import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;

public class Generator {
	RetreetExtractor unfused;
	RetreetExtractor fused;
	RetreetExtractor relation;
	PrintWriter writer;

	public Generator(String filename, RetreetExtractor unfused) {
		this.unfused = unfused;
		File file = new File("//Users//yanjunwang//Documents//work//fusion//implementation//output//" + filename + ".mona");
		// file.getParentFile().mkdirs();
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException fnfe) {
			System.out.println(fnfe);
		}
		
	}

	public String l(String prefix, String blockid, String varname) {
		return prefix + blockid + varname;
	}

	// generate argument list for predicates
	public String genarglist(String p, String var, List<String> list) {
		// main function should have an unique blockid
		String arglist = l(p, "main", var);
		for (String blockid : list) {
			arglist = arglist + ", " + l(p, blockid, var);
		}
		return arglist;
	}

	// generate mona file that checks the validity of fusion.
	public void genfuse() {
		writer.println("ws2s;\n");
		genconfig("Configuration", "C", unfused);
		writer.close();
		
	}

	// generate configuration predicate 
	public void genconfig(String configname, String p, RetreetExtractor extractor) {
		List<String> funcs = extractor.getFuncs();
		Map<String, Block> rblocks = extractor.getRdcdBlocks();
		List<String> rblocklist = extractor.getRdcdBlocklist();
		Set<String> calls = extractor.getCalls();
		Set<String> rnoncalls = extractor.getRdcdNoncalls();
		Map<String, List<String>> rfuncBlock = extractor.getRdcdFuncBlock();
		Map<String, List<String>> sequential = extractor.getSequential();


		String v = "x";
		// first the predicate signature
		writer.print("pred " + configname + "(var1 x, var2 ");
		writer.print(genarglist(p, v, rblocklist));
		writer.println(") = ");

		writer.println("\t(root in " + l(p, "main", v) + ")");

		if(!rfuncBlock.get("main").isEmpty()) {
			List<String> binMain = rfuncBlock.get("main");
			writer.print("\t& ~(ex1 v where v ~= root: ");
			for (int i = 0; i < binMain.size(); i++) {
				if (i == 0) {
					writer.print("v in " + l(p, binMain.get(i), v));
				} else {
					writer.print(" | v in " + l(p, binMain.get(i), v));
				}
			}
			writer.println(")");
		}


	}



}	