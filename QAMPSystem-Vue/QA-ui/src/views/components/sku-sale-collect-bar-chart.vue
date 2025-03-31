<template>
<div class="chart">
  <div id="chartId" ref="EcharRef" class="monitorContainer"></div>
</div>

</template>
<script setup>
import * as echarts from 'echarts';
import { onMounted } from 'vue';
const props = defineProps({
  chartOption: {
    type: Object,
    default: () => {},
  },
});
// 坐标轴的颜色
const axisColor = ref('#D9D9D9');
const axisColor2 = ref('#999999');
// 柱条的颜色
const itemStyleColor = ref('#91B0FF');
onMounted(() => {
  setOption();
});
const setOption = () => {
  const chartDom = document.getElementById('chartId')
  const myChart = echarts.init(chartDom)
  let option = null
  const grid = {
    left: '3%',    // 改用百分比适配
    right: '3%',
    top: '15%',
    bottom: '15%',  // 增加底部空间
    containLabel: true
  };
  nextTick(() => {
    setTimeout(() => {
      option = {
        title: {
          text: '项目需求分布',
          left: 'center',
          top: '10',
          textStyle: {
            color: '#333333',
            fontWeight: 'bolder',
            fontSize: 14,
          },
        },
        grid: grid,
        legend: {
          bottom: 0,
          data: props.chartOption.legendData,
        },
        xAxis: {
          type: 'category',
          data: props.chartOption.xAxisData,
          axisLine: {
            lineStyle: {
              color: axisColor.value,
            },
          },
          axisTick: {
            show: true,
            lineStyle: {
              color: axisColor.value,
            },
          },
          axisLabel: {
            interval: 0,
            align: 'center',
            color: axisColor2.value,
            formatter: function (value) {
              return value.length > 3 ? value.substring(0, 3) + '\n...' : value;
            },
          },
        },
        yAxis: {
          type: 'value',
          name: props.chartOption.yAxisName,
          nameTextStyle: {
            color: axisColor2.value,
          },
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            color: axisColor2.value,
          },
          splitLine: {
            lineStyle: {
              color: axisColor.value,
            },
          },
        },
        series: getSeriesOption(),
        // TODO：细节调整
        tooltip: {
          formatter: '需求数：{c}<br />{b}',
          backgroundColor: '#FFFFFF',
          borderColor: '#eef3f8',
          borderWidth: 1,
          textStyle: {
            color: '#8C8C8C',
          },
          padding: 12,
        },
      };
      myChart.setOption(option)
    }, 10);
  });
};
const getSeriesOption = () => {
  return [
    {
      type: 'bar',
      data: props.chartOption.seriesData,
      itemStyle: {
        color: itemStyleColor.value,
        barBorderRadius: [4, 4, 0, 0],
      },
      barWidth: 14,
      barGap: '100%',
    },
  ];
};

// 添加窗口resize监听
window.addEventListener('resize', () => {
  myChart.resize();
});

// 添加组件卸载时的清理
import { onUnmounted } from 'vue';
onUnmounted(() => {
  window.removeEventListener('resize', () => myChart?.resize());
});
</script>
<style lang="scss" scoped>
.chart {
  position: relative;
  width: 100%;  // 修改为全宽
  height: 100%;
}

.monitorContainer {
  width: 100%;
  height: 100%;  // 改为继承父容器高度
  min-height: 300px;  // 添加最小高度保证展示
}
.show {
  visibility: visible;
}

.hidden {
  visibility: hidden;
}
</style>
