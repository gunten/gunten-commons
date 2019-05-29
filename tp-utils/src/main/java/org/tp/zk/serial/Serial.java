package org.tp.zk.serial;

public interface Serial {
    /**
     * 获得一个10位数字的流水号
     *
     * @param path   需要指定1个ZK路径   建议：【/系统标识/应用标识/业务标识/】
     * @return
     */
    String getSerialNum(String path);

    /**
     * 获得一个10位数字的流水号
     * @param path   需要指定1个ZK路径   建议：【/系统标识/应用标识/业务标识/】
     * @param node   定义个性化目录格式  如：DefaultIndividualNode   按照月维度从0开始生成流水号
     * @return
     */
    String getSerialNum(String path, IndividualNode node);
}
