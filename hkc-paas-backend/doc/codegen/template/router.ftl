<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign comment=model.tabComment>
<#assign system=vars.system>
    {
        name: '${class}Edit',
        path: '/${system}/${package}/${classVar}Edit/:id?',
        component: () => import('@/views/${system}/${package}/${classVar}Edit'),
        hidden: true
    },
    {
        path: '/${system}/${package}',
        component: Layout,
        children: [
        {
            path: '${classVar}List',
            name: '${classVar}List',
            component: () => import('@/views/${system}/${package}/${classVar}List'),
            meta: { title: '${comment}管理', icon: 'form' }
          }
        ]
    },
