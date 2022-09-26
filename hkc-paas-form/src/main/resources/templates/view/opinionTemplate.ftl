<div>
    <div v-for="(opinion,index) in opinionList" style="margin-bottom: 20px">
        <tr>
            <td>
                【{{opinionMap[opinion.checkStatus]}}】
                <span style="margin-left: 10px;">{{ opinion.remark }}</span>
            </td>
        </tr>
        <tr>
            <td style="padding-left:50px">
                {{ opinion.handlerUserName }}({{opinion.handlerUserDeptName}})
                <span style="margin-left: 20px;">{{ opinion.createTime }}</span>
            </td>
        </tr>
    </div>
</div>