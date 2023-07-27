package top.xiyang6666.treecommand;

public class TreeCommand {
    private NodeCommand rootNode = null;

    /**
     * 设置指令
     *
     * @param name     名称
     * @param executor 执行函数
     * @param template 模版
     * @return 根指令节点
     */
    public NodeCommand command(String name, NodeCommandExecutor executor, NodeCommand template) {
        this.rootNode = template;
        if (this.rootNode == null) {
            this.rootNode = new NodeCommand();
        }
        this.rootNode.setup(name, null, executor);
        return this.rootNode;
    }

    /**
     * 根据参数列表返回指令节点
     *
     * @param strings 参数列表
     * @return 指令节点
     */
    public NodeCommand getNodeCommand(String[] strings) {
        NodeCommand node = this.rootNode;
        for (String string : strings) {
            if (string.equals("")) {
                continue;
            }
            node = node.getChildByName(string);
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    public String getName() {
        return this.rootNode.getName();
    }
}
