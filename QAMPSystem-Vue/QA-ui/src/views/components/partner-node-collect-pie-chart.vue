<template>
  <div id="chartTop" ref="EcharRef" class="monitorContainer"></div>
</template>
<script setup>
import * as echarts from 'echarts';
import { ref, watch, onMounted, onUnmounted } from 'vue';

const EcharRef = ref(null);
const chartInstance = ref(null);
const chartData = ref([]);

const props = defineProps({
  chartOption: {
    type: Object,
    default: () => ({}),
  },
});

// 初始化图表
const initChart = () => {
  if (chartInstance.value) return;
  chartInstance.value = echarts.init(EcharRef.value);
};

// 渲染图表
const renderChart = () => {
  if (!chartInstance.value) return;
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}<br />总占比：{d}%',
      backgroundColor: '#FFFFFF',
      borderColor: '#eef3f8',
      borderWidth: 1,
      textStyle: {
        color: '#8C8C8C',
      },
      padding: 12,
    },
    series: [{
      type: 'pie',
      roseType: 'radius',
      radius: [33, 100],
      center: ['50%', '50%'],
      data: chartData.value,
      animationEasing: 'cubicInOut',
      animationDuration: 2600,
    }],
    color: ['#85A7FF', '#99EBBD', '#FFB18B', '#C6EBFF', '#BECCE6'],
  };
  
  chartInstance.value.setOption(option);
};

// 监听数据变化
watch(() => props.chartOption.seriesData, (newVal) => {
  chartData.value = newVal || [];
  renderChart();
}, { immediate: true, deep: true });

// 生命周期
onMounted(() => {
  initChart();
  window.addEventListener('resize', () => chartInstance.value?.resize());
});

onUnmounted(() => {
  chartInstance.value?.dispose();
  window.removeEventListener('resize', () => chartInstance.value?.resize());
});
</script>
