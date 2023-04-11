# 1.ark-rule-platform？
&emsp;&emsp;ark-rule-platform是ark系列框架中负责规则引擎管控和执行的平台。
# 2.ark-rule-platform解决了什么问题？
&emsp;&emsp;通常在营销、风控等系统中散落着大量规则决策的场景，而每个系统的规则引擎执行实现方式也各有不同。在活动页也有用到规则决策的需求，所以我需要开发出一个通用的规则引擎平台，来满足以上场景。
# 3.使用场景
- 风控系统中各种规则决策
- 营销系统中各种活动规则，如满减，满赠，多件折扣等
- Bcp系统中各种检查规则，如检查用户是否有资格参与活动，是否有资格领取优惠券等
# 4.系统设计指标
•方便的接入各种业务规则：统一集团的规则引擎系统，收拢不同业务系统的规则，避免规则散落在不同的系统里。

•统一规则执行引擎组件，使后期开发、需求方、产品等固定使用一套规则或脚本的语法。减少学习成本。

•避免不同业务系统重复开发规则引擎系统增加人力成本。

•使一些尚未使用规则引擎的开发人员了解使用引擎，避免堆大量规则逻辑硬编码在系统里。

•高性能，前期集中执行规则，后期通过规则前置方式，最大限度提升性能。

•支持灰度发布，可按IP维度灰度生效。

•支持版本管理，可对快速回滚到上个版本。

•内置一些通用的动作，比如发券，发短信。

•可监控性：重要服务指标监控，重要异常电话报警。

# 5.术语


•业务线：不同的业务线使用，如风控，促销。可用来隔离不同业务线的数据。

•规则组：一组规则的集合体，如活动场景，促销场景，风控场景。

•规则：一组函数或表达式（由and or ，+ - * /等组合起来）的组合体。 region = 华北 and area = 北京。

•规则结果：命中规则后执行的动作，可返回自定义脚本执行的结果或返回Boolean或自定义字符串。

•元数据：组成规则的原子单位，如上面规则中的region ,area。

•规则脚本：用来生成具体规则的模板，包含了组成规则的元数据之间的逻辑关系。如：${area} and ${area} and ${time} 。从规则的元数据表读取数据替换后的规则为area=hebai and area=beijing and time > 20191011。

•剪枝算法：规则引擎执行一组规则时，对一组规则用剪枝算法做过滤。表达式引擎最后对过滤后的规则进行计算，可根据调解过滤掉一批无需计算的规则。

# 6.如何使用？
```java
1、引入pom
<dependency>
    <groupId>com.ark.rule</groupId>
    <artifactId>ark-rule-platform-api</artifactId>
    <version>1.0</version>
</dependency>

2.配置dubbo consumer
<dubbo:reference interface="com.ark.rule.center.api.service.fg.IRuleService" ref="ruleService" version="1.0" />
<dubbo:reference interface="com.ark.rule.center.api.service.bg.IRuleBgService" ref="ruleBgService" version="1.0" />

3、添加基础数据
    3.1 添加业务线（灌入DB数据）
    3.2.新建元数据（灌入DB数据）
    
4、添加规则（组）接口
IRuleBgService.saveRuleGroup(RuleGroupDTO req);(id不存在则新建，存在则编辑)

5、执行规则接口
IRuleService.execute(ExecuteReqDTO execute);
```