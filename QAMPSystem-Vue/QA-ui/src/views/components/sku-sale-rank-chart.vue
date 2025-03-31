<template>
  <div class="box bgc3 sku-sale-rank">
    <!-- TODO: 分辨大怎么展示问UI -->
    <div class="header">
      <div class="title">
        工时统计<span class="sub-title">{{ start }} ~ {{ end }}</span>
      </div>
    </div>
    <div class="body">
      <el-row v-for="(item, index) in skuSaleRank" :key="index">
        <el-col :span="5">
          <div :class="'top top' + (index + 1)">
            {{ index + 1 }}
          </div>
        </el-col>
        <el-col :span="13">
          <div class="sku-name" :title="item.skuName">
            {{ item.skuName }}
          </div>
        </el-col>
        <el-col :span="6">
          <div class="count">{{ item.count }}小时</div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script setup>
import { onMounted } from 'vue';
import dayjs from 'dayjs';
// 定义变量
const skuSaleRank = ref([
    {
        "skuId": "0",
        "skuName": "董金荣",
        "count": 216,
        "amount": 0
    },
    {
        "skuId": "0",
        "skuName": "王恺靖",
        "count": 214,
        "amount": 0
    },
    {
        "skuId": "0",
        "skuName": "徐家权",
        "count": 210,
        "amount": 0
    },
    {
        "skuId": "0",
        "skuName": "高诗岩",
        "count": 184,
        "amount": 0
    },
    {
        "skuId": "0",
        "skuName": "杜峰",
        "count": 160,
        "amount": 0
    },
    {
        "skuId": "0",
        "skuName": "佟跃",
        "count": 158,
        "amount": 0
    },
    {
        "skuId": "0",
        "skuName": "潘婧",
        "count": 144,
        "amount": 0
    },
    {
        "skuId": "0",
        "skuName": "张浩嘉",
        "count": 134,
        "amount": 0
    }
])
const start = dayjs().startOf('month').format('YYYY.MM.DD');
const end = dayjs().endOf('day').format('YYYY.MM.DD');
</script>
<style lang="scss" scoped>
@import '@/assets/styles/variables.module.scss';

.sku-sale-rank {
  display: flex;
  flex-direction: column;
  height: calc((100vh - 120px) * 0.6);
  min-height: 538px;
  background: #FFFFFF;
  border-radius: 20px;

  .body {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    margin-top: 20px;

    .top {
      display: inline-block;
      width: 16px;
      height: 20px;
      margin-left: 10px;
      background: url('@/assets/user-task-stats/top.png');
      text-align: center;
      font-size: 12px;
      font-weight: normal;
      color: #E9B499;
      line-height: 14px;
    }

    .top1 {
      width: 21px;
      height: 20px;
      background: url('@/assets/user-task-stats/top1.png');
      color: #8E5900;
    }

    .top2 {
      width: 21px;
      height: 20px;
      background: url('@/assets/user-task-stats/top2.png');
      color: #494949;
    }

    .top3 {
      width: 21px;
      height: 20px;
      background: url('@/assets/user-task-stats/top3.png');
      color: #CF6D3D;
    }

    .sku-name {
      height: 20px;
      font-size: 14px;
      font-weight: 500;
      color: $--color-text-primary;
      line-height: 20px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }

    .count {
      height: 20px;
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #737589;
      line-height: 20px;
      text-align: right;
    }
  }
}
</style>
