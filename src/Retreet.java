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

		// ParseTreeWalker walker = new ParseTreeWalker();
        // RetreetListener listener = new RetreetListener();
        // walker.walk(listener, tree);
	}
}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException err){
		throw err;
	}	
}	