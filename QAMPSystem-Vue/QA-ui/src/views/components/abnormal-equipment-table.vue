<template>
  <div class="box abnormal-equipment">
    <div class="header">
      <div class="title">逾期任务监控</div>
      <el-icon @click="handleMore"><MoreFilled /></el-icon>
    </div>
    <el-scrollbar v-if="listData.length" class="scrollbar body">
      <el-table
        :data="listData"
        fit
        highlight-current-row
        style="width: 100%"
        :header-cell-style="{
          padding: '7px 0 6px',
          background: '#EFF0F2',
          'font-weight': '400',
          'text-align': 'left',
          color: '#333333',
        }"
        :cell-style="{
          padding: '15px 0',
          'text-align': 'left',
          color: '#999999',
        }"
      >
        <el-table-column label="计划完成时间" width="160px">
          <template #default="scope">
            <span>{{ scope.row.updateTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="逾期任务" show-overflow-tooltip>
          <template #default="scope">
            <span>{{ scope.row.addr }}</span>
          </template>
        </el-table-column>
        <el-table-column label="逾期天数" width="120px">
          <template #default="scope">
            <span>{{ scope.row.overdueDays }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>
    <!-- TODO：一开始显示加载中 -->
    <div v-else class="empty">
      <img src="@/assets/images/empty.png" />
      <div class="tips">暂无数据</div>
    </div>
  </div>
</template>
<script setup>
import { onMounted } from 'vue';
// 定义变量
const router = useRouter();
const listData = ref([
        {
            "createBy": null,
            "createTime": "2020-12-18 ",
            "updateBy": null,
            "updateTime": "2025-01-14 ",
            "remark": null,
            "id": 80,
            "innerCode": "A1000001",
            "vmTypeId": 1,
            "vmTypeName": "饮料机",
            "channelMaxCapacity": 10,
            "nodeId": 6,
            "addr": "任务管理前端改造",
            "lastSupplyTime": "2023-03-22",
            "businessId": 1,
            "regionId": 3,
            "regionName": "北京-顺义区",
            "ownerId": 28,
            "longitudes": 0,
            "vmStatus": 1,
            "clientId": "70122567fcc13e7615e7239812c20448",
            "ownerName": "金燕龙合作商",
            "latitude": 0,
            "runningStatus": "{\"key\": \"10001\",\"value\":\"正常\" }",
            "policyId": 1,
            overdueDays: 1,
        },
        {
            "createBy": null,
            "createTime": "2020-12-18 ",
            "updateBy": null,
            "updateTime": "2024-02-18",
            "remark": null,
            "id": 81,
            "innerCode": "Ut548Hpf",
            "vmTypeId": 1,
            "vmTypeName": "饮料机",
            "channelMaxCapacity": 10,
            "nodeId": 2,
            "addr": "工时逻辑改造",
            "lastSupplyTime": "2000-01-01",
            "businessId": 2,
            "regionId": 1,
            "regionName": "北京-海淀区",
            "ownerId": 1,
            "longitudes": 0,
            "vmStatus": 1,
            "clientId": "3792e9c9b390e291514fd4f9b8fe95bb",
            "ownerName": "金燕龙合作商",
            "latitude": 0,
            "runningStatus": "{\"key\": \"10001\",\"value\":\"正常\" }",
            "policyId": 2,
            overdueDays: 2,
        },
        {
            "createBy": null,
            "createTime": "2020-12-18 ",
            "updateBy": null,
            "updateTime": "2025-02-25 ",
            "remark": null,
            "id": 82,
            "innerCode": "RHaV9Zaz",
            "vmTypeId": 1,
            "vmTypeName": "饮料机",
            "channelMaxCapacity": 10,
            "nodeId": 3,
            "addr": "用例集脑图管理选型调研",
            "lastSupplyTime": "2000-01-01",
            "businessId": 1,
            "regionId": 2,
            "regionName": "北京-昌平区",
            "ownerId": 1,
            "longitudes": 0,
            "vmStatus": 1,
            "clientId": "1b9e4df47a4c55124b54a4cb00b62123",
            "ownerName": "金燕龙合作商",
            "latitude": 0,
            "runningStatus": "{\"key\": \"10001\",\"value\":\"正常\" }",
            "policyId": 2,
            overdueDays: 5,
        },
        {
            "createBy": null,
            "createTime": "2020-12-18 ",
            "updateBy": null,
            "updateTime": "2025-03-16 ",
            "remark": null,
            "id": 83,
            "innerCode": "f8uK9ixf",
            "vmTypeId": 1,
            "vmTypeName": "饮料机",
            "channelMaxCapacity": 10,
            "nodeId": 4,
            "addr": "测试用例执行管理",
            "lastSupplyTime": "2022-11-29",
            "businessId": 2,
            "regionId": 2,
            "regionName": "北京-昌平区",
            "ownerId": 1,
            "longitudes": 0,
            "vmStatus": 1,
            "clientId": "d764e5e764db441de0ed5dbf68d48f90",
            "ownerName": "金燕龙合作商",
            "latitude": 0,
            "runningStatus": "{\"key\": \"10001\",\"value\":\"正常\" }",
            "policyId": 1,
            overdueDays: 3,
        }
    ]);

// 点击更多
const handleMore = () => {
  router.push({ path: 'manager/task/task' });
};
</script>
<style lang="scss" scoped>
@import '@/assets/styles/variables.module.scss';

.abnormal-equipment {
  display: flex;
  flex-direction: column;
  height: calc((100vh - 120px) * 0.4);
  min-height: 353px;
  background: #ffffff;
  border-radius: 20px;

  .more {
    color: $--color-primary;
    cursor: pointer;
  }

  .empty {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .tips {
      margin-top: 25px;
      color: #20232a;
      font-size: 14px;
    }
  }

  .body {
    flex: 1;
    margin-top: 20px;
  }
}
</style>