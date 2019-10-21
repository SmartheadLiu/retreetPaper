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

	public Generator(String filename, RetreetExtractor unfused, RetreetExtractor fused, RetreetExtractor relation) {
		this.unfused = unfused;
		this.fused = fused;
		this.relation = relation;
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
		writer.println();
		genconfig("ConfigurationFused", "D", fused);
		writer.println();
		genordered("Ordered", "Configuration", "C", unfused);
		writer.println();
		genordered("OrderedFused", "ConfigurationFused", "D", fused);
		writer.println();
		gendependence("Ordered", "C", unfused);
		writer.println();
		genconvert("C", "D", unfused.getRdcdBlocklist(), fused.getRdcdBlocklist(), relation);
		writer.println();
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
		List<String> ortmp = new LinkedList<String>();
		List<String> andtmp = new LinkedList<String>();
		List<String> innerandtmp = new LinkedList<String>();

		String v = "x";
		// first the predicate signature
		writer.print("pred " + configname + "(var1 " + v + ", var2 ");
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
		writer.print("\t& (");
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
		writer.println("\t& (all1 z where z ~= root:");
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
		writer.print("\t\t");
		writer.print(getAnd(andtmp, "\t\t", true));
		andtmp.clear();
		writer.print("\n\t\t)");

		// configuration comes to an end
		writer.println(";");

	}

	public void genordered(String ordername, String configname, String p, RetreetExtractor extractor) {
		List<String> funcs = extractor.getFuncs();
		Map<String, Block> rblocks = extractor.getRdcdBlocks();
		List<String> rblocklist = extractor.getRdcdBlocklist();
		Set<String> calls = extractor.getCalls();
		Set<String> rnoncalls = extractor.getRdcdNoncalls();
		Map<String, List<String>> rfuncBlock = extractor.getRdcdFuncBlock();
		Map<String, List<String>> sequential = extractor.getSequential();
		List<String> ortmp = new LinkedList<String>();
		List<String> andtmp = new LinkedList<String>();

		String x = "x";
		String y = "y";

		// first the predicate signature
		writer.print("pred " + ordername + "(var1 " + x + ", " + y + ", var2 ");
		writer.print(genarglist(p, x, rblocklist));
		writer.print(", ");
		writer.print(genarglist(p, y, rblocklist));
		writer.println(") = ");

		// the labels for x and y should not be exactly the same
		andtmp.add(l(p, "main", x) + " = " + l(p, "main", y));
		for (String id : rblocklist) {
			andtmp.add(l(p, id, x) + " = " + l(p, id, y));
		}
		writer.println("\t~(" + getAnd(andtmp, "", false) + ")");
		andtmp.clear();

		// labels for x and y should be valid configurations
		writer.println("\t& " + configname + "(" + x + ", " + genarglist(p, x, rblocklist) + ")");
		writer.println("\t& " + configname + "(" + y + ", " + genarglist(p, y, rblocklist) + ")");

		// exists a node z which precede x and y in the tree, such that
			// every node v precede z, v in labels of x <=> v in labels of y
			// but labels of x and labels of y start to differ on z or children of z.
		// first part: every node v precede z, v in labels of x <=> v in labels of y
		writer.println("\t& (ex1 z: (z <= " + x + ") & (z <= " + y + ")");
		writer.println("\t\t& (all1 v:");
		writer.println("\t\t\t(v < z) => ");
		writer.print("\t\t\t(\t");
		andtmp.add("(v in " + l(p, "main", x) + " <=> v in " + l(p, "main", y) + ")");
		for (String id : rblocklist) {
			andtmp.add("(v in " + l(p, id, x) + " <=> v in " + l(p, id, y) + ")");
		}
		writer.print(getAnd(andtmp, "\t\t\t\t", true));
		andtmp.clear();
		writer.println(" )");
		writer.println("\t\t\t)");
		// second part: but labels of x and labels of y start to differ on z or children of z.
		writer.print("\t\t& (");
		// for every call block
		// find out the function which the call block actually calls
		// get the sequential relation corresponding to the function
		// list all possibilities
		List<String> seqmain = sequential.get("main");
		for (int i = 0; i < seqmain.size(); i++) {
			andtmp.add("(z in " + l(p, "main", x) + ")");
			andtmp.add("(z in " + l(p, "main", y) + ")");
			// get the block, check if it's a call
			Block iblock = rblocks.get(seqmain.get(i));
			if (iblock.getCallFlag()) {
				// if it is a call, get the callseq of the call, z.callseq should be in that call block
				List<String> icallseq = iblock.callseq;
				String iseq = "";
				for (int m = 1; m < icallseq.size(); m++) {
					if (icallseq.get(m).equals("left")) {
						iseq = iseq + ".0";
					} else if (icallseq.get(m).equals("right")) {
						iseq = iseq + ".1";
					}
				}
				andtmp.add("(z" + iseq + " in " + l(p, seqmain.get(i), x) + ")");
			} else {
				// if it's noncall, z should be in the noncall block
				andtmp.add("(z in " + l(p, seqmain.get(i), x) + ")");
			}
			for (int j = i + 1; j < seqmain.size(); j++) {
				Block jblock = rblocks.get(seqmain.get(j));
				if (jblock.getCallFlag()) {
					// if it is a call, get the callseq of the call, z.callseq should be in that call block
					List<String> jcallseq = jblock.callseq;
					String jseq = "";
					for (int m = 1; m < jcallseq.size(); m++) {
						if (jcallseq.get(m).equals("left")) {
							jseq = jseq + ".0";
						} else if (jcallseq.get(m).equals("right")) {
							jseq = jseq + ".1";
						}
					}
					andtmp.add("(z" + jseq + " in " + l(p, seqmain.get(j), y) + ")");
				} else {
					// if it's noncall, z should be in the noncall block
					andtmp.add("(z in " + l(p, seqmain.get(j), y) + ")");
				}
				ortmp.add(getAnd(andtmp, "\t\t\t\t", true));
				andtmp.remove(andtmp.size() - 1);
			}
			andtmp.clear();
		}
		for (String callid : calls) {
			String funcname = rblocks.get(callid).getCallname();
			List<String> seqfunc = sequential.get(funcname);
			for (int i = 0; i < seqfunc.size(); i++) {
				andtmp.add("(z in " + l(p, callid, x) + ")");
				andtmp.add("(z in " + l(p, callid, y) + ")");
				// get the block, check if it's a call
				Block iblock = rblocks.get(seqfunc.get(i));
				if (iblock.getCallFlag()) {
					// if it is a call, get the callseq of the call, z.callseq should be in that call block
					List<String> icallseq = iblock.callseq;
					String iseq = "";
					for (int m = 1; m < icallseq.size(); m++) {
						if (icallseq.get(m).equals("left")) {
							iseq = iseq + ".0";
						} else if (icallseq.get(m).equals("right")) {
							iseq = iseq + ".1";
						}
					}
					andtmp.add("(z" + iseq + " in " + l(p, seqfunc.get(i), x) + ")");
				} else {
					// if it's noncall, z should be in the noncall block
					andtmp.add("(z in " + l(p, seqfunc.get(i), x) + ")");
				}
				for (int j = i + 1; j < seqfunc.size(); j++) {
					Block jblock = rblocks.get(seqfunc.get(j));
					if (jblock.getCallFlag()) {
						// if it is a call, get the callseq of the call, z.callseq should be in that call block
						List<String> jcallseq = jblock.callseq;
						String jseq = "";
						for (int m = 1; m < jcallseq.size(); m++) {
							if (jcallseq.get(m).equals("left")) {
								jseq = jseq + ".0";
							} else if (jcallseq.get(m).equals("right")) {
								jseq = jseq + ".1";
							}
						}
						andtmp.add("(z" + jseq + " in " + l(p, seqfunc.get(j), y) + ")");
					} else {
						// if it's noncall, z should be in the noncall block
						andtmp.add("(z in " + l(p, seqfunc.get(j), y) + ")");
					}
					ortmp.add(getAnd(andtmp, "\t\t\t\t", true));
					andtmp.remove(andtmp.size() - 1);
				}
				andtmp.clear();
			}
		}
		writer.print(getOr(ortmp, "\t\t\t", true));
		ortmp.clear();
		writer.println("\n\t\t\t)");
		writer.print("\t\t)");

		// ordered comes to an end
		writer.println(";");

	}

	public void gendependence(String ordername, String p, RetreetExtractor extractor) {
		List<String> funcs = extractor.getFuncs();
		Map<String, Block> rblocks = extractor.getRdcdBlocks();
		List<String> rblocklist = extractor.getRdcdBlocklist();
		Set<String> calls = extractor.getCalls();
		Set<String> rnoncalls = extractor.getRdcdNoncalls();
		Map<String, List<String>> rfuncBlock = extractor.getRdcdFuncBlock();
		Map<String, List<String>> sequential = extractor.getSequential();
		List<String> ortmp = new LinkedList<String>();
		List<String> andtmp = new LinkedList<String>();

		String x = "x";
		String y = "y";

		// first the predicate signature
		writer.print("pred Dependence(var1 " + x + ", " + y + ", var2 ");
		writer.print(genarglist(p, x, rblocklist));
		writer.print(", ");
		writer.print(genarglist(p, y, rblocklist));
		writer.println(") = ");

		// x y should be ordered
		writer.println("\t" + ordername + "(" + x + ", " + y + ", " + genarglist(p, x, rblocklist) + ", " + genarglist(p, y, rblocklist) + ")");

		// list the dependence
		// for every noncall block, find out the write set
		// for each variable in the write set, find the other noncall block that is reading or writing to that variable
		// for each field in the write set, find out the callseq, find the other noncall block that is reading or writing to that field
		writer.print("\t & (");
		for (String ncid : rnoncalls) {
			Block nc = rblocks.get(ncid);
			for (String otherncid : rnoncalls) {
				Block othernc = rblocks.get(otherncid);
				if (!otherncid.equals(ncid)) {
					// for each variable in the write set, find the other noncall block that is reading or writing to that variable
					for (String v : nc.writevar) {
						for (String writev : othernc.writevar) {
							if (v.equals(writev)) {
								andtmp.add(x + " in " + l(p, ncid, x));
								andtmp.add(y + " in " + l(p, otherncid, y));
								andtmp.add(x + " = " + y);
								ortmp.add("(" +  getAnd(andtmp, "", false)  + ")");
								andtmp.clear();
							}
						}
						for (String readv : othernc.writevar) {
							if (v.equals(readv)) {
								andtmp.add(x + " in " + l(p, ncid, x));
								andtmp.add(y + " in " + l(p, otherncid, y));
								andtmp.add(x + " = " + y);
								ortmp.add("(" +  getAnd(andtmp, "", false)  + ")");
								andtmp.clear();
							}
						}
					}

					// for each field in the write set, find out the callseq, find the other noncall block that is reading or writing to that field
					for (List<String> field : nc.writefield) {
						String callseq = "";
						for (int i = 1; i < field.size() - 1; i++) {
							if (field.get(i).equals("left")) {
								callseq = callseq + ".0";
							} else if (field.get(i).equals("right")) {
								callseq = callseq + ".1";
							}
						}
						String fieldname = field.get(field.size() - 1);
						for (List<String> otherfield : othernc.writefield) {
							String othercallseq = "";
							for (int i = 1; i < otherfield.size() - 1; i++) {
								if (otherfield.get(i).equals("left")) {
									othercallseq = othercallseq + ".0";
								} else if (otherfield.get(i).equals("right")) {
									othercallseq = othercallseq + ".1";
								}
							}
							String otherfieldname = otherfield.get(otherfield.size() - 1);
							if (fieldname.equals(otherfieldname)) {
								andtmp.add(x + " in " + l(p, ncid, x));
								andtmp.add(y + " in " + l(p, otherncid, y));
								andtmp.add(x + callseq + " = " + y + othercallseq);
								ortmp.add("(" +  getAnd(andtmp, "", false)  + ")");
								andtmp.clear();
							}
						}
						for (List<String> otherfield : othernc.readfield) {
							String othercallseq = "";
							for (int i = 1; i < otherfield.size() - 1; i++) {
								if (otherfield.get(i).equals("left")) {
									othercallseq = othercallseq + ".0";
								} else if (otherfield.get(i).equals("right")) {
									othercallseq = othercallseq + ".1";
								}
							}
							String otherfieldname = otherfield.get(otherfield.size() - 1);
							if (fieldname.equals(otherfieldname)) {
								andtmp.add(x + " in " + l(p, ncid, x));
								andtmp.add(y + " in " + l(p, otherncid, y));
								andtmp.add(x + callseq + " = " + y + othercallseq);
								ortmp.add("(" +  getAnd(andtmp, "", false)  + ")");
								andtmp.clear();
							}
						}
					}
				}
			}
		}
		writer.print(getOr(ortmp, "\t\t", true));
		ortmp.clear();
		writer.print(" )");

		// dependence comes to an end
		writer.println("\t;");

	}

	public void genconvert(String pu, String pf, List<String> ufblck, List<String> fblck, RetreetExtractor extractor) {
		Map<String, List<String>> unfused2fused = extractor.getUnfused2fused();
    	Map<String, List<String>> fused2unfused = extractor.getFused2unfused();
    	List<String> ortmp = new LinkedList<String>();
		List<String> andtmp = new LinkedList<String>();

		String x = "x";

		// first the predicate signature
		writer.print("pred Convert(var2 ");
		writer.print(genarglist(pu, x, ufblck));
		writer.print(", ");
		writer.print(genarglist(pf, x, fblck));
		writer.println(") = ");

		// then for all node u, if u is in an unfused block, u should be in the corresponding fused block, and vice versa
		writer.println("\t(all1 u:");
		writer.print("\t\t( ");
		andtmp.add("(u in " + l(pu, "main", x) + " <=> u in " + l(pf, "main", x) + ")");
		for (String unfusedid : ufblck) {
			List<String> fusedlist = unfused2fused.get(unfusedid);
			for (String fusedid : fusedlist) {
				ortmp.add("u in " + l(pf, fusedid, x));
			}
			andtmp.add("(u in " + l(pu, unfusedid, x) + " => (" + getOr(ortmp, "", false) + "))");
			ortmp.clear();
		}
		for (String fusedid : fblck) {
			List<String> unfusedlist = fused2unfused.get(fusedid);
			for (String unfusedid : unfusedlist) {
				ortmp.add("u in " + l(pu, unfusedid, x));
			}
			andtmp.add("(u in " + l(pf, fusedid, x) + " => (" + getOr(ortmp, "", false) + "))");
			ortmp.clear();
		}
		writer.print(getAnd(andtmp, "\t\t", true));
		andtmp.clear();
		writer.print(" )");

		// convert comes to an end
		writer.println("\n\t);");

	}

}	