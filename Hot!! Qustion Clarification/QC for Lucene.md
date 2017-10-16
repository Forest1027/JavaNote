# QC for Lucene
Lucene不是独立的程序，它是源码包。

solr是一个程序，全文检索服务器。它是基于Lucene写好的程序。

索引库按照相关度排序，可以人为干预。使用评分机制

## 什么是全文检索？为什么使用全文检索？
1. 前情
	1. 数据分为结构化数据、非结构化数据
	2. 结构化数据：有固定长度、有限长度。**结构化数据的搜索比较容易**(eg:数据库、元数据等)
	3. 非结构化数据：不定长、无固定格式的数据。**查询难**(eg:邮件、网页、磁盘上的文件等)
	4. 非结构化数据难以查询，但是对于查询这类数据的需求要十分大，因此需要找到查询这种数据的办法
2. 非结构化数据查询方法
	1. 顺序扫描法 Serial Scanning
		1. 一个一个文档看，速度相当慢
	2. 全文检索 Full-text Search
		1. 将非结构化数据中的一部分提取出来，重新组织，使其变得有一定结构
		2. 再对这有结构的数据进行搜索
		3. 这部分重组后变得有结构的数据叫做**索引**
		4. 先创建索引，再对索引进行搜索的过程就叫**全文检索**

>注意：全文检索创建索引的过程比较费时间，但是一旦索引创建完毕，可以多次使用。因此，是很值得的。这是典型的**以空间换时间、以时间换时间**的优化方法

3. 应用场景
	1. 对于数据量大、数据结构不固定的数据可采用全文检索方式
	2. 百度、google、论坛站内搜索等

## 如何实现全文检索
1. 流程图
	1. 索引流程
		1. 创建索引
			1. 获得原始文档（如何获得原始文档，见下方的“信息采集”）
			2. 创建文档对象：将原始文档创建成文档对象。文档中包括域、域中存储对象
			3. 分析文档(分词)
				1. 使文档对象中包含域，需要对原始文档进行分析
				2. 如提取单词、字母转小写、去标点符号、去除停用词
				3. 提取过后的结果叫做term。term包括两部分，文档的域名和词的内容
			4. 创建索引(在term上建立索引)
				1. 倒排索引结构，实现通过词汇找文档。
					1. 一个关键词对应一个文档的链表（文档id）
				2. 传统的方法是顺序扫描方法，通过文档找词汇。数据量大、搜索慢。
	2. 搜索流程/查询索引
		1. 用户查询接口
			1. Lucene不提供用户搜索界面，需自己实现
			2. 用户输入搜索关键字查询
		2. 创建查询对象
		3. 执行查询
		4. 渲染结果：高亮结果等
2. 信息采集
	1. what
		1. 从互联网(爬虫)、数据库(jdbc)、文件系统(IO)获取需要搜索的原始信息
	2. how
		1. Lucene不提供信息采集的类库，需要自己写爬虫
		2. 可利用以下开源软件实现信息采集
			1. [Nutch](http://lucene.apache.org/nutch), Nutch是apache的一个子项目，包括大规模爬虫工具，能够抓取和分辨web网站数据。
			2. [jsoup](http://jsoup.org/ )，jsoup 是一款Java 的HTML解析器，可直接解析某个URL地址、HTML文本内容。它提供了一套非常省力的API，可通过DOM，CSS以及类似于jQuery的操作方法来取出和操作数据。
			3. [heritrix](http://sourceforge.net/projects/archive-crawler/files/)，Heritrix 是一个由 java 开发的、开源的网络爬虫，用户可以使用它来从网上抓取想要的资源。其最出色之处在于它良好的可扩展性，方便用户实现自己的抓取逻辑。

## 中文分词器
1. 分词器执行流程
	1. 从一个Reader字符流开始，创建一个基于Reader的Tokenizer分词器，经过三个TokenFilter生成语汇单元Token。
	2. 要看分析器的分析效果，只需要看Tokenstream中的内容就可以了。每个分析器都有一个方法tokenStream，返回一个tokenStream对象。
2. 中文分词器
	1. Lucene自带
		1. StandardAnalyzer
			1. 单字分词：就是按照中文一个字一个字地进行分词。如：“我爱中国”，效果：“我”、“爱”、“中”、“国”。
		2. CJKAnalyzer
			1. 二分法分词：按两个字进行切分。如：“我是中国人”，效果：“我是”、“是中”、“中国”“国人”。
		3. SmartChineseAnalyzer
			1. 对中文支持较好，但扩展性差，扩展词库，禁用词库和同义词库等不好处理
	2. 第三方中文分词器
		1. paoding： 庖丁解牛最新版在 https://code.google.com/p/paoding/ 中最多支持Lucene 3.0，且最新提交的代码在 2008-06-03，在svn中最新也是2010年提交，已经过时，不予考虑。
		2. mmseg4j：最新版已从 https://code.google.com/p/mmseg4j/ 移至 https://github.com/chenlb/mmseg4j-solr，支持Lucene 4.10，且在github中最新提交代码是2014年6月，从09年～14年一共有：18个版本，也就是一年几乎有3个大小版本，有较大的活跃度，用了mmseg算法。
		3. **IK-analyzer**(停用词和扩展词添加方便)： 最新版在https://code.google.com/p/ik-analyzer/上，支持Lucene 4.10从2006年12月推出1.0版开始， IKAnalyzer已经推出了4个大版本。最初，它是以开源项目Luence为应用主体的，结合词典分词和文法分析算法的中文分词组件。从3.0版本开 始，IK发展为面向Java的公用分词组件，独立于Lucene项目，同时提供了对Lucene的默认优化实现。在2012版本中，IK实现了简单的分词 歧义排除算法，标志着IK分词器从单纯的词典分词向模拟语义分词衍化。 但是也就是2012年12月后没有在更新。
		4. ansj_seg：最新版本在 https://github.com/NLPchina/ansj_seg tags仅有1.1版本，从2012年到2014年更新了大小6次，但是作者本人在2014年10月10日说明：“可能我以后没有精力来维护ansj_seg了”，现在由”nlp_china”管理。2014年11月有更新。并未说明是否支持Lucene，是一个由CRF（条件随机场）算法所做的分词算法。
		5. imdict-chinese-analyzer：最新版在 https://code.google.com/p/imdict-chinese-analyzer/ ， 最新更新也在2009年5月，下载源码，不支持Lucene 4.10 。是利用HMM（隐马尔科夫链）算法。
		6. Jcseg：最新版本在git.oschina.net/lionsoul/jcseg，支持Lucene 4.10，作者有较高的活跃度。利用mmseg算法。

## lucene和solr的区别
lucene(jar包)：在web程序中使用其api来维护、优化、管理索引库;solr(war包):solr直接维护索引库我们不用管，web程序调用solr来完成检索。【好处：解耦】

## 分词
1. ik
2. 配置方法
	1. 导入jar包到tomcat下
	2. 导入配置文件IKAnalyzer.cfg.xml，扩展词词典，停用词词典到tomcat的web-inf/classes下
	3. 配置schema.xml（solrcore的conf目录下）

	```
	<!-- IKAnalyzer-->
    <fieldType name="text_ik" class="solr.TextField">
      <analyzer class="org.wltea.analyzer.lucene.IKAnalyzer"/>
    </fieldType>
	<!--IKAnalyzer Field-->
	<field name="title_ik" type="text_ik" indexed="true" stored="true" />
	<field name="content_ik" type="text_ik" indexed="true" stored="false" multiValued="true"/>
	```



