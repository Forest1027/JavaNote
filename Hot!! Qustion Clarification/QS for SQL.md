# QS for SQL
## Oracle
可以自动扩容

oracle分页（rownum）和mysql分页（limit）的区别

1. [体系结构](http://blog.csdn.net/sinat_33363493/article/details/51782609)
2. 视图
	1. 是什么？——*一个虚拟表/一条语句*
		1. 是一种数据库对象，是从一个或多个数据表/视图中导出的虚表。**
		2. 视图对应的数据不存在视图中，而是存在所引用的数据表中。
	2. 有什么优点？
		1. 简化数据操作
		2. 着重于特定数据。不必要的数据或敏感数据可以不出现在视图中
		3. 视图提供简单有效的安全机制。定制不同用户的访问权限
		4. 提供向后兼容性。使用户能够在表的架构更改时为表创建向后兼容接口
	3. 特点
		1. 【视图存储的不是数据，是sql语句】更改视图数据，表的数据也会自然更改，反之亦然。不过一般不会这么做，会在创建视图的时候声明只读，因为视图的主要目的是建来简化操作的。
		2. 键保留表
			1. 是什么？——该表的主键列全部显示在视图中，并且它们的值在视图中都是唯一且非空的。
			2. 视图中，键保留表的字段是可以更新的，非键保留表是不能更新的。
3. 物化视图/实体化视图
	1. 是什么？
		1. 指定查询语句返回的结果集
		2. 与普通视图相比，物化视图是建立的副本，类似一张表，需要占用存储空间。
		3. 执行效率与查询一个表相同
4. 序列
	1. 是什么？
		1. oracle提供的用于产生一系列唯一数字的数据库对象
5. 索引
	1. 是什么？
		1. 索引是用于加速数据存取的数据对象。
		2. 需要占据存储空间。形式类似于“树”，树的节点存储的是每条记录的物理地址
6. PL/SQL
	1. 是什么？
		1. oracle对sql语言的过程化扩展。增加了过程处理语句(如分支、循环等)
	2. 存储函数
		1. 自定义函数
		2. 接收一个或多个参数，返回一个结果
	3. 存储过程
		1. 是什么？
			1. 被命名的PL/SQL块，存储在数据库中，是数据库对象的一种
			2. 完成一定功能的代码块。一批sql代码
		2. 与存储函数的区别
			1. 存储函数中有返回值，且必须返回；存储过程没有返回值，可以通过传出参数返回多个值
			2. 存储函数可以在select语句中直接使用；存储过程不能在select中使用，多数被应用程序调用
			3. 存储函数一般是封装一个查询结果；存储过程一般封装一段事务代码
		3. 使用场合。
			1. 要操作的数据很多，传入传出的参数很少。

## MySQL

## MySQL语句优化（说出8条）
1. not exists代替not in
	1. 如果查询语句使用了not in，那么对内外表都进行全表扫描，没有用到索引；而not exists的子查询依然能用到已经建好的表上的索引。
2. 尽量不采用不利用索引的操作符
	1. 如in，not in，is null，is not null等
	2. 给经常做搜索的字段建立索引
3. 使用limit
	1. 已知返回结果有多少条（少量）的时候，加一个limit可以提高性能，因为查到这么多条就不查了
	1. select * from table LIMIT 5,10; #返回第6-15行数据
	2. select * from table LIMIT 5; #返回前5行
	3. select * from table LIMIT 0,5; #返回前5行
where和having的区别

## MySQL数据库优化
1. 建立视图
	1. 简单。用户查询数据的关注点只放在数据上就好，表与表之间的连接无需关注。
	2. 安全。使用视图的用户只能查询他被允许查询的结果集。
	3. 数据独立。将表与java代码隔离开，如果表发生变动，只需要改变视图中的相应值，不用改变java代码。
	4. 提高查询效率【物化视图】
	5. **注意**：视图不允许添加、删除数据，允许修改数据。是否允许修改数据也分多种情况，如键保留表可以、非键保留表不可以。
2. 使用存储过程
	1. 例如按照id删除一批数据。如果使用java代码，那么在java中对id进行判断，再一个个传id到数据库执行删除操作。要知道，连接数据库是需要时间的，如此会浪费大量时间；而使用存储过程，可在其中使用PL语句判断，然后直接删除，省去了连接的时间。
3. 选择合适的存储引擎
	1. innodb 默认的。支持事务、支持外键，但对比myisam的存储引擎，innodb 写的处理效率差一些并且会占用更多的磁盘空间以保留数据和索引
	2. myisam 不支持事务、不支持外键，但访问速度非常快，查询添加快。如字典表，就可以使用myisam存储。【字典表，如快递选择的计件单位--辆、包、袋等，这种字段主要用于查询，在开始一次性添加后，偶尔会再次添加，但是基本没有修改和删除的可能】
	3. memory 访问数据非常快，因为数据是存在内存中。但是一旦服务关闭，表中数据就会丢失。
4. 合理利用索引
	1. *对相关列使用索引是提高select 操作性能的最佳途径*
	2. *myisam 和innodb 存储引擎的表默认创建的都是btree 索引。默认情况下，memory 存储引擎使用hash 索引，但也支持btree 索引。*
	3. 最适合索引的列是出现在where子句中的列，或连接子句中指定的列，不一定是要选择的列。-->给where后面出现的列建立索引
	4. 使用唯一索引。建立索引的字段要能区分出每条记录，如以身份证号建索引与以性别建索引的区别。
	5. 使用短索引。如长城园1号，长城园2号等等，建立索引的时候，可以忽略掉“长城园”，将目光聚焦在真正起作用的文字上。
	6. 不要过度索引。
5. 尽量少加锁，少加事务
	1. 这是从提高效率的角度说的。加锁和事务都不可避免的拖累效率，所以需要在保证数据安全和完整性与效率之间做一个权衡。
6. SQL优化
	1. [大批量插入数据时](https://yq.aliyun.com/ziliao/59864)
		1. 导入的数据按照主键的顺序排列，因为Innodb类型的表是按照主键的顺序保存的。
		2. 导入数据前执行SET UNIQUE_CHECKS=0，关闭唯一性校验，在导入结束后执行SET UNIQUE_CHECKS=1，恢复唯一性校验
		3. 导入前执行SET AUTOCOMMIT=0，关闭自动提交，导入结束后再执行SET AUTOCOMMIT=1，打开自动提交
	2. 表字段优化
		1. 增加冗余列-->少关联表
		2. 增加派生列-->可新增基于原表计算出来的列，免得每次都要算
		3. 重新组表-->建视图
		4. 分割表【表的垂直切割】-->原本可以放在一张大表里，但是其中只有某几个字段常用，即可将这些字段独立出来。
		5. 中间表/临时表-->少关联，多表关联时可以直接查一张表。