package com.redxun.common.base.search;

import com.redxun.common.base.entity.SqlModel;

import java.util.List;

/**
 * SQL构建工具类
 *
 */
public class SqlBuilder {
    /**
     * 根据FieldLogic构建SQLModel
     * @param logic
     * @return
     */
    public  static SqlModel build(FieldLogic logic){
        List<WhereParam> whereParams=logic.getWhereParams();

        SqlModel oModel=new SqlModel();

        StringBuilder sb=new StringBuilder();
        int i=0;
        for(WhereParam param:whereParams){
            if(i>0){
                sb.append(" " +logic.getLogic() +" ");
            }
            if(param instanceof  QueryParam){
                QueryParam queryParam=(QueryParam)param;
                sb.append(queryParam.getSql());
                oModel.getParams().put(queryParam.getFieldName() ,queryParam.getValue());
            }
            else {
                FieldLogic fieldLogic=(FieldLogic)param;
                SqlModel model= build(fieldLogic);

                sb.append("("+ model.getSql() +")");
                oModel.getParams().putAll(model.getParams());
            }
            i++;
        }
        oModel.setSql(sb.toString());

        return oModel;

    }

    /**
     * 测试用例
     * @param args
     */
    public static void main(String[] args) {
        QueryParam queryParam1=new  QueryParam("A","=","1");
        QueryParam queryParam2=new  QueryParam("B","=","2");
        QueryParam queryParam3=new  QueryParam("C","=","3");
        QueryParam queryParam4=new  QueryParam("D","=","4");

        QueryParam queryParam5=new  QueryParam("E","=","5");
        QueryParam queryParam6=new  QueryParam("F","=","6");

        FieldLogic logic=new FieldLogic("and");

        FieldLogic logic1=new FieldLogic("or");
        logic1.addParams(queryParam1);
        logic1.addParams(queryParam2);

        FieldLogic logic2=new FieldLogic("or");
        logic2.addParams(queryParam3);
        logic2.addParams(queryParam4);

        FieldLogic logic3=new FieldLogic("AND");
        logic3.addParams(queryParam5);
        logic3.addParams(queryParam6);

        logic2.addParams(logic3);


        logic.addParams(logic1);
        logic.addParams(logic2);

        SqlModel sqlModel= SqlBuilder.build(logic);
        System.out.println(sqlModel.getSql());

    }


}
