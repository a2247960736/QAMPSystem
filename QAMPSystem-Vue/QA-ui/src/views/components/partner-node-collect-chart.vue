<template>
  <div class="box partner-node-collect">
    <div class="header">
      <div class="title">现目需求数Top5</div>
      <!-- 需要确认是否已注册/导入svg-icon组件 -->
      <svg-icon name="more" class="more" @click="handleMoreClick" />
    </div>
    <el-row :gutter="20" type="flex" align="middle" class="body">
      <el-col :span="17">
       <div id="chartTop" ref="EcharRef" class="monitorContainer" style="height: 400px;"></div>
      </el-col>
      <el-col :span="7">
        
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
// 添加缺失的导入
import PartnerNodeCollectPieChart from './partner-node-collect-pie-chart.vue'

import * as echarts from 'echarts';
import { onMounted, ref, nextTick, } from 'vue';

// // 定义变量
const pieChartOption = ref({
  seriesData: [
    {
      name: '电商平台',
      value: 5,
    },
    {
      name: '质量管理平台前端',
      value: 6,
    },
    {
      name: '质量管理平台后端',
      value: 5,
    },
    {
      name: '用例脑图平台',
      value: 2,
    },
    {
      name: '测试用例执行嵌入脑图平台',
      value: 1,
    },
  ],
});
// console.log('饼图数据:', pieChartOption.value);

// const width = ref('100%');
// const height = ref('300px');
// const chart = ref('chart');
// const EcharRef = ref(null);

// // 处理更多点击事件，这里暂时为空
// const handleMoreClick = () => {
//   // 可以在这里添加具体逻辑
// };

// onMounted(() => {
//   setOption();
// });

const setOption = () => {
  const chartDom = document.getElementById('chartTop');
  const myChart = echarts.init(chartDom);
  let option = null;
  nextTick(() => {
    setTimeout(() => {
      option = {
        // TODO：细节调整，小圈，阴影，线的长度...
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
        label: {
          formatter: ['{name|{b}}', '{percentage|{d}%}'].join('\n'),
          rich: {
            name: {
              color: '#333333',
              fontWeight: 'bolder',
              align: 'left',
            },
            percentage: {
              color: '#000000',
              lineHeight: 15,
              align: 'left',
            },
          },
        },
        labelLine: {
          lineStyle: {
            color: '#BFBFBF',
          },
        },
        series: [
          {
            type: 'pie',
            roseType: 'radius',
            radius: [33, 100],
            center: ['50%', '50%'],
            data: pieChartOption.value.seriesData,
            animationEasing: 'cubicInOut',
            animationDuration: 2600,
          },
        ],
        color: ['#85A7FF', '#99EBBD', '#FFB18B', '#C6EBFF', '#BECCE6'],
      };
      myChart.setOption(option);
    }, 10);
  });
};


const EcharRef = ref(null);
let myChart = null;

onMounted(() => {
  nextTick(() => {
    initChart();
  });
});

const initChart = () => {
  if (!EcharRef.value) return;
  
  // 销毁旧实例
  if (myChart) {
    myChart.dispose();
  }
  
  // 初始化图表
  myChart = echarts.init(EcharRef.value);
  
  // 修改为饼图配置
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
  label: {
    formatter: ['{name|{b}}', '{percentage|{d}%}'].join('\n'),
    rich: {
      name: {
        color: '#333333',
        fontWeight: 'bolder',
        align: 'left',
      },
      percentage: {
        color: '#000000',
        lineHeight: 15,
        align: 'left',
      },
    },
  },
  labelLine: {
    lineStyle: {
      color: '#BFBFBF',
    },
  },
  series: [{
    type: 'pie',
    roseType: 'radius',
    radius: [33, 100],
    center: ['50%', '50%'],
    data: pieChartOption.value.seriesData,
    animationEasing: 'cubicInOut',
    animationDuration: 2600,
  }],
  color: ['#85A7FF', '#99EBBD', '#FFB18B', '#C6EBFF', '#BECCE6']
};

  myChart.setOption(option);
  
  // 窗口resize监听
  window.addEventListener('resize', () => {
    myChart.resize();
  });
};

// 组件卸载时清理
onUnmounted(() => {
  if (myChart) {
    myChart.dispose();
    myChart = null;
  }
});
</script>

<style scoped>
.partner-node-collect {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -ms-flex-direction: column;
  flex-direction: column;
  height: calc(40vh - 48px);
  min-height: 353px;
  background: #fff;
  border-radius: 20px;
}

.partner-node-collect .body {
  -webkit-box-flex: 1;
  -ms-flex: 1;
  flex: 1;
}

.partner-node-collect .body .chart {
  padding-top: 16px;
}

.partner-node-collect .body .collect {
  width: 154px;
  height: 230px;
  padding-top: 47px;
  padding-left: 38px;
  background: linear-gradient(135deg, transparent, #f8f8f9 0) 0 0,
    linear-gradient(-135deg, transparent 12px, #f8f8f9 0) 100% 0,
    linear-gradient(-45deg, transparent, #f8f8f9 0) 100% 100%,
    linear-gradient(45deg, transparent 12px, #f8f8f9 0) 0 100%;
  background-size: 50% 50%;
  background-repeat: no-repeat;
}

.partner-node-collect .body .collect .count {
  height: 33px;
  font-size: 24px;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  color: #072074;
  line-height: 33px;
}

.partner-node-collect .body .collect .count2 {
  margin-top: 20px;
}

.partner-node-collect .body .collect .name {
  height: 17px;
  margin-top: 6px;
  font-size: 12px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #000412;
  line-height: 17px;
}

.partner-node-collect .more {
  color: #5f84ff;
  cursor: pointer;
}
</style>