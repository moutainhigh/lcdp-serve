package com.redxun.common.xml;

import org.dom4j.VisitorSupport;
import java.util.ListIterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.Visitor;
import org.dom4j.VisitorSupport;
import org.dom4j.io.SAXReader;

/**
 * xml 更改命名空间
 */
public class NamesapceChangingVisitor extends VisitorSupport {
    /**
     * 来自命名空间
     */
    private Namespace from;
    /**
     * 更改为的命名空间
     */
    private Namespace to;

        public NamesapceChangingVisitor(Namespace from, Namespace to) {
            this.from = from;
            this.to = to;
        }

    /**
     * 访问某个节点
     * @param node
     */
    public void visit(Element node) {
            Namespace ns = node.getNamespace();

            if (ns.getURI().equals(from.getURI())) {
                QName newQName = new QName(node.getName(), to);
                node.setQName(newQName);
            }

            ListIterator namespaces = node.additionalNamespaces().listIterator();
            while (namespaces.hasNext()) {
                Namespace additionalNamespace = (Namespace) namespaces.next();
                if (additionalNamespace.getURI().equals(from.getURI())) {
                    namespaces.remove();
                }
            }
        }

}
