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

	public String getOr(List<String> store, String tabs, boolean newline) {
		String ret = "";
		for (int i = 0; i < store.size(); i++) {
			if (i == 0) {
				ret = ret + store.get(i);
			} else {
				if (newline) {
					ret = ret + "\n" + tabs + "| " + store.get(i);
				} else {
					ret = ret + tabs + " | " + store.get(i);
				}
			}
		}
		return ret;
	}

	public String getAnd(List<String> store, String tabs, boolean newline) {
		String ret = "";
		for (int i = 0; i < store.size(); i++) {
			if (i == 0) {
				ret = ret + store.get(i);
			} else {
				if (newline) {
					ret = ret + "\n" + tabs + "& " + store.get(i);
				} else {
					ret = ret + tabs + " & " + store.get(i);
				}
			}
		}
		return ret;
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
		List<String> ortmp = new LinkedList<String>();
		List<String> andtmp = new LinkedList<String>();
		List<String> innerandtmp = new LinkedList<String>();

		String v = "x";
		// first the predicate signature
		writer.print("pred " + configname + "(var1 x, var2 ");
		writer.print(genarglist(p, v, rblocklist));
		writer.println(") = ");

		// root should be in main block
		writer.println("\t(root in " + l(p, "main", v) + ")");

		// other nodes should not in blocks of main function
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

		// only one of blocks of main function should in root
		if (rfuncBlock.get("main").size() > 1) {
			// if there are more than one blocks in main function
			List<String> binmain = rfuncBlock.get("main");
			writer.print("\t& (");
			for (int i = 0; i < binmain.size(); i++) {
				if (i > 0) {
					writer.print("\t\t& ");
				}
				writer.print("(root in " + l(p, binmain.get(i), v) + " => (");
				List<String> store = new LinkedList<String>();
				for (int j = 0; j < binmain.size(); j++) {
					if (i != j) {
						store.add("root notin " + l(p, binmain.get(j), v));
					}
				}
				writer.print(getAnd(store, "", false));
				writer.print("))\n");
			}
			writer.println("\t\t)");
		}

		// non-leaf nodes should be calls, first consider the special case: main function, then call in calls
		writer.println("\t& (all1 u where u ~= x: (");
		writer.print("\t\t\t");
		for (String id : rfuncBlock.get("main")) {
			// for every block b in blocks of func
			if (calls.contains(id)) {
				// check if b is also in calls, which means b is a call block
				List<String> callseq = rblocks.get(id).callseq;
				String seq = "";
				for (int i = 0; i < callseq.size(); i++) {
					if (i == 0) {
						// assume all calls work on the same node. Maybe TODO later.
						seq = seq + "u";
					} else {
						if (callseq.get(i).equals("left")) {
							seq = seq + ".0";
						} else if (callseq.get(i).equals("right")) {
							seq = seq + ".1";
						}
					}
				}
				ortmp.add(seq + " in " + l(p, id, v));
			}
		}
		andtmp.add("(u in " + l(p, "main", v) + " => " + "(" + getOr(ortmp, "", false) + ") )");
		ortmp.clear();
		for (String callid : calls) {
			// node u is in a call block implies that
			// there are nodes (u itself or u.0, u.1 ...) in the blocks of the called function
			String funcname = rblocks.get(callid).getCallname();
			for (String id : rfuncBlock.get(funcname)) {
				// for every block b in blocks of func
				if (calls.contains(id)) {
					// check if b is also in calls, which means b is a call block
					List<String> callseq = rblocks.get(id).callseq;
					String seq = "";
					for (int i = 0; i < callseq.size(); i++) {
						if (i == 0) {
							// assume all calls work on the same node. Maybe TODO later.
							seq = seq + "u";
						} else {
							if (callseq.get(i).equals("left")) {
								seq = seq + ".0";
							} else if (callseq.get(i).equals("right")) {
								seq = seq + ".1";
							}
						}
					}
					ortmp.add(seq + " in " + l(p, id, v));
				}
			}
			andtmp.add("(u in " + l(p, callid, v) + " => " + "(" + getOr(ortmp, "", false) + ") )");
			ortmp.clear();
		}
		writer.print(getAnd(andtmp, "\t\t\t", true));
		andtmp.clear();
		writer.println("\n\t\t\t)\n\t\t)");

		// if x is in a call, then x should be in one and only one of noncall blocks that correspond to that call
		// for every call in calls
		// find out the function that the call belongs to
		// find out the noncalls in that function
		// x should be in one of the noncalls.
		writer.print("\t& (");
		List<String> ncinfunc = new LinkedList<String>();
		for (String id : rfuncBlock.get("main")) {
			if (rnoncalls.contains(id)) {
				ncinfunc.add(id);
			}
		}
		for (int i = 0; i < ncinfunc.size(); i++) {
			innerandtmp.add(v + " in " + l(p, ncinfunc.get(i), v));
			for (int j = 0; j < ncinfunc.size(); j++) {
				if (i != j) {
					innerandtmp.add(v + " notin " + l(p, ncinfunc.get(j), v));
				}
			}
			ortmp.add("(" + getAnd(innerandtmp, "", false) + ")");
			innerandtmp.clear();
		}
		ncinfunc.clear();
		andtmp.add("(" + v + " in " + l(p, "main", v) + " => " + getOr(ortmp, "", false) + ")");
		ortmp.clear();
		for (String callid : calls) {
			String funcname = rblocks.get(callid).getCallname();
			for (String id : rfuncBlock.get(funcname)) {
				if (rnoncalls.contains(id)) {
					ncinfunc.add(id);
				}
			}
			for (int i = 0; i < ncinfunc.size(); i++) {
				innerandtmp.add(v + " in " + l(p, ncinfunc.get(i), v));
				for (int j = 0; j < ncinfunc.size(); j++) {
					if (i != j) {
						innerandtmp.add(v + " notin " + l(p, ncinfunc.get(j), v));
					}
				}
				ortmp.add("(" + getAnd(innerandtmp, "", false) + ")");
				innerandtmp.clear();
			}
			ncinfunc.clear();
			andtmp.add("(" + v + " in " + l(p, callid, v) + " => " + getOr(ortmp, "", false) + ")");
			ortmp.clear();
		}
		writer.print(getAnd(andtmp, "\t\t", true));
		andtmp.clear();
		writer.println("\n\t\t)");

		// for node v which is not x, v should not in any of noncall blocks
		writer.println("\t& (all1 v where v ~= x:");
		for (String noncallid : rnoncalls) {
			andtmp.add("v notin " + l(p, noncallid, v));
		}
		writer.print("\t\t(");
		writer.print(getAnd(andtmp, "", false));
		andtmp.clear();
		writer.print(")");
		writer.println("\n\t\t)");

		// if x is in one of noncall blocks, then x is not in any other noncall block
		writer.print("\t&(");
		for (String i : rnoncalls) {
			for (String j : rnoncalls) {
				if (!i.equals(j)) {
					innerandtmp.add(v + " notin " + l(p, j, v));
				}

			}
			andtmp.add("(" + v + " in " + l(p, i, v) + " => (" + getAnd(innerandtmp, "", false) + "))");
			innerandtmp.clear();
		}
		writer.print(getAnd(andtmp, "\t\t", true));
		andtmp.clear();
		writer.println("\n\t\t)");

		// for node z which is not root, 
		// if z is in call block that is calling on children nodes, 
		// then there exist w where w is the parent of z
		writer.print("\t&(");
		// for every call in calls, find out their corresponding block
		// check if the call is calling on child node
		// convert the callseq to "0" or "1" ...
		for (String callid : calls) {
			List<String> callseq = rblocks.get(callid).callseq;
			if (callseq.size() > 1) {
				String seq = "";
				for (int i = 1; i < callseq.size(); i++) {
					if (callseq.get(i).equals("left")) {
						seq = seq + ".0";
					} else if (callseq.get(i).equals("right")) {
						seq = seq + ".1";
					}
				}
				String zinLabel = "z in " + l(p, callid, v);
				andtmp.add("(" + zinLabel + " => ex1 w: w" + seq + " = z)");
			}
		}
		writer.print(getAnd(andtmp, "\t\t", true));
		andtmp.clear();
		writer.print("\n\t\t)");

		// configuration comes to an end
		writer.println(";");

	}







}	