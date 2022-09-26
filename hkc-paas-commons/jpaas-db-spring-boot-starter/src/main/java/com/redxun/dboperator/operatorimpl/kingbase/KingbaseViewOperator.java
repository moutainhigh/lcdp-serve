package com.redxun.dboperator.operatorimpl.kingbase;

import com.redxun.dboperator.operatorimpl.oracle.OracleViewOperator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Oracle 视图操作的实现类。
 * 
 * <pre>
 * 作者：redxun
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 * 
 * 
 */
@Component(value="kingbase_ViewOperator")
@Scope("prototype")
public class KingbaseViewOperator extends OracleViewOperator {

}
