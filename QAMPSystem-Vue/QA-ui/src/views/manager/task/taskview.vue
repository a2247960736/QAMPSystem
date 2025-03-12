<template>
  <div class="container">
    <!-- 日期选择部分 -->
    <div class="date-picker">
      <el-date-picker
        v-model="selectedMonth"
        type="month"
        value-format="YYYY-MM"
        @change="handleMonthChange"
      />
    </div>

    <!-- 日历部分 -->
    <div class="calendar">
      <!-- 星期标题 -->
      <div class="weekdays">
        <div v-for="day in weekdays" :key="day" class="weekday">{{ day }}</div>
      </div>

      <!-- 日期格子 -->
      <div v-for="(week, index) in calendarWeeks" :key="index" class="week">
        <div
          v-for="(date, i) in week"
          :key="i"
          class="day"
          :class="{ 'other-month': !date.isCurrentMonth }"
          @click="handleDayClick(date)"
        >
          <div class="day-number">{{ date.day }}</div>
          <div 
            v-if="date.works.length > 0"
            class="work-infos"
          >
            <div 
              v-for="(work, idx) in date.works"
              :key="idx"
              class="work-info"
              :class="[
                `status-${work.status}`,
                work.position
              ]"
            >
              {{ truncateText(work.name, 15) }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 弹窗 -->
<el-dialog v-model="dialogVisible" :title="`${selectedDate} 任务详情`" width="60%">
  <el-table :data="tasksOnSelectedDate" stripe style="width: 100%">
    <el-table-column type="index" width="50" label="序号" />
    <el-table-column prop="taskName" label="任务名称" min-width="180" />
    <el-table-column label="优先级" width="100">
      <template #default="{row}">
        <el-tag :type="priorityTagType[row.priority]">
          {{ priorityMap[row.priority] }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="状态" width="120">
      <template #default="{row}">
        <el-tag :type="statusTagType[row.status]">
          {{ statusMap[row.status] }}
        </el-tag>
      </template>
    </el-table-column>
  </el-table>
</el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElDatePicker, ElDialog } from 'element-plus'
import { listTask } from "@/api/manager/task";
//新增api/page
import { loadAllParams } from "@/api/page/page";
const taskList = ref([]);

// 当前选择的月份（格式：YYYY-MM）
const selectedMonth = ref(new Date().toISOString().slice(0, 7))
// 日历数据
const calendarWeeks = ref([])
// 弹窗控制
const dialogVisible = ref(false)
// 选中的日期
const selectedDate = ref('')
// 选中日期的任务列表
const tasksOnSelectedDate = ref([])

// 星期标题
const weekdays = ['一', '二', '三', '四', '五', '六', '日']

// 状态映射
const statusMap = {
  NOT_STARTED: '未开始',
  IN_PROGRESS: '进行中',
  OVERDUE: '已逾期',
  COMPLETED: '已完成',
  PAUSED: '已暂停'
}

// 优先级映射
const priorityMap = {
  HIGH: '高',
  MEDIUM: '中',
  LOW: '低'
}

// 标签类型映射
const priorityTagType = {
  HIGH: 'danger',
  MEDIUM: 'warning',
  LOW: 'success'
}

const statusTagType = {
  NOT_STARTED: 'info',
  IN_PROGRESS: '',
  OVERDUE: 'danger',
  COMPLETED: 'success',
  PAUSED: 'warning'
}

// 获取任务列表
const getList = async () => {
  try {
    const response = await listTask(loadAllParams);
    taskList.value = response.rows;
  } catch (error) {
    console.error("Failed to fetch task list:", error);
  }
};

// 生成日历
const generateCalendar = (year, month) => {
  const weeks = [];
  const firstDay = new Date(year, month - 1, 1);
  const lastDay = new Date(year, month, 0);

  // 调整第一天为周一
  let day = firstDay.getDay() === 0 ? 6 : firstDay.getDay() - 1;
  let week = [];

  // 添加上个月的日期
  for (let i = day; i > 0; i--) {
    const prevDate = new Date(year, month - 1, 1 - i);
    week.push(createDayObject(prevDate, false));
  }

  // 添加本月日期
  for (let d = 1; d <= lastDay.getDate(); d++) {
    const currentDate = new Date(year, month - 1, d);
    week.push(createDayObject(currentDate, true));
    if (week.length === 7) {
      weeks.push([...week]);
      week = [];
    }
  }

  // 添加下个月的日期
  while (week.length < 7) {
    const nextDate = new Date(year, month, week.length - day + 1);
    week.push(createDayObject(nextDate, false));
  }
  if (week.length) weeks.push(week);

  return weeks;
};

// 创建日期对象
const createDayObject = (date, isCurrentMonth) => {
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const dateStr = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;

  const works = [];

  for (const task of taskList.value) {
    const taskStart = new Date(task.startTime);
    const taskEnd = new Date(task.endTime);
    const currentDate = new Date(dateStr);

    if (currentDate >= taskStart && currentDate <= taskEnd) {
      // 计算当月边界
      const currentMonthFirstDay = new Date(year, month - 1, 1);
      const currentMonthLastDay = new Date(year, month, 0);

      // 计算实际起止日期
      const actualStart = taskStart < currentMonthFirstDay ? currentMonthFirstDay : taskStart;
      const actualEnd = taskEnd > currentMonthLastDay ? currentMonthLastDay : taskEnd;

      // 确定位置状态
      let position = '';
      if (currentDate.getTime() === actualStart.getTime()) {
        position = 'start';
      } else if (currentDate.getTime() === actualEnd.getTime()) {
        position = 'end';
      } else {
        position = 'middle';
      }

      works.push({
        name: `${task.taskName}--${statusMap[task.status]}`,
        status: task.status,
        position: position
      });
    }
  }

  return {
    day: day,
    date: dateStr,
    isCurrentMonth,
    works: works
  };
};

// 月份变更处理
const handleMonthChange = () => {
  const [year, month] = selectedMonth.value.split('-');
  calendarWeeks.value = generateCalendar(parseInt(year), parseInt(month));
};

// 日期点击处理
const handleDayClick = (date) => {
  selectedDate.value = date.date;
  tasksOnSelectedDate.value = taskList.value.filter(task => {
    const taskStart = new Date(task.startTime);
    const taskEnd = new Date(task.endTime);
    const currentDate = new Date(selectedDate.value);
    return currentDate >= taskStart && currentDate <= taskEnd;
  });
  dialogVisible.value = true;
};

// 截断文本
const truncateText = (text, maxLength) => {
  if (text.length > maxLength) {
    return text.substring(0, maxLength) + '...';
  }
  return text;
};

// 初始化日历和任务列表
onMounted(() => {
  getList().then(() => {
    handleMonthChange();
  });
});
</script>

<style scoped>
.container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.date-picker {
  margin-bottom: 20px;
}

.calendar {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
  display: grid;
  gap: 10px;
}

.weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  text-align: center;
  padding: 10px 0;
  color: #909399;
}

.week {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  min-height: 100px;
  position: relative;
  gap: 5px;
}


.day {
  border: 1px solid #ebeef5;
  padding: 8px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  z-index: 1;
  height: 120px; /* 固定高度 */
  min-width: 120px; /* 添加最小宽度 */
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}

.day:hover {
  background-color: #f5f7fa;
}

.other-month {
  color: #c0c4cc;
  background-color: #fafafa;
}

.day-number {
  font-weight: bold;
  margin-bottom: 4px;
  flex-shrink: 0; /* 防止数字区域被压缩 */
}

.work-infos {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
  overflow-y: auto; /* 添加垂直滚动 */
  max-height: 90px; /* 限制最大高度 */
   scrollbar-width: thin;  
}

/* Chrome/Safari滚动条样式 */
.work-infos::-webkit-scrollbar {
  width: 4px;
}
.work-infos::-webkit-scrollbar-track {
  background: #f1f1f1;
}
.work-infos::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 2px;
}

.work-info {
  font-size: 12px;
  padding: 4px;
  padding: 4px 8px;
  color: white;
  display: block;
  position: relative;
  z-index: 2;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
   margin: 2px 0;
  flex-shrink: 0;  
}


/* 状态颜色 */
.status-NOT_STARTED { background-color: #dcdcdc; }
.status-IN_PROGRESS { background-color: #409eff; }
.status-OVERDUE { background-color: #f56c6c; }
.status-COMPLETED { background-color: #67c23a; }
.status-PAUSED { background-color: #e6a23c; }

/* 连续高亮效果 */
.start,
.middle,
.end {
  margin: 0;
  border-radius: 12px;
}

.el-dialog__body ul {
  list-style-type: none;
  padding: 0;
}

.el-dialog__body li {
  margin: 5px 0;
}
</style>
