//package com.changgou.canal.listener;
//import java.util.List;
//
//import com.alibaba.otter.canal.protocol.CanalEntry;
//
//import com.xpand.starter.canal.annotation.CanalEventListener;
//import com.xpand.starter.canal.annotation.ListenPoint;
//
//@CanalEventListener
//public class MyCanalListener {
//    @ListenPoint(schema = "changghou_business", table = "tb_ad", eventType = CanalEntry.EventType.INSERT)
//    public void onInsert(CanalEntry.RowChange rowChange) {
//        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
//            // 输出新增的数据
//            System.out.println("新增数据：" + rowData.getAfterColumnsList());
//        }
//    }
//
//    @ListenPoint(schema = "changghou_business", table = "tb_ad", eventType = CanalEntry.EventType.UPDATE)
//    public void onUpdate(CanalEntry.RowChange rowChange) {
//        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
//            // 输出更新前的数据
//            System.out.println("更新前的数据：" + rowData.getBeforeColumnsList());
//            // 输出更新后的数据
//            System.out.println("更新后的数据：" + rowData.getAfterColumnsList());
//        }
//    }
//
//    @ListenPoint(schema = "changghou_business", table = "tb_ad", eventType = CanalEntry.EventType.DELETE)
//    public void onDelete(CanalEntry.RowChange rowChange) {
//        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
//            // 输出删除的数据
//            System.out.println("删除数据：" + rowData.getBeforeColumnsList());
//        }
//    }
//}
