package com.changgou.canal;

import java.net.InetSocketAddress;
import java.util.List;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;

public class CanalClient {
    public static void main(String args[]) {
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("192.168.31.6", 11111), "example", "", "");

        try {
            connector.connect();
            connector.subscribe("changgou_business.tb_ad");
            connector.rollback();

            while (true) {
                Message message = connector.getWithoutAck(100);
                long batchId = message.getId();
                int size = message.getEntries().size();

                if (batchId == -1 || size == 0) {
                    // 如果没有新数据，则休眠一段时间
                    Thread.sleep(1000);
                } else {
                    // 处理新的数据
                    for (Entry entry : message.getEntries()) {
                        if (entry.getEntryType() != CanalEntry.EntryType.ROWDATA) {
                            continue;
                        }

                        RowChange rowChange = null;
                        try {
                            rowChange = RowChange.parseFrom(entry.getStoreValue());
                        } catch (Exception e) {
                            throw new RuntimeException("解析出错：" + entry, e);
                        }

                        EventType eventType = rowChange.getEventType();
                        System.out.println(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                                entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                                entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                                eventType));

                        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                            if (eventType == EventType.DELETE) {
                                printColumn(rowData.getBeforeColumnsList());
                            } else if (eventType == EventType.INSERT) {
                                printColumn(rowData.getAfterColumnsList());
                            } else {
                                System.out.println("-------> before");
                                printColumn(rowData.getBeforeColumnsList());
                                System.out.println("-------> after");
                                printColumn(rowData.getAfterColumnsList());
                            }
                        }
                    }

                    // 确认新数据已经处理完成
                    connector.ack(batchId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.disconnect();
        }
    }

    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
