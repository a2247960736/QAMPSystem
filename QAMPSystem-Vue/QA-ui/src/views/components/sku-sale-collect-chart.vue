<template>
  <div class="box sku-sale-collect">
    <div class="header">
      <div class="title">
        项目数据<span class="sub-title"
          >{{ datePickerFormat[0] }} ~ {{ datePickerFormat[1] }}</span
        >
      </div>
      <common-week-month-year @handleChange="handleRadioGroupSelChange" />
    </div>
    <div class="charts">
     
      <sku-sale-collect-bar-chart
        id="region-collect"
        title="项目需求分布"
        :chart-option="barChartOption"
        style="flex: 1; height: 100%; min-height: 300px;"
      />
    </div>
  </div>
</template>
<script setup>
import * as echarts from 'echarts';
import { onMounted } from 'vue';
import dayjs from 'dayjs';
import CommonWeekMonthYear from '@/components/week-month-year/index.vue';
import SkuSaleCollectBarChart from './sku-sale-collect-bar-chart.vue';
// 定义变量
const datePickerSel = ref([]);
const datePickerFormat = ref([]);
const radioGroupSel = ref('week');
const userTaskStatus = ref([]);
const lineChartOption = ref({
  xAxisData: [
      "2024-05-13",
      "2024-05-14",
      "2024-05-15",
      "2024-05-16"
  ],
  seriesData: [5,10,12,15],
  yAxisName: '单位：个',
});
const collectType = ref(1); // 统计时间类型，1:按日统计，2:按月统计
const barChartOption = ref({
  xAxisData: ["电商平台", "质量平台前端", "质量平台后端", "用例脑图平台"],
  seriesData: [5,6,5,2,1],
  yAxisName: '单位：元',
});
onMounted(()=>{
    handleRadioGroupSelChange(radioGroupSel.value)
})
//
const handleRadioGroupSelChange = (radioGroup) => {
  radioGroupSel.value = radioGroup;
  const startFormat = dayjs()
      .startOf(radioGroupSel.value)
      .format('YYYY.MM.DD')
    const endFormat = dayjs()
      .endOf('day')
      .format('YYYY.MM.DD')
    datePickerFormat.value = [startFormat, endFormat]
};
</script>
<style lang="scss" scoped>
.sku-sale-collect {
  .charts {
    height: calc(100% - 40px); // 减去头部高度
    padding: 0 20px 20px;
    
    // 如果使用弹性布局
    display: flex;
    flex-direction: column;
  }
}
</style>