# RETREET
RETREET编程实现
<p>1.使用antlr规则编写retreet语法，并生成相应的lexer、parser;
  antlr4生成的文件主要有retreetParser.java、retreetLexer.java、retreet.tokens、retreetListener.java、retreetBaseListener.java等。
  retreetBaseListener是生成的监听器接口默认实现类。
</p>
<p>2.将生成的parser、lexer与Java程序集成。便于后续用Java提取信息。——Test.java
  </p>
<p>Test.java 为提取信息的java主程序。Fig4中的1-5由Info类实现，6由Info2类实现，7由Params类实现，8由Nodes类实现。其中Info使用Listener接口，6/7/8使用Visitor模式。
  </p>
  <p>information01、information02为目前提取信息的运行截图。
 </p>
<p>第9条信息Path（t）有些疑惑，请见issue。
</p>
<p>Fig5: Relations between blocks程序还尚未调试成功 
</p>
